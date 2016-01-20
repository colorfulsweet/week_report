package com.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bean.hibernate.User;
import com.service.user.IUserManager;
import com.text.TextInfo;
/**
 * 处理与用户相关请求的控制器
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value="/user")
public class UserController {
	
	@Autowired
	private IUserManager userManager;
	
	@Autowired
	private HttpSession session;
	/**
	 * 验证该用户名是否已存在的控制器
	 * @param username 用户名
	 * @return 不存在返回true,存在返回false
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value="/checkUserExist.do",method=RequestMethod.POST)
	public @ResponseBody String checkUserExist(String username) 
								throws UnsupportedEncodingException{
		
		username = URLDecoder.decode(username,"UTF-8");
		return userManager.checkUserExist(username);
	}
	
	/**
	 * 创建用户请求的控制器
	 * @param username 用户名
	 * @param password 密码
	 * @param checkcode 验证码
	 * @return 验证码错误重定向至"index.jsp", 否则转发至"page/index.do"
	 */
	@RequestMapping(value="/create.do",method=RequestMethod.POST)
	public String createUser(String username,String password,String checkcode){
		if(session.getAttribute("checkcode").equals(checkcode)){
			User user = new User(username,password);
			user = userManager.createUser(user);
			if(user != null){
				session.setAttribute("user", user);
			}
		}
		return "redirect:../index.jsp";
	}
	/**
	 * 登陆验证用户请求的控制器
	 * @param user 用户实体对象
	 * @param remember 是否记住登陆状态(是否写入Cookie) //暂未完成
	 * @param response HTTP响应
	 * @return 验证结果
	 */
	@RequestMapping(value="/check.do",method=RequestMethod.POST)
	public ModelAndView checkUser(User user,boolean remember,HttpServletResponse response) {
		user = userManager.checkUser(user);
		ModelAndView mav = new ModelAndView();
		if(user != null && user.getStatus()){
			mav.setViewName("redirect:../index.jsp");
			session.setAttribute("user", user);
		} else {
			mav.setViewName("WEB-INF/views/login.jsp");
			if (user == null){
				mav.addObject("info","用户名/密码 错误");
			} else if (!user.getStatus()){
				mav.addObject("info", "该用户已被禁用");
			}
		}
		return mav;
	}
	/**
	 * 修改密码请求的控制器
	 * @param newPwd 新密码
	 * @return 页面重定向至"index.jsp"
	 */
	@RequestMapping(value="/changePwd.do",method=RequestMethod.POST)
	public String changePassword(String newPwd){
		User user = (User) session.getAttribute("user");
		user.setPassword(newPwd);
		userManager.changePassword(user);
		
		return "redirect:../index.jsp";
	}
	/**
	 * 用户注销请求的控制器
	 * @return 页面重定向至"index.jsp"
	 */
	@RequestMapping(value="/logout.do",method=RequestMethod.GET)
	public String logOut(){
		session.invalidate();
		return "redirect:../index.jsp";
	}
	
	/**
	 * 生成验证码请求的控制器
	 * @param response HTTP响应
	 * @throws IOException HTTP响应对象获取字节输出流失败
	 */
	@RequestMapping(value="/createCheckCode.do")
	public void createCheckCode(HttpServletResponse response) throws IOException{
		int width=60;//图片宽度
		int height=20;//图片高度
		//禁止浏览器缓存随机图片
		response.setDateHeader("Expires", -1);
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		
		//告诉客户端以图片方式打开接收到的数据
		response.setContentType("image/jpeg");
		
		String code = userManager.createCheckCode(width, height,response.getOutputStream());
		//把随机生成的数值保存到session,供验证使用
		session.setAttribute("checkcode", code);
	}
	
	/**
	 * 检验验证码是否输入正确 请求的控制器
	 * @param code 用户输入的验证码字符串
	 * @return 正确返回"true",错误返回"false"
	 */
	@RequestMapping(value="/checkCode.do",method=RequestMethod.GET)
	public @ResponseBody
	String checkCode(String code){
		String codeNow = (String) session.getAttribute("checkcode");
		//忽略大小写进行比较
		if(codeNow.compareToIgnoreCase(code) == 0){
			return TextInfo.UserInfo.CHECKCODE_TRUE;
		} else {
			return TextInfo.UserInfo.CHECKCODE_FALSE;
		}
	}
}
