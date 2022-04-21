package com.game.manager;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

//import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;
import org.springframework.util.StringUtils;

import com.game.proto.Message.NRoom;
import com.game.proto.Message.Result;
import com.game.util.GsonUtils;
import com.game.util.HttpUtil;
import com.game.util.PropertyUtil;
import com.game.vo.BattleUserVo;
import com.game.vo.LiveUserVo;
import com.game.vo.ResultInfo;
/**
 * @author 贾超博
 *
 * Component in Manager
 *
 * BattleServerManager Class
 *
 *
 */
public class BattleManager {
	
	public static BattleManager Instance=new BattleManager();
	//key=> ip:httpPort:tcpPort  value:房间数
	public Map<String, Integer> battleServerMap=Collections.synchronizedMap(new HashMap<>());
	
	/**
	 * 更新房间数
	 * @param ipPortStr
	 * @param type 1、添加  2、减少
	 */
	public void updateRoomNum(String ipPortStr,int type) {
		Integer num=BattleManager.Instance.battleServerMap.get(ipPortStr);
		if (num==null) {
			num=0;
		}else {
			if(type==1) {				
				num++;
			}else {
				num--;				
			}
		}
		BattleManager.Instance.battleServerMap.put(ipPortStr, num);
	}
    
	/**
	 * 发送添加对战用户信息
	 */
	public boolean SendAddBattleUserInfo(NRoom.Builder roomBuilder,BattleUserVo battleUserVo) {
		try {
			//查找房间数最少的
			Set<Entry<String, Integer>> set=battleServerMap.entrySet();
			int minNum = -1;
			String ipPortStr="";
			for (Entry<String, Integer> entry : set) {
				int num=entry.getValue();
				if(minNum == -1 || minNum > num) {
					minNum = num;
					ipPortStr = entry.getKey();
				}
			}
			if (!StringUtils.isEmpty(ipPortStr)) {
				String [] ipPortArr = ipPortStr.split(":");
				String url="http://"+ipPortArr[0]+":"+ipPortArr[1]+"/BattleController/AddBattleUserInfo";
				String result=HttpUtil.sendPost(url, GsonUtils.objToJson(battleUserVo));
				ResultInfo resultInfo = GsonUtils.jsonToObj(result, ResultInfo.class);
				if(resultInfo != null && resultInfo.result==Result.Success) {  //成功
					roomBuilder.setIpPortStr(ipPortStr);
					this.updateRoomNum(ipPortStr, 1);
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 发送添加直播用户信息
	 */
	public boolean SendAddLiveUserInfo(String ipPortStr,LiveUserVo liveUserVo) {
		try {
			if (!StringUtils.isEmpty(ipPortStr)) {
				String [] ipPortArr = ipPortStr.split(":");
				String url="http://"+ipPortArr[0]+":"+ipPortArr[1]+"/BattleController/AddLiveUserInfo";
				String result=HttpUtil.sendPost(url, GsonUtils.objToJson(liveUserVo));
				ResultInfo resultInfo = GsonUtils.jsonToObj(result, ResultInfo.class);
				if(resultInfo != null && resultInfo.result==Result.Success) {  //成功
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
