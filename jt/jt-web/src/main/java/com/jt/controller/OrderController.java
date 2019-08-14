package com.jt.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.pojo.Order;
import com.jt.pojo.OrderItem;
import com.jt.pojo.User;
import com.jt.service.DubboCartService;
import com.jt.service.DubboOrderService;
import com.jt.util.UserThreadLocal;
import com.jt.vo.SysResult;

@Controller
@RequestMapping("/order")
public class OrderController {
	
	@Reference(timeout = 5000,check = false)
	private DubboOrderService orderService;
	@Reference(timeout = 5000,check = false)
	private DubboCartService cartService;
	
	//实现页面跳转
	@RequestMapping("/create")
	public String orderCreate(Model model) {
		Long userId = UserThreadLocal.getUser().getId();
		List<Cart> carts = cartService.findCartListByUser(userId);
		model.addAttribute("carts", carts);
		return "order-cart";
	}
	
	
	@RequestMapping("/submit")
	@ResponseBody
	public SysResult saveOrder(Order order) {
		try {
			Long userId = UserThreadLocal.getUser().getId();
			order.setUserId(userId);
			//当订单入库时需要返回订单ID号
			String orderId = orderService.saveOrder(order);
			if(!StringUtils.isEmpty(orderId)) {
				
				return SysResult.ok(orderId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.fail();
	}
	
	//根据orderId查询订单数据
	@RequestMapping("/success")
	public String findOrderById(String id,Model model) {

		Order order = orderService.findOrderById(id);
		model.addAttribute("order", order);
		return "success";
	}
	
	
	
	
	
	
	
	
	
}
