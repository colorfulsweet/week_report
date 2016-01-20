package com.text;
/**
 * 后台回传使用的文本信息
 * @author Administrator
 *
 */
public class TextInfo {
	public class UserInfo {
		public static final String AVAILABLE = "available";//用户名可用
		public static final String EXIST = "user_exist";//用户已存在
		public static final String SUCCESS = "success";//用户登陆验证成功
		public static final String FAILED = "failed";//用户名/密码错误
		public static final String FORBID = "forbid";//用户已被禁用
		public static final String CHECKCODE_FALSE = "checkcode_false";//验证码正确
		public static final String CHECKCODE_TRUE = "checkcode_true";//验证码错误
	}
	public class Admin{
		public static final String SUCCESS = "success";//操作成功
		public static final String FAILED = "failed";//操作失败
		
		public static final String USER_LOCK = "user_lock";//用户当前状态为禁用
		public static final String USER_UNLOCK = "user_unlock";//用户当前状态为可用
	}
	
}
