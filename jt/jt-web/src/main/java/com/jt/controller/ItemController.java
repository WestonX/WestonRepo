package com.jt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;

//编辑前台Controller
@Controller
@RequestMapping("/items")
public class ItemController {
	
	@Autowired
	private ItemService itemService;
	 
	
	@RequestMapping("/{itemId}")
	public String findItemById(@PathVariable Long itemId,
			Model model) {
		//根据商品id号查询数据
		Item item = 
			itemService.findItemById(itemId);
		ItemDesc itemDesc = 
			itemService.findItemDescById(itemId);
		//将查询到的数据保存到request域中
		//页面通过el表达式获取
		model.addAttribute("item", item);
		model.addAttribute("itemDesc", itemDesc);
		return "item"; //跳转商品展现页面
	}
}
