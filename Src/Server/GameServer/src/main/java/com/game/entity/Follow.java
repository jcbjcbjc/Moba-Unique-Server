package com.game.entity;

public class Follow {
    public Integer id;
    public Integer followId;
    public Integer userId;
    
    /*非数据库实体字段*/
    public boolean status; // 在线状态
    
    public Follow() {
    	
    }
    
	public Follow(Integer followId, Integer userId) {
		super();
		this.followId = followId;
		this.userId = userId;
	}


}