package com.game.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CachedThreadPoolUtil {

    private static ExecutorService cachedThreadPool;

    public static ExecutorService instance(){
        if(cachedThreadPool==null){
            cachedThreadPool=newCachedThreadPool();
        }
        return cachedThreadPool;
    }

    /**
     * 创建可缓存的线程池，2小时空闲则销毁
     * @return
     */
    private static ExecutorService newCachedThreadPool() {
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                2, TimeUnit.HOURS,
                new SynchronousQueue<Runnable>());
    }
}
