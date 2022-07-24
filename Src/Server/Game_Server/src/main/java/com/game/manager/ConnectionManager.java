package com.game.manager;


import com.game.entity.Character;
import com.game.entity.User;
import com.game.network.NetConnection;

import io.netty.channel.ChannelHandlerContext;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
/**
 * @author 贾超博
 *
 * Component in Manager
 *
 * ConnectionManager Class
 *
 *
 */

/**
 * 储存所有用户 id 对应的 链接
 * 也 可以通过 链接获取当前用户的信息
 * <p>
 * 用户登录成功后,储存,用户断线之后,需要移出
 */
public class ConnectionManager {

    // 默认128, 可以储存 96 个链接,根据user id,获取连接
    private static Map<Integer, NetConnection> conns = Collections.synchronizedMap(new HashMap<>(128));
    // ChannelHandlerContext 获取用户id,再通过用户id 获取NetConnection ;
    private static Map<ChannelHandlerContext, Integer> ctxs = Collections.synchronizedMap(new HashMap<>(128));

    // 通过ChannelHandlerContext 查询用户是否已登录 返回NetConnection
    public static NetConnection getConnection(ChannelHandlerContext ctx) {
        Integer userId = ctxs.get(ctx);
        if (userId == null || userId == 0) return null;
        return conns.get(userId);
    }

    public static void addToConnection(User ch, NetConnection conn) {
        System.out.println("用户: " + ch.id + " 已加入连接");
        conns.put(ch.id, conn);
        ctxs.put(conn.ctx, ch.id);
    }

    public static void removeConnection(ChannelHandlerContext context) {
        Integer userId = ctxs.get(context);
        conns.remove(userId);
        ctxs.remove(context);
        System.out.println("玩家id: " + userId + "断开连接");
        context.close();
    }

    public static void removeConnection(int userId) {
        NetConnection netConnection = conns.get(userId);
        conns.remove(userId);
        if (netConnection != null) {			
        	ctxs.remove(netConnection.ctx);
        	netConnection.ctx.close();
		}
    }

    public static NetConnection getConnection(int userId) {
        return conns.get(userId);

    }

    public static NetConnection getConnByCharacterId(int characterId) {
        for (Integer in : conns.keySet()) {
        	Integer selectCharacterId=conns.get(in).getSession().user.characterId;
            if(selectCharacterId != null && selectCharacterId == characterId){
                return conns.get(in);
            }
        }
        return null;
    }
}
