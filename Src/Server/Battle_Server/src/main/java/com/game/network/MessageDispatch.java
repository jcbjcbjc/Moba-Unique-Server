package com.game.network;

import com.game.manager.UserManager;
import com.game.models.User;
//import com.game.proto.Message;
//import com.game.proto.Message.*;
import com.game.network.Connection.ConnectionManager;
import com.game.network.Connection.NetConnection;
import com.game.network.proto.C2BNet;

import com.game.network.service.BattleService;
import com.game.service.*;
import com.game.spring.SpringBeanUtil;

import io.netty.channel.ChannelHandlerContext;
import kcp.Ukcp;

// 接收消息分发
public class MessageDispatch {
    BattleService battleService;
    UserManager userManager;
    public MessageDispatch() {
    	battleService = SpringBeanUtil.getBean(BattleService.class);
    	userManager=UserManager.Instance;
    }

    public static MessageDispatch Instance=new MessageDispatch();

    public void DispatchData(ChannelHandlerContext ctx, C2BNet.C2BNetMessageRequest message) {
		int userId=message.getUserId();
		User battleUser=userManager.users.get(userId);  //对战用户
		User liveUser=userManager.liveUsers.get(userId);  //直播用户
		if(battleUser==null && liveUser==null) {  //非法请求
			System.out.println("非法请求");
			return;
		}

		NetConnection conn= ConnectionManager.getConnection(userId);
		if (conn == null) {
			conn=new NetConnection(ctx, battleUser != null ? battleUser : liveUser);
			ConnectionManager.addToConnection(userId, conn);
		}else {
			conn.ctx=ctx;
		}

		//帧操作请求
		if (message.hasFrameHandles()) {
			battleService.OnFrameHandle(conn, message.getFrameHandles());
			return;
		}

		//补帧请求
		if (message.hasRepairFrameReq()) {
			battleService.OnRepairFrame(conn, message.getRepairFrameReq());
			return;
		}

		//进度转发请求
		if (message.hasPercentForward()) {
			battleService.OnPercentForward(conn, message.getPercentForward());
			return;
		}

		//游戏结束请求
		if (message.hasGameOverReq()) {
			battleService.OnGameOver(conn, message.getGameOverReq());
			return;
		}


	}
	public void DispatchData(Ukcp kcp, C2BNet.C2BNetMessageRequest message) {
	}
}
