package com.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bean.hibernate.User;
import com.service.email.IEmailManager;
import com.service.report.IReportManager;
import com.text.enums.Role;

/**
 * 处理页面转发请求的控制器
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value="/page")
public class PageController {
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private IReportManager reportManager;
	
	@Autowired
	private IEmailManager emailManager;
	/**
	 * 接收登录请求的控制器
	 * @param role 用户角色
	 * @return 普通用户转发至"user/check.do",管理员转发至"admin/check.do"
	 */
	@RequestMapping(value="/login.do",method=RequestMethod.POST)
	public String login(Role role){
		switch(role){
		case user:return "user/check.do";
		case admin:return "admin/check.do";
		default:
			throw new IllegalArgumentException("角色权限参数不正确");
		}
	}
	
	/**
	 * 打开修改密码页面的请求(首先验证session是否超时)
	 * @param model 模型对象
	 * @return session超时转发至"WEB-INF/error_pages/timeout.html",否则转发至"WEB-INF/views/report/change_password.jsp"
	 */
	@RequestMapping(value="/changePwd.do",method=RequestMethod.GET)
	public String toChangePwd(Model model){
		if(checkLogin() == null){
			return "WEB-INF/error_pages/timeout.html";
		}
		return "WEB-INF/views/report/change_password.jsp";
	}
	/**
	 * 打开定制周报页面的请求
	 * @param model 模型对象
	 * @return session超时转发至"WEB-INF/error_pages/timeout.html",否则转发至"WEB-INF/views/report/custom-made.jsp"
	 */
	@RequestMapping(value="/customMade.do",method=RequestMethod.GET)
	public String toCustomMade(Model model){
		User user = checkLogin();
		if(user == null){
			return "WEB-INF/error_pages/timeout.html";
		}
		model.addAttribute("ExpList", reportManager.selectAllExperience(user));
		return "WEB-INF/views/report/custom-made.jsp";
	}
	/**
	 * 打开生成周报页面的请求
	 * @param model 模型对象
	 * @return session超时转发至"WEB-INF/error_pages/timeout.html",否则转发至"WEB-INF/views/report/product.jsp"
	 */
	@RequestMapping(value="/product.do",method=RequestMethod.GET)
	public String toProduct(Model model){
		User user = checkLogin();
		if(user == null){
			return "WEB-INF/error_pages/timeout.html";
		}
		model.addAttribute("ExpList", reportManager.selectAllExperience(user));
		model.addAttribute("PartnerList", reportManager.selectAllPartner(user));
		return "WEB-INF/views/report/product.jsp";
	}
	/**
	 * 打开邮箱配置页面的请求
	 * @param model 模型对象
	 * @return session超时转发至"WEB-INF/error_pages/timeout.html",否则转发至"WEB-INF/views/report/mailbox_config.jsp"
	 */
	@RequestMapping(value="/mailboxConfig.do",method=RequestMethod.GET)
	public String toMailboxConfig(Model model){
		User user = checkLogin();
		if(user == null){
			return "WEB-INF/error_pages/timeout.html";
		}
		return "WEB-INF/views/report/mailbox_config.jsp";
	}
	/**
	 * 打开发送邮件页面的请求
	 * @param model 模型对象
	 * @return 请求转发至"WEB-INF/views/report/send_email.jsp"
	 */
	@RequestMapping(value="/sendEmail.do",method=RequestMethod.GET)
	public String toSendEmail(Model model){
		User user = checkLogin();
		model.addAttribute("EmailList", emailManager.selectAllEmailInfo(user));
		return "WEB-INF/views/report/send_email.jsp";
	}
	/**
	 * 访问主页的请求
	 * @param model 模型对象
	 * @return 已登录转发至"WEB-INF/views/explorer.jsp",否则转发至"WEB-INF/views/login.jsp"
	 */
	@RequestMapping(value="/index.do",method={RequestMethod.GET,RequestMethod.POST})
	public String toIndex(Model model) {
		if(checkLogin() == null){
			return "WEB-INF/views/login.jsp";
		} else {
			return "WEB-INF/views/explorer.jsp";
		}
	}
	private User checkLogin(){
		User user = (User) session.getAttribute("user");
		return user;
	}
}
