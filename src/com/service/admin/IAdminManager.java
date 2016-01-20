package com.service.admin;

import java.util.List;

import org.hibernate.HibernateException;

import com.bean.Admin;
import com.bean.hibernate.SmtpHost;
import com.bean.hibernate.User;

public interface IAdminManager {
	/**
	 * 验证管理员账户的正确性
	 * @param admin
	 * @return 验证为正确返回true,错误返回false
	 */
	public boolean checkAdmin(Admin admin);
	
	/**
	 * 初始化数据库
	 */
	public void initDatabase() throws HibernateException;
	
	/**
	 * 获取用户列表
	 * @param page 分页查询-页码
	 * @param pageSize 每页的记录数量
	 * @return 所有用户对象的集合
	 */
	public List<User> getUserList(int page,int pageSize);
	/**
	 * 统计用户的总数
	 * @return 用户总数
	 */
	public long getUserCount();
	/**
	 * 获取SMTP服务器列表
	 * @param page 分页查询-页码
	 * @param pageSize 每页的记录数量
	 * @return SmtpHost集合
	 */
	public List<SmtpHost> getSmtpList(int page,int pageSize);
	/**
	 * SMTP表的记录总数
	 * @return 总数
	 */
	public long getSmtpCount();
	/**
	 * 删除用户
	 * @param user 包含ID信息的用户实体对象
	 * @return 成功返回"success" 失败返回"failed"
	 */
	public String delUser(User user);
	
	/**
	 * 修改用户的状态(可用改为禁用,禁用改为可用)
	 * @param user 用户对象(只包含ID的值)
	 * @return 成功启用返回"user_unlock" 成功禁用返回"user_lock" 操作失败返回"failed"
	 */
	public String lockUser(User user);
}
