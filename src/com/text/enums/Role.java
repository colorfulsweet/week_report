package com.text.enums;
/**
 * 用于表示用户角色的枚举
 * @author Administrator
 *
 */
public enum Role {
	user("user"),//普通用户
	admin("admin")//管理员
	;
	private String name;
	private Role(String name){
		this.name = name;
	}
	public String getName(){
		return name;
	}
}
