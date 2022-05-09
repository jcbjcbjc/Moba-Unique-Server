package com.game.netty;

import com.backblaze.erasure.fec.Snmp;
import com.game.manager.ConnectionManagerKCP;
import com.game.network.MessageDispatch;
import com.game.proto.C2BNet;
import com.game.proto.Message;
import com.google.protobuf.InvalidProtocolBufferException;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBufUtil;
import org.beykery.jkcp.KcpOnUdp;
import org.beykery.jkcp.KcpServer;

/**
 * @author beykery
 */
public class CKCPServer extends KcpServer {

    public CKCPServer(int port, int workerSize) {
        super(port, workerSize);
    }

    @Override
    public void handleReceive(ByteBuf buf, KcpOnUdp kcp) {
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
        assert nm != null;
        System.out.println(nm.getMessageType());
//        System.out.println(nm);
    }

    @Override
    public void handleException(Throwable ex, KcpOnUdp kcp) {
        System.out.println(ex);
    }

    @Override
    public void handleClose(KcpOnUdp kcp) {
        System.out.println("kasichauschiasc");
    }




}