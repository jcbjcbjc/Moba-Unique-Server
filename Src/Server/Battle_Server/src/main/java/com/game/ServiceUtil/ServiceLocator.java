package com.game.ServiceUtil;
import com.game.network.Connection.ConnectionManager;
import com.game.network.Connection.Impl.ConnectionManagerTCP;
import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.Type;
import java.util.Map;

public class ServiceLocator {
    private static ConnectionManager connectionManager=new ConnectionManagerTCP();

    public <T> T Get(){
         return
    }
}
