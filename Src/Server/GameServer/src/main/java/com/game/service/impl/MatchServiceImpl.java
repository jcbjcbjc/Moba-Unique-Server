package com.game.service.impl;

import org.springframework.stereotype.Service;

import com.game.entity.User;
import com.game.manager.ConnectionManager;
import com.game.manager.MatchManager;
import com.game.manager.RoomManager;
import com.game.network.NetConnection;
import com.game.proto.C2GNet.MatchResponse;
//import com.game.proto.Message.MatchResponse;
import com.game.proto.Message.MyRoomResponse;
import com.game.proto.C2GNet.NRoom;
import com.game.proto.C2GNet.NetMessageResponse;
import com.game.proto.C2GNet.Result;
import com.game.proto.C2GNet.StartMatchRequest;
import com.game.proto.C2GNet.StartMatchResponse;
import com.game.proto.C2GNet.UserStatus;
//import com.game.proto.Message.MatchResponse;
//import com.game.proto.Message.MyRoomResponse;
//import com.game.proto.Message.NRoom;
//import com.game.proto.Message.NetMessageResponse;
//import com.game.proto.Message.Result;
//import com.game.proto.Message.StartMatchRequest;
//import com.game.proto.Message.StartMatchResponse;
//import com.game.proto.Message.UserStatus;
import com.game.service.MatchService;
import com.game.vo.ResultInfo;
@Service
public class MatchServiceImpl implements MatchService {

	/**
	 * 开始匹配
	 */
	@Override
	public void OnStartMatch(NetConnection connection, StartMatchRequest startMatchRequest) {
		User user=connection.getSession().user;
		NetMessageResponse.Builder response = connection.getResponse();
		StartMatchResponse.Builder startMatchResponse=StartMatchResponse.newBuilder();
		MatchManager.Instance.StartMatch(user);
		response.setStartMatchRes(startMatchResponse);
		connection.send();
	}

	/**
	 * 匹配响应
	 */
	@Override
	public void OnMatchResponse(int userId, ResultInfo resultInfo, NRoom.Builder roomBuilder, boolean isMatch) {
		NetConnection connection=ConnectionManager.getConnection(userId);
		User user = null;
		if(connection != null) {
			user = connection.getSession().user;			
		}
		if(resultInfo.result == Result.Success) {  //成功  
		  RoomManager.Instance.AddRoomUserIds(roomBuilder.getRoomId(), userId);//加入房间用户统计集合中
		  if(user != null) {			  
			user.setStatus(UserStatus.Game);
			user.roomId=roomBuilder.getRoomId();

			if(!isMatch) {   //未匹配
				user.liveFanSiIdList.clear();
			}
		  }
		}else {  //失败
		  RoomManager.Instance.RemoveRoomUserIds(roomBuilder.getRoomId(), userId);//移除房间用户统计集合中
		  if(user != null) {			  
	        user.setStatus(UserStatus.Normal);
		    user.roomId=0;
			user.teamId=0;
		  }
		}
		if(connection == null) {
			return;
		}
		NetMessageResponse.Builder response = connection.getResponse();
		MatchResponse.Builder matchResponse=MatchResponse.newBuilder();
		matchResponse.setResult(resultInfo.result).setErrormsg(resultInfo.errormsg).setRoom(roomBuilder);
		response.setMatchRes(matchResponse);
		connection.send();
	}


	
}
