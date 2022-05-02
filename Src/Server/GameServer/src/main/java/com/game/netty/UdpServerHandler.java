package com.game.netty;
import com.game.network.MessageDispatch;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;

import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;
import com.game.proto.Message.*;
import io.netty.channel.socket.DatagramPacket;

@Sharable
public class UdpServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
        ByteBuf msg = packet.content();

        final byte[] array;
        final int offset;
        final int length = msg.readableBytes();
        if (msg.hasArray()) {
            array = msg.array();
            offset = msg.arrayOffset() + msg.readerIndex();
        } else {
            array = ByteBufUtil.getBytes(msg, msg.readerIndex(), length, false);
            offset = 0;
        }
        NetMessage nm = NetMessage.getDefaultInstance().getParserForType().parseFrom(array, offset, length);
        System.out.println(nm);
        MessageDispatch.Instance.receiveData(ctx, nm.toBuilder(),packet.sender());
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause)
            throws Exception{
        ctx.close();
        cause.printStackTrace();
    }
}
