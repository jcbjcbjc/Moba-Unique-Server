package com.game.network.Server;

import com.game.network.Connection.ConnectionManager;

import com.game.network.MessageDispatch;
//import com.game.proto.Message;
import com.game.network.proto.C2BNet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

import java.net.InetSocketAddress;

public class WebSocketServerHandler extends SimpleChannelInboundHandler<BinaryWebSocketFrame> {
    public static void init() {

    }
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIp = insocket.getAddress().getHostAddress();
        //此处不能使用ctx.close()，否则客户端始终无法与服务端建立连接
        System.out.println("handlerAdded:" + clientIp + ctx.name());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerRemoved");
        // 清除 NetConnection
        ConnectionManager.removeConnection(ctx);
        ctx.flush();
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        // 发生异常之后关闭连接（关闭channel），随后从ChannelGroup中移除
//        ctx.channel().close();
        System.out.println("exceptionCaught");
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BinaryWebSocketFrame frame) throws Exception {
        BinaryWebSocketFrame img= (BinaryWebSocketFrame) frame;
        ByteBuf msg=img.content();

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
        C2BNet.C2BNetMessage nm = C2BNet.C2BNetMessage.getDefaultInstance().getParserForType().parseFrom(array, offset, length);
//        System.out.println(nm);
        MessageDispatch.Instance.DispatchData(ctx, nm.getRequest());
    }

	/*@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		// 心跳包检测读超时
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent e = (IdleStateEvent) evt;
			if (e.state() == IdleState.ALL_IDLE) {
				ctx.channel().close();
			}
		}
	}*/
}
