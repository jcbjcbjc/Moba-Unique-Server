package com.game.service;

import com.game.network.NetConnection;
//import com.game.proto.Message.NRoom;
//import com.game.proto.Message.StartMatchRequest;

import com.game.proto.C2GNet.NRoom;
import com.game.proto.C2GNet.StartMatchRequest;
import com.game.vo.ResultInfo;

public interface MatchService {

	// 开始匹配
    void OnStartMatch(NetConnection connection, StartMatchRequest startMatchRequest);
    
    //匹配响应
    void OnMatchResponse(int userId, ResultInfo resultInfo, NRoom.Builder roomBuilder, boolean isMatch);
}
