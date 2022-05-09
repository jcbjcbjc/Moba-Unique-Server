package com.game.netty;

import com.game.network.MessageDispatch;
import com.game.proto.C2GNet;
import com.game.service.UserService;
import com.game.spring.SpringBeanUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

@Component
@ChannelHandler.Sharable
public class TCPServerHandler extends SimpleChannelInboundHandler<Object> {

    static UserService userService;

    public static void init() {
        userService = SpringBeanUtil.getBean(UserService.class);
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf Buf=(ByteBuf) msg;

        final byte[] array;
        final int offset;
        final int length = Buf.readableBytes();
        if (Buf.hasArray()) {
            array = Buf.array();
            offset = Buf.arrayOffset() + Buf.readerIndex();
        } else {
            array = ByteBufUtil.getBytes(Buf, Buf.readerIndex(), length, false);
            offset = 0;
        }
        C2GNet.C2GNetMessage nm = C2GNet.C2GNetMessage.getDefaultInstance().getParserForType().parseFrom(array, offset, length);
        System.out.println(nm);
        MessageDispatch.Instance.receiveData(ctx, nm.toBuilder());
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
//      userService = SpringBeanUtil.getBean(UserService.class);
        userService.gameLeave(ctx);
        ctx.flush();
        ctx.close();
    }

    /**
     * 异常处理
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        // 发生异常 关闭连接
        System.out.println("引擎"+getRemoteAddress(ctx)+"的通道发生异常，断开连接");
        ctx.close();
    }

    /**
     * 心跳机制 超时处理
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // 心跳包检测读超时
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.ALL_IDLE) {
                ctx.channel().close();
            }
        }
    }

    /**
     * 获取client对象：ip+port
     * @param channelHandlerContext
     * @return
     */
    public String getRemoteAddress(ChannelHandlerContext channelHandlerContext){
        String socketString = "";
        socketString = channelHandlerContext.channel().remoteAddress().toString();
        return socketString;
    }

    /**
     * 获取client的ip
     * @param channelHandlerContext
     * @return
     */
    public String getIPString(ChannelHandlerContext channelHandlerContext){
        String ipString = "";
        String socketString = channelHandlerContext.channel().remoteAddress().toString();
        int colonAt = socketString.indexOf(":");
        ipString = socketString.substring(1,colonAt);
        return ipString;
    }
}