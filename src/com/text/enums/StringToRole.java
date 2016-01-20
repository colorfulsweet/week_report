package com.text.enums;

import org.springframework.core.convert.converter.Converter;

/**
 * 由String类型转化为Role枚举类型对象的工具类
 * @author Administrator
 *
 */
public class StringToRole implements Converter<String,Role> {
	@Override
	public Role convert(String role) {
		for(Role r:Role.values()){
			if(r.getName().equals(role)){
				return r;
			}
		}
		return null;
	}
}
