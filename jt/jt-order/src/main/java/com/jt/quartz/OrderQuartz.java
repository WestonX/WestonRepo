package com.jt.quartz;



import java.util.Calendar;
import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.OrderMapper;
import com.jt.pojo.Order;

//准备订单定时任务
@Component
public class OrderQuartz extends QuartzJobBean{
	
	@Autowired
	private OrderMapper orderMapper;
	/**
	 * 需求:将超时订单状态修改  未支付的状态进行修改
	 * 判断:	date - create > 30分钟  一定超时
	 * 		date - 30> create
	 * 		create < date - 30	
	 * sql:
	 * 	 update order set status = 6,updated = date
	 * 	 where status = 1 and created < timeOut
	 */
	@Override
	@Transactional
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		//计算超时时间 date - 30分钟
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, -30);
		Date timeOut = calendar.getTime();
		Order order = new Order();
		order.setStatus(6)
			 .setUpdated(new Date());
		UpdateWrapper<Order> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("status",1)
					 .lt("created",timeOut);
		orderMapper.update(order, updateWrapper);
		System.out.println("定时任务执行完成!!!!!");
	}
}
