package com.game.network;
import com.backblaze.erasure.FecAdapt;
import com.backblaze.erasure.fec.Snmp;
import com.game.models.User;
//import com.game.proto.Message;
import com.game.proto.C2BNet;
import com.google.protobuf.MessageLite;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import kcp.ChannelConfig;
import kcp.KcpListener;
import kcp.KcpServer;
import kcp.Ukcp;

import java.net.InetSocketAddress;

public class NetConnectionKCP {
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
    public void sendLiveFrameRes(C2BNet.C2BNetMessageResponse.Builder message2) {
        if(message != null) {   //合并包
            message.setLiveFrameRes(message2.getLiveFrameRes());
            this.send();
        }else {
            this.kcp.write(Unpooled.wrappedBuffer(message2.build().toByteArray()));
        }
    }

    public void send() {
        if(message != null) {
            this.kcp.write(Unpooled.wrappedBuffer(message.build().toByteArray()));
            message=null;
        }
    }


    public void send(C2BNet.C2BNetMessageResponse.Builder message2) {
        this.kcp.write(Unpooled.wrappedBuffer(message2.build().toByteArray()));
    }
}
