package com.game.service;

import com.game.network.NetConnection;
import com.game.proto.Message.FrameHandle;
import com.game.proto.Message.GameOverRequest;
import com.game.proto.Message.PercentForward;
import com.game.proto.Message.RepairFrameRequest;

public interface BattleService {
	
	 // 帧操作
    void OnFrameHandle(NetConnection connection, FrameHandle frameHandle);
    // 进度转发
    void OnPercentForward(NetConnection connection, PercentForward percentForward);
    // 游戏结束
    void OnGameOver(NetConnection connection, GameOverRequest gameOverRequest);
    // 补帧
    void OnRepairFrame(NetConnection connection, RepairFrameRequest repairFrameRequest);
}
