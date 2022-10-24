package com.game.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

@Sharable
public class Prepender extends MessageToByteEncoder<ByteBuf> {
    public Prepender() {
    }

    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
        int bodyLen = msg.readableBytes();
        out.ensureWritable(4 + bodyLen);
        out.writeByte(bodyLen & 0xff);
        out.writeByte((0xff00 & bodyLen) >> 8 );
        out.writeByte((0xff0000 & bodyLen) >> 16);
        out.writeByte((0xff000000 & bodyLen) >> 24);

        out.writeBytes(msg, msg.readerIndex(), bodyLen);
    }

}
