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
 * 表示参与者的实体类
 * @author Administrator
 *
 */
@Entity
@Table(name="t_partner")
public class Partner {
	@Id
	@GenericGenerator(name="idGen2",strategy="uuid")
	@GeneratedValue(generator="idGen2")
	@Column(name="partner_id")
	private String id;
	
	@Column(name="partner_name")
	private String partnerName;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPartnerName() {
		return partnerName;
	}
	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
}
