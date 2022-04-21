package com.game.network;

import com.game.proto.Message.*;
import io.netty.channel.ChannelHandlerContext;
/**
 * @author 贾超博
 *
 * Component in network
 *
 * NetConnection Class
 *
 *
 */

// 发送消息
public class NetConnection {

    // 连接管道，保存长连接信息
    public ChannelHandlerContext ctx;

    // 记录当前 用户的信息
    NetSession session = new NetSession();

    // 做后处理
    private NetMessage.Builder netMessage;
    private NetMessageResponse.Builder message;


    public NetSession getSession() {
        return session;
    }

    public NetConnection(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    public NetMessageResponse.Builder getResponse() {
        if (netMessage == null) {
            netMessage = NetMessage.newBuilder();
            message = NetMessageResponse.newBuilder();
        }
        return message;
    }


    public void send() {
        getResponse();
        // 做后处理
        if(session.user != null){
            message = session.user.postProcess(message);
        }

        netMessage.setResponse(message);
        // System.out.println("build: " + netMessage);
        ctx.write(netMessage);
        ctx.flush();
        netMessage = null;
    }

}
