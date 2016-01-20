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
 * 表示心得体会的实体类
 * @author Administrator
 *
 */
@Entity
@Table(name="t_exp")
public class Experience {
	@Id
	@GenericGenerator(name="idGen2",strategy="uuid")
	@GeneratedValue(generator="idGen2")
	@Column(name="exp_id")
	private String id;
	
	@Column(name="exp_text",columnDefinition="text")
	private String text;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	public Experience(){}
	public Experience(String text,User user){
		this.text = text;
		this.user = user;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
}
