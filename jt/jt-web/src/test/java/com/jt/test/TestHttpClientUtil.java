package com.jt.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jt.util.HttpClientService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestHttpClientUtil {
	
	@Autowired
	private HttpClientService httpClientService;
	
	//获取manage中itemjson串
	@Test
	public void testGet() {
		String url = "http://manage.jt.com/web/item/findItemById/562379";
		//测试传参 要求传递id/name
		Map<String,String> params = new HashMap<>();
		params.put("id", "1001");
		params.put("name", "tomcat");
		String result = 
				httpClientService.doGet(url, params);
		System.out.println(result);
	}
}
