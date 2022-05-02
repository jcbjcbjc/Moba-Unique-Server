package com.game.manager;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.game.models.Room;
import com.game.models.User;
/**
 * @author 贾超博
 *
 * Core System Manager
 *
 *Manage Room
 *
 */

public class RoomManager {
	
	public static RoomManager Instance = new RoomManager();
	
	//key:房间id	value:用户id集合
	public Map<Integer, Room> rooms=Collections.synchronizedMap(new HashMap<Integer, Room>());
	public Map<Integer, Room> liveRooms=Collections.synchronizedMap(new HashMap<Integer, Room>());
	
	/**
	 * 游戏结束
	 */
	public void GameOver(int roomId) {
		this.removeUser(rooms, roomId);
		this.removeUser(liveRooms, roomId);
	}
	
	private void removeUser(Map<Integer, Room> roomMap, int roomId) {
		Room room=roomMap.remove(roomId);
		if(room != null) {
			room.isGameOver=true;
			for (User user: room.users) {
				UserManager.Instance.users.remove(user.id);
				UserManager.Instance.liveUsers.remove(user.id);
				ConnectionManagerKCP.removeConnection(user.id);
			}
		}
	}
}
