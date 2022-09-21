package com.game.network;
import com.game.models.User;
import com.game.proto.C2BNet;
import com.game.proto.Message.*;
import io.netty.channel.ChannelHandlerContext;

public class NetConnection {
    // 连接管道，保存长连接信息
    public ChannelHandlerContext ctx;
    public User user;

    public NetConnection(ChannelHandlerContext ctx, User user) {
        this.ctx = ctx;
        this.user=user;
    }
    private C2BNet.C2BNetMessage.Builder netMessage= C2BNet.C2BNetMessage.newBuilder();
    private C2BNet.C2BNetMessageResponse.Builder message;

    public C2BNet.C2BNetMessageResponse.Builder getResponse() {
        if (message == null) {
            message = C2BNet.C2BNetMessageResponse.newBuilder();
        }
        return message;
    }

    /**
     * 发送帧操作
     * @param message2
     */
    public void sendFrameHandleRes(C2BNet.C2BNetMessageResponse.Builder message2) {
        if(message != null) {   //合并包
            message.setFrameHandleRes(message2.getFrameHandleRes());
            this.send();
        }else {
            netMessage.setResponse(message2);
            ctx.writeAndFlush(netMessage);
            netMessage=C2BNet.C2BNetMessage.newBuilder();
        }
    }

    /**
     * 发送直播帧操作
     * @param message2
     */
    public void sendLiveFrameRes(C2BNet.C2BNetMessageResponse.Builder message2) {
        if(message != null) {   //合并包
            message.setLiveFrameRes(message2.getLiveFrameRes());
            this.send();
        }else {
            netMessage.setResponse(message2);
            ctx.writeAndFlush(netMessage);
            netMessage=C2BNet.C2BNetMessage.newBuilder();
        }
    }

    public void send() {
        if(message != null) {
            netMessage.setResponse(message);
            ctx.writeAndFlush(netMessage);
            message=null;
            netMessage=C2BNet.C2BNetMessage.newBuilder();
        }
    }


    public void send(C2BNet.C2BNetMessage.Builder message2) {
        ctx.writeAndFlush(message2);
    }
}
