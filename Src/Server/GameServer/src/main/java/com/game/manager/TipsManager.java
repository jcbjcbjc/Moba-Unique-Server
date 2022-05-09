package com.game.manager;

import com.game.network.NetConnection;
import com.game.proto.Message.HeartBeatResponse;
import com.game.proto.C2GNet.NetMessageResponse;
import com.game.proto.C2GNet.TipsResponse;
import com.game.proto.C2GNet.TipsType;
import com.game.proto.C2GNet.TipsWorkType;
//import com.game.proto.Message.NetMessageResponse;
//import com.game.proto.Message.TipsResponse;
//import com.game.proto.Message.TipsType;
//import com.game.proto.Message.TipsWorkType;

public class TipsManager {
	public static TipsManager Instance=new TipsManager();
	
	/**
	 * 给指定的用户提示
	 * @param userId
	 * @param tipsType
	 * @param tipsWorkType
	 * @param content
	 * @param isSend  是否立即发送
	 */
	public void ShowTips(int userId, TipsType tipsType, TipsWorkType tipsWorkType,String content,boolean isSend) {
		NetConnection connection=ConnectionManager.getConnection(userId);
		if(connection == null) {
			return;
		}
		NetMessageResponse.Builder response = connection.getResponse();
		TipsResponse.Builder tipsResponse=TipsResponse.newBuilder();
		tipsResponse.setTipsType(tipsType); 
		if(tipsWorkType != null) {
		   tipsResponse.setTipsWorkType(tipsWorkType);
		}
		tipsResponse.setContent(content);
		response.setTipsRes(tipsResponse);
		if (isSend) {			
			connection.send();
		}
	}
}
