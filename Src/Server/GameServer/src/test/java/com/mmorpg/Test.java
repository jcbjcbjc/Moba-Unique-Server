package com.mmorpg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;

import org.springframework.util.StringUtils;

import com.game.util.ReadTxt;

public class Test {

	  /**
     * 读取 文件
     */
    private static String readToString(String fileName) {
        String encoding = "UTF-8";
        File file = new File(fileName);
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(filecontent, encoding);
        } catch (UnsupportedEncodingException e) {
            System.err.println("The OS does not support " + encoding);
            e.printStackTrace();
            return null;
        }
    }
    
	public static void main(String[] args) {
//		 String txt = readToString("E:\\game\\moba\\Src\\Server\\GameServer\\src\\main\\resources\\config\\RockerSpeed.txt");
////		 System.out.println(txt);
//		 String [] strArr=txt.split("\n");
//		 String newStr="";
//		 for (String str : strArr) {
//			 if(StringUtils.isEmpty(str)) {
//				 continue;
//			 }
//			String [] arr = str.split(",");
//			 float x=Float.valueOf(arr[1]);
//			 float y=Float.valueOf(arr[2]);
//			 int ds = Integer.valueOf(arr[0]);
//			 if(ds >= 0 && ds <= 90) {  //右下
//				 x = Math.abs(x);
//				 y = Math.abs(y);
//			 }else if(ds > 90 && ds < 180) {  //右上
//				 x = Math.abs(x);
//				 y = -Math.abs(y);
//			 }else if(ds >= 180 && ds <= 270) {  //左上
//				 x = -Math.abs(x);
//				 y = -Math.abs(y);
//			 }else {  //左下
//				 x = -Math.abs(x);
//				 y = Math.abs(y);				
//			 }
//			 newStr+=arr[0]+","+x+","+y+"\n";
//		}
//		 System.out.println(newStr);
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);
        String newStr="";
		float radius=1;
		for (int i = 0; i < 360; i++) {
			double y=radius * Math.sin(i*Math.PI/180); //对边
			double x = Math.sqrt(radius * radius - y*y);
			
		 if(i >= 0 && i < 90) {  //右下
			 double tempX = Math.abs(x);
			 x = Math.abs(y);
			 y = tempX;
		 }else if(i > 90 && i < 180) {  //右上
			 double tempX = -Math.abs(x);
			 x = Math.abs(y);
			 y = tempX;
		 }else if(i >= 180 && i < 270) {  //左上
			 double tempX = -Math.abs(x);
			 x = -Math.abs(y);
			 y = tempX;
		 }else {  //左下
			 double tempX = Math.abs(x);
			 x = -Math.abs(y);
			 y = tempX;				
		 }
			newStr += i+","+blxs(x+"")+","+blxs(y+"")+"\n";
		}
		System.out.println(newStr);
	}
	
	public static int blxs(String num) {
		int len = num.indexOf(".")+3;
		if(len > num.length()) {
			len = num.length();
		}
		return (int)(Float.valueOf(num.substring(0, len))*100);
	}
}
