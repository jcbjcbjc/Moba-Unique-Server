package com.game.util;

import java.io.*;
import java.util.Set;

import org.springframework.core.io.ClassPathResource;

public class ReadTxt {
    static ReadTxt readTxt = new ReadTxt();
    
    public static void main(String[] args) {
        String s = getDefine("ItemDefine");
        System.out.println(s);
    }

    // 给出文件名，返回 所有内容
    public static String getDefine(String fileName) {
        // 本机时
        return readToString( "/config/" + fileName);
        // linux时
        //return readToString("/root/myPro/config/" + fileName);
    }

    /**
     * 读取 文件
     */
    private static String readToString(String fileName) {
        String encoding = "UTF-8";
        String content = "";
        try {
        	InputStream in = new ClassPathResource(fileName).getInputStream();
        	BufferedReader br = new BufferedReader(new InputStreamReader(in, encoding));
        	String str;
			while ((str = br.readLine()) != null) {
				content += str; 
			}
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
            return content;
    }
}
