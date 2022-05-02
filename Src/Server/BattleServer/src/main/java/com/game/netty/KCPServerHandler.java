package com.game.netty;
import com.backblaze.erasure.FecAdapt;
import com.backblaze.erasure.fec.Snmp;

import com.game.manager.ConnectionManagerKCP;
import com.game.proto.Message;
import com.google.protobuf.InvalidProtocolBufferException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import kcp.ChannelConfig;
import kcp.KcpListener;
import kcp.KcpServer;
import kcp.Ukcp;
import com.game.network.MessageDispatch;

public class KCPServerHandler implements KcpListener {


    @Override
    public void onConnected(Ukcp ukcp) {

        System.out.println("有连接进来"+Thread.currentThread().getName()+ukcp.user().getRemoteAddress());
    }

    @Override
    public void handleReceive(ByteBuf buf, Ukcp kcp) {
       /* short curCount = buf.getShort(buf.readerIndex());
        System.out.println(Thread.currentThread().getName()+"  收到消息 "+curCount);
        kcp.write(buf);
        if (curCount == -1) {
            kcp.close();
        }*/
        final byte[] array;
        final int offset;
        final int length = buf.readableBytes();
        if (buf.hasArray()) {
            array = buf.array();
            offset = buf.arrayOffset() + buf.readerIndex();
        } else {
            array = ByteBufUtil.getBytes(buf, buf.readerIndex(), length, false);
            offset = 0;
        }
        Message.NetMessageRequest2 nm = null;
        try {
            nm = Message.NetMessageRequest2.getDefaultInstance().getParserForType().parseFrom(array, offset, length);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
//        System.out.println(nm);
        MessageDispatch.Instance.DispatchData(kcp,nm);
    }

    @Override
    public void handleException(Throwable ex, Ukcp kcp) {
        ex.printStackTrace();
    }

    @Override
    public void handleClose(Ukcp kcp) {
        ConnectionManagerKCP.removeConnection(kcp);
        System.out.println(Snmp.snmp.toString());
        Snmp.snmp  = new Snmp();
    }
}
