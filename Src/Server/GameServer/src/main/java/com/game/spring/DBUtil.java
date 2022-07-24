package com.game.spring;

import com.game.dao.*;
import com.game.entity.Character;
import com.game.entity.Follow;
import com.game.entity.User;

import com.game.service.FollowService;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * @author 贾超博
 *
 * DB Operation Class
 *
 *
 */


/**
 * 数据库操作都到这里执行
 */

public class DBUtil {
	
   public static DBUtil Instance=new DBUtil();
	
   private UserDao userDao;
   private CharacterDao characterDao;
   private FollowDao followDao;

	public UserDao getUserDao() {
		if(userDao==null) {
			userDao=SpringBeanUtil.getBean(UserDao.class);
		}
		return userDao;
	}

	public CharacterDao getCharacterDao() {
		if(characterDao==null) {
			characterDao=SpringBeanUtil.getBean(CharacterDao.class);
		}
		return characterDao;
	}

	public FollowDao getFollowDao() {
		if(followDao==null) {
			followDao=SpringBeanUtil.getBean(FollowDao.class);
		}
		return followDao;
	}
	
	

}
