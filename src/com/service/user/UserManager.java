package com.service.user;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import com.bean.hibernate.User;
import com.text.TextInfo;
import com.utils.HibernateUtils;
import com.utils.Md5;

@Service
public class UserManager implements IUserManager {
	/**
	 * 验证用户是否已存在
	 * @param username 用户名
	 * @return 不存在返回true,存在返回false
	 */
	@Override
	public String checkUserExist(String username){
		Session session = null;
		Transaction tx = null;
		List<?> result = null;
		try{
			session = HibernateUtils.getSession();
			tx = session.beginTransaction();
			result = session.createQuery("from User where username=?")
					.setParameter(0, username).list();
			tx.commit();
		} catch (HibernateException e){
			e.printStackTrace();
			tx.rollback();
		} finally{
			HibernateUtils.closeSession(session);
		}
		if(!result.isEmpty()){
			//用户名已存在
			return TextInfo.UserInfo.EXIST;
		}else {
			//用户名可用
			return TextInfo.UserInfo.AVAILABLE;
		}
	}
	/**
	 * 创建用户
	 * @param user 用户实体对象
	 * @return 用户实体对象
	 */
	@Override
	public User createUser(User user){
		String encryptPassword = Md5.encrypt(user.getPassword());
		user.setPassword(encryptPassword);
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateUtils.getSession();
			tx = session.beginTransaction();
			session.save(user);
			tx.commit();
		} catch (HibernateException e){
			e.printStackTrace();
			tx.rollback();
			return null;
		} finally{
			HibernateUtils.closeSession(session);
		}
		return user;
	}
	
	/**
	 * 验证用户名和密码的正确性
	 * @param user 用户实体对象
	 * @return 正确返回用户实体对象, 错误返回null
	 */
	@Override
	public User checkUser(User user){
		String encryptPassword = Md5.encrypt(user.getPassword());
		user.setPassword(encryptPassword);
		Session session = null;
		Transaction tx = null;
		List<?> result = null;
		try{
			session = HibernateUtils.getSession();
			tx = session.beginTransaction();
			result = session.createQuery("from User us where us.username=? and us.password=?")
					.setParameter(0, user.getUsername())
					.setParameter(1, user.getPassword()).list();
			
			tx.commit();
		} catch (HibernateException e){
			e.printStackTrace();
			tx.rollback();
		} finally{
			HibernateUtils.closeSession(session);
		}
		if(result.isEmpty()){
			return null;
		}else {
			return (User)result.get(0);
		}
	}
	
	/**
	 * 修改用户密码
	 * @param user
	 * @return 成功返回true,失败返回false
	 */
	@Override
	public boolean changePassword(User user){
		user.setPassword(Md5.encrypt(user.getPassword()));
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateUtils.getSession();
			tx = session.beginTransaction();
			session.update(user);
			
			tx.commit();
		} catch (HibernateException e){
			e.printStackTrace();
			tx.rollback();
			return false;
		} finally{
			HibernateUtils.closeSession(session);
		}
		return true;
	}
	
	/**
	 * 生成随机验证码图片
	 * @param width 图片宽度
	 * @param height 图片高度
	 * @param output 字节输出流
	 * @return 生成的随机字符串
	 */
	@Override
	public String createCheckCode(int width,int height,OutputStream output){
		BufferedImage image = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
		// 获取图形上下文
		Graphics g = image.getGraphics();
		// 设定背景色
		g.setColor(new Color(0xDCDCDC));
		g.fillRect(0, 0, width, height);
		// 画边框
		g.setColor(Color.black);
		g.drawRect(0, 0, width - 1, height - 1);
		// 取随机产生的认证码
		String strEnsure = "";
		// 4代表4位验证码,如果要生成更多位的认证码,则加大数值
		for (int i = 0; i < 4; ++i) {
			strEnsure += mapTable[(int) (mapTable.length * Math.random())];
		}
		// 　　将认证码显示到图像中,如果要生成更多位的认证码,增加drawString语句
		g.setColor(Color.black);
		g.setFont(new Font("Atlantic Inline", Font.PLAIN, 18));
		String str = strEnsure.substring(0, 1);
		g.drawString(str, 8, 17);
		str = strEnsure.substring(1, 2);
		g.drawString(str, 20, 15);
		str = strEnsure.substring(2, 3);
		g.drawString(str, 35, 18);
		str = strEnsure.substring(3, 4);
		g.drawString(str, 45, 15);
		// 随机产生10个干扰点
		Random rand = new Random();
		for (int i = 0; i < 10; i++) {
			int x = rand.nextInt(width);
			int y = rand.nextInt(height);
			g.drawOval(x, y, 1, 1);
		}
		// 释放图形上下文
		g.dispose();
		try {
			// 输出图像到页面
			ImageIO.write(image, "JPEG", output);
		} catch (IOException e) {
			return "";
		}
		return strEnsure;
	}
	//验证码的字符表
	private static char mapTable[] = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
		'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
		'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8',
		'9' };
}
