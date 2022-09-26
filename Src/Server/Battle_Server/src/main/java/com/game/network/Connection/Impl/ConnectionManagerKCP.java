package com.game.network.Connection.Impl;

import com.game.network.Connection.ConnectionManager;
import com.game.network.Connection.NetConnection;
import kcp.Ukcp;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ConnectionManagerKCP implements ConnectionManager {
    private static Map<Integer, NetConnectionKCP> conns = Collections.synchronizedMap(new HashMap<>(128));
    // ChannelHandlerContext 获取用户id,再通过用户id 获取NetConnection
    private static Map< NetConnectionKCP,Integer> ctxs = Collections.synchronizedMap(new HashMap<>(128));

//    @Override
//    public NetConnectionKCP getConnection(Ukcp ctx) {
//        return null;
//    }

    @Override
    public <T> NetConnectionKCP getConnection(T ctx) {
        return null;
    }

    @Override
    public void addToConnection(int userId, NetConnection conn) {
        System.out.println("用户: " + userId + " 已加入连接");
        conns.put(userId, (NetConnectionKCP) conn);
        ctxs.put((NetConnectionKCP) conn, userId);
    }
    @Override
    public <T> void removeConnection( T t) {
            Ukcp kcp=(Ukcp)t;
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

    @Override
    public void removeConnection(int userId) {
        NetConnectionKCP netConnection = conns.get(userId);
        conns.remove(userId);
        if(netConnection != null) {
            netConnection.kcp.close();
        }
        else{
            System.out.println("uKcp binding userId"+userId+"doesn't exist");
        }
    }
    @Override
    public NetConnectionKCP getConnection(int userId) {
        return conns.get(userId);

    }

}
