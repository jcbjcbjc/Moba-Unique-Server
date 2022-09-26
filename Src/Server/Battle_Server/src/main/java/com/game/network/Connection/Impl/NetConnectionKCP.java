package com.game.network.Connection.Impl;
import com.game.models.User;
//import com.game.proto.Message;
import com.game.network.Connection.NetConnection;
import com.game.network.proto.C2BNet;
import io.netty.buffer.Unpooled;
import kcp.Ukcp;

public class NetConnectionKCP implements NetConnection {
    public Ukcp kcp;
    public User user;

    public NetConnectionKCP(User user, Ukcp kcp) {
        this.kcp = kcp;
        this.user=user;
    }

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
    @Override
    public void sendFrameHandleRes(C2BNet.C2BNetMessageResponse.Builder message2) {
        if(message != null) {   //合并包
            message.setFrameHandleRes(message2.getFrameHandleRes());
            this.send();
        }else {
            this.kcp.write(Unpooled.wrappedBuffer(message2.build().toByteArray()));
        }
    }

    /**
     * 发送直播帧操作
     * @param message2
     */
    @Override
    public void sendLiveFrameRes(C2BNet.C2BNetMessageResponse.Builder message2) {
        if(message != null) {   //合并包
            message.setLiveFrameRes(message2.getLiveFrameRes());
            this.send();
        }else {
            this.kcp.write(Unpooled.wrappedBuffer(message2.build().toByteArray()));
        }
    }
    @Override
    public void send() {
        if(message != null) {
            this.kcp.write(Unpooled.wrappedBuffer(message.build().toByteArray()));
            message=null;
        }
    }

    @Override
    public void send(C2BNet.C2BNetMessage.Builder message2) {
        this.kcp.write(Unpooled.wrappedBuffer(message2.build().toByteArray()));
    }
}
