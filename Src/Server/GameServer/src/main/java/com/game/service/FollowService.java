package com.game.service;

import com.game.network.NetConnection;
import com.game.proto.Message;
import com.game.proto.Message.FollowListRequest;
import com.game.proto.Message.UserRegisterRequest;

import org.springframework.stereotype.Service;

@Service
public interface FollowService {

	// 关注列表查询
    void OnFollowList(NetConnection connection, FollowListRequest followListReq);
    
}
