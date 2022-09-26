package com.game.network.Connection.Impl;


import com.game.manager.RoomManager;
import com.game.models.User;
import com.game.network.Connection.ConnectionManager;
import com.game.network.Connection.NetConnection;
import io.netty.channel.ChannelHandlerContext;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
/**
 * @author 贾超博
 *
 * Core System Manager
 *
 *
 *储存所有用户 id 对应的 链接
 * 也 可以通过 链接获取当前用户的信息
 * <p>
 * 用户登录成功后,储存,用户断线之后,需要移出
 */

public class ConnectionManagerTCP implements ConnectionManager {

    // 默认128, 可以储存 96 个链接,根据user id,获取连接
    private static Map<Integer, NetConnectionTCP> conns = Collections.synchronizedMap(new HashMap<>(128));
    // ChannelHandlerContext 获取用户id,再通过用户id 获取NetConnection ;
    private static Map<ChannelHandlerContext, Integer> ctxs = Collections.synchronizedMap(new HashMap<>(128));

    // 通过ChannelHandlerContext 查询用户是否已登录 返回NetConnection
    @Override
    public <T> NetConnection getConnection(T t) {
        ChannelHandlerContext ctx= (ChannelHandlerContext) t;
        Integer userId = ctxs.get(ctx);
        if (userId == null || userId == 0) return null;
        return conns.get(userId);
    }
    @Override
    public void addToConnection(int userId, NetConnection conn) {
        System.out.println("用户: " + userId + " 已加入连接");
        NetConnectionTCP netConnectionTCP= (NetConnectionTCP) conn;
        conns.put(userId, netConnectionTCP);
        ctxs.put(netConnectionTCP.ctx, userId);
    }

    @Override
    public <T> void removeConnection(T t) {
        ChannelHandlerContext context=(ChannelHandlerContext)t;
        Integer userId = ctxs.get(context);
        if(userId!=null){
            removeConnection(userId);
        }
    }
    @Override
    public void removeConnection(int userId) {
        NetConnectionTCP netConnection = conns.get(userId);
        conns.remove(userId);
        if(netConnection != null) {
            User user=netConnection.user;
            RoomManager.Instance.removeUser(user);
            ctxs.remove(netConnection.ctx);
            System.out.println("玩家id: " + userId + "断开连接");
            netConnection.ctx.close();
        }
    }
    @Override
    public NetConnection getConnection(int userId) {
        return conns.get(userId);
    }
}
