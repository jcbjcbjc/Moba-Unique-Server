package com.game.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.game.manager.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.game.entity.User;
import com.game.network.NetConnection;
import com.game.proto.C2GNet.*;
//import com.game.proto.Message.AddLiveRequest;
//import com.game.proto.Message.AddLiveResponse;
//import com.game.proto.Message.AddRoomRequest;
//import com.game.proto.Message.AddRoomResponse;

//import com.game.proto.Message.GameOver2Request;
//import com.game.proto.Message.InviteRequest;
//import com.game.proto.Message.InviteResponse;
//import com.game.proto.Message.KickOutRequest;
//import com.game.proto.Message.KickOutResponse;
//import com.game.proto.Message.NRoom;
//import com.game.proto.Message.NetMessageResponse;
//import com.game.proto.Message.NickNameSearchRequest;
//import com.game.proto.Message.NickNameSearchResponse;
//import com.game.proto.Message.OutRoomRequest;
//import com.game.proto.Message.OutRoomResponse;
//import com.game.proto.Message.Result;
//import com.game.proto.Message.MyRoomRequest;
//import com.game.proto.Message.MyRoomResponse;
//import com.game.proto.Message.RoomStartGameRequest;
//import com.game.proto.Message.RoomStartGameResponse;
//import com.game.proto.Message.RoomUser;
//import com.game.proto.Message.TipsType;

//import com.game.proto.Message.UploadBiFenRequest;
//import com.game.proto.Message.UserStatus;
//import com.game.proto.Message.ValidateOpenRoomRequest;
//import com.game.proto.Message.ValidateOpenRoomResponse;
import com.game.service.MatchService;
import com.game.service.RoomService;
import com.game.vo.ResultInfo;
@Service
public class RoomServiceImpl implements RoomService {

	@Autowired
	private MatchService matchService;
	
	/**
	 * 我的房间
	 */
	@Override
	public void OnMyRoom(NetConnection connection, MyRoomRequest myRoomRequest) {
		User user=connection.getSession().user;
		NetMessageResponse.Builder response = connection.getResponse();
		MyRoomResponse.Builder myRoomResponse=MyRoomResponse.newBuilder();
		NRoom room=RoomManager.Instance.GetRoom(user.roomId);
		if(room==null) {  //开房间
			room=RoomManager.Instance.OpenRoom(user);
		}
		myRoomResponse.setRoom(room);
		response.setMyRoomRes(myRoomResponse);
		connection.send();
	}

	/**
	 * 邀请请求
	 */
	@Override
	public void OnInviteRequest(NetConnection sender, InviteRequest inviteRequest) {
		NetMessageResponse.Builder senderResponse = sender.getResponse();
		InviteResponse.Builder senderInviteResponse=InviteResponse.newBuilder();
		User senderUser=sender.getSession().user;
		User toUser=UserManager.Instance.getUser(inviteRequest.getToUserId());
		
		ResultInfo inviteResultInfo = RoomManager.Instance.AddRoomValidate(senderUser, toUser, true);
		//未效验通过给发送者响应
		if(inviteResultInfo.result == Result.Failed) {
			senderInviteResponse.setResultmsg(inviteResultInfo.result).setErrormsg(inviteResultInfo.errormsg);
			senderResponse.setInviteRes(senderInviteResponse);
			sender.send();
			return;
		}
		//通过效验,给被邀请人响应
		NetConnection toNetConnection=ConnectionManager.getConnection(inviteRequest.getToUserId());
		NetMessageResponse.Builder toResponse = toNetConnection.getResponse();
		toResponse.setInviteReq(inviteRequest);
		toNetConnection.send();
		//发送者响应提示
		TipsManager.Instance.ShowTips(senderUser.id, TipsType.Tips, null, "已发送邀请！", true);
	}

	/**
	 * 邀请响应(普通用户同意)
	 */
	@Override
	public void OnInviteResponse(NetConnection sender, InviteResponse inviteResponse) {
		User senderUser=sender.getSession().user;
		NetMessageResponse.Builder sendeRresponse = sender.getResponse();
		InviteResponse.Builder senderInviteResponseBuilder=inviteResponse.toBuilder();
		InviteRequest inviteRequest=inviteResponse.getInviteRequest();
		NetConnection fromNetConnection=ConnectionManager.getConnection(inviteRequest.getFromUserId());  //房主
		InviteResponse.Builder fromInviteResponseBuilder=inviteResponse.toBuilder();
		
		if(inviteResponse.getResultmsg()==Result.Success) {
			if(fromNetConnection==null) {
				senderInviteResponseBuilder.setResultmsg(Result.Failed).setErrormsg("请求者已下线！");
			}else { //同意邀请
				User fromUser=fromNetConnection.getSession().user;
				NetMessageResponse.Builder fromResponse = fromNetConnection.getResponse();
				ResultInfo resultInfo = RoomManager.Instance.AddRoom(fromUser, senderUser, inviteRequest.getTeamId(), false);
			    if(resultInfo.result == Result.Success) {  //加入房间成功
			    	senderInviteResponseBuilder.setResultmsg(Result.Success).setErrormsg("加入房间成功！");
					fromInviteResponseBuilder.setResultmsg(Result.Success).setErrormsg(inviteRequest.getToNickName()+"加入房间！");
			    }else {  //加入房间未成功
			    	senderInviteResponseBuilder.setResultmsg(Result.Failed).setErrormsg(resultInfo.errormsg);
			    }
			   fromResponse.setInviteRes(fromInviteResponseBuilder.build());
			   fromNetConnection.send();
			}
		}else {  //拒绝邀请，给邀请者消息
			if(fromNetConnection != null) {
				NetMessageResponse.Builder fromResponse = fromNetConnection.getResponse();
				fromInviteResponseBuilder.setResultmsg(Result.Failed).setErrormsg(inviteRequest.getToNickName()+"拒绝邀请！");
				fromResponse.setInviteRes(fromInviteResponseBuilder.build());
				fromNetConnection.send();
			}
		}
		sendeRresponse.setInviteRes(senderInviteResponseBuilder.build());
		sender.send();
	}

	/**
	 * 踢出请求
	 */
	@Override
	public void OnKickOut(NetConnection connection, KickOutRequest kickOutRequest) {
		User user=connection.getSession().user;
		NetMessageResponse.Builder response = connection.getResponse();
		KickOutResponse.Builder kickOutResponse=KickOutResponse.newBuilder();
		
		User outUser=UserManager.Instance.getUser(kickOutRequest.getUserId());  //踢出用户
		ResultInfo resultInfo = RoomManager.Instance.KickOut(user, outUser);
		kickOutResponse.setResult(resultInfo.result).setErrormsg(resultInfo.errormsg);
		
		response.setKickOutRes(kickOutResponse);
		connection.send();
	}

	/**
	 * 开始游戏请求
	 */
	@Override
	public void OnRoomStartGame(NetConnection connection, RoomStartGameRequest roomStartGameRequest) {
		User user=connection.getSession().user;
		NetMessageResponse.Builder response = connection.getResponse();
		RoomStartGameResponse.Builder roomStartGameResponse=RoomStartGameResponse.newBuilder();
		
		ResultInfo resultInfo = RoomManager.Instance.StartGame(user);
		if(resultInfo.result==Result.Success) {   //开始游戏成功，开始匹配
			MatchManager.Instance.StartGameForRoom(RoomManager.Instance.GetRoom(user).toBuilder());
			//matchService.OnStartMatch(connection, null);
		}
		roomStartGameResponse.setResult(resultInfo.result).setErrormsg(resultInfo.errormsg);
		
		response.setRoomStartGameRes(roomStartGameResponse);
		connection.send();
	}

	/**
	 * 昵称搜索请求
	 */
	@Override
	public void OnNickNameSearch(NetConnection connection, NickNameSearchRequest nickNameSearchRequest) {
		User user=connection.getSession().user;
		NetMessageResponse.Builder response = connection.getResponse();
		NickNameSearchResponse.Builder nickNameSearchResponse=NickNameSearchResponse.newBuilder();
		List<User> normalUserList=UserManager.Instance.queryByUserStatus(UserStatus.Normal);
		if(StringUtils.isEmpty(nickNameSearchRequest.getNickName())){ 
			//打乱顺序
			if(normalUserList != null && normalUserList.size() > 0) {
				Collections.shuffle(normalUserList);
			}
		}else {  //按昵称查找
			List<User> queryUserList=new ArrayList<User>();
			for (User normalUser : normalUserList) {
				if(normalUser.nickname.contains(nickNameSearchRequest.getNickName())) {
					queryUserList.add(normalUser);
				}
			}
			normalUserList=queryUserList;
		}
		List<RoomUser> roomUserList=new ArrayList<>();
		for (int i = 0; i < normalUserList.size(); i++) {
			if(i > 2) {
				break;	
			}
			User normalUser=normalUserList.get(i);
			RoomUser.Builder roomUser=RoomUser.newBuilder();
			roomUser.setUserId(normalUser.id);
			roomUser.setNickName(normalUser.nickname);
			roomUser.setCCharacterId(normalUser.character.cId);
			roomUserList.add(roomUser.build());
		}
		nickNameSearchResponse.addAllRoomUser(roomUserList);
		response.setNickNameSearchRes(nickNameSearchResponse);
		connection.send();
	}

	/**
	 * 加入房间请求
	 */
	@Override
	public void OnAddRoomRequest(NetConnection sender, AddRoomRequest addRoomRequest) {
		User senderUser=sender.getSession().user;
		NetMessageResponse.Builder senderResponse = sender.getResponse();
		AddRoomResponse.Builder senderAddRoomResponse=AddRoomResponse.newBuilder();
		
		NRoom room=RoomManager.Instance.GetRoom(addRoomRequest.getRoomId());
		if(room==null) {
			senderAddRoomResponse.setResult(Result.Failed).setErrormsg("未找到房间！");
			senderResponse.setAddRoomRes(senderAddRoomResponse);
			sender.send();
			return;
		}
		
		User roomOwnerUser = UserManager.Instance.getUser(room.getUserId());
		ResultInfo resultInfo = RoomManager.Instance.AddRoomValidate(roomOwnerUser, senderUser, false);
		//未效验通过给发送者响应
		if(resultInfo.result==Result.Failed) {			
			senderAddRoomResponse.setResult(resultInfo.result).setErrormsg(resultInfo.errormsg);
			senderResponse.setAddRoomRes(senderAddRoomResponse);
			sender.send();
			return;
		}
		//通过效验,给接收响应(房主)
		NetConnection toNetConnection=ConnectionManager.getConnection(roomOwnerUser.id);
		NetMessageResponse.Builder toResponse = toNetConnection.getResponse();
		toResponse.setAddRoomReq(addRoomRequest);
		toNetConnection.send();
		//发送者响应提示
		TipsManager.Instance.ShowTips(senderUser.id, TipsType.Tips, null, "已发送申请！", true);
	}

	/**
	 * 加入房间响应(房主同意)
	 */
	@Override
	public void OnAddRoomResponse(NetConnection sender, AddRoomResponse addRoomResponse) {
		User senderUser=sender.getSession().user;
		NetMessageResponse.Builder response = sender.getResponse();
		AddRoomResponse.Builder senderAddRoomResponseBuilder=addRoomResponse.toBuilder();
		AddRoomRequest addRoomRequest=addRoomResponse.getAddRoomRequest();
		NetConnection fromNetConnection=ConnectionManager.getConnection(addRoomRequest.getFromUserId());
		AddRoomResponse.Builder fromAddRoomResponseBuilder=addRoomResponse.toBuilder();
		if(addRoomResponse.getResult()==Result.Success) {
			if(fromNetConnection==null) {
				senderAddRoomResponseBuilder.setResult(Result.Failed).setErrormsg("请求者已下线！");
			}else { //同意加入(房主)
				User fromUser=fromNetConnection.getSession().user;
				NetMessageResponse.Builder fromResponse = fromNetConnection.getResponse();
				ResultInfo resultInfo = RoomManager.Instance.AddRoom(senderUser, fromUser, senderAddRoomResponseBuilder.getTeamId(), true);
			    if(resultInfo.result == Result.Success) {  //加入房间成功
			    	fromAddRoomResponseBuilder.setResult(Result.Success).setErrormsg("加入房间成功！");
			    	senderAddRoomResponseBuilder.setResult(Result.Success).setErrormsg(addRoomRequest.getFromNickName()+"加入房间！");
			    }else {  //加入房间未成功
			    	senderAddRoomResponseBuilder.setResult(Result.Failed).setErrormsg(resultInfo.errormsg);
			    }
			   fromResponse.setAddRoomRes(fromAddRoomResponseBuilder.build());
			   fromNetConnection.send();
			}
		}else {  //房主拒绝加入
			if(fromNetConnection != null) {
				NetMessageResponse.Builder fromResponse = fromNetConnection.getResponse();
				fromAddRoomResponseBuilder.setResult(Result.Failed).setErrormsg("房主拒绝加入");
				fromResponse.setAddRoomRes(fromAddRoomResponseBuilder.build());
				fromNetConnection.send();
			}
		}
		response.setAddRoomRes(senderAddRoomResponseBuilder.build());
		sender.send();
	}

	/**
	 * 退出房间请求
	 */
	@Override
	public void OnOutRoom(NetConnection sender, OutRoomRequest outRoomRequest) {
		User user=sender.getSession().user;
		NetMessageResponse.Builder response = sender.getResponse();
		OutRoomResponse.Builder outRoomResponse=OutRoomResponse.newBuilder();
		ResultInfo ret=RoomManager.Instance.OutRoom(user);
		outRoomResponse.setResult(ret.result).setErrormsg(ret.errormsg);
		response.setOutRoomRes(outRoomResponse);
		sender.send();
	}

	/**
	 * 游戏结束请求
	 */
	@Override
	public void OnGameOver2(NetConnection sender, GameOver2Request gameOver2Request) {
		User user=sender.getSession().user;
		RoomManager.Instance.ValidateGameIsOver(user, gameOver2Request);
	}

	/**
	 * 进入直播请求
	 */
	@Override
	public void OnAddLive(NetConnection sender, AddLiveRequest addLiveRequest) {
		User user=sender.getSession().user;
		NetMessageResponse.Builder response = sender.getResponse();

		ResultInfo<NRoom> ret=RoomManager.Instance.AddLive(user, addLiveRequest.getUserId());

		AddLiveResponse.Builder addLiveResponse=AddLiveResponse.newBuilder();
		addLiveResponse.setResult(ret.result).setErrormsg(ret.errormsg).setRoom(ret.data);
		response.setAddLiveRes(addLiveResponse);
		sender.send();
	}

	/**
	 * 上传比分请求
	 */
	@Override
	public void OnUploadBiFen(NetConnection sender, UploadBiFenRequest uploadBiFenRequest) {
		User user=sender.getSession().user;
		RoomManager.Instance.UploadBiFen(user, uploadBiFenRequest.getBiFen());
	}

	/**
	 * 效验是否可以开房间请求
	 */
	@Override
	public void OnValidateOpenRoom(NetConnection connection, ValidateOpenRoomRequest validateOpenRoomRequest) {
		User user=connection.getSession().user;
		NetMessageResponse.Builder response = connection.getResponse();
		
		ValidateOpenRoomResponse.Builder validateOpenRoomResponse=ValidateOpenRoomResponse.newBuilder();
		
		ResultInfo resultInfo = RoomManager.Instance.ValidateOpenRoom(user);
		validateOpenRoomResponse.setResult(resultInfo.result).setErrormsg(resultInfo.errormsg);
		
		response.setValidateOpenRoomRes(validateOpenRoomResponse);
		connection.send();
	}

	
}
