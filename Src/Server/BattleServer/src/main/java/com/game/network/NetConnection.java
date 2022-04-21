package com.game.network;

import com.game.models.User;
import com.game.proto.Message.*;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author 贾超博
 *
 * Entity Class
 *
 *
 *
 */
// 发送消息
public class NetConnection {

    // 连接管道，保存长连接信息
    public ChannelHandlerContext ctx;
    public User user;

    public NetConnection(ChannelHandlerContext ctx, User user) {
        this.ctx = ctx;
        this.user=user;
    }
    
    private NetMessageResponse2.Builder message;

    public NetMessageResponse2.Builder getResponse() {
        if (message == null) {
            message = NetMessageResponse2.newBuilder();
        }
        return message;
    }
    
    /**
     * 发送帧操作
     * @param message2
     */
    public void sendFrameHandleRes(NetMessageResponse2.Builder message2) {
    	if(message != null) {   //合并包
    		message.setFrameHandleRes(message2.getFrameHandleRes());
    		this.send();
    	}else {    		
    		ctx.writeAndFlush(message2);
    	}
    }
    
    /**
     * 发送直播帧操作
     * @param message2
     */
    public void sendLiveFrameRes(NetMessageResponse2.Builder message2) {
    	if(message != null) {   //合并包
    		message.setLiveFrameRes(message2.getLiveFrameRes());
    		this.send();
    	}else {    		
    		ctx.writeAndFlush(message2);
    	}
    }
    
    public void send() {
      if(message != null) {	
        ctx.writeAndFlush(message);
        message=null;
      }
    }

    
    public void send(NetMessageResponse2.Builder message2) {
        ctx.writeAndFlush(message2);
    }
}
