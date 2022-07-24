package com.game.dao;

import com.game.entity.User;

import java.util.List;

public interface UserDao {

	User queryByUserNamePassword(User user);
	
	List<User> queryByIds(List<Integer> ids);
	
	int add(User user);
	
	int update(User user);
	
	User queryById(Integer userId);

}