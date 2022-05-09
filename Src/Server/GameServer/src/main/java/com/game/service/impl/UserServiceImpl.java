package com.game.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.game.entity.Character;
import com.game.entity.Follow;
import com.game.entity.User;
import com.game.manager.ConnectionManager;

import com.game.manager.PowerRankingManager;
import com.game.manager.RoomManager;
import com.game.manager.UserManager;
import com.game.network.NetConnection;
import com.game.proto.Message.AddAttrType;
import com.game.proto.Message.AttrPromoteInfoResponse;
import com.game.proto.Message.AttrPromoteRequest;
import com.game.proto.C2GNet.CharacterDetailRequest;
import com.game.proto.C2GNet.CharacterDetailResponse;
import com.game.proto.Message.CombatPowerRankingRequest;
import com.game.proto.Message.CombatPowerRankingResponse;
import com.game.proto.C2GNet.FollowRequest;
import com.game.proto.C2GNet.FollowResponse;
import com.game.proto.C2GNet.HeartBeatRequest;
import com.game.proto.C2GNet.HeartBeatResponse;
import com.game.proto.Message.ItemType;
import com.game.proto.Message.NItem;
import com.game.proto.C2GNet.NUser;
import com.game.proto.C2GNet.NetMessageResponse;
import com.game.proto.C2GNet.Result;
import com.game.proto.Message.SwitchCharacterRequest;
import com.game.proto.Message.SwitchCharacterResponse;
import com.game.proto.Message.UnLockCharacter;
import com.game.proto.Message.UnLockRequest;
import com.game.proto.Message.UnLockResponse;
import com.game.proto.C2GNet.UpdateNickNameRequest;
import com.game.proto.C2GNet.UpdateNickNameResponse;
import com.game.proto.C2GNet.UserLoginRequest;
import com.game.proto.C2GNet.UserLoginResponse;
import com.game.proto.C2GNet.UserRegisterRequest;
import com.game.proto.C2GNet.UserRegisterResponse;
import com.game.proto.C2GNet.UserStatusQueryRequest;
import com.game.proto.C2GNet.UserStatusQueryResponse;
import com.game.service.UserService;
import com.game.spring.DBUtil;
import com.game.util.Common;
import com.game.util.RandomUtil;

import io.netty.channel.ChannelHandlerContext;

@Service
public class UserServiceImpl implements UserService {

	/**
	 * 注册
	 */
	public void register(NetConnection connection, UserRegisterRequest register) {
		NetMessageResponse.Builder response = connection.getResponse();
		UserRegisterResponse.Builder userRegisterResponse = UserRegisterResponse.newBuilder();

		User user = new User(register.getUserName(), register.getPassword(), Common.getRandomNickName(3), "[]");
		int result = DBUtil.Instance.getUserDao().add(user);
		if (result > 0) {
			/* 注册用的时候可以创建用户默认解锁英雄 */
			List<Character> list = new ArrayList<Character>();
			Character character = new Character(1, user.id);
			list.add(character);
			list.add(new Character(2, user.id));
			DBUtil.Instance.getCharacterDao().addList(list);
			user.characterId = character.id;
			user.update();

			userRegisterResponse.setResult(Result.Success).setErrormsg("注册成功");
		} else {
			userRegisterResponse.setResult(Result.Failed).setErrormsg("注册失败");
		}
		response.setUserRegister(userRegisterResponse);
		connection.send();
	}

	/**
	 * 登录
	 */
	public void userLogin(NetConnection connection, UserLoginRequest login) {
		NetMessageResponse.Builder response = connection.getResponse();
		UserLoginResponse.Builder userLoginResponse = UserLoginResponse.newBuilder();
		User user = new User(login.getUserName(), login.getPassward());
		user = DBUtil.Instance.getUserDao().queryByUserNamePassword(user);
		if (user != null) {
			// 判断用户是否在线
			User onLineUser = UserManager.Instance.userMap.get(user.id);
			if (onLineUser != null) {
				userLoginResponse.setResult(Result.Failed).setErrormsg("登录用户已在线");
			} else {
				// 恢复用户数据
				User liveUser = RoomManager.Instance.userMap.get(user.id);
				if (liveUser != null) {
					user = liveUser;
				}
				connection.getSession().user = user;
				ConnectionManager.addToConnection(user, connection);
				NUser nUser = UserManager.Instance.addUser(user);
				userLoginResponse.setResult(Result.Success).setErrormsg("ok").setUser(nUser);
			}
		} else {
			userLoginResponse.setResult(Result.Failed).setErrormsg("账号或密码错误");
		}
		response.setUserLogin(userLoginResponse);
		connection.send();
	}

	// 游戏离线
	public void gameLeave(ChannelHandlerContext ctx) {
		// 获取角色 NetConnection
		NetConnection connection = ConnectionManager.getConnection(ctx);
		if (connection != null) {
			User user = connection.getSession().user;
			// 通知 他人
			userLeave(user);
			// 退出房间
			RoomManager.Instance.OutRoom(user);
			// 清除 NetConnection
			ConnectionManager.removeConnection(ctx);
		}
	}

	void userLeave(User user) {
		if (user == null) {
			return;
		}
		UserManager.Instance.removeUser(user.id);
		// 通知我的关注者，我下线了
		user.leave();
		// 退出进入直播用户的粉丝集合
		User enterLiveUser = UserManager.Instance.getUser(user.enterLiveUserId);
		if (enterLiveUser != null) {
			enterLiveUser.liveFanSiIdList.remove(user.id);
		}

	}

	/**
	 * 英雄详情
	 */
	@Override
	public void characterDetail(NetConnection connection, CharacterDetailRequest characterDetail) {
		User currentUser = connection.getSession().user;
		NetMessageResponse.Builder response = connection.getResponse();
		CharacterDetailResponse.Builder characterDetailResponse = CharacterDetailResponse.newBuilder();
		User user = UserManager.Instance.getUser(characterDetail.getUserId()); // 查用户
		if (user != null) {
			NUser.Builder nUserBuild = user.infoBase(
					characterDetail.getTCharacterId() != 0 ? characterDetail.getTCharacterId() : user.characterId);
			// 判断当前用户是否关注
			Follow follow = currentUser.followManager.follows.get(user.id);
			nUserBuild.setIsFollow(follow != null);
			characterDetailResponse.setResult(Result.Success).setErrormsg("").setUser(nUserBuild.build());
		} else {
			characterDetailResponse.setResult(Result.Failed).setErrormsg("对方不在线！");
		}
		response.setCharacterDetail(characterDetailResponse);
		connection.send();
	}

	/**
	 * 修改昵称
	 */
	@Override
	public void updateNickName(NetConnection connection, UpdateNickNameRequest updateNickName) {
		NetMessageResponse.Builder response = connection.getResponse();
		UpdateNickNameResponse.Builder updateNickNameResponse = UpdateNickNameResponse.newBuilder();
		connection.getSession().user.setNickname_(updateNickName.getNickname());
		response.setUpdateNickName(updateNickNameResponse);
		connection.send();
	}



	/**
	 * 关注/取消关注
	 */
	@Override
	public void onFollow(NetConnection connection, FollowRequest followReq) {
		User user = connection.getSession().user;
		NetMessageResponse.Builder response = connection.getResponse();
		FollowResponse.Builder followResponse = FollowResponse.newBuilder();
		String msg = "";
		if (followReq.getIsFollow()) { // 关注
			user.followManager.addFollow(followReq.getUserId());
			msg = "关注成功！";
		} else { // 取消关注
			user.followManager.removeFollow(followReq.getUserId());
			msg = "取消关注成功！";
		}
		followResponse.setResult(Result.Success).setErrormsg(msg);
		response.setFollowRes(followResponse);
		connection.send();
	}

	/**
	 * 心跳
	 */
	@Override
	public void heartBeat(NetConnection connection, HeartBeatRequest heartBeatRequest) {
		User user = connection.getSession().user;
		NetMessageResponse.Builder response = connection.getResponse();
		HeartBeatResponse.Builder heartBeatResponse = HeartBeatResponse.newBuilder();
		if (user.roomId != 0) {
			heartBeatResponse.setLiveFenSiCount(user.liveFanSiIdList.size());
		}
		response.setHeartBeatRes(heartBeatResponse);
		connection.send();
	}

	/**
	 * 用户在线、离线状态查询
	 */
	@Override
	public void OnUserStatusQuery(NetConnection connection, UserStatusQueryRequest userStatusQueryRequest) {
		NetMessageResponse.Builder response = connection.getResponse();
		UserStatusQueryResponse.Builder userStatusQueryResponse = UserStatusQueryResponse.newBuilder();
		List<Integer> userIds = userStatusQueryRequest.getUserIdsList();
		if (userIds == null || userIds.size() < 1) {
			return;
		}
		for (Integer userId : userIds) {
			User user = UserManager.Instance.getUser(userId);
			userStatusQueryResponse.addStatus(user != null);
		}
		response.setUserStatusQueryRes(userStatusQueryResponse);
		connection.send();
	}

}
