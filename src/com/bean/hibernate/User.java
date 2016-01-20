package com.bean.hibernate;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 表示用户的实体类
 * @author Administrator
 *
 */
@Entity
@Table(name="t_user")
public class User {
	
	@Id
	@GenericGenerator(name="idGen",strategy="guid")
	@GeneratedValue(generator="idGen")
	@Column(name="user_id")
	private String id;//主键
	
	@Column(name="user_name",length=50)
	private String username;//用户姓名
	
	@Column(name="password",length=50)
	private String password;//用户密码
	
	@Column(name="create_time",columnDefinition="timestamp")
	private Date createTime;//用户的创建时间
	
	@Column(name="status")
	private boolean status;//用户的状态,true代表可用,false代表冻结
	
	@OneToMany(targetEntity=Experience.class)
	@JoinColumn(name="user_id")
	private Set<Experience> exps;
	
	@OneToMany(targetEntity=Partner.class)
	@JoinColumn(name="user_id")
	private Set<Partner> partners;
	
	@OneToMany(targetEntity=EmailInfo.class)
	@JoinColumn(name="user_id")
	private Set<EmailInfo> emailInfos;
	
	public User(){}
	public User(String username,String password){
		this.username = username;
		this.password = password;
		createTime = new Date();
		status = true;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public Set<Experience> getExps() {
		return exps;
	}
	public void setExps(Set<Experience> exps) {
		this.exps = exps;
	}
	public Set<Partner> getPartners() {
		return partners;
	}
	public void setPartners(Set<Partner> partners) {
		this.partners = partners;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public boolean getStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public Set<EmailInfo> getEmailInfos() {
		return emailInfos;
	}
	public void setEmailInfos(Set<EmailInfo> emailInfos) {
		this.emailInfos = emailInfos;
	}
}
