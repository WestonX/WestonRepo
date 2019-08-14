package com.jt.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.pojo.User;
import com.jt.service.DubboCartService;
import com.jt.util.UserThreadLocal;
import com.jt.vo.SysResult;

@Controller
@RequestMapping("/cart")
public class CartController {
	
	@Reference(timeout=3000,check=false)
	private DubboCartService cartService;
	
	@RequestMapping("/show")
	public String findCartListByUser(Model model) {
		/*
		 * User user = (User) request.getAttribute("JT_USER"); Long userId =
		 * user.getId();
		 */
		Long userId = UserThreadLocal.getUser().getId();
		List<Cart> cartList = 
				cartService.findCartListByUser(userId);
		model.addAttribute("cartList", cartList);
		return "cart";	//转发到cart.jsp页面
	}
	
	@RequestMapping("/add/{itemId}")
	public String saveCart(Cart cart) {
		Long userId = UserThreadLocal.getUser().getId();
		cart.setUserId(userId);
		cartService.saveCart(cart);
		return "redirect:/cart/show";
	}
	
	//实现数量修改
	@RequestMapping("/update/num/{itemId}/{num}")
	@ResponseBody
	public SysResult updateCartNum(Cart cart) {
		try {
			Long userId = UserThreadLocal.getUser().getId();
			cart.setUserId(userId);
			cartService.updateCartNum(cart);
			return SysResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.fail();
		}
	}
	
	
	

}
