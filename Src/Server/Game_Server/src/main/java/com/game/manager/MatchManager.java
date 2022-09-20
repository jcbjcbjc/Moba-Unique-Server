package com.game.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.game.entity.User;
import com.game.network.NetConnection;
import com.game.proto.C2GNet;
import com.game.proto.C2GNet.NRoom;

import com.game.proto.C2GNet.Result;
import com.game.proto.C2GNet.RoomStatus;
import com.game.proto.C2GNet.RoomUser;

import com.game.proto.C2GNet.AllTeam;

import com.game.service.MatchService;
import com.game.service.RoomService;
import com.game.spring.DBUtil;
import com.game.spring.SpringBeanUtil;
import com.game.util.RandomUtil;
import com.game.vo.BattleUserVo;
import com.game.vo.ResultInfo;
/**
 * @author 贾超博
 *
 * Component in Manager
 *
 * MatchManager Class
 *
 *
 */
public class MatchManager {
	public static MatchManager Instance = new MatchManager();
	
	//房间用户集合  key:用户id
	public List<User> userList = Collections.synchronizedList(new ArrayList<User>());
	//有房间用户集合  key:用户id
//	public List<User> haveRoomUserList = Collections.synchronizedList(new ArrayList<User>());
	MatchService matchService;
	//TODO test
	public int gameOverMs=2*10;  //游戏超时毫秒 2小时
	//public int gameOverMs=2*60*60*1000;  //游戏超时毫秒 2小时
	
	public MatchManager() {
		matchService = SpringBeanUtil.getBean(MatchService.class);
	}
	/**
	 * 开始匹配
	 */
	public void StartMatch(User user) {
		//获取已匹配用户数据
		User matchUser = RoomManager.Instance.userMap.get(user.id);
		if(matchUser != null && matchUser.roomId != 0) {   //已匹配
			NRoom room = RoomManager.Instance.rooms.get(matchUser.roomId);  //获取匹配房间
			long jg=new Date().getTime()-matchUser.matchTime;
			if(jg <= gameOverMs && room != null && room.getRoomStatus() == RoomStatus.GameIn) {   //游戏未超时，重新进入游戏			
				NRoom.Builder roomBuilder = room.toBuilder();
			  	 ResultInfo resultInfo=new ResultInfo(Result.Success,"存在对局，恢复游戏！");
				//匹配响应
			  	 matchService.OnMatchResponse(matchUser.id, resultInfo, roomBuilder, true);
				return;
			}
		}
		user.matchTime=new Date().getTime();
		if(this.userList.contains(user)) {
			return;
		}
		System.out.println("已经加入匹配队列");
		this.userList.add(user);
	}

	public void StartGameForRoom(NRoom.Builder roomBuilder){
		ResultInfo resultInfo=null;
		//发送添加对战用户信息
		List<Integer> userIdList = RoomManager.Instance.GetRoomUserIdList(roomBuilder);
		boolean result= BattleManager.Instance.SendAddBattleUserInfo(roomBuilder, new BattleUserVo(roomBuilder.getRoomId(), userIdList));
		if (result) {
			roomBuilder.setRoomStatus(RoomStatus.GameIn);
			roomBuilder.setRandomSeed(RandomUtil.getRandomNum(0, 100)); //随机数种子
			NRoom room= roomBuilder.build();
			RoomManager.Instance.rooms.put(roomBuilder.getRoomId(), room);

			resultInfo=new ResultInfo(Result.Success,"匹配成功");
		}else {
			resultInfo=new ResultInfo(Result.Failed,"请求对战服异常，请联系管理员！");
			//移除用户
			RoomManager.Instance.RemoveUserMapByRoom(roomBuilder);
		}
		//匹配响应
		for (RoomUser roomUser : roomBuilder.getAllTeam(0).getTeamList()) {
			matchService.OnMatchResponse(roomUser.getUserId(), resultInfo, roomBuilder, false);
		}
		for (RoomUser roomUser : roomBuilder.getAllTeam(1).getTeamList()) {
			matchService.OnMatchResponse(roomUser.getUserId(),resultInfo, roomBuilder, false);
		}
	}
	/**
	 * 制造假号
	 */
	//public void createFalseNumber() {
	// 		List<String> namePwdList=new ArrayList<>();
	// 		namePwdList.add("1");
	// 		namePwdList.add("12");
	// 		namePwdList.add("1234");
	// 		namePwdList.add("12345");
	// 		namePwdList.add("123456");
	// 		for (String namePwd : namePwdList) {
	// 			User user=new User(namePwd, namePwd);
	// 			user=DBUtil.Instance.getUserDao().queryByUserNamePassword(user);
	// 			if (user != null) {
	// //            ConnectionManager.addToConnection(user, new NetConnection(null));
	// 				NUser nUser=UserManager.Instance.addUser(user);
	// 				user.matchTime=new Date().getTime();
	// 				this.userList.add(user);
	// 			}
	// 		}
	// 	}

	/**
	 * 移除离线、超时用户
	 * @param list
	 */
	private void RemoveOffLineOverTimeUser(List<User> list) {
		for (int i = 0; i < list.size(); i++) {
			User user = list.get(i);
			User newUser = UserManager.Instance.userMap.get(user.id);
			if (newUser ==  null ||  new Date().getTime()-DataManager.Instance.gameConfig.MaxMatchTime*1000 > user.matchTime) {
				list.remove(i);
				i--;
			}
		}
	}
	
	/**
	 * 计算房间用户总战力
	 * @param list
	 * @return
	 */
	private float CalcuCombat(List<RoomUser> list) {
		float sumCombat=0;
		for (int j = 0; j < list.size(); j++) {
			RoomUser roomUser = list.get(j);
			User u=UserManager.Instance.userMap.get(roomUser.getUserId());
			sumCombat += u.character.att+u.character.def;						  
		}
		return sumCombat;
	}
	
	/**
	 * 队伍添加匹配用户
	 * @param roomBuilder
	 * @param count
	 * @param userIndex
	 * @param teamId
	 */
	private void TeamAddMatchUser(NRoom.Builder roomBuilder,int count,int userIndex,int teamId) {
		if(count==0) {
			return;
		}
		for (int j = 0; j < count; j++) {
			if(userList.size() > userIndex) {
				User user=userList.remove(userIndex);
				user.roomId=roomBuilder.getRoomId();
				user.teamId=teamId;
				RoomManager.Instance.userMap.put(user.id, user);
				RoomUser roomUser=RoomManager.Instance.CreatorRoomUser(user,teamId);
				AllTeam.Builder allTeam=roomBuilder.getAllTeam(teamId).toBuilder();
				allTeam.addTeam(roomUser);
				roomBuilder.addAllTeam(teamId,allTeam);
			}
		}
	}
	
	/**
	 * 匹配
	 */
	public void Match() {
		//移除离线用户
		this.RemoveOffLineOverTimeUser(this.userList);
		if (userList.size() < RoomManager.Instance.minTeamNum*2) {
    		return;
    	}else {    		
    		NRoom.Builder roomBuilder = NRoom.newBuilder();
    		roomBuilder.setRoomId(RoomManager.Instance.GetRoomId());
			AllTeam.Builder Team1=AllTeam.newBuilder();
			AllTeam.Builder Team2=AllTeam.newBuilder();

			roomBuilder.addAllTeam(Team1);
			roomBuilder.addAllTeam(Team2);


    		//开始匹配
    		this.RoomMatch(roomBuilder);
    	}
	}
	
	/**
	 * 房间匹配
	 * @param roomBuilder
	 * @return
	 */
    private void RoomMatch(NRoom.Builder roomBuilder) {		
    	int len = userList.size() % 2 == 0 ? userList.size() : userList.size() - 1;
    	len = len > RoomManager.Instance.teamNum * 2 ? RoomManager.Instance.teamNum * 2 : len;
    	for (int j = 0; j < len ; j++) {
		  if (j % 2 == 0) {
			  this.TeamAddMatchUser(roomBuilder, 1, 0, 0);
		  } else {
			  this.TeamAddMatchUser(roomBuilder, 1, 0, 1);
		  }
    	}
    	
		ResultInfo resultInfo=null;
		  //发送添加对战用户信息
		  List<Integer> userIdList = RoomManager.Instance.GetRoomUserIdList(roomBuilder);
		  boolean result= BattleManager.Instance.SendAddBattleUserInfo(roomBuilder, new BattleUserVo(roomBuilder.getRoomId(), userIdList));
		  if (result) {
			  roomBuilder.setRoomStatus(RoomStatus.GameIn);
			  roomBuilder.setRandomSeed(RandomUtil.getRandomNum(0, 100)); //随机数种子
			  NRoom room= roomBuilder.build();
			  RoomManager.Instance.rooms.put(roomBuilder.getRoomId(), room);
			  
			  resultInfo=new ResultInfo(Result.Success,"匹配成功");
		  }else {
			  resultInfo=new ResultInfo(Result.Failed,"请求对战服异常，请联系管理员！");	
			//移除用户
			 RoomManager.Instance.RemoveUserMapByRoom(roomBuilder);
		  }
	  //匹配响应
		 for (RoomUser roomUser : roomBuilder.getAllTeam(0).getTeamList()) {
			  matchService.OnMatchResponse(roomUser.getUserId(), resultInfo, roomBuilder, false);
		  }
		  for (RoomUser roomUser : roomBuilder.getAllTeam(1).getTeamList()) {
			  matchService.OnMatchResponse(roomUser.getUserId(),resultInfo, roomBuilder, false);
		  }
	}
    
	
    int updateCount=0;
	public void Update() {
		//每5秒执行一次
		if (updateCount==0) {
		   this.Match();	
		}
		++updateCount;
		if (updateCount >= 50) {
			updateCount=0;
		}
	}
	
}

