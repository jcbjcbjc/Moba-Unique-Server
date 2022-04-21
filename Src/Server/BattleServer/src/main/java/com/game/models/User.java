package com.game.models;

import com.game.enums.UserStatus;
/**
 * @author 贾超博
 *
 * Entity Class
 *
 *
 */
public class User {

	public int id;
	public int rooomId; // 房间id
	public UserStatus userStatus = UserStatus.normal; // 用户状态

	public User(int id, int rooomId) {
		super();
		this.id = id;
		this.rooomId = rooomId;
	}

}
