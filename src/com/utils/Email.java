package com.utils;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.log4j.Logger;

public class Email {
	private static Logger log = Logger.getLogger(Email.class);
	private MimeMessage mimeMsg; // MIME邮件对象

	private Session session; // 邮件会话对象
	private Properties props; // 系统属性

	private String username = null; // smtp认证用户名和密码
	private String password = null;

	private Multipart mp; // Multipart对象,邮件内容,标题,附件等内容均添加到其中后再生成MimeMessage对象
	private boolean flag = true;
	
	/**
	 * 构造发送邮件的实例对象
	 * (注意:部分邮箱为了安全, 存在独立的客户端授权密码, 所以这个密码使用的不一定是登陆密码)
	 * @param smtp 发送邮件的SMTP服务器地址
	 * @param username 邮箱登陆名
	 * @param password 邮箱密码
	 */
	public Email(String smtp,String username,String password){
		setSmtpHost(smtp);
		createMimeMessage();
		this.username = username;
		this.password = password;
	}
	
	/**
	 * 发送电子邮件到指定的信箱
	 * @param title 邮件标题 
	 * @param mailBody 邮件内容(遵循html语法)
	 * @param fileds 附件的数据源(不添加附件可传null)
	 * @param fileName 附件的名称(null则使用原文件名)
	 * @param sendTo 收件人
	 * @param sendFrom 发件人
	 * @param copyTo 抄送地址(不填加可传null)
	 * @return 成功返回true 失败返回false
	 */
	public boolean sendMail(String title,
							String mailBody,
							FileDataSource fileds,
							String fileName,
							String sendTo,
							String sendFrom,
							String copyTo){
		setNeedAuth(true);//校验身份
		setSubject(title);//邮件标题
		setBody(mailBody);//将要发送的 内容放入邮件体 
		addFileAffix(fileds,fileName);//添加附件
		setSendTo(sendTo);//接收人
		setSendFrom(sendFrom);//发送人
		setCopyTo(copyTo);//添加抄送
		if(flag){
			return sendOut();//发送邮件
		} else {
			log.error("邮件配置有错误, 邮件未发送...");
			return false;
		}
	}
	/**
	 * 发送电子邮件到指定的信箱
	 * @param texts 邮件相关内容的Map集合
	 * @param fileds 附件的数据源(不添加附件可传null)
	 * @return 发送成功返回true 失败返回false
	 */
	public boolean sendMail(Map<String,String> texts,FileDataSource fileds){
		setNeedAuth(true);//校验身份
		Iterator<Entry<String, String>> it = texts.entrySet().iterator();
		String fileName = null;
		while(it.hasNext()){
			Entry<String,String> pair = it.next();
			switch(pair.getKey()){
				case "title" :setSubject(pair.getValue());break;
				case "mailBody" :setBody(pair.getValue());break;
				case "sendTo" :setSendTo(pair.getValue());break;
				case "sendFrom" :setSendFrom(pair.getValue());break;
				case "copyTo" :setCopyTo(pair.getValue());break;
				case "fileName" :fileName = pair.getValue();break;
				default :
					throw new IllegalArgumentException("Map中传递的参数不正确");
			}
		}
		addFileAffix(fileds,fileName);
		if(flag) {
			return sendOut();//发送邮件
		} else {
			log.error("邮件配置有错误, 邮件未发送...");
			return false;
		}
	}
	/**
	 * 设置系统属性
	 * @param hostName stmp主机名(IP地址)
	 */
	private void setSmtpHost(String hostName) {
		log.info("设置系统属性：mail.smtp.host = " + hostName);
		if (props == null){
			props = System.getProperties(); // 获得系统属性对象
		}
		props.put("mail.smtp.host", hostName); // 设置SMTP主机
	}
	/**
	 * 创建邮件会话
	 */
	private void createMimeMessage() {
		log.info("准备获取邮件会话对象！");
		session = Session.getDefaultInstance(props, null); 
		
		log.info("准备创建MIME邮件对象！");
		mimeMsg = new MimeMessage(session); 
		mp = new MimeMultipart();
	}

	/**
	 * 设置stmp身份认证
	 * @param need
	 */
	private void setNeedAuth(boolean need) {
		log.info("设置smtp身份认证：mail.smtp.auth = " + need);
		if (props == null){
			props = System.getProperties();
		}
		if (need) {
			props.put("mail.smtp.auth", "true");
		} else {
			props.put("mail.smtp.auth", "false");
		}
	}
	/**
	 * 设置邮件主题
	 * @param mailSubject 邮件主题文本
	 */
	private void setSubject(String mailSubject) {
		log.info("设置邮件主题！");
		try {
			mimeMsg.setSubject(mailSubject);
		} catch (Exception e) {
			log.error("设置邮件主题发生错误！");
			e.printStackTrace();
			flag = false;
		}
	}

	/**
	 * 设置邮件正文
	 * @param mailBody 邮件正文文本
	 */
	private void setBody(String mailBody) {
		log.info("设置邮件正文...");
		try {
			BodyPart bp = new MimeBodyPart();
			bp.setContent(
					"<meta http-equiv=Content-Type content=text/html; charset=UTF-8>"
							+ mailBody, "text/html;charset=UTF-8");
			mp.addBodyPart(bp);
		} catch (Exception e) {
			log.error("设置邮件正文时发生错误！");
			e.printStackTrace();
			flag = false;
		}
	}

	/**
	 * 添加附件
	 * @param fileds 文件数据源
	 * @param fileName 附件的名称(null则使用文件的原名)
	 */
	private void addFileAffix(FileDataSource fileds,String fileName) {
		if(fileds == null){
			return;
		}
		if(fileName != null){
			try {
				//对附件名称重新编码
				fileName = MimeUtility.encodeText(fileName);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				log.error("附件名称编码类型错误!");
			}
		}
		log.info("增加邮件附件：" +  fileds.getName());
		try {
			BodyPart bp = new MimeBodyPart();
			bp.setDataHandler(new DataHandler(fileds));
			bp.setFileName(fileName==null?fileds.getName():fileName);
			mp.addBodyPart(bp);
		} catch (MessagingException e) {
			log.error("增加邮件附件：" + fileds.getName() + "发生错误！");
			e.printStackTrace();
			flag = false;
		}
	}
	
	/**
	 * 设置发信人
	 * @param sendFrom 发送的E-mail地址
	 */
	private void setSendFrom(String sendFrom) {
		log.info("设置发信人！");
		try {
			mimeMsg.setFrom(new InternetAddress(sendFrom)); // 设置发信人
		} catch (MessagingException e) {
			log.error("设置发信人时发生错误!");
			e.printStackTrace();
			flag = false;
		}
	}
	/**
	 * 设置接收人
	 * @param sendTo 接收的E-mail地址(多个收件人以;分隔)
	 */
	private void setSendTo(String sendTo) {
		for(String to : sendTo.split(";")){
			log.info("设置接收人: " + to);
			try {
				mimeMsg.setRecipients(Message.RecipientType.TO,
									InternetAddress.parse(to));
			} catch (MessagingException e) {
				log.error("设置接收人发生错误");
				e.printStackTrace();
				flag = false;
			}
		}
	}
	/**
	 * 设置抄送
	 * @param copyTo 抄送地址
	 */
	private void setCopyTo(String copyTo) {
		if(copyTo == null || copyTo.length()==0){
			return;
		}
		log.info("设置邮件抄送--"+copyTo);
		try {
			mimeMsg.setRecipients(Message.RecipientType.CC,
					(Address[]) InternetAddress.parse(copyTo));
		} catch (MessagingException e) {
			log.error("添加抄送失败!");
			e.printStackTrace();
			flag = false;
		}
	}

	/**
	 * 发送邮件
	 * @return 成功返回true 失败返回false
	 */
	private boolean sendOut() {
		Transport transport = null;
		try {
			mimeMsg.setContent(mp);
			mimeMsg.saveChanges();
			log.info("正在发送邮件....");

			Session mailSession = Session.getInstance(props, null);
			transport = mailSession.getTransport("smtp");
			transport.connect((String) props.get("mail.smtp.host"), username,
					password);
			transport.sendMessage(mimeMsg,
					mimeMsg.getRecipients(Message.RecipientType.TO));

			log.info("发送邮件成功！");
			return true;
		} catch (MessagingException e) {
			log.error("邮件发送失败！");
			e.printStackTrace();
			return false;
		} finally {
			if(transport!=null && transport.isConnected()){
				try {
					transport.close();
				} catch (MessagingException e) {
					e.printStackTrace();
				}	
			}
		}
	}
}
