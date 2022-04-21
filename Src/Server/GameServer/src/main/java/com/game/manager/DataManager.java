package com.game.manager;

import com.alibaba.fastjson.JSON;
import com.game.data.*;
import com.game.entity.Character;
import com.game.spring.SpringBeanUtil;
import com.game.util.GsonUtils;
import com.game.util.ReadTxt;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.io.IOUtils;
import org.apache.ibatis.annotations.Insert;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

import java.util.HashMap;
import java.util.Map;
/**
 * @author 贾超博
 *
 * Component in Manager
 *
 * DataManager Class
 *
 *
 */
public class DataManager {

    public static DataManager Instance = new DataManager();


    public GameConfig gameConfig = null;


    public void init() {
        System.out.println("开始加载DataManager...");
        String json;
        json = ReadTxt.getDefine("GameConfig.txt");
        gameConfig = GsonUtils.jsonToObj(json, GameConfig.class);
    }
}

