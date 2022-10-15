package com.game.netty;

import com.game.manager.ConnectionManager;
import com.game.manager.ConnectionManagerKCP;
import com.game.manager.RoomManager;
import com.game.network.MessageDispatch;
import com.game.proto.C2BNet;
import com.game.proto.C2BNet;
import com.game.spring.SpringBeanUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

@Component
@ChannelHandler.Sharable
public class TCPServerHandler extends ChannelInboundHandlerAdapter /*SimpleChannelInboundHandler<Object>*/ {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        C2BNet.C2BNetMessage nm=(C2BNet.C2BNetMessage) msg;
        //System.out.println(nm);
        MessageDispatch.Instance.DispatchData(ctx,nm.getRequest());
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
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIp = insocket.getAddress().getHostAddress();
        System.out.println("handlerRemoved"+ clientIp + ctx.name());
        ConnectionManager.removeConnection(ctx);
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