package com.bean;

import java.text.ParseException;
import java.util.Date;

import com.utils.DateUtils;
/**
 * 表示下周工作计划任务的实体类
 * @author Administrator
 *
 */
public class Task {
	private String taskName;//任务名称
	private Date taskDate;//预计完成时间
	private String chargePerson;//负责人
	private String otherPartner;//其他参与人
	public Task(){}
	public Task(String taskName,String taskDate,String chargePerson,String otherPartner){
		this.taskName = taskName;
		try {
			this.taskDate = DateUtils.sdf1.parse(taskDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.chargePerson = chargePerson;
		this.otherPartner = otherPartner;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public Date getTaskDate() {
		return taskDate;
	}
	public void setTaskDate(Date taskDate) {
		this.taskDate = taskDate;
	}
	public String getChargePerson() {
		return chargePerson;
	}
	public void setChargePerson(String chargePerson) {
		this.chargePerson = chargePerson;
	}
	public String getOtherPartner() {
		return otherPartner;
	}
	public void setPartner(String otherPartner) {
		this.otherPartner = otherPartner;
	}
	
}
