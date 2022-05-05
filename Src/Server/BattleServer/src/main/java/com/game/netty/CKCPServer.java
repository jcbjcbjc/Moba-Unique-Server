package com.game.netty;

import com.backblaze.erasure.fec.Snmp;
import com.game.manager.ConnectionManagerKCP;
import com.game.network.MessageDispatch;
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
        if (c == 0) {
            start = System.currentTimeMillis();
        }
        c++;
        String content = buf.toString(Charset.forName("utf-8"));
        System.out.println("msg:" + content + " kcp--> " + kcp);
        if (c < 10000) {
            kcp.send(buf);//echo
        } else {
            System.out.println("cost:" + (System.currentTimeMillis() - start));
            this.close();
        }
        /*final byte[] array;
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
        MessageDispatch.Instance.DispatchData(kcp,nm);*/
    }

    @Override
    public void handleException(Throwable ex, KcpOnUdp kcp) {
        System.out.println(ex);
    }

    @Override
    public void handleClose(KcpOnUdp kcp) {
        ConnectionManagerKCP.removeConnection(kcp);
        System.out.println(Snmp.snmp.toString());
        Snmp.snmp  = new Snmp();
    }

    private static long start;
    private static int c = 0;


}