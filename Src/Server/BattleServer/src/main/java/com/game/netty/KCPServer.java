package com.game.netty;
import com.backblaze.erasure.FecAdapt;
import com.backblaze.erasure.fec.Snmp;
import io.netty.buffer.ByteBuf;
import kcp.ChannelConfig;
import kcp.KcpListener;
import kcp.KcpServer;
import kcp.Ukcp;
public class KCPServer implements Server{
    @Override
    public void bind(int port) throws Exception{
        KCPServerHandler KcpServerHandler = new KCPServerHandler();

        ChannelConfig channelConfig = new ChannelConfig();
        channelConfig.nodelay(true,40,2,true);
        channelConfig.setSndwnd(512);
        channelConfig.setRcvwnd(512);
        channelConfig.setMtu(512);
        channelConfig.setFecAdapt(new FecAdapt(3,1));
        channelConfig.setAckNoDelay(true);
        channelConfig.setTimeoutMillis(10000);
        channelConfig.setUseConvChannel(true);
        channelConfig.setCrc32Check(true);
        KcpServer kcpServer = new KcpServer();
        kcpServer.init(KcpServerHandler,channelConfig,port);


    }
}
