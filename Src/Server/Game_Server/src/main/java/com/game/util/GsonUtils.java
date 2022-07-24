package com.game.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

public class GsonUtils {

    public static  Gson  gson = new GsonBuilder().disableHtmlEscaping().create();

    /**
     * 对象转json
     * @param obj
     * @return
     */
    public static String objToJson(Object obj){
        return gson.toJson(obj);
    }

    /**
     * 字符串转对象
     */
    public static <T> T jsonToObj(String str, Class<T> cls) {
        return gson.fromJson(str, cls);
    }

    /**
     * 字符串转map
     */
    public static  <V,T> Map<V, T> jsonToMap(String str, TypeToken typeToken) {
        return gson.fromJson(str, typeToken.getType());
    }

    
    public static void main(String[] args) {
    }

    /**
     * 转成list
     */
    public static <T> List<T> GsonToList(String str, Class<T> cls) {
        List<T> list = null;
        if (str != null) {
            list = gson.fromJson(str, new TypeToken<List<T>>(){}.getType());
        }
        return list;
    }
    
    /**
     * 转成list
     */
    public static <T> List<T> GsonToList(String str, TypeToken typeToken) {
        List<T> list = null;
        if (str != null) {
            list = gson.fromJson(str, typeToken.getType());
        }
        return list;
    }
}