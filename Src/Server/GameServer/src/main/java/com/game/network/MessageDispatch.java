package com.game.network;

import com.game.manager.ConnectionManager;
import com.game.proto.Message.*;
import com.game.service.*;
import com.game.spring.SpringBeanUtil;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;

/**
 * @author 贾超博
 *
 * Component in network
 *
 * MessageDispatch Class
 *
 *
 */
// 接收消息分发
public class MessageDispatch {
    UserService userService;
    FollowService followService;
    RoomService roomService;
    ChatService chatService;
    MatchService matchService;
    
    public MessageDispatch() {
        userService = SpringBeanUtil.getBean(UserService.class);
        followService = SpringBeanUtil.getBean(FollowService.class);
        roomService = SpringBeanUtil.getBean(RoomService.class);
        chatService = SpringBeanUtil.getBean(ChatService.class);
        matchService = SpringBeanUtil.getBean(MatchService.class);
    }

    public static MessageDispatch Instance;// = new MessageDispatch();

    public static MessageDispatch GetInstance(){
        if(Instance==null){
            Instance = new MessageDispatch();
        }
        return Instance;
    }
    public void receiveData(ChannelHandlerContext ctx, NetMessage.Builder message) {

        NetConnection conn = null;
        // 如果 是注册/登录,先创建连接；如果不是，判断是否已连接
        if (message.getRequest().hasUserRegister() || message.getRequest().hasUserLogin()) {
            conn = createConne(ctx);
            System.out.println("conn: " + conn);
        } else {
            conn = ConnectionManager.getConnection(ctx);
            if (conn == null) {
                // 暂时不处理
                System.out.println("收到一个无连接请求 或 未知功能接口,已拒绝");
                return;
            }
        }
        
        if (message.getRequest().hasHeartBeatReq()) {
            System.out.println("收到心跳 请求");
            userService.heartBeat(conn, message.getRequest().getHeartBeatReq());
        }

        if (message.getRequest().hasUserRegister()) {
            System.out.println("收到 注册 请求");
            userService.register(conn, message.getRequest().getUserRegister());
        }

        if (message.getRequest().hasUserLogin()) {
            System.out.println("收到 用户登录 请求");
            userService.userLogin(conn, message.getRequest().getUserLogin());
        }



        if (message.getRequest().hasCharacterDetail()) {
            System.out.println("收到 英雄详情 请求");
            userService.characterDetail(conn, message.getRequest().getCharacterDetail());
        }

        if (message.getRequest().hasUpdateNickName()) {
            System.out.println("收到 修改昵称 请求");
            userService.updateNickName(conn, message.getRequest().getUpdateNickName());
        }
        

        if (message.getRequest().hasFollowReq()) {
            System.out.println("收到 关注/取消关注 请求");
            userService.onFollow(conn, message.getRequest().getFollowReq());
        }



        if (message.getRequest().hasMyRoomReq()) {
            System.out.println("收到我的房间 请求");
            roomService.OnMyRoom(conn, message.getRequest().getMyRoomReq());
        }
        
        if (message.getRequest().hasInviteReq()) {
            System.out.println("收到邀请 请求");
            roomService.OnInviteRequest(conn, message.getRequest().getInviteReq());
        }
        
        if (message.getRequest().hasInviteRes()) {
            System.out.println("收到邀请 响应");
            roomService.OnInviteResponse(conn, message.getRequest().getInviteRes());
        }
        
        if (message.getRequest().hasKickOutReq()) {
            System.out.println("收到踢出 请求");
            roomService.OnKickOut(conn, message.getRequest().getKickOutReq());
        }
        
        if (message.getRequest().hasRoomStartGameReq()) {
            System.out.println("收到开始游戏 请求");
            roomService.OnRoomStartGame(conn, message.getRequest().getRoomStartGameReq());
        }
        
        if (message.getRequest().hasNickNameSearchReq()) {
            System.out.println("收到昵称搜索 请求");
            roomService.OnNickNameSearch(conn, message.getRequest().getNickNameSearchReq());
        }
        
        if (message.getRequest().hasAddRoomReq()) {
            System.out.println("收到加入房间 请求");
            roomService.OnAddRoomRequest(conn, message.getRequest().getAddRoomReq());
        }
        
        if (message.getRequest().hasAddRoomRes()) {
            System.out.println("收到加入房间 响应");
            roomService.OnAddRoomResponse(conn, message.getRequest().getAddRoomRes());
        }
        
        if (message.getRequest().hasOutRoomReq()) {
            System.out.println("收到退出房间 请求");
            roomService.OnOutRoom(conn, message.getRequest().getOutRoomReq());
        }        
        
        if (message.getRequest().hasChatReq()) {
            System.out.println("收到聊天 请求");
            chatService.OnChat(conn, message.getRequest().getChatReq());
        }
        
        if (message.getRequest().hasUserStatusQueryReq()) {
            System.out.println("收到用户在线、离线状态查询 请求");
            userService.OnUserStatusQuery(conn, message.getRequest().getUserStatusQueryReq());
        }
        if (message.getRequest().hasStartMatchReq()) {
        	 System.out.println("收到开始匹配 请求");
        	matchService.OnStartMatch(conn, message.getRequest().getStartMatchReq());;
        }
        if (message.getRequest().hasGameOver2Req()) {
       	  System.out.println("收到游戏结束请求");
       	  roomService.OnGameOver2(conn, message.getRequest().getGameOver2Req());;
        }
        if (message.getRequest().hasAddLiveReq()) {
          System.out.println("收到 进入直播请求");
          roomService.OnAddLive(conn, message.getRequest().getAddLiveReq());;
        }
        if (message.getRequest().hasUploadBiFenReq()) {
          System.out.println("收到 上传比分请求");
          roomService.OnUploadBiFen(conn, message.getRequest().getUploadBiFenReq());;
        }
        if (message.getRequest().hasFollowListReq()) {
            System.out.println("收到 关注列表请求");
            followService.OnFollowList(conn, message.getRequest().getFollowListReq());;
        }
        if (message.getRequest().hasValidateOpenRoomReq()) {
            System.out.println("收到 效验是否可以开房间请求");
            roomService.OnValidateOpenRoom(conn, message.getRequest().getValidateOpenRoomReq());
        }
    }

    // 注册,登录,需要创建新的 连接

    NetConnection createConne(ChannelHandlerContext context) {
        NetConnection connection = ConnectionManager.getConnection(context);
        if (connection == null) {
            connection = new NetConnection(context);
        }
        return connection;
    }
}
