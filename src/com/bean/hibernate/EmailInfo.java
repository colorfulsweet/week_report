package com.bean.hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 表示一次发送邮件的基本配置的实体类
 * @author Administrator
 *
 */
@Entity
@Table(name="t_email")
public class EmailInfo {
	@Id
	@GenericGenerator(name="idGen2",strategy="uuid")
	@GeneratedValue(generator="idGen2")
	private String id;//主键
	
	@Column(name="smtp_host")
	private String smtpHost;//邮箱服务器地址
	
	@Column(name="username")
	private String username;//邮箱服务器登陆名
	
	@Column(name="password")
	private String password;//登陆密码
	
	@Column(name="send_to")
	private String sendTo;//收件人地址
	
	@Column(name="send_from")
	private String sendFrom;//发件人地址
	
	@Column(name="copy_to")
	private String copyTo;//抄送
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;//所属用户
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSmtpHost() {
		return smtpHost;
	}
	public void setSmtpHost(String stmpHost) {
		this.smtpHost = stmpHost;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSendTo() {
		return sendTo;
	}
	public void setSendTo(String sendTo) {
		this.sendTo = sendTo;
	}
	public String getSendFrom() {
		return sendFrom;
	}
	public void setSendFrom(String sendFrom) {
		this.sendFrom = sendFrom;
	}
	public String getCopyTo() {
		return copyTo;
	}
	public void setCopyTo(String copyTo) {
		this.copyTo = copyTo;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
}
