package com.game.spring;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.game.util.PropertyUtil;
/**
 * @author 贾超博
 *
 * Component
 *
 * Schedule
 *
 *
 */
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class ScheduleTask {
	
//	/**
//	 * 更新配置
//	 */ 
//    @Scheduled(cron = "0 */5 * * * ?")
//    private void updateConfig() {
//    	Set<Object> keys=PropertyUtil.keys();
//    	for (Object key : keys) {
//    		if(key==null) {
//    			continue;
//    		}
//    		String keyStr=key.toString();
//    		if (keyStr.equals("hall.service")) {  //大厅服务
//    			
//			}
//		}
//    }
//    
//    
//    /**
//     * 启动执行
//     */
//    @PostConstruct
//    public void init(){ 
//      this.updateConfig();
//    }
}