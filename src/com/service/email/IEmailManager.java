package com.service.email;

import java.util.List;

import com.bean.hibernate.EmailInfo;
import com.bean.hibernate.SmtpHost;
import com.bean.hibernate.User;

public interface IEmailManager {
	/**
	 * 保存发送邮件的基本信息
	 * @param info
	 */
	public String saveEmailInfo(EmailInfo info,User user);
	/**
	 * 删除发送邮件的基本信息
	 * @param info
	 */
	public void delEmailInfo(EmailInfo info);
	/**
	 * 查询指定用户所有的Email发送地址信息
	 * @param user
	 * @return 表示发送地址的实体对象集合
	 */
	public List<EmailInfo> selectAllEmailInfo(User user);
	/**
	 * 根据发送邮件的基本信息ID查询数据库获得基本信息发送邮件
	 * @param emailInfoId
	 * @param path 附件的路径
	 * @param enclosureName 发送时的附件名称
	 * @return 成功返回true 失败返回false
	 */
	public boolean sendMail(String emailInfoId,String path,String enclosureName);
	/**
	 * 保存SMTP域名
	 * @param smtpHost 实体类
	 * @return 成功返回true, 失败返回false
	 */
	public boolean saveSmtpHost(SmtpHost smtpHost);
}
