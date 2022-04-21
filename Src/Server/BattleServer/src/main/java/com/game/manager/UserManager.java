package com.game.manager;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.game.models.User;
/**
 * @author 贾超博
 *
 * Core System Manager
 *
 *Manage User
 *
 * add or remove user dynamically
 *
 */

public class UserManager {
	
	public static UserManager Instance = new UserManager();
	
	//key:用户id	value:用户
	public Map<Integer, User> users=Collections.synchronizedMap(new HashMap<Integer, User>());
	public Map<Integer, User> liveUsers=Collections.synchronizedMap(new HashMap<Integer, User>());
	
}
