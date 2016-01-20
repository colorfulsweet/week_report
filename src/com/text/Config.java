package com.text;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
/**
 * 配置信息(从"config.properties"文件中读取)
 * @author Administrator
 *
 */
public class Config {
	public static final String ADMIN_NAME;//管理员用户名
	public static final String ADMIN_PASSWORD;//管理员密码
	public static final int INITIAL_TIME;//初始刷新时间间隔
	static {
		Properties p = new Properties();
		InputStream in = Config.class.getClassLoader().getResourceAsStream("config.properties");
		try {
			p.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ADMIN_NAME = p.getProperty("admin_name");
		ADMIN_PASSWORD = p.getProperty("admin_password");
		INITIAL_TIME = Integer.parseInt(p.getProperty("initial_time", "60"));
	}
}
