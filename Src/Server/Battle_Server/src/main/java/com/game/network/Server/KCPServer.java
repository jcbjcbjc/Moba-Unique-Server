package com.game.network.Server;
import kcp.ChannelConfig;
import kcp.KcpServer;

public class KCPServer implements Server{
    @Override
    public void bind(int port) throws Exception{
        KCPServerHandler KcpServerHandler = new KCPServerHandler();

        ChannelConfig channelConfig = new ChannelConfig();
        channelConfig.nodelay(true,40,2,true);
        channelConfig.setSndwnd(300);
        channelConfig.setRcvwnd(300);
        channelConfig.setMtu(512);
        channelConfig.setAckNoDelay(true);
        channelConfig.setTimeoutMillis(4000);
        channelConfig.setUseConvChannel(true);
        channelConfig.setCrc32Check(false);

        KcpServer kcpServer = new KcpServer();
        kcpServer.init(KcpServerHandler,channelConfig,port);


    }
}
