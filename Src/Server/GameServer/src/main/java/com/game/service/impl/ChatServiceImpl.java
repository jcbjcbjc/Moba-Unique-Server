package com.game.service.impl;

import com.game.entity.Character;
import com.game.entity.User;
import com.game.manager.ChatManager;
import com.game.manager.ConnectionManager;
import com.game.manager.RoomManager;
import com.game.network.NetConnection;
import com.game.proto.Message;
import com.game.proto.C2GNet.*;
//import com.game.proto.Message.ChatChannel;
//import com.game.proto.Message.ChatMessage;
//import com.game.proto.Message.ChatRequest;
//import com.game.proto.Message.ChatResponse;
//import com.game.proto.Message.NetMessageResponse;
//import com.game.proto.Message.Result;
//import com.game.proto.Message.UserStatus;
import com.game.service.ChatService;
import com.game.service.UserService;
import com.game.util.TimeUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {
	
	@Autowired
	private UserService userService;
	
    @Override
    public void OnChat(NetConnection sender, ChatRequest chatRequest) {
        User user = sender.getSession().user;
        NetMessageResponse.Builder senderResponse = sender.getResponse();
        
        ChatMessage.Builder chatMessage= chatRequest.getChatMessage().toBuilder();
        chatMessage.setTime(new Date().getTime());
        ChatResponse.Builder senderChatResponseBuilder = ChatResponse.newBuilder();
        // 私聊
        if (chatMessage.getChatChannel() == ChatChannel.Private) {
            System.out.println("收到私聊,发送人: " + user.nickname);
            NetConnection toNetConnection = ConnectionManager.getConnection(chatMessage.getToId()); //接收人
            if (toNetConnection == null) {
            	senderChatResponseBuilder.setResult(Result.Failed).setErrormsg("对方不在线");
            	senderResponse.setChatRes(senderChatResponseBuilder);
                sender.send();
                return;
            }
            if (user.id == chatMessage.getToId()) {
                senderChatResponseBuilder.setResult(Result.Failed).setErrormsg("不能对自己私聊");
                senderResponse.setChatRes(senderChatResponseBuilder);
                sender.send();
                return;
            }

            // 给对方发送私聊信息
            NetMessageResponse.Builder toResponse = toNetConnection.getResponse();
            ChatResponse.Builder chatResponse = ChatResponse.newBuilder();
            chatResponse.addPrivateMessages(chatMessage).setResult(Result.Success);
            
            toResponse.setChatRes(chatResponse);
            toNetConnection.send();

            // 通知自己, 私聊信息 发送成功
            senderResponse.setChatRes(chatResponse);
            sender.send();
        } else {
        	//房间消息效验
        	if(chatMessage.getChatChannel()==ChatChannel.RoomChat && (user.roomId==0 || (user.getStatus()!=UserStatus.Room && 
        			user.getStatus()!=UserStatus.Game && user.getStatus()!=UserStatus.Live ))) {
        		senderChatResponseBuilder.setResult(Result.Failed).setErrormsg("你没有加入房间或游戏中或观看直播中");
                senderResponse.setChatRes(senderChatResponseBuilder);
                sender.send();
                return;
        	}
          if(chatMessage.getChatChannel()==ChatChannel.RoomChat) {
        	if(user.getStatus() == UserStatus.Game) {
        		chatMessage.setEnterLiveUserId(user.id);
			}else if(user.getStatus() == UserStatus.Live) {
				chatMessage.setEnterLiveUserId(user.enterLiveUserId);
			}
          }
            // 私聊 以外的信息
            ChatManager.Instance.AddChat(user, chatMessage.build());
            
            //房间内玩家或粉丝发送心跳消息（会把管道内存在的房间消息刷出去）
            if(chatMessage.getChatChannel()==ChatChannel.RoomChat) {
            	List<Integer> userIdList = RoomManager.Instance.roomUserIds.get(user.roomId);
            	if(userIdList != null && userIdList.size() > 0) {
            		for (Integer userId : userIdList) {
            			NetConnection connection = ConnectionManager.getConnection(userId);
            			if(connection == null) {
            				continue;
            			}
            			userService.heartBeat(connection, null);
					}
            	}
            }else {
            	sender.send();
            }
        }
        
    }
}
