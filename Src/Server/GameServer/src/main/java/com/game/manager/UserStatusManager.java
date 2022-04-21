package com.game.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.game.entity.Follow;
import com.game.entity.User;
import com.game.proto.Message.NUserStatusChange;
/**
 * 用户在线、离线状态管理器
 * @author Administrator
 *
 */
public class UserStatusManager {

	User owner;
	public Map<Integer,NUserStatusChange> userStatusMap=Collections.synchronizedMap(new HashMap<Integer, NUserStatusChange>());  //离线列表
    
    public UserStatusManager(User user) {
    	this.owner = user;
    }
    public boolean hasUserStatus() {
        return userStatusMap.size() > 0;
    }
    
    /**
     * 添加用户状态
     */
    public void AddUserStatus(int userId,boolean status) {
    	if(owner.id==userId) {
    		return;
    	}
    	NUserStatusChange nUserStatusChange=NUserStatusChange.newBuilder().setUserId(userId).setStatus(status).build();
        this.userStatusMap.put(userId, nUserStatusChange);   	
    }
    
    /**
     * 移除用户状态
     */
    public void RemoveUserStatus(int userId) {
        this.userStatusMap.remove(userId);   	
    }
    
    /**
     * 获取用户状态列表
     * @return
     */
    public List<NUserStatusChange> getNUserStatusInfos() {
    	List<NUserStatusChange> list=new ArrayList<NUserStatusChange>(userStatusMap.values());
    	userStatusMap.clear();
    	return list;
    }
    
}
