package com.game.spring;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.game.entity.Character;
import com.game.entity.User;
import com.game.manager.BattleManager;
import com.game.manager.ChatManager;
import com.game.manager.PowerRankingManager;
import com.game.manager.UserManager;
import com.game.util.PropertyUtil;
import com.game.vo.FollowFenSiVo;
/**
 * @author 贾超博
 *
 * System
 *
 * Config Class
 *
 *
 */


@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class ScheduleTask {
	
	/**
	 * 更新对象到数据库
	 */ 
    @Scheduled(cron = "0 */3 * * * ?")
    private void timerExecute() {
    	UserManager.Instance.updateObjDBInfo();
    }
    
	/**
	 * 每天0点执行
	 */ 
    @Scheduled(cron = "0 0 0 * * ?")
    private void timerExecute2() {
    	//战力统计
    	PowerRankingManager.Instance.combatPowerStat();
    	//清理聊天数据
    	ChatManager.Instance.ClearChatMessage();
    }
    
    /**
	 * 更新配置
	 */ 
    @Scheduled(cron = "0 */5 * * * ?")
    private void updateConfig() {
    	Set<Object> keys=PropertyUtil.keys();
    	for (Object key : keys) {
    		if(key==null) {
    			continue;
    		}
    		String keyStr=key.toString();
    		if (keyStr.contains("battle.service")) {
    			String ipPort=PropertyUtil.getProperty(keyStr);
    			BattleManager.Instance.updateRoomNum(ipPort, 1);
			}
		}
    }

	private void InitAllModules(){}
    
    /**
     * 启动执行
     */
    @PostConstruct
    public void init(){ 
      this.updateConfig();
    }
    
}