package com.game.manager;

import com.game.network.NetConnectionKCP;
import com.game.network.NetConnectionWebSocket;
import io.netty.channel.ChannelHandlerContext;
import kcp.Ukcp;
import org.beykery.jkcp.KcpOnUdp;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ConnectionManagerKCP {
    private static Map<Integer, NetConnectionKCP> conns = Collections.synchronizedMap(new HashMap<>(128));
    // ChannelHandlerContext 获取用户id,再通过用户id 获取NetConnection
    private static Map< NetConnectionKCP,Integer> ctxs = Collections.synchronizedMap(new HashMap<>(128));
    public static void addToConnection(int userId, NetConnectionKCP conn) {
        System.out.println("用户: " + userId + " 已加入连接");
        conns.put(userId, conn);
        ctxs.put(conn, userId);
    }
    public static void removeConnection( Ukcp kcp) {
            Integer userId = ctxs.get(kcp);
            if(userId!=null){
                conns.remove(userId);
                ctxs.remove(kcp);
                System.out.println("Player: " + userId + "is unconnected");
                kcp.close();
            }else{
                System.out.println("illegal uKCP is unconnected ");
            }
    }


    public static void removeConnection(int userId) {
        NetConnectionKCP netConnection = conns.get(userId);
        conns.remove(userId);
        if(netConnection != null) {
            netConnection.kcp.close();
        }
        else{
            System.out.println("uKcp binding userId"+userId+"doesn't exist");
        }
    }

    public static NetConnectionKCP getConnection(int userId) {
        return conns.get(userId);

    }

}
