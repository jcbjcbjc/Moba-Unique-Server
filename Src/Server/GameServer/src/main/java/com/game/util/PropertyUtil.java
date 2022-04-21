package com.game.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.util.Properties;
import java.util.Set;

/**
 * Desc:properties文件获取工具类
 * Created by hafiz.zhang on 2016/9/15.
 */
public class PropertyUtil {
    private static final Logger logger = LoggerFactory.getLogger(PropertyUtil.class);
    private static Properties props;
    static{
        loadProps();
    }

    synchronized static private void loadProps(){
        props = new Properties();
        InputStream in = null;
        try {
//            String path = new ClassPathResource("").getFile().getPath();//PropertyUtil.class.getResource("/").getPath();
//            System.out.println("PropertyUtil path="+path);
            in = new ClassPathResource("/conf.properties").getInputStream();
            
            //获取web-inf目录的路径
//            path=path.substring(1, path.indexOf("classes"));
//            in = new FileInputStream(path+"/conf.properties");
            props.load(in);
        } catch (FileNotFoundException e) {
            logger.error("conf.properties文件未找到");
        } catch (IOException e) {
            logger.error("出现IOException");
        } finally {
            try {
                if(null != in) {
                    in.close();
                }
            } catch (IOException e) {
                logger.error("conf.properties文件流关闭出现异常");
            }
        }
        logger.info("加载properties文件内容完成...........");
        logger.info("properties文件内容：" + props);
    }

    public static String getProperty(String key){
        if(null == props) {
            loadProps();
        }
        return props.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        if(null == props) {
            loadProps();
        }
        return props.getProperty(key, defaultValue);
    }
    
    public static Set<Object> keys(){
    	return props.keySet();
    }
}