package com.game.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.game.manager.ConnectionManagerKCP;
import com.game.network.NetConnectionKCP;
import com.game.network.NetConnectionWebSocket;
import org.springframework.stereotype.Service;

import com.game.enums.UserStatus;
import com.game.manager.ConnectionManager;
import com.game.manager.RoomManager;
import com.game.models.Room;
import com.game.models.User;
import com.game.proto.Message.FrameHandle;
import com.game.proto.Message.GameOverRequest;
import com.game.proto.Message.NetMessageResponse2;
import com.game.proto.Message.PercentForward;
import com.game.proto.Message.PercentForwardResponse;
import com.game.proto.Message.RepairFrame;
import com.game.proto.Message.RepairFrameRequest;
import com.game.proto.Message.RepairFrameResponse;
import com.game.service.BattleService;

@Service
public class BattleServiceImpl implements BattleService {

	/**
	 * 帧操作
	 */
	@Override
	public void OnFrameHandle(NetConnectionKCP connection, FrameHandle frameHandle) {
		User user=connection.user;
		Room room=RoomManager.Instance.rooms.get(user.rooomId);
		if(room==null) {
			return;
		}
		room.AddUserFrameHandle(user.id, frameHandle);
	}

	/**
	 * 进度转发
	 */
	@Override
	public void OnPercentForward(NetConnectionKCP connection, PercentForward percentForward) {
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
		
		NetMessageResponse2.Builder response=NetMessageResponse2.newBuilder();
		response.setPercentForwardRes(percentForwardBuilder);
		
		for (User u : room.users) {
				NetConnectionKCP conn= ConnectionManagerKCP.getConnection(u.id);
				if(conn == null) {
					continue;
				}
//				System.out.println(user.id +"===="+ u.id);
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
		NetMessageResponse2.Builder response=conn.getResponse();
		response.setRepairFrameRes(repairFrameResponseBuilder);
//		conn.send();
	}

}
