package com.game.manager;

import com.game.network.NetConnectionKCP;
import com.game.network.NetConnectionWebSocket;
import io.netty.channel.ChannelHandlerContext;
import kcp.Ukcp;

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
    public static void removeConnection(Ukcp kcp) {
        Integer userId = ctxs.get(kcp);
        conns.remove(userId);
        ctxs.remove(kcp);
        System.out.println("玩家id: " + userId + "断开连接");
        kcp.close();
    }


    public static void removeConnection(int userId) {
        NetConnectionKCP netConnection = conns.get(userId);
        conns.remove(userId);
        if(netConnection != null) {
            netConnection.kcp.close();
        }
    }

    public static NetConnectionKCP getConnection(int userId) {
        return conns.get(userId);

    }

}
