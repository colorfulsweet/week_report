package com.controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bean.hibernate.EmailInfo;
import com.bean.hibernate.SmtpHost;
import com.bean.hibernate.User;
import com.service.email.IEmailManager;
import com.text.TextInfo;

/**
 * 与发送邮件相关请求的控制器
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value="/mail")
public class MailController {
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private ServletContext context;
	
	@Autowired
	private IEmailManager emailManager;
	/**
	 * 保存邮件发送信息
	 * @param info
	 */
	@RequestMapping(value="/save.do")
	public void saveEmailInfo(EmailInfo info){
		User user = (User) session.getAttribute("user");
		emailManager.saveEmailInfo(info,user);
	}
	/**
	 * 删除邮件发送信息
	 * @param info
	 */
	@RequestMapping(value="/del.do")
	public void delEmailInfo(EmailInfo info){
		emailManager.delEmailInfo(info);
	}
	/**
	 * 发送邮件
	 * @param emailInfoId
	 * @return 成功返回"success" 失败返回"failed"
	 */
	@RequestMapping(value="/send.do",method=RequestMethod.POST)
	public @ResponseBody
	String sendEmail(@RequestParam(value="id")String emailInfoId){
		String path = context.getRealPath("temp/"+session.getAttribute("fileName")+".xls");
		String enclosureName = (String) session.getAttribute("enclosureName");
		if(emailManager.sendMail(emailInfoId,path,enclosureName)){
			session.removeAttribute("fileName");
			return TextInfo.UserInfo.SUCCESS;
		} else {
			return TextInfo.UserInfo.FAILED;
		}
	}
	/**
	 * 保存SMTP域名
	 * @param smtpHost 实体类
	 * @return 成功返回"success" 失败返回"failed"
	 */
	@RequestMapping(value="/saveHost.do",method=RequestMethod.POST)
	public @ResponseBody
	String addSmtpHost(SmtpHost smtpHost){
		if(emailManager.saveSmtpHost(smtpHost)){
			return TextInfo.UserInfo.SUCCESS;
		}
		return TextInfo.UserInfo.FAILED;
	}
}
