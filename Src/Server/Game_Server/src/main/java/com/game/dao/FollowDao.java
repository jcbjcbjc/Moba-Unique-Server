package com.game.dao;

import com.game.entity.Character;
import com.game.entity.Follow;
import com.game.vo.FollowFenSiVo;

import org.apache.ibatis.annotations.*;

import java.util.List;

public interface FollowDao {
    
	int delete(Integer id);
	
	int add(Follow follow);
	
	List<Follow> queryByUserId(Integer userId);
	//查询粉丝数量
	List<FollowFenSiVo> queryFenSiCount(List<Integer> ids);

}