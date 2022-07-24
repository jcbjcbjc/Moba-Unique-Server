package com.game.util;

import java.util.Random;

/**
 * @Author 乔占江 qq:59663479
 * @Data 2021/2/2 21:32
 * @Description
 * @Version 1.0
 */
public class RandomUtil {

    static Random random = new Random();

    /**
     * 随机生成指定范围随机数
     * @param min
     * @param max
     * @return
     */
    public static int getRandomNum(int min,int max){
        return random.nextInt(max - min + 1) + min; 
    }

    
}
