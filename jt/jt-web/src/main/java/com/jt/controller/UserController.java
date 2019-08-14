package com.jt.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.User;
import com.jt.service.DubboUserService;
import com.jt.vo.SysResult;

import redis.clients.jedis.JedisCluster;

@Controller
@RequestMapping("/user")
public class UserController {

	@Reference(timeout=3000,check=false)
	private DubboUserService userService;
	
	@Autowired
	private JedisCluster jedisCluster;

	//实现页面跳转  RESTFul
	@RequestMapping("/{moduleName}")
	public String module(@PathVariable String moduleName) {

		return moduleName;
	}

	//实现用户新增
	@RequestMapping("/doRegister")
	@ResponseBody
	public SysResult saveUser(User user) {
		try {
			userService.saveUser(user);
			return SysResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.fail();
		}
	}

	//HttpServletResponse response SpringMVC提供的支持.底层就是servlet
	//实现用户登陆
	@RequestMapping("/doLogin")
	@ResponseBody
	public SysResult doLogin(User user,HttpServletResponse response) {
		try {
			//用户登陆成功后,返回秘钥
			String token = userService.doLogin(user);
			if(!StringUtils.isEmpty(token)) {
				//用户操作正确,将数据保存到cookie中
				Cookie cookie = new Cookie("JT_TICKET",token);
				cookie.setDomain("jt.com");    //标识共享的权限
				cookie.setMaxAge(7*24*3600);   //单位秒
				cookie.setPath("/");           //代表Cookie的权限
				response.addCookie(cookie);
				return SysResult.ok();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.fail();
	}
	
	/**
	 * 	实现用户登出操作
	 *  1.删除redis缓存
	 *  2.删除cookie
	 *  
	 *  思路:
	 * 1.利用request对象获取Cookie信息,之后获取token
	 * 2.删除redis.必须先获取Cookie中的token
	 * 3.删除cookie
	 * 
	 *复习:
	 *	cookie.setMaxAge(大于0); 代表超时时间
	 *	cookie.setMaxAge(0);     代表删除cookie
	 *  cookie.setMaxAge(-1);    代表关闭会话后删除cookie
	 *	
	 * @return
	 */
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request,
			HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		String token = null;
		if(cookies.length !=0) {
			for (Cookie cookie : cookies) {
				String cookieName = "JT_TICKET";
				if(cookieName.equals(cookie.getName())) {
					//证明Cookie成功获取
					token = cookie.getValue();
					break;
				}
			}
		}
		
		//2.根据token删除redis数据
		jedisCluster.del(token);
		//3.删除Cookie  null个别浏览器可能会有问题
		Cookie cookie = new Cookie("JT_TICKET","");
		cookie.setMaxAge(0);
		cookie.setPath("/");
		cookie.setDomain("jt.com");
		response.addCookie(cookie);
		
		//4.重定向到系统首页   
		//重定向:本次业务结束,开始新的业务
		//转发:  在一个业务中完成
		return "redirect:/"; //重定向到根目录
	}
}
