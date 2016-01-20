package com.controller;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bean.Admin;
import com.bean.hibernate.User;
import com.service.admin.IAdminManager;
import com.text.TextInfo;

/**
 * 管理员有关请求的控制器
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value="/admin")
public class AdminController {
	
	@Autowired
	private ServletContext context;
	
	@Autowired
	private IAdminManager adminManager;
	
	private static Logger log = Logger.getLogger(AdminController.class);
	/**
	 * 验证管理员账户的正确性, 管理员账户正确则请求转发至admin_index.jsp, 错误则重定向至index.jsp
	 * @param username 用户名
	 * @param password 密码
	 * @return 页面视图模型
	 */
	@RequestMapping(value="/check.do",method=RequestMethod.POST)
	public ModelAndView checkAdmin(String username,String password){
		Admin admin = new Admin(username,password);
		ModelAndView mav = null;
		if(adminManager.checkAdmin(admin)){
			mav = new ModelAndView("WEB-INF/views/admin/admin_index.jsp");
		} else {
			mav = new ModelAndView("redirect:../index.jsp");
		}
		return mav;
	}
	/**
	 * 初始化数据库(创建表)
	 */
	@RequestMapping(value="/initDatabase.do",method=RequestMethod.GET)
	public @ResponseBody
	String initDatabase(){
		try{
			adminManager.initDatabase();
			return TextInfo.Admin.SUCCESS;
		} catch (Exception e){
			e.printStackTrace();
			return TextInfo.Admin.FAILED;
		}
	}
	/**
	 * 重置日期序列刷新时间间隔
	 * @param time 时间间隔
	 */
	@RequestMapping(value="/changeRefreshTime.do",method=RequestMethod.GET)
	public @ResponseBody
	String changeRefreshTime(int time){
		if(time>0 && time<=1000){
			context.setAttribute("time", time);
			log.info("日期序列刷新间隔已修改, 当前时间间隔为"+time+"分钟");
			synchronized (context) {
				//唤醒日期刷新线程
				context.notify();
			}
			return TextInfo.Admin.SUCCESS;
		} else {
			return TextInfo.Admin.FAILED;
		}
	}
	/**
	 * 删除用户
	 * @param user 用户对象(只包含ID的值)
	 * @return 成功返回"success" 失败返回"failed"
	 */
	@RequestMapping(value="/delUser.do",method=RequestMethod.POST)
	public @ResponseBody
	String delUser(User user){
		return adminManager.delUser(user);
	}
	/**
	 * 修改用户的状态(可用改为禁用,禁用改为可用)
	 * @param user 用户对象(只包含ID的值)
	 * @return 成功启用返回"user_unlock" 成功禁用返回"user_lock" 操作失败返回"failed"
	 */
	@RequestMapping(value="/lockUser.do",method=RequestMethod.POST)
	public @ResponseBody
	String lockUser(User user){
		return adminManager.lockUser(user);
	}
	/**
	 * 获取用户信息列表
	 * @param page 页码(默认是1)
	 * @param pageSize 每页显示的数量(默认是10)
	 * @return 显示信息列表的页面
	 */
	@RequestMapping(value="/getUserInfo.do",method=RequestMethod.GET)
	public ModelAndView getUserInfo(
			@RequestParam(value="page",required=false,defaultValue="1")int page,
			@RequestParam(value="pageSize",required=false,defaultValue="10")int pageSize
			){
		
		ModelAndView mav = new ModelAndView("WEB-INF/views/admin/user_info.jsp");
		mav.addObject("users", adminManager.getUserList(page,pageSize));
		long rowCount = adminManager.getUserCount();
		long pageCount = rowCount%pageSize==0 ? rowCount/pageSize : rowCount/pageSize+1;
		mav.addObject("pageCount", pageCount);
		mav.addObject("pageNow",page);
		mav.addObject("pageSize", pageSize);
		return mav;
	}
	/**
	 * 获取SMTP服务器列表
	 * @return 显示信息列表的页面
	 */
	@RequestMapping(value="/getSmtpInfo.do",method=RequestMethod.GET)
	public ModelAndView getSmtpInfo(
			@RequestParam(value="page",required=false,defaultValue="1")int page,
			@RequestParam(value="pageSize",required=false,defaultValue="10")int pageSize
			){
		ModelAndView mav = new ModelAndView("WEB-INF/views/admin/smtp_info.jsp");
		mav.addObject("smtpHosts",adminManager.getSmtpList(page,pageSize));
		long rowCount = adminManager.getSmtpCount();
		long pageCount = rowCount%pageSize==0 ? rowCount/pageSize : rowCount/pageSize+1;
		mav.addObject("pageCount", pageCount);
		mav.addObject("pageNow",page);
		mav.addObject("pageSize", pageSize);
		return mav;
	}
}
