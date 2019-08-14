package com.jt.test;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

public class TestHttpClient {
	
	/**
	 * 1.创建httpClient对象
	 * 2.指定url请求路径
	 * 3.创建请求方法对象  get/post
	 * 4.发起request请求,获取response响应
	 * 5.判断请求是否正确   检查状态码是否为200
	 * 	 true  则通过response对象获取响应数据
	 * 	 false 则抛出异常 编辑日志.
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	@Test
	public void testGet() throws ClientProtocolException, IOException {
		HttpClient httpClient = 
				HttpClients.createDefault();
		String url = "http://manage.jt.com/web/item/findItemById/562379";
		//HttpGet httpGet = new HttpGet(url);
		HttpPost httpPost = new HttpPost(url);
		HttpResponse httpResponse = httpClient.execute(httpPost);
		if(httpResponse.getStatusLine()
					   .getStatusCode() == 200) {
			System.out.println("恭喜你获取数据成功!!!!");
			HttpEntity entity = httpResponse.getEntity();
			String data = EntityUtils.toString(entity);
			System.out.println(data);
		}else {
			System.out.println("用户请求有误!!!!!");
		}
	}
}
