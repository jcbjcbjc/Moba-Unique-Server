package com.game.netty;

import com.game.proto.Message.*;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

public class BootNettyServer {

    public void bind(int port) throws Exception {

        /**
         * 配置服务端的NIO线程组
         * NioEventLoopGroup 是用来处理I/O操作的Reactor线程组
         * bossGroup：用来接收进来的连接，workerGroup：用来处理已经被接收的连接,进行socketChannel的网络读写，
         * bossGroup接收到连接后就会把连接信息注册到workerGroup
         * workerGroup的EventLoopGroup默认的线程数是CPU核数的二倍
         */
        // 这两个都是无限循环的
        //EventLoopGroup bossGroup = new NioEventLoopGroup(2);
        EventLoopGroup Group = new NioEventLoopGroup();

        try {
            Bootstrap  bootstrap = new  Bootstrap ();
            bootstrap.group(Group)
                    // 主线程处理
                    .channel(NioDatagramChannel.class)
                    // 广播
                    .option(ChannelOption.SO_BROADCAST, true)
                    // 设置读缓冲区为2M
                    .option(ChannelOption.SO_RCVBUF, 2048 * 1024)
                    // 设置写缓冲区为1M
                    .option(ChannelOption.SO_SNDBUF, 1024 * 1024)
                    .handler(new ChannelInitializer<NioDatagramChannel>() {

                        @Override
                        protected void initChannel(NioDatagramChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new HttpServerCodec());
                            pipeline.addLast(new ChunkedWriteHandler());
                            pipeline.addLast(new HttpObjectAggregator(65536));

                            pipeline.addLast( new UdpServerHandler());

                        }
                    });

            System.out.println("netty server start success!");
            /**
             * 绑定端口，并且同步,生成ChannelFuture对象,这里已经启动服务器
             */
            ChannelFuture cf = bootstrap.bind(port).sync();
            /**
             * 对关闭通道进行监听
             */
            cf.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            System.out.println("关闭线程");
        } finally {
            /**
             * 退出，释放线程池资源
             */
            System.out.println("释放线程池资源");
            Group.shutdownGracefully();
        }

    }
}
