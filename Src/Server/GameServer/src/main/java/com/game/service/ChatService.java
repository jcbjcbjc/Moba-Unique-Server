package com.game.service;

import com.game.network.NetConnection;
import com.game.proto.C2GNet.ChatRequest;
//import com.game.proto.Message.ChatRequest;

import org.springframework.stereotype.Service;

@Service
public interface ChatService {
    // 接收 聊天信息
    void OnChat(NetConnection conn, ChatRequest chatRequest);

}
