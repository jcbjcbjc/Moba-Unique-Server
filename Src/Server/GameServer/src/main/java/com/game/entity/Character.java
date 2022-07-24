package com.game.entity;



import com.game.proto.C2GNet.NCharacter;
import com.game.spring.DBUtil;
/**
 * @author 贾超博
 *
 * Entity Class
 *
 * Character Class
 */
public class Character {
    public Integer id;
    public int cId;  //配置id
    public int level=1;
    public long exp;
    public Integer userId;
    public int attSpot;
    public int defSpot;
    public int hpSpot;
    public int criSpot;
    public int resuSpot;
    public int speedSpot;
    public int cdSpot;
    
    //属性数值
    public float att;  //攻击
    public float def;  //防御
    public float hp; //血量
    public float cri; //暴击率
    public float resu;  //复活率
    public float speed;  //速度提升率
    public float speedValue;  //速度值
    public float cd;  //降低cd率
    public long levelExp;  //等级经验
    
    /*非数据库实体字段*/
    public boolean isUpdateFlag=false;  //是否变更
    public Character() {
    	
    }
    
    public Character(int cId, Integer userId) {
		super();
		this.cId = cId;
		this.userId = userId;

	}

	public NCharacter getCharacterInfo() {

    	return NCharacter.newBuilder()

    				.build(); 
    }
    

  /**
   *   更新到数据库
   */
  public void updateToDB() {  
    if(this.isUpdateFlag) {
       DBUtil.Instance.getCharacterDao().update(this);	
 	   this.isUpdateFlag=false;
    }
  }
  

  
}