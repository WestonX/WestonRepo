package com.jt.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.experimental.Accessors;

//创建商品对象  全部属性定义使用包装类型
@Data
@Accessors(chain = true)
@TableName("tb_item")
@JsonIgnoreProperties(ignoreUnknown = true) 
//排除未知属性 适用于对象转化时有多余的属性
public class Item extends BasePojo{
	@TableId(type = IdType.AUTO)
	private Long id;				//商品id号
	private String title;			//商品标题信息
	private String sellPoint;		//商品卖点信息
	private Long   price;			//保存数据时扩大100倍,展现时缩小100倍//double有精度问题. double计算速度慢
	private Integer num;			//商品数量
	private String barcode;			//条形码
	private String image;			//保存图片url地址 a.jpg,b.jpg,c.jpg
	private Long   cid;				//商品分类信息
	private Integer status;			//1正常，2下架，3删除
	
	
	//为了实现页面图片正确回显添加该方法
	public String[] getImages() {
		
		return image.split(",");
	}
	//如果只有get方法那么json转化时必然调用set方法赋值.
}
