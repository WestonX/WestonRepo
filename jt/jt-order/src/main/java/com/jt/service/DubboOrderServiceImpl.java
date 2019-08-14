package com.jt.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.OrderItemMapper;
import com.jt.mapper.OrderMapper;
import com.jt.mapper.OrderShippingMapper;
import com.jt.pojo.Order;
import com.jt.pojo.OrderItem;
import com.jt.pojo.OrderShipping;

@Service
public class DubboOrderServiceImpl implements DubboOrderService {
	
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderShippingMapper orderShippingMapper;
	@Autowired
	private OrderItemMapper orderItemMapper;
	
	@Transactional  //事务控制
	@Override
	public String saveOrder(Order order) {
		String totalPrice = totalPrice(order);
		String orderId = "" + order.getUserId()
		+ System.currentTimeMillis();
		Date date = new Date();
		//1.入库订单
		order.setOrderId(orderId)
			 .setPayment(totalPrice)
			 .setStatus(1)
			 .setCreated(date)
			 .setUpdated(date);
		orderMapper.insert(order);
		System.out.println("订单入库成功!!!!!!");
		
		//2.入库订单物流
		OrderShipping shipping = order.getOrderShipping();
		shipping.setOrderId(orderId)
				.setCreated(date)
				.setUpdated(date);
		orderShippingMapper.insert(shipping);
		System.out.println("订单物流成功!!!!!");
		
		//3.订单商品入库
		//insert into 表名(xxx,xx,xx) values(xxx,xxx,xxx),(xxx,xxx,xx)....
		List<OrderItem> lists = order.getOrderItems();
		for (OrderItem orderItem : lists) {
			orderItem.setOrderId(orderId)
					 .setCreated(date)
					 .setUpdated(date);
			orderItemMapper.insert(orderItem);
		}
		System.out.println("订单入库全部成功!!!!!");
		return orderId;
	}

	@Override
	public Order findOrderById(String id) {
		Order order = orderMapper.selectById(id);
		OrderShipping shipping = orderShippingMapper.selectById(id);
		QueryWrapper<OrderItem> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("order_id", id);
		List<OrderItem> items = orderItemMapper.selectList(queryWrapper);
		order.setOrderShipping(shipping).setOrderItems(items);
		return order;
	}
	
	
	//计算订单的总价 price = (商品价格 * 数量) +....
	public String totalPrice(Order order) {
		List<OrderItem> orderItems = order.getOrderItems();
		Long totalPrice = 0L;
		for (OrderItem orderItem : orderItems) {
			//long 
			totalPrice += orderItem.getPrice() * orderItem.getNum();
		}  
		return ""+totalPrice;
	}
}
