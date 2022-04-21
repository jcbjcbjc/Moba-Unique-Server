package com.game.service;

import com.game.entity.Character;
import com.game.entity.User;
import com.game.manager.UserManager;
import com.game.manager.ConnectionManager;
import com.game.network.NetConnection;
import com.game.proto.Message.*;

import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface UserService {
    // 注册
    void register(NetConnection connection, UserRegisterRequest register);
    // 登录
    void userLogin(NetConnection connection, UserLoginRequest login);
    //游戏离线
    void gameLeave(ChannelHandlerContext ctx);
    
    //英雄详情
    void characterDetail(NetConnection connection, CharacterDetailRequest characterDetail);    
    //修改昵称
    void updateNickName(NetConnection connection,UpdateNickNameRequest updateNickName);

    //关注
    void onFollow(NetConnection connection,FollowRequest followReq);   
    //解锁

    //心跳
    void heartBeat(NetConnection connection,HeartBeatRequest heartBeatRequest);
    //用户在线、离线状态查询
    void OnUserStatusQuery(NetConnection connection,UserStatusQueryRequest userStatusQueryRequest);
}
