package com.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bean.Infomation;
import com.bean.Task;
import com.bean.hibernate.Partner;
import com.bean.hibernate.User;
import com.service.report.IReportManager;
import com.text.TextInfo;

/**
 * 处理与周报相关请求的控制器
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value="/report")
public class ReportController {
	
	@Autowired
	private ServletContext context;
	
	@Autowired
	private IReportManager reportManager;
	
	@Autowired
	private HttpSession session;
	/**
	 * 生成并下载周报请求的调用方法
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/download.do",method=RequestMethod.POST)
	public void downloadExcel( HttpServletRequest request,HttpServletResponse response ) 
							throws IOException{
		//文件输出流
		OutputStream output = response.getOutputStream();
		//解析请求中包含的周报内容信息
		Map<String,Object> info = analyseInfo(request);
		String[] dates_str = (String[]) info.get("dates_str");
		//设置文件名并进行转码
		String file_name = "工作周报（"+info.get("name") + dates_str[0]+"至"+dates_str[6]+"）.xls";
		file_name = URLEncoder.encode(file_name, "utf-8");
		//让浏览器知道有文件要下载 ,浏览器会自动弹出下载对话框
		response.setHeader("Location",file_name);
		response.setHeader("Content-Disposition", "attachment; filename="+file_name);
		reportManager.conductExcel(info,output);
		
		output.flush();
		output.close();
	}
	/**
	 * 保存Excel文件到临时文件夹
	 * @param request
	 * @return 成功返回"success", 失败返回"failed"
	 */
	@RequestMapping(value="/saveExcel.do",method=RequestMethod.POST)
	public @ResponseBody
	String saveExcel(HttpServletRequest request) {
		//随机生成文件名
		String fileName = UUID.randomUUID().toString();
		FileOutputStream output = null;
		try {
			Map<String, Object> info = analyseInfo(request);
			String path = context.getRealPath("temp/"+fileName+".xls");
			output = new FileOutputStream(path);
			reportManager.conductExcel(info,output);
			output.flush();
			//若该文件在服务器生成成功,则先删除该用户之前生成的旧文件(如果存在)
			//再将文件名保存到Session
			String oldFileName = (String) session.getAttribute("fileName");
			if(oldFileName != null){
				File file = new File(context.getRealPath("temp/"+oldFileName+".xls"));
				//首先检验该文件是否存在(上次生成是否成功)
				if(file.exists()){
					file.delete();
				}
			}
			String[] dates_str = (String[]) info.get("dates_str");
			String enclosureName = "工作周报（"+info.get("name") + dates_str[0]+"至"+dates_str[6]+"）.xls";
			
			session.setAttribute("enclosureName", enclosureName);//附件发送时候的文件名
			session.setAttribute("fileName", fileName);//服务器中存储的文件的名称
			return TextInfo.UserInfo.SUCCESS;
		} catch (IOException e) {
			e.printStackTrace();
			return TextInfo.UserInfo.FAILED;
		} finally{
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	/**
	 * 添加(保存)心得体会请求的控制器
	 * @param text 心得体会文本
	 * @param response http响应
	 * @throws IOException
	 */
	@RequestMapping(value="/addExp.do",method=RequestMethod.POST)
	public void addExperience ( String text , HttpServletResponse response ) 
					throws IOException{
		text = URLDecoder.decode(text,"UTF-8");
		User user = (User) session.getAttribute("user");
		String id = reportManager.addExperience(text, user);
		response.getWriter().print(id);
	}
	/**
	 * 删除心得体会请求的控制器
	 * @param expId 心得体会ID
	 * @return "success"
	 */
	@RequestMapping(value="/delExp.do",method=RequestMethod.GET)
	public @ResponseBody 
	String delExperience(@RequestParam(value="id") String expId){
		
		reportManager.delExperience(expId);
		return "success";
	}
	/**
	 * 添加参与者
	 * @param partner 实体对象
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value="/addPartner.do",method=RequestMethod.POST)
	public @ResponseBody 
	String addPartner(Partner partner) throws UnsupportedEncodingException{
		String partnerName = URLDecoder.decode(partner.getPartnerName(),"UTF-8");
		partner.setPartnerName(partnerName);
		String id = reportManager.addPartner(partner,(User)session.getAttribute("user"));
		return id;
	}
	/**
	 * 删除参与者
	 * @param partner 实体对象
	 */
	@RequestMapping(value="/delPartner.do",method=RequestMethod.GET)
	public void delPartner(Partner partner){
		reportManager.delPartner(partner);
	}
	/**
	 * 解析请求中包含的周报具体内容信息
	 * @param request HTTP请求
	 * @return 将周报主要内容封装的Map集合
	 * @throws FileNotFoundException 未找到Excel模板
	 */
	private Map<String,Object> analyseInfo(HttpServletRequest request) throws FileNotFoundException{
		String[] dates_str = (String[])context.getAttribute("dates_str");
		Date[] dates = (Date[])context.getAttribute("dates");
		//获取姓名信息
		String name = request.getParameter("name");
		//获取用户自定义心得体会 还是从预定义列表当中选择
		String flag = request.getParameter("exps");
		String expText = null;
		if("custom".equals(flag)){
			expText = request.getParameter("exp");
		} else {
			expText = reportManager.selectExperience(flag);
		}
		
		List<Infomation> infos = new ArrayList<Infomation>();
		for(int i=1 ; i<=dates.length ; i++){
			String task = request.getParameter("task"+i);
			String partner = request.getParameter("partner"+i);
			String question = request.getParameter("question"+i);
			String answer = request.getParameter("answer"+i);
			Infomation info = new Infomation(task,partner,question,answer);
			infos.add(info);
		}
		List<Task> tasks = new ArrayList<Task>();
		int countTask = Integer.parseInt(request.getParameter("countTask"));
		for(int i=1 ; i<=countTask ; i++){
			String taskName = request.getParameter("taskName"+i);
			String taskDate = request.getParameter("taskDate"+i);
			String chargePerson = request.getParameter("chargePerson"+i);
			String otherPartner = request.getParameter("otherPartner"+i);
			Task task = new Task(taskName,taskDate,chargePerson,otherPartner);
			tasks.add(task);
		}
		//获得文件的全路径
		String real_path = context.getRealPath("工作周报模板.xls");
		//文件输入流
		FileInputStream fis = new FileInputStream(real_path);
		//对Excel模板做处理
		Map<String,Object> info = new HashMap<String,Object>();
		info.put("input", fis);
		info.put("name", name);
		info.put("infos", infos);
		info.put("tasks", tasks);
		info.put("expText", expText);
		info.put("dates_str", dates_str);
		info.put("dates", dates);
		
		return info;
	}
}
