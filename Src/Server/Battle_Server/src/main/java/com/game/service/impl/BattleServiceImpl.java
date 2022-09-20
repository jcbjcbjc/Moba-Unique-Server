package com.game.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.game.manager.ConnectionManagerKCP;
import com.game.network.NetConnectionKCP;
import com.game.network.NetConnectionWebSocket;
import com.game.proto.C2BNet;
import org.springframework.stereotype.Service;

import com.game.enums.UserStatus;
import com.game.manager.ConnectionManager;
import com.game.manager.RoomManager;
import com.game.models.Room;
import com.game.models.User;
import com.game.proto.C2BNet.FrameHandle;
import com.game.proto.C2BNet.GameOverRequest;
import com.game.proto.C2BNet.C2BNetMessageResponse;
import com.game.proto.C2BNet.PercentForward;
import com.game.proto.C2BNet.PercentForwardResponse;
import com.game.proto.C2BNet.RepairFrame;
import com.game.proto.C2BNet.RepairFrameRequest;
import com.game.proto.C2BNet.RepairFrameResponse;
//import com.game.proto.Message.FrameHandle;
//import com.game.proto.Message.GameOverRequest;
//import com.game.proto.Message.NetMessageResponse2;
//import com.game.proto.Message.PercentForward;
//import com.game.proto.Message.PercentForwardResponse;
//import com.game.proto.Message.RepairFrame;
//import com.game.proto.Message.RepairFrameRequest;
//import com.game.proto.Message.RepairFrameResponse;
import com.game.service.BattleService;

@Service
public class BattleServiceImpl implements BattleService {

	/**
	 * 帧操作
	 */
	@Override
	public void OnFrameHandle(NetConnectionKCP connection, C2BNet.FrameHandlesFromClient frameHandles) {
		User user=connection.user;
		Room room=RoomManager.Instance.rooms.get(user.rooomId);
		if(room==null) {
			return;
		}
		for(FrameHandle fh : frameHandles.getFrameHandlesList() ){
			room.AddUserFrameHandle(user.id, fh);
		}

	}

	/**
	 * 进度转发
	 */
	@Override
	public void OnPercentForward(NetConnectionKCP connection, PercentForward percentForward) {
		System.out.println("OnPercentForward");
		User user=connection.user;
		//当前资源加载成功
		if(percentForward.getPercent() >= 100) {  
			user.userStatus=UserStatus.resourcesLoadSucess;  
		}
		//将当前用户加载进度转发其它人
		Room room=RoomManager.Instance.rooms.get(user.rooomId);
		if(room==null) {
			return;
		}
		
		PercentForwardResponse.Builder percentForwardBuilder=PercentForwardResponse.newBuilder();
		percentForwardBuilder.setPercentForward(percentForward);
		percentForwardBuilder.setAllUserLoadSucess(room.ValidateAllUserLoadSucess());
		
		C2BNetMessageResponse.Builder response=C2BNetMessageResponse.newBuilder();
		response.setPercentForwardRes(percentForwardBuilder);
		
		for (User u : room.users) {
				NetConnectionKCP conn= ConnectionManagerKCP.getConnection(u.id);
				if(conn == null) {
					continue;
				}
				//System.out.println(user.id +"===="+ u.id);
				conn.send(response);
		}
	}

	/**
	 * 游戏结束
	 */
	@Override
	public void OnGameOver(NetConnectionKCP connection, GameOverRequest gameOverRequest) {
		User user=connection.user;
		user.userStatus=UserStatus.GameOver;
		Room room=RoomManager.Instance.rooms.get(user.rooomId);
		if(room==null) {
			return;
		}
		room.ValidateGameIsOver();   //效验游戏是否结束
	}

	/**
	 * 补帧
	 */
	@Override
	public void OnRepairFrame(NetConnectionKCP conn, RepairFrameRequest repairFrameRequest) {
		User user=conn.user;
		Room room=RoomManager.Instance.rooms.get(user.rooomId);
		if(room==null) {
			return;
		}
		//获取补帧数据
		List<RepairFrame> rangeFrameList=new ArrayList<>();
		room.GetRangeFrame(repairFrameRequest.getStartFrame(), repairFrameRequest.getEndFrame(), rangeFrameList);
		
		RepairFrameResponse.Builder repairFrameResponseBuilder=RepairFrameResponse.newBuilder();
		repairFrameResponseBuilder.addAllRepairFrames(rangeFrameList);
		C2BNetMessageResponse.Builder response=conn.getResponse();
		response.setRepairFrameRes(repairFrameResponseBuilder);
//		conn.send();
	}
}
