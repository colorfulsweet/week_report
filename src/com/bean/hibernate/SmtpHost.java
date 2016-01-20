package com.bean.hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="t_smtp_host")
public class SmtpHost {
	@Id
	@GenericGenerator(name="idGen2",strategy="uuid")
	@GeneratedValue(generator="idGen2")
	private String id;
	
	@Column(name="server_name")
	private String serverName;
	
	@Column(name="host")
	private String host;
	
	@Column(name="smtp_host")
	private String smtpHost;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getSmtpHost() {
		return smtpHost;
	}
	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}

}
