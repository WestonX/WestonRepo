package com.jt.util;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 说明:该工具类中主要实现对象与json互转
 * static 方法被用户直接调用
 * @author Administrator
 *
 */
public class ObjectMapperUtil {
	//成员变量:是否有线程安全性问题????
	//private static final Integer abc = 123; //有
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	//1.对象转化为json
	public static String toJSON(Object target) {
		String result = null;
		try {
			result = objectMapper.writeValueAsString(target);
		} catch (Exception e) {
			e.printStackTrace();
			//或者打印日志
			throw new RuntimeException();
		}
		return result;
	}
	
	//2.转化对象  json/class
	public static <T> T toObject(String json,Class<T> targetClass) {
		T target = null;
		try {
			target = objectMapper.readValue(json, targetClass);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return target;
	}
}
