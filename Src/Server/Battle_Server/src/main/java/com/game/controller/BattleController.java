package com.game.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.game.proto.Message;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.game.manager.RoomManager;
import com.game.manager.UserManager;
import com.game.models.Room;
import com.game.models.User;

import com.game.vo.BattleUserVo;
import com.game.vo.LiveUserVo;
import com.game.vo.ResultInfo;
/**
 * @author 贾超博
 *
 * Http Call
 *
 * Be called by GameServer
 *
 *
 */

@Controller
@RequestMapping("/BattleController")
public class BattleController {


	/**
     * 添加战斗用户信息
     *
     * @return
     */
    @RequestMapping("/AddBattleUserInfo")
    @ResponseBody
    public ResultInfo AddBattleUserInfo(@RequestBody BattleUserVo battleUserVo) {
    	List<User> userList=Collections.synchronizedList(new ArrayList<>());  //用户集合
    	for (Integer userId : battleUserVo.userIdList) {
    		User user=new User(userId, battleUserVo.roomId);
    		userList.add(user);
			UserManager.Instance.adduser(userId, user);
		}
    	RoomManager.Instance.GameOver(battleUserVo.roomId);
    	RoomManager.Instance.rooms.put(battleUserVo.roomId, new Room(battleUserVo.roomId, userList));
		System.out.println("RoomId: " + battleUserVo.roomId + "has been created");
		return new ResultInfo(Message.Result.Success, "");
    }
    
	/**
     * 添加直播用户信息
     *
     * @return
     */
    @RequestMapping("/AddLiveUserInfo")
    @ResponseBody
    public ResultInfo AddLiveUserInfo(@RequestBody LiveUserVo liveUserVo) {
    	User user=new User(liveUserVo.userId, liveUserVo.roomId);
		UserManager.Instance.liveUsers.put(liveUserVo.userId, user);    
		
		Room room=RoomManager.Instance.liveRooms.get(liveUserVo.roomId);
		if(room==null) {
			List<User> userList=Collections.synchronizedList(new ArrayList<>());  //用户集合
			userList.add(user);
			room=new Room(liveUserVo.roomId, userList);
		}else {
			room.users.add(user);
		}
		RoomManager.Instance.liveRooms.put(liveUserVo.roomId, room);
    	
    	return new ResultInfo(Message.Result.Success, "");
    }
}
