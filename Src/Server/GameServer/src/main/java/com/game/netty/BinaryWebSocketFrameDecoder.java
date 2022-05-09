package com.game.netty;

import com.game.dao.UserDao;
import com.game.manager.ConnectionManager;
import com.game.network.MessageDispatch;
import com.game.network.NetConnection;
import com.game.service.UserService;
import com.game.spring.SpringBeanUtil;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.codec.CorruptedFrameException;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.game.proto.C2GNet.*;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * I/O数据读写处理类
 */
@Sharable
public class BinaryWebSocketFrameDecoder extends SimpleChannelInboundHandler<BinaryWebSocketFrame> {

    static UserService userService;

    public static void init() {
        userService = SpringBeanUtil.getBean(UserService.class);
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
        C2GNetMessage nm = C2GNetMessage.getDefaultInstance().getParserForType().parseFrom(array, offset, length);
        System.out.println(nm);
        MessageDispatch.Instance.receiveData(ctx, nm.toBuilder());
    }

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
}