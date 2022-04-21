package com.game.manager;

import com.game.entity.Character;
import com.game.entity.Follow;
import com.game.entity.User;
import com.game.network.NetConnection;
import com.game.proto.Message;
import com.game.proto.Message.NUser;
import com.game.spring.DBUtil;
import com.game.spring.SpringBeanUtil;
import com.game.vo.FollowFenSiVo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class FollowManager {
    User owner;

    public Map<Integer,Follow> follows;  //关注列表

    public FollowManager(User user) {
        this.owner = user;
        this.initFollows();
    }
    
    // 将数据库中的关注信息 取出
    void initFollows() {
        if (follows == null) {
        	follows = Collections.synchronizedMap(new HashMap<Integer, Follow>());
        	List<Follow> followList= DBUtil.Instance.getFollowDao().queryByUserId(owner.id);
        	for (Follow follow : followList) {
        		follows.put(follow.followId, follow);
			}
        }
    }

    //  将 数据库中的 用户关注信息，转为网络传输的 list
    public List<NUser> getFollowInfos() {
        List<NUser> NUsers = new ArrayList<>();
        List<Integer> offLineuserIds=new ArrayList<>();
        Collection<Follow> followList=follows.values();
        //查询用户粉丝数量
        List<Integer> userIds=new ArrayList<Integer>();
        for (Follow follow : followList) {
        	userIds.add(follow.followId);
        }
    	List<FollowFenSiVo> followFenSiVos=DBUtil.Instance.getFollowDao().queryFenSiCount(userIds);
    	Map<Integer, Integer> fenSiCountMap=new HashMap<>();
    	if(followFenSiVos != null) {
    		for (FollowFenSiVo followFenSiVo : followFenSiVos) {
    			fenSiCountMap.put(followFenSiVo.followId, followFenSiVo.count);
    		}
    	}
    	
        for (Follow follow : followList) {
        	 User user = UserManager.Instance.userMap.get(follow.followId);  //被关注用户
             boolean status = false;
             if (user != null) {  //在线
                 status = true;
                 NUsers.add(this.dbToNetUser(user, status, fenSiCountMap.get(user.id)));
             }else {  //离线
            	 offLineuserIds.add(follow.followId);
             }
        }
        if(offLineuserIds.size() > 0) {
          List<User> users=DBUtil.Instance.getUserDao().queryByIds(offLineuserIds);
          for (User user : users) {
              NUsers.add(this.dbToNetUser(user, false, fenSiCountMap.get(user.id)));			
		  }
        }
        
        return NUsers;
    }
    
    public NUser dbToNetUser(User user,boolean status,int fenSiCount) {
    	return user.infoBase(user.characterId).setStatus(status).setFenSiCount(fenSiCount).build();
    }

    // 添加关注
    public void addFollow(Integer followId) {
        Follow follow = new Follow(followId, owner.id);
        follow.status=true;
        this.follows.put(follow.followId,follow);
        DBUtil.Instance.getFollowDao().add(follow);
    }



    //移除关注
    public void removeFollow(int followId) {
        Follow follow = follows.remove(followId);
        if (follow != null) {
        	DBUtil.Instance.getFollowDao().delete(follow.id);
        }
    }

    public Follow getFollow(int followId) {
    	return follows.get(followId);
    }


}
