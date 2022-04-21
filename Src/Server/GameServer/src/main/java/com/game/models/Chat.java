package com.game.models;

import com.game.entity.Character;
import com.game.entity.User;
import com.game.manager.ChatManager;
import com.game.manager.RoomManager;
import com.game.manager.UserManager;
import com.game.proto.Message;
import com.game.proto.Message.ChatMessage;
import com.game.proto.Message.ChatRoomType;
import com.game.proto.Message.NRoom;
import com.game.proto.Message.UserStatus;

import java.util.ArrayList;
import java.util.List;

public class Chat {

    User owner;

    int compIdx;   //综合
    public int roomChatIdx;   //房间
    List<ChatMessage> compMsg = new ArrayList<>();
    List<ChatMessage> roomChatMsg = new ArrayList<>();
    List<ChatMessage> resultRoomChatMsg = new ArrayList<>();
    
    public Chat(User user) {
        this.owner = user;
    }

    public Message.ChatResponse GetChat(Message.ChatResponse resp) {
        // 去聊天管理器 获取属于自己的聊天信息
        Message.ChatResponse.Builder builder = resp.toBuilder();
        //综合消息
        compMsg.clear();
        compIdx = ChatManager.Instance.GetCompMsg(compIdx, compMsg);
        if (compMsg.size() > 0) {
            builder.addAllCompMessages(compMsg);
        }
        
        //房间消息
        if (owner.roomId != 0) {
          roomChatMsg.clear();
    	  resultRoomChatMsg.clear();
          roomChatIdx = ChatManager.Instance.GetRoomChatMsg(owner.roomId, roomChatIdx, roomChatMsg);
          if (roomChatMsg.size() > 0) {
        	  //房间、游戏、直播消息筛选
        	for (ChatMessage chatMessage : roomChatMsg) {
        		if(owner.id == chatMessage.getFromId()) {  //自己发送的消息
        			resultRoomChatMsg.add(chatMessage);
        			continue;
        		}
				if(owner.getStatus() == UserStatus.Room && chatMessage.getChatRoomType() == ChatRoomType.Room_) {
					resultRoomChatMsg.add(chatMessage);
				}else if(owner.getStatus() == UserStatus.Game) {
				  if(chatMessage.getChatRoomType() == ChatRoomType.Game_) {  //判断友方消息
					NRoom room = RoomManager.Instance.GetRoom(owner.roomId);
					if(room != null) {
					  boolean myResult = RoomManager.Instance.ExistUserRoom(owner.id, room.getMyList());  //效验是否存在友队
				      boolean otherResult = RoomManager.Instance.ExistUserRoom(chatMessage.getFromId(), room.getMyList());  //效验是否存在友队
					  if(myResult == otherResult) { //和发送消息的人同一队伍
						 resultRoomChatMsg.add(chatMessage);
				      }
					}
				  }else if(chatMessage.getChatRoomType() == ChatRoomType.Live_) {  //判断玩家观看直播的主角是自己
					User fromUser = UserManager.Instance.getUser(chatMessage.getFromId());
					if(fromUser != null && fromUser.enterLiveUserId == owner.id) {						
						resultRoomChatMsg.add(chatMessage);
					}
				  }
				}else if(owner.getStatus() == UserStatus.Live && chatMessage.getChatRoomType() == ChatRoomType.Live_) {  
					//自己的观看直播的主角和消息玩家的观看直播的主角一致 或 消息玩家是被关注者
					User fromUser = UserManager.Instance.getUser(chatMessage.getFromId());
					if((fromUser != null && owner.enterLiveUserId == fromUser.enterLiveUserId) || 
						owner.enterLiveUserId == chatMessage.getFromId()) {						
						resultRoomChatMsg.add(chatMessage);
					}
				}
			}
              builder.addAllRoomMessages(resultRoomChatMsg);
          }
        }
        
        return builder.build();
    }

}
