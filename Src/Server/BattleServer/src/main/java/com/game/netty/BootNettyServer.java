package com.game.netty;

import com.game.proto.Message.*;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
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
        EventLoopGroup bossGroup = new NioEventLoopGroup(2);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            /**
             * ServerBootstrap 是一个启动NIO服务的辅助启动类
             */
            ServerBootstrap bootstrap = new ServerBootstrap();
            /**
             * 设置group，将bossGroup， workerGroup线程组传递到ServerBootstrap
             */
            bootstrap = bootstrap.group(bossGroup, workerGroup);
            /**
             * ServerSocketChannel是以NIO的selector为基础进行实现的，用来接收新的连接，这里告诉Channel通过NioServerSocketChannel获取新的连接
             */
            bootstrap = bootstrap.channel(NioServerSocketChannel.class);
            /**
             * option是设置 bossGroup，childOption是设置workerGroup
             * netty 默认数据包传输大小为1024字节, 设置它可以自动调整下一次缓冲区建立时分配的空间大小，避免内存的浪费    最小  初始化  最大 (根据生产环境实际情况来定)
             * 使用对象池，重用缓冲区
             */
            // bootstrap = bootstrap.option(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator(64, 10496, 1048576));
            // bootstrap = bootstrap.childOption(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator(64, 10496, 1048576));
            bootstrap = bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
            bootstrap = bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);


            /**
             * 设置 I/O处理类,主要用于网络I/O事件，记录日志，编码、解码消息  -- 设值处理器
             */
            bootstrap = bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                	ChannelPipeline pipeline = ch.pipeline();
                	pipeline.addLast(new HttpServerCodec());
                	pipeline.addLast(new ChunkedWriteHandler());
                	pipeline.addLast(new HttpObjectAggregator(65536));
                	pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
                	
                	pipeline.addLast(new BinaryWebSocketFrameEncoder());
                	pipeline.addLast(new ProtobufEncoder());

                	pipeline.addLast(new BinaryWebSocketFrameDecoder());
                	// 客户端300秒没收发包，便会触发UserEventTriggered事件到IdleEventHandler
//        			pipeline.addLast(new IdleStateHandler(0, 0, 300));

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
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
