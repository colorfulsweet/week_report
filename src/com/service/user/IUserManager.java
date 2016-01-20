package com.service.user;

import java.io.OutputStream;

import com.bean.hibernate.User;

public interface IUserManager {
	/**
	 * 验证用户是否已存在
	 * @param username
	 * @return 不存在返回true,存在返回false
	 */
	public String checkUserExist(String username);
	/**
	 * 创建用户
	 * @param user 用户实体对象
	 * @return 用户实体对象
	 */
	public User createUser(User user);
	/**
	 * 验证用户名和密码的正确性
	 * @param user 用户实体对象
	 * @return 正确返回用户实体对象, 错误返回null
	 */
	public User checkUser(User user);
	/**
	 * 修改用户密码
	 * @param user
	 * @return 操作成功返回true,失败返回false
	 */
	public boolean changePassword(User user);
	/**
	 * 生成随机验证码图片
	 * @param width 图片宽度
	 * @param height 图片高度
	 * @param output 字节输出流
	 * @return 生成的随机字符串
	 */
	public String createCheckCode(int width,int height,OutputStream output);

}
