package com.game.manager;

import com.game.entity.Character;
import com.game.entity.User;
import com.game.proto.Message;
import com.game.proto.Message.ChatChannel;
import com.game.proto.Message.ChatMessage;
import com.game.util.TimeUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ChatManager {
	public static ChatManager Instance = new ChatManager();

    List<ChatMessage> compMsg = Collections.synchronizedList(new ArrayList<>());     //综合消息
    Map<Integer, List<ChatMessage>> roomChatMsg = Collections.synchronizedMap(new HashMap<>());  //房间消息
    private int maxChatRecoredNums=20;  //最大拉取条数

    /**
     * 添加消息
     */
    public void AddChat(User user, ChatMessage chatMessage) {
    	if(chatMessage.getChatChannel()==ChatChannel.Comp) {
    		compMsg.add(chatMessage);
            return;
    	}
    	if(chatMessage.getChatChannel()==ChatChannel.RoomChat) {
    		List<ChatMessage> roomMsgList = roomChatMsg.get(user.roomId);
            if (roomMsgList == null) {
            	roomMsgList = new ArrayList<>();
            	roomChatMsg.put(user.roomId, roomMsgList);
            }
            roomMsgList.add(chatMessage);
    		return;    		
    	}
    }

    /**
     * 获取系统消息
     */
    public int GetCompMsg(int index, List<ChatMessage> result) {
        return this.GetNewMessages(index, result, compMsg);
    }


    /**
     * 获取房间消息
     */
    public int GetRoomChatMsg(int roomId, int index, List<ChatMessage> result) {
        List<ChatMessage> chatMessages = roomChatMsg.get(roomId);
        if (chatMessages == null) {
            return 0;
        } else {
        	return this.GetNewMessages(index, result, chatMessages);
        }
    }

    /**
     * 根据索引获取消息并返回最新索引
     * @param idx  当前索引
     * @param result  查找到的消息
     * @param messages  消息数据
     * @return  最新索引
     */
    private int GetNewMessages(int idx, List<ChatMessage> result, List<ChatMessage> messages){
        if (idx == 0){
            if(messages.size() > maxChatRecoredNums){
                idx = messages.size()- maxChatRecoredNums;
            }
        }
        for (; idx < messages.size(); idx++){
            result.add(messages.get(idx));
        }
        return idx;
    }

    /**
     * 清理聊天消息
     */
    public void ClearChatMessage() {
      if(compMsg.size() > maxChatRecoredNums) {
    	compMsg=compMsg.subList(compMsg.size()-maxChatRecoredNums, compMsg.size());
      }
      Set<Entry<Integer, List<ChatMessage>>> roomChatSet=roomChatMsg.entrySet();
      for (Entry<Integer, List<ChatMessage>> entry : roomChatSet) {
    	  List<ChatMessage> list=entry.getValue();
    	  if(list.size() > maxChatRecoredNums) {
    		  list=list.subList(list.size()-maxChatRecoredNums, list.size());
    		  roomChatMsg.put(entry.getKey(), list);
    	  }
	  }
    }
}
