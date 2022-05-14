package com.game.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.core.util.AbstractWatcher.ReconfigurationRunnable;

import com.game.manager.FollowManager;

import com.game.manager.UserStatusManager;
import com.game.manager.RoomManager;
import com.game.manager.StatusManager;
import com.game.models.Chat;
//import com.game.proto.Message.ChatResponse;
import com.game.proto.Message.FollowListResponse;
import com.game.proto.Message.NCharacter;
import com.game.proto.Message.NItem;
import com.game.proto.C2GNet.ChatResponse;
import com.game.proto.C2GNet.NRoom;
import com.game.proto.C2GNet.NUser;
import com.game.proto.Message.NUserStatusChange;
import com.game.proto.C2GNet.NetMessageResponse;
import com.game.proto.C2GNet.StatusNotify;

import com.game.proto.C2GNet.UnLockCharacter;
import com.game.proto.C2GNet.UserStatus;
import com.game.proto.C2GNet.UserStatusChangeResponse;

//import com.game.proto.Message.NRoom;
//import com.game.proto.Message.NUser;
//import com.game.proto.Message.NUserStatusChange;
//import com.game.proto.Message.NetMessageResponse;
//import com.game.proto.Message.StatusNotify;
//import com.game.proto.Message.TeamType;
//import com.game.proto.Message.UnLockCharacter;
//import com.game.proto.Message.UserStatus;
//import com.game.proto.Message.UserStatusChangeResponse;
import com.game.spring.DBUtil;

/**
 * @author 贾超博
 *
 * Entity Class
 *
 * User Class
 */

public class User {
    public Integer id;
    public String username;
    public String password;
    public String nickname; 
    private long coin;
    public Integer characterId;  //已选角色id
    public String bag;
    public Integer Lastlogintime;//last login time
    
    /*非数据库实体字段*/
    public boolean isUpdateFlag=false;  //是否变更
    public Map<Integer, Character> characters=null;  //所有角色信息
    public Character character; //模型属性
    public int fenSiCount;  //粉丝数量
    public int roomId; //房间id
    public int teamId;
    private UserStatus status=UserStatus.Normal;  //用户状态
    public long matchTime;  //匹配时间
    public List<Integer> liveFanSiIdList=new ArrayList<>();   //观战id集合
    public int enterLiveUserId;  //进入观战用户的id

    public User(){}
    public User(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
    
    public User(String username, String password, String nickname, String bag) {
		super();
		this.username = username;
		this.password = password;
		this.nickname = nickname;
		this.bag = bag;
	}




    public StatusManager statusManager; // 金币，物品，状态管理
    public FollowManager followManager;
    public UserStatusManager offLineUserManager;
//    public Team team; // 队伍信息
//    int teamUpdateTs; // 队伍信息变更时间戳
    public Chat chat;  //聊天信息
    
    public NUser info() {
    	 //初始化用户所有角色
    	if(characters==null) { 
       	   characters=new HashMap<>();
       	 List<Character> list = DBUtil.Instance.getCharacterDao().queryByUserId(id);
       	 for (Character character : list) {
           	characters.put(character.id, character);
   		 }
        }
    	NUser.Builder builder = this.infoBase(characterId);
    	
        List<Integer> idList= DBUtil.Instance.getCharacterDao().queryIdsByUserId(id);  //解锁角色id集合
        for (int i=0;i<idList.size();i++) {
        	int tId=idList.get(i);
        	UnLockCharacter.Builder unLockCharacter=UnLockCharacter.newBuilder();
        	unLockCharacter.setTid(tId);
        	unLockCharacter.setCid(characters.get(tId).cId);
            builder.addUnLockCharacters(unLockCharacter); 
		}
        
        // 背包

        // 物品，金币，状态管理
        statusManager = new StatusManager(this);
        // 关注
        followManager = new FollowManager(this);
        
        //离线用户列表管理器
        offLineUserManager=new UserStatusManager(this);
        chat = new Chat(this);

        return builder.build();
    }
    
    /**
     * 初始化道具
     */
    public void InitItems() {

    }
    
    /**
     * 添加角色
     * @param character
     */
    public void AddCharacter(Character character) {
    	if(character==null || character.id==null) {
    		return;
    	}
    	characters.put(character.id, character);
    }
    
    /**
     * 基础信息
     * @return
     */
    public NUser.Builder infoBase(Integer charaId){
    	NUser.Builder builder = NUser.newBuilder();
        builder.setId(id);
        builder.setNickname(nickname);  //昵称
        builder.setCoin(coin);   //金币
        builder.setFenSiCount(fenSiCount); //粉丝数量
        builder.setUserStatus(status);  //用户状态
       if(roomId != 0) {   
         NRoom room = RoomManager.Instance.GetRoom(roomId);
        if(room != null) {  
          builder.setBiFen(room.getBiFen()); //比分 
          /*boolean myResult = RoomManager.Instance.ExistUserRoom(id, room.getTeam1List());  //效验是否存在友队
          builder.setTeamType(myResult ? TeamType.My : TeamType.Enemy);  //队伍类型
          builder.setRoomNum(RoomManager.Instance.GetRoomUserIdsNum(roomId));  //房间人数*/
        }
       }
        if(characters != null && charaId != null) {
           character=characters.get(charaId);
           builder.setCharacter(character.getCharacterInfo());  //已选角色信息
        }else if(charaId != null) {
        	character=DBUtil.Instance.getCharacterDao().queryById(charaId);
        	builder.setCharacter(character.getCharacterInfo());
        }
        return builder;
    }

    // 后处理
    public NetMessageResponse.Builder postProcess(NetMessageResponse.Builder msg) {
        // 金币，物品 信息
        if (statusManager.hasStatus()) {
            StatusNotify statusList = statusManager.getStatusList();
            msg.setStatusNotify(statusList);
        }
        
        //离线用户列表
        if(offLineUserManager.hasUserStatus()) {
        	UserStatusChangeResponse build=UserStatusChangeResponse.newBuilder().addAllUserStatusChanges(offLineUserManager.getNUserStatusInfos()).build();
        	msg.setUserStatusChangeRes(build);
        }

        // 队伍信息
//        if (team != null && teamUpdateTs < team.timeStamp) {
//            TeamUpdateResponse.Builder builder = TeamUpdateResponse.newBuilder().setTeamInfo(team.teamToInfo());
//            msg.setTeamUpdate(builder);
//            teamUpdateTs = team.timeStamp;
//        }
        
        //聊天信息
        ChatResponse chatResponse = chat.GetChat(msg.getChatRes());
        msg.setChatRes(chatResponse);

        return msg;
    }
    //下线处理
    public void leave(){
//        followManager.sendFollow(false);
        this.updateToDB();
//        if(team != null){
//            team.removeMember(this);
//        }
    }
    

    public long getCoin() {
        return coin;
    }

    public void setCoin(long coin) {
        if (this.coin == coin) {
            return;
        }
        if (statusManager != null) {
            statusManager.addGoldChange(coin - this.coin);
        }
        this.coin = coin;
        this.isUpdateFlag=true;
    }
    
    /**
     *   更新到数据库
     */
    public void updateToDB() {  
    	if(this.isUpdateFlag) {
    		this.update();
    		if(this.characters != null) {
        		Collection<com.game.entity.Character> characters=this.characters.values();
        		for (com.game.entity.Character character : characters) {					
        			character.updateToDB();
				}
        	}
    		this.isUpdateFlag=false;
    	}
    }
    
    public void update() {
    	this.updateBag();
    	DBUtil.Instance.getUserDao().update(this);	
    }
    
    /**
     * 更新背包
     */
    public void updateBag() {

    }
    
    /**
     * 切换角色
     * @param charaId
     */
    public Character chanageCharacter(Integer charaId) {
    	Character character=characters.get(charaId);
    	if(character == null) {
    		return null;
    	}
    	characterId=charaId;
    	this.isUpdateFlag=true;
    	character.updateToDB();
    	return character;
    }
    
	public void setNickname_(String nickname) {
		if(!nickname.equals(this.nickname)) {
		  this.nickname = nickname;
		  this.isUpdateFlag=true;
		}
	}

	/**
	 * 设置状态
	 */
	public void setStatus(UserStatus userStatus) {
		this.status=userStatus;
		if(userStatus == UserStatus.Normal) {
			this.roomId=0;
            this.teamId=0;
			this.chat.roomChatIdx=0;
		}
	}
	public UserStatus getStatus() {
		return status;
	}
	
}