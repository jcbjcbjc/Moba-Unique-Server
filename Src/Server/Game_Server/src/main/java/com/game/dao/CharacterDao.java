package com.game.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.game.entity.Character;
import com.game.entity.User;

public interface CharacterDao {

	int addList(List<Character> list);
	
	int update(Character character);
	
	List<Character> queryByUserId(@Param("userId") Integer userId);
	
	Character queryById(@Param("id") Integer id);

	List<Integer> queryIdsByUserId(@Param("userId") Integer userId);
	
	List<Character> queryCombatPowerStat();
	
	Character queryByUserIdCId(@Param("userId") Integer userId,@Param("cId") Integer cId);
}