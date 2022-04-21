package com.game.util;

import java.util.Date;

public class TimeUtil {
    private static long beginTime;
    public static long deltaTime=0;  //两帧直接的间隔
    public static long lastTick=0;


    public static void init() {
        beginTime = System.currentTimeMillis();
    }

    // 获取游戏运行时间，秒为单位
    public static int getRunTime() {
        return (int) (System.currentTimeMillis() - beginTime) / 1000;
    }

    // 获取一个时间戳，以项目启动为0 毫秒
    public static int getTimeStamp() {
        return (int) (System.currentTimeMillis() - beginTime);
    }
    
    public static void Tick() {
    	long currentTick=new Date().getTime();
    	if(lastTick==0) {
    		lastTick=currentTick;
    	}
    	deltaTime=currentTick-lastTick;
    	lastTick=currentTick;
    }

}
