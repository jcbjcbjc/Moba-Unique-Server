package com.game.vo;

import java.util.List;

public class BattleUserVo {

	public int roomId; // 房间id
	public List<Integer> userIdList; // 用户id集合

	public BattleUserVo(int roomId, List<Integer> userIdList) {
		super();
		this.roomId = roomId;
		this.userIdList = userIdList;
	}

}
