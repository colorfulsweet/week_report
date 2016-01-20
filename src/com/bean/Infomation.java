package com.bean;

public class Infomation {
	public Infomation(){}
	public Infomation(String task,String partner,String question,String answer){
		this.task = task;
		this.partner = partner;
		this.question = question;
		this.answer = answer;
		texts = new String[]{task,partner,question,answer};
	}
	private String[] texts = null;
	private String task;
	private String partner;
	private String question;
	private String answer;
	public String getTask() {
		return task;
	}
	public void setTask(String task) {
		this.task = task;
	}
	public String getPartner() {
		return partner;
	}
	public void setPartner(String partner) {
		this.partner = partner;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String[] getTexts() {
		return texts;
	}
	public void setTexts(String[] texts) {
		this.texts = texts;
	}
	
}
