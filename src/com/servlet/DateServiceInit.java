package com.servlet;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.text.Config;
import com.utils.DateUtils;
import com.utils.HibernateUtils;
import com.utils.XmlUtils;
/**
 * 在服务器启动的时候就加载该Servlet
 * 并启动一个线程, 该线程每隔一定时间就刷新一次ServletContext当中的日期序列
 * @author Administrator
 *
 */
public class DateServiceInit extends HttpServlet implements Runnable {
	private static final long serialVersionUID = 5302681176367111493L;
	private ServletContext context;
	private static Logger log = Logger.getLogger(DateServiceInit.class);
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		//当服务启动 ,该Servlet自动加载  init方法会自动调用
		context = this.getServletContext();
		//将刷新的时间间隔保存在ServletContext当中
		context.setAttribute("time", Config.INITIAL_TIME);
		File temp = new File(context.getRealPath("temp/"));
		//检测临时文件夹是否存在
		if(temp.exists()){
			//如果存在则删除其中的所有文件
			for(String path : temp.list()){
				File file = new File(temp.getPath()+"/"+path);
				file.delete();
			}
		}
		//删除临时文件夹
		temp.delete();
		//创建临时文件夹
		if(temp.mkdirs()){
			log.info("创建Excel缓存目录成功");
		} else {
			log.error("创建Excel缓存目录失败");
		}
		context.setAttribute("menuList",new XmlUtils().readMenuXml("menu.xml"));
		HibernateUtils.buildConfiguration();
		Thread thread = new Thread(this,"DateSequence");
		thread.start();
	}

	@Override
	public void run() {
		log.info("日期序列自动刷新线程已启动, 初始刷新间隔为"+context.getAttribute("time")+"分钟");
		while(true){
			//线程获得对象锁, 这个锁属于context对象
			//需要使用context对象执行休眠和唤醒
			synchronized(context){
				//每次循环都从ServletContext当中获取时间间隔的值
				int time = (Integer) context.getAttribute("time");
				log.info("日期已刷新刷新(间隔"+time+"分钟),当前日期序列为");
				Date[] dates = DateUtils.getDateSequence();
				String[] dates_str = DateUtils.getDateSequenceStr();
				for(String date_str : dates_str){
					//将当前的日期序列信息打印到控制台
					log.info(date_str+" ");
				}
				//将计算出的日期序列信息保存在ServletContext
				context.setAttribute("dates", dates);
				context.setAttribute("dates_str", dates_str);
				try {
					//线程休眠
					context.wait(time*60*1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.getRequestDispatcher("index.jsp").forward(req, resp);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doGet(req, resp);
	}
}
