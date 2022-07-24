package com.game.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.game.entity.Character;
import com.game.entity.User;
import com.game.spring.DBUtil;
import com.game.spring.ScheduleTask;
import com.game.vo.FollowFenSiVo;

public class PowerRankingManager {
	public static PowerRankingManager Instance = new PowerRankingManager();
	
    public List<User> combatPowerStatList=Collections.synchronizedList(new ArrayList<>());  //战力排行
    public Map<Integer,Integer> myCombatPowerStat=Collections.synchronizedMap(new HashMap<>()); //我的战力排行
    
    public void init(){
    	//战力排行统计
        combatPowerStat();
    } 
    
    /**
	 * 战力统计
	 */
    public void combatPowerStat() {
    	//先更新修改的用户数据
    	UserManager.Instance.updateObjDBInfo();
    	//战力统计
    	combatPowerStatList.clear();
    	List<Character> characters=DBUtil.Instance.getCharacterDao().queryCombatPowerStat();
       if(characters == null || characters.size() == 0) {
    	   return;
       }
       List<Integer> userIds=new ArrayList<Integer>();
       for (int i = 0; i < characters.size(); i++) {
    	   Character character=characters.get(i);
		   if(i < 100) {
			 userIds.add(character.userId);
		   }
		   myCombatPowerStat.put(character.userId, i+1);  //我的战力排行
	   }
       //获取前100排行的用户
    	List<User> users=DBUtil.Instance.getUserDao().queryByIds(userIds);
    	//查询用户粉丝数量
    	List<FollowFenSiVo> followFenSiVos=DBUtil.Instance.getFollowDao().queryFenSiCount(userIds);  
    	for (User user : users) {
    		for (Character character : characters) {
				if(user.id==character.userId.intValue()) {					
					user.character=character;
					break;
				}
			}
		}
    	
    	for (User user : users) {
    		for (FollowFenSiVo followFenSiVo : followFenSiVos) {
				if(user.id==followFenSiVo.followId) {					
					user.fenSiCount=followFenSiVo.count;
					break;
				}
			}
		}
    	combatPowerStatList=users;
    }
    
    
    		
}
