package com.game.service;

import org.springframework.stereotype.Service;

import com.game.network.NetConnection;
import com.game.proto.C2GNet.AddLiveRequest;
import com.game.proto.C2GNet.AddRoomRequest;
import com.game.proto.C2GNet.AddRoomResponse;
import com.game.proto.C2GNet.GameOver2Request;
import com.game.proto.C2GNet.InviteRequest;
import com.game.proto.C2GNet.InviteResponse;
import com.game.proto.C2GNet.KickOutRequest;
import com.game.proto.C2GNet.NickNameSearchRequest;
import com.game.proto.C2GNet.OutRoomRequest;
import com.game.proto.C2GNet.MyRoomRequest;
import com.game.proto.C2GNet.RoomStartGameRequest;
import com.game.proto.C2GNet.UploadBiFenRequest;
import com.game.proto.C2GNet.ValidateOpenRoomRequest;
//import com.game.proto.Message.AddLiveRequest;
//import com.game.proto.Message.AddRoomRequest;
//import com.game.proto.Message.AddRoomResponse;
//import com.game.proto.Message.GameOver2Request;
//import com.game.proto.Message.InviteRequest;
//import com.game.proto.Message.InviteResponse;
//import com.game.proto.Message.KickOutRequest;
//import com.game.proto.Message.NickNameSearchRequest;
//import com.game.proto.Message.OutRoomRequest;
//import com.game.proto.Message.MyRoomRequest;
//import com.game.proto.Message.RoomStartGameRequest;
//import com.game.proto.Message.UploadBiFenRequest;
//import com.game.proto.Message.ValidateOpenRoomRequest;

@Service
public interface RoomService {

	//我的房间
    void OnMyRoom(NetConnection connection,MyRoomRequest roomListRequest);
    //邀请请求
    void OnInviteRequest(NetConnection connection,InviteRequest inviteRequest);
    //邀请响应
    void OnInviteResponse(NetConnection connection,InviteResponse inviteResponse);
    //踢出请求
    void OnKickOut(NetConnection connection,KickOutRequest kickOutRequest);
    //开始游戏请求
    void OnRoomStartGame(NetConnection connection,RoomStartGameRequest roomStartGameRequest);
    //昵称搜索请求
    void OnNickNameSearch(NetConnection connection,NickNameSearchRequest nickNameSearchRequest);
    //加入房间请求
    void OnAddRoomRequest(NetConnection connection,AddRoomRequest addRoomRequest);
    //加入房间响应
    void OnAddRoomResponse(NetConnection connection,AddRoomResponse addRoomResponse);
    //退出房间请求
    void OnOutRoom(NetConnection connection,OutRoomRequest outRoomRequest);
    //游戏结束请求
    void OnGameOver2(NetConnection connection,GameOver2Request gameOver2Request);
    //进入直播请求
    void OnAddLive(NetConnection connection,AddLiveRequest addLiveRequest);
    //上传比分请求
    void OnUploadBiFen(NetConnection connection,UploadBiFenRequest uploadBiFenRequest);
    //效验是否可以开房间请求
    void OnValidateOpenRoom(NetConnection connection,ValidateOpenRoomRequest validateOpenRoomRequest);
}
