package com.game.manager;

import java.io.Console;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.game.data.BattleConfig;
import com.game.entity.User;
import com.game.network.NetConnection;
import com.game.proto.C2GNet;
import com.game.proto.C2GNet.GameOver2Request;
import com.game.proto.C2GNet.NRoom;
import com.game.proto.C2GNet.Result;
import com.game.proto.C2GNet.RoomStatus;
import com.game.proto.C2GNet.RoomUser;

import com.game.proto.C2GNet.TipsType;
import com.game.proto.C2GNet.TipsWorkType;
import com.game.proto.C2GNet.UserStatus;
//import com.game.proto.Message.GameOver2Request;
//import com.game.proto.Message.NRoom;
//import com.game.proto.Message.Result;
//import com.game.proto.Message.RoomStatus;
//import com.game.proto.Message.RoomUser;
//import com.game.proto.Message.TeamType;
//import com.game.proto.Message.TipsType;
//import com.game.proto.Message.TipsWorkType;
//import com.game.proto.Message.UserStatus;
import com.game.vo.LiveUserVo;
import com.game.vo.ResultInfo;

/**
 * 房间管理
 * 
 * @author Administrator
 *
 */
public class RoomManager {

	public static RoomManager Instance = new RoomManager();

	// key:房间id
	public Map<Integer, NRoom> rooms = Collections.synchronizedMap(new HashMap<Integer, NRoom>());
	// 匹配成功将用户数据存起来，避免掉线用户数据丢失
	public Map<Integer, User> userMap = Collections.synchronizedMap(new HashMap<>());
	// 对战房间用户集合便于统计人数 key:房间id value:用户id集合
	public Map<Integer, List<Integer>> roomUserIds = Collections.synchronizedMap(new HashMap<>());

	public int teamNum = BattleConfig.TeamNum; // 队伍人数
	public int minTeamNum = 1; // 每队最少人数

	private int roomIdx = 1000;
	public int overNum = BattleConfig.overNum;// 2; //游戏结束人数判断

	public NRoom GetRoom(int roomId) {
		return rooms.get(roomId);
	}

	/**
	 * 加入房间用户效验
	 * 
	 * @param roomOwnerUser 房主
	 * @param user          普通用户
	 * @param isRoomOwner   是否是房主操作
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public ResultInfo AddRoomValidate(User roomOwnerUser, User user, boolean isRoomOwner) {
		if (isRoomOwner) { // 房主操作
			if (user == null) {
				return new ResultInfo(Result.Failed, "对方不在线！");
			}
			NRoom room = this.GetRoom(roomOwnerUser.roomId);
			if (room != null && room.getUserId() != roomOwnerUser.id) {
				return new ResultInfo(Result.Failed, "你不是房主！");
			}
		} else { // 普通用户操作
			if (roomOwnerUser == null) {
				return new ResultInfo(Result.Failed, "房主不在线！");
			}
		}
		NRoom room = this.GetRoom(roomOwnerUser.roomId);
		if (room == null) {
			return new ResultInfo(Result.Failed, isRoomOwner ? "你当前没有开房间！" : "未找到房间！");
		}

		boolean myResult = this.ExistUserRoom(user.id, room.getAllTeam(0).getTeamList());
		if (myResult) {
			return new ResultInfo(Result.Failed, isRoomOwner ? "对方已存在友方列表！" : "你已存在友方列表！");
		}
		boolean enemyResult = this.ExistUserRoom(user.id, room.getAllTeam(1).getTeamList());
		if (enemyResult) {
			return new ResultInfo(Result.Failed, isRoomOwner ? "对方已存在敌方列表！" : "你已存在敌方列表！");
		}

		if (user.getStatus() == UserStatus.Room) {
			return new ResultInfo(Result.Failed, isRoomOwner ? "对方已在别人房间中！" : "你已存在别人房间中！");
		}

		if (user.getStatus() == UserStatus.Game) {
			return new ResultInfo(Result.Failed, isRoomOwner ? "对方已在游戏中！" : "你已在游戏中！");
		}

		return new ResultInfo(Result.Success, "");
	}

	/**
	 * 效验用户是否存在友方、敌方队伍中
	 * 
	 * @param userId       用户id
	 * @param roomUserList 友方、敌方列表
	 * @return
	 */
	public boolean ExistUserRoom(int userId, List<RoomUser> roomUserList) {
		if (roomUserList != null) {
			for (RoomUser roomUser : roomUserList) {
				if (roomUser.getUserId() == userId) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 邀请用户响应
	 */
	@SuppressWarnings("rawtypes")
	public ResultInfo AddRoom(User roomOwnerUser, User user, int teamId, boolean isRoomOwner) {
		ResultInfo resultInfo = this.AddRoomValidate(roomOwnerUser, user, isRoomOwner);
		if (resultInfo.result == Result.Failed) {
			return resultInfo;
		}
		NRoom.Builder room = GetRoom(roomOwnerUser.roomId).toBuilder();
		RoomUser roomUser = RoomUser.newBuilder().setUserId(user.id).setNickName(user.nickname)
				.setCCharacterId(user.character.cId).build();
		if (room.getAllTeam(teamId).getTeamCount() >= teamNum) {
			return new ResultInfo(Result.Failed, "Team已达上限！");
		}
		room.getAllTeam(teamId).getTeamList().add(roomUser);
		/*if (teamId == 1) {
			if (room.getAllTeam(1).getTeamCount() >= teamNum) {
				return new ResultInfo(Result.Failed, "Team1已达上限！");
			}
			room.getAllTeam(1).getTeamList().add(roomUser);
		} else if (teamType == TeamType.Enemy) {
			if (room.getTeam2Count() >= teamNum) {
				return new ResultInfo(Result.Failed, "Team2已达上限！");
			}
			room.addTeam2(roomUser);
		}*/
		rooms.put(room.getRoomId(), room.build());
		user.setStatus(UserStatus.Room); // 更新用户状态
		user.roomId = room.getRoomId();
		user.teamId=teamId;
		return resultInfo;
	}

	/**
	 * 友方、敌方队伍中移除用户
	 * 
	 * @param outUserId
	 * @param roomBuild
	 * @return
	 */
	public boolean RemoveUserRoom(int outUserId, NRoom.Builder roomBuild) {
		List<C2GNet.AllTeam> AllTeamList = roomBuild.getAllTeamList();

		for(C2GNet.AllTeam allTeam : AllTeamList){
			List<RoomUser> roomUserList=allTeam.getTeamList();
			if (roomUserList != null) {
				for (int i = 0; i < roomUserList.size(); i++) {
					RoomUser roomUser = roomUserList.get(i);
					if (roomUser.getUserId() == outUserId) {
						roomUserList.remove(i);
						/*roomBuild.getAllTeam(teamId).getTeamList().remove(i);*/
						return true;
					}
				}
			}
		}
		return false;

	}

	/**
	 * 踢出用户
	 */
	@SuppressWarnings("rawtypes")
	public ResultInfo KickOut(User user, User outUser) {
		NRoom room = rooms.get(user.roomId);
		if (room == null) {
			return new ResultInfo(Result.Failed, "你没有开房间！");
		}
		if (room.getUserId() != user.id) {
			return new ResultInfo(Result.Failed, "你不是房主！");
		}
		NRoom.Builder roomBuild = room.toBuilder();

		boolean theResult = this.RemoveUserRoom(outUser.id, roomBuild);

		if (!theResult) {
			return new ResultInfo(Result.Failed, "用户已不存在房间中！");
		}
		rooms.put(roomBuild.getRoomId(), roomBuild.build());
		outUser.setStatus(UserStatus.Normal); // 更新用户状态
		TipsManager.Instance.ShowTips(outUser.id, TipsType.Tips, TipsWorkType.KickOutRoom, "房主将你踢出房间！", true);
		return new ResultInfo(Result.Success, "踢出用户成功！");
	}

	/**
	 * 修改房间中所有用户的的状态
	 * 
	 * @param room
	 * @param userStatus
	 */
	public void UpdateRoomUserStatus(NRoom room, UserStatus userStatus) {
		for( C2GNet.AllTeam allTeam : room.getAllTeamList()  ){
			List<RoomUser> roomUserList=allTeam.getTeamList();
			for (RoomUser roomUser : roomUserList) {
				User user = UserManager.Instance.getUser(roomUser.getUserId());
				if (user != null) {
					user.setStatus(userStatus);
				}
			}
		}
	}

	/**
	 * 开始游戏
	 */
	@SuppressWarnings("rawtypes")
	public ResultInfo StartGame(User user) {
		NRoom room = rooms.get(user.roomId);
		if (room == null) {
			return new ResultInfo(Result.Failed, "你没有开房间！");
		}
		if (room.getUserId() != user.id) {
			return new ResultInfo(Result.Failed, "你不是房主！");
		}
//		if(room.getMyCount() != teamNum || room.getEnemyCount() != teamNum) {
//			return new ResultInfo(Result.Failed, "人数未达到！");					
//		}

		return new ResultInfo(Result.Success, "开始游戏！");
	}
	/**
	 * 开始游戏
	 */
	@SuppressWarnings("rawtypes")
	public NRoom GetRoom(User user){
		NRoom room = rooms.get(user.roomId);

			//TODO log

		return room;
	}
	/**
	 * 退出房间
	 */
	@SuppressWarnings("rawtypes")
	public ResultInfo OutRoom(User user) {
		boolean isMatch = RoomManager.Instance.userMap.containsKey(user.id);
		if (isMatch) { // 已匹配
			return new ResultInfo(Result.Failed, "已匹配不能退出房间！");
		}
		NRoom room = rooms.get(user.roomId);
		if (room == null) {
			return new ResultInfo(Result.Failed, "你没有加入房间！");
		}
		if (room.getUserId() == user.id) { // 房主
			this.roomUserIds.remove(room.getRoomId()); // 从房间用户统计中移除房间
			rooms.remove(user.roomId);
			this.UpdateRoomUserStatus(room, UserStatus.Normal);
			// 同步消息给房间其他玩家
			if (room.getRoomStatus() == RoomStatus.Normal_) {
				for( C2GNet.AllTeam allTeam : room.getAllTeamList()  ){
					List<RoomUser> roomUserList=allTeam.getTeamList();
					for (RoomUser roomUser : roomUserList) {
						if (roomUser.getUserId() != room.getUserId()) {
							TipsManager.Instance.ShowTips(roomUser.getUserId(), TipsType.Tips, TipsWorkType.DismissRoom,
									"房主解散房间！", true);
						}
					}
				}
			}
			// 清空房间消息
			ChatManager.Instance.roomChatMsg.remove(user.roomId);
			return new ResultInfo(Result.Success, "解散房间成功！");
		} else { // 非房主
			RoomManager.Instance.RemoveRoomUserIds(room.getRoomId(), user.id); // 从房间用户统计中移除用户

			NRoom.Builder roomBuild = room.toBuilder();
			boolean theResult = this.RemoveUserRoom(user.id,  roomBuild);

			if (!theResult) {
				return new ResultInfo(Result.Failed, "用户已不存在房间中！");
			}
			rooms.put(roomBuild.getRoomId(), roomBuild.build());
			user.setStatus(UserStatus.Normal);
			// 同步消息给房主
			if (room.getRoomStatus() == RoomStatus.Normal_) {
				TipsManager.Instance.ShowTips(room.getUserId(), TipsType.Tips, TipsWorkType.OutRoom,
						user.nickname + "退出房间！", true);
			}
			return new ResultInfo(Result.Success, "退出房间成功！");
		}
	}

	/**
	 * 开房间
	 */
	public NRoom OpenRoom(User user) {
		int roomId = this.GetRoomId();
		user.roomId = roomId;
		user.teamId=0;
		user.setStatus(UserStatus.Room);

		RoomUser roomUser = RoomUser.newBuilder().setUserId(user.id).setNickName(user.nickname)
				.setCCharacterId(user.character.cId).build();
		C2GNet.AllTeam.Builder Team1=C2GNet.AllTeam.newBuilder();
		Team1.addTeam(roomUser);
		NRoom room = NRoom.newBuilder().setRoomId(roomId).setUserId(user.id).addAllTeam(Team1).build();
		rooms.put(roomId, room);
		return room;
	}

	/**
	 * 获取房间id
	 * 
	 * @return
	 */
	public int GetRoomId() {
		return ++roomIdx;
	}

	/**
	 * 创建房间用户
	 * 
	 * @param user 用户
	 * @param teamId
	 * @return RoomUser
	 */
	public RoomUser CreatorRoomUser(User user,int teamId) {
		RoomUser roomUser = RoomUser.newBuilder().setUserId(user.id).setNickName(user.nickname)
				.setCCharacterId(user.character.cId).setUser(user.info()).setTeamId(teamId).setRoomNum(GetRoomUserIdsNum(user.roomId)) .build();
		return roomUser;
	}

	/**
	 * 获取房间所有用户id
	 * 
	 * @param roomBuilder
	 * @return
	 */
	public List<Integer> GetRoomUserIdList(NRoom.Builder roomBuilder) {
		List<Integer> userIdList = new ArrayList<>();
		for( C2GNet.AllTeam allTeam : roomBuilder.getAllTeamList()  ){
			List<RoomUser> roomUserList=allTeam.getTeamList();
			for (RoomUser roomUser : roomUserList) {
				userIdList.add(roomUser.getUserId());
			}
		}
		return userIdList;
	}

	/**
	 * 根据房间移除房间用户map
	 */
	public void RemoveUserMapByRoom(NRoom.Builder roomBuilder) {
		for( C2GNet.AllTeam allTeam : roomBuilder.getAllTeamList()  ){
			List<RoomUser> roomUserList=allTeam.getTeamList();
			for (RoomUser roomUser : roomUserList) {
				userMap.remove(roomUser.getUserId());
			}
		}
	}

	/**
	 * 根据状态查询用户数量
	 * 
	 * @param room
	 * @param status
	 * @return
	 */
	public int GetRoomUserCountByStatus(NRoom room, UserStatus status) {
		int count = 0;
		for( C2GNet.AllTeam allTeam : room.getAllTeamList()  ){
			List<RoomUser> roomUserList=allTeam.getTeamList();
			for (RoomUser roomUser : roomUserList) {
				User u = UserManager.Instance.getUser(roomUser.getUserId());
				if (u.getStatus() == status) {
					count++;
				}
			}
		}

		return count;
	}

	/**
	 * 效验游戏是否结束
	 * 
	 * @return
	 */
	public synchronized void ValidateGameIsOver(User user, GameOver2Request gameOver2Request) {
		if (user.getStatus() != UserStatus.Game) { // 不是游戏中状态
			return;
		}
		NRoom room = GetRoom(user.roomId);
		if (room == null) { // 无房间
			return;
		}
		user.setStatus(UserStatus.GameOver);
		 int gameOverNum = this.GetRoomUserCountByStatus(room, UserStatus.GameOver);
		// //游戏结束人数

		 //游戏结束
		if(gameOverNum >= overNum) {
			rooms.remove(user.roomId); // 移除房间
			this.roomUserIds.remove(room.getRoomId()); // 从房间用户统计中移除房间
			this.UpdateRoomUserStatus(room, UserStatus.Normal); // 修改房间用户为正常状态
			BattleManager.Instance.updateRoomNum(gameOver2Request.getIpPortStr(), 2); // 减少服务器房间数
			this.RemoveUserMapByRoom(room.toBuilder()); // 移除房间用户信息
			// gameover and send message
			//sync the message
			for( C2GNet.AllTeam allTeam : room.getAllTeamList()  ){
				List<RoomUser> roomUserList=allTeam.getTeamList();
				for (RoomUser roomUser : roomUserList) {
					NetConnection connection=ConnectionManager.getConnection(roomUser.getUserId());
					if(connection == null) {
						return;
					}
					C2GNet.NetMessageResponse.Builder response = connection.getResponse();
					C2GNet.GameOver2Response.Builder gameoverResponse=C2GNet.GameOver2Response.newBuilder();
					response.setGameOver2Res(gameoverResponse);
					connection.send();
				}
			}
		}
	}

	/**
	 * 进入观战
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ResultInfo<NRoom> AddLive(User user, int targetUserId) {
		User targetUser = UserManager.Instance.getUser(targetUserId);
		if (targetUser == null) {
			return new ResultInfo(Result.Failed, "此玩家已离线！");
		}
		if (targetUser.getStatus() != UserStatus.Game) { // 不是游戏中状态
			return new ResultInfo(Result.Failed, "此玩家未直播！");
		}
		NRoom room = GetRoom(targetUser.roomId);
		if (room == null) { // 无房间
			return new ResultInfo(Result.Failed, "此玩家无房间！");
		}

		boolean result = BattleManager.Instance.SendAddLiveUserInfo(room.getIpPortStr(),
				new LiveUserVo(room.getRoomId(), user.id));
		if (result) {
			user.enterLiveUserId = targetUserId; // 设置进入直播用户的id
			if (!targetUser.liveFanSiIdList.contains(user.id)) { // 添加到直播用户粉丝列表中
				targetUser.liveFanSiIdList.add(user.id);
			}
			user.setStatus(UserStatus.Live); // 设置状态为直播中
			user.roomId = room.getRoomId(); // 设置房间号
			RoomManager.Instance.AddRoomUserIds(room.getRoomId(), user.id);
			return new ResultInfo(Result.Success, "进入直播成功！", room);
		} else {
			return new ResultInfo(Result.Failed, "玩家直播服务器异常！");
		}
	}

	/**
	 * 上传比分
	 */
	public void UploadBiFen(User user, String biFen) {
		if (user.getStatus() != UserStatus.Game) { // 不是游戏中状态
			return;
		}
		NRoom room = GetRoom(user.roomId);
		if (room == null) { // 无房间
			return;
		}
		if (!biFen.equals(room.getBiFen())) { // 不相等,覆盖房间比分
			NRoom.Builder roomBuild = room.toBuilder();
			roomBuild.setBiFen(biFen);
			rooms.put(room.getRoomId(), roomBuild.build());
		}
	}

	/**
	 * 添加用户id到房间 (玩家+观看直播用户)
	 * 
	 * @param userId
	 */
	public void AddRoomUserIds(int roomId, int userId) {
		List<Integer> userIdList = this.roomUserIds.get(roomId);
		if (userIdList == null) {
			userIdList = Collections.synchronizedList(new ArrayList<>()); // 用户id集合
			userIdList.add(userId);
			this.roomUserIds.put(roomId, userIdList);
		} else {
			if (!userIdList.contains(userId)) {
				userIdList.add(userId);
			}
		}
	}

	/**
	 * 移除用户id到房间 (玩家+观看直播用户)
	 * 
	 * @param userId
	 */
	public void RemoveRoomUserIds(int roomId, Integer userId) {
		List<Integer> userIdList = this.roomUserIds.get(roomId);
		if (userIdList != null) {
			userIdList.remove(userId);
		}
	}

	/**
	 * 获取房间用户数量 (玩家+观战用户)
	 * 
	 * @param roomId
	 */
	public int GetRoomUserIdsNum(int roomId) {
		List<Integer> userIdList = this.roomUserIds.get(roomId);
		if (userIdList == null) {
			return 0;
		} else {
			return userIdList.size();
		}
	}

	/**
	 * 效验是否可以开房间请求
	 */
	@SuppressWarnings("rawtypes")
	public ResultInfo ValidateOpenRoom(User user) {
		NRoom room = this.GetRoom(user.roomId);
		if (room != null) {
			return new ResultInfo(Result.Failed, "你已经开过房间了！");
		}
		boolean isMatch = RoomManager.Instance.userMap.containsKey(user.id);
		if (isMatch) { // 已匹配
			return new ResultInfo(Result.Failed, "你已匹配不能开房间了！");
		}
		return new ResultInfo(Result.Success, "");
	}
}
