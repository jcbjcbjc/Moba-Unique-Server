package com.game.network;

import com.game.manager.ConnectionManager;
import com.game.manager.ConnectionManagerKCP;
import com.game.manager.UserManager;
import com.game.models.User;
import com.game.proto.Message;
import com.game.proto.Message.*;
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

    public void DispatchData(ChannelHandlerContext ctx, NetMessageRequest2 message) {

        
    }
	public void DispatchData(Ukcp kcp, Message.NetMessageRequest2 message) {
		int userId=message.getUserId();
		User battleUser=userManager.getuser(userId) ;  //对战用户
		User liveUser=userManager.getliveuser(userId) ;  //直播用户
		if(battleUser==null && liveUser==null) {  //非法请求
			System.out.println("非法请求");
			return;
		}

		NetConnectionKCP conn= ConnectionManagerKCP.getConnection(userId);
		if (conn == null) {
			conn=new NetConnectionKCP( battleUser != null ? battleUser : liveUser,kcp);
			ConnectionManagerKCP.addToConnection(userId, conn);
		}else {
			conn.kcp=kcp;
		}

		//帧操作请求
		if (message.hasFrameHandle()) {
			battleService.OnFrameHandle(conn, message.getFrameHandle());
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
}
