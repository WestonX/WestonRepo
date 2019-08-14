package com.jt.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import com.jt.pojo.User;
import com.jt.util.ObjectMapperUtil;
import com.jt.util.UserThreadLocal;

import redis.clients.jedis.JedisCluster;
//spring4的版本需要立即重写3个拦截器方法
//spring5的版本 需要哪个方法自己重写即可.
@Component	//将对象交给spring容器管理
public class UserInterceptor implements HandlerInterceptor{
	
	@Autowired
	private JedisCluster jedisCluster;
	
	/**
	 * 实现用户请求的拦截
	 * 如果用户满足登陆要求,则用户跳转页面
	 * 如果用户没有登陆,则重定向到登陆页面
	 * 
	 * 验证用户是否登陆步骤:
	 * 	1.获取cookie数据.
	 *  2.从cookie获取token数据.
	 *  3.通过token数据查询redis.
	 *  4.判断是否含有数据
	 *  	true 表示用户已经登陆 放行请求
	 *  	false 表示用户没有登陆则重定向到
	 *  	登陆页面.
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		Cookie[] cookies = request.getCookies();
		String token = null;
		for (Cookie cookie : cookies) {
			
			if("JT_TICKET".equals(cookie.getName())) {
				token = cookie.getValue();
				break;
			}
		}
		
		if(!StringUtils.isEmpty(token)) {
			//表示token数据有值,查询redis缓存服务器
			String userJSON = jedisCluster.get(token);
			
			if(!StringUtils.isEmpty(userJSON)) {
				//表示redis服务器中有数据,则表示用户登陆成功.予以放行
				System.out.println("用户验证已经登陆,放行");
				User user = 
						ObjectMapperUtil.toObject(userJSON, User.class);
				request.setAttribute("JT_USER", user);
				
				//2.将数据保存到threadLocal中
				UserThreadLocal.setUser(user);
				return true;
			}
		}
		
		System.out.println("用户没有登陆,予以拦截!!!!");
		response.sendRedirect("/user/login.html");
		//true代表放行   false 代表拦截
		return false;
	}
	
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
		//为了防止内存泄漏关闭ThreadLocal
		UserThreadLocal.remove();
	}
}	
