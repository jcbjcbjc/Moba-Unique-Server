package com.game.netty;
import com.backblaze.erasure.FecAdapt;
import com.backblaze.erasure.fec.Snmp;

import com.game.manager.ConnectionManagerKCP;
//import com.game.proto.Message;
import com.game.proto.C2BNet;
import com.google.protobuf.InvalidProtocolBufferException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.util.CharsetUtil;
import kcp.ChannelConfig;
import kcp.KcpListener;
import kcp.KcpServer;
import kcp.Ukcp;
import com.game.network.MessageDispatch;
import com.game.proto.C2BNet;

import java.nio.charset.Charset;

public class KCPServerHandler implements KcpListener {

    @Override
    public void onConnected(Ukcp ukcp) {
        System.out.println("有连接进来"+Thread.currentThread().getName()+ukcp.user().getRemoteAddress());
    }

    @Override
    public void handleReceive(ByteBuf buf, Ukcp kcp) {
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
        C2BNet.C2BNetMessage nm = null;
        try {
            nm = C2BNet.C2BNetMessage.getDefaultInstance().getParserForType().parseFrom(array, offset, length);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
//        System.out.println(nm);
        assert nm != null;
        MessageDispatch.Instance.DispatchData(kcp,nm.getRequest());
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
