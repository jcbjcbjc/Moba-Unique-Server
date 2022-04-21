package com.game.util;

import java.io.IOException;
import java.nio.charset.UnsupportedCharsetException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class HttpUtil {

	/**
	 * 发送post请求
	 * @param url  链接
	 * @param param  参数
	 * @return  返回值
	 */
	public static String sendPost(String url,String param) {
		String result=null;
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);
			StringEntity s = new StringEntity(param, "UTF-8");
			s.setContentEncoding("UTF-8");
			s.setContentType("application/json;charset=utf-8");
			post.setEntity(s);
			HttpResponse res = client.execute(post);
			result = EntityUtils.toString(res.getEntity());
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return result;
	}
}
