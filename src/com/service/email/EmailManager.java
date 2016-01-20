package com.service.email;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.FileDataSource;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import com.bean.hibernate.EmailInfo;
import com.bean.hibernate.SmtpHost;
import com.bean.hibernate.User;
import com.utils.Email;
import com.utils.HibernateUtils;

@Service
public class EmailManager implements IEmailManager{
	/**
	 * 保存发送邮件的基本信息
	 * @param info
	 */
	@Override
	public String saveEmailInfo(EmailInfo info,User user) {
		info.setUser(user);
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateUtils.getSession();
			tx = session.beginTransaction();
			session.save(info);
			tx.commit();
		} catch (HibernateException e){
			e.printStackTrace();
			tx.rollback();
		} finally{
			HibernateUtils.closeSession(session);
		}
		return info.getId();
	}
	/**
	 * 删除发送邮件的基本信息
	 * @param info
	 */
	@Override
	public void delEmailInfo(EmailInfo info){
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateUtils.getSession();
			tx = session.beginTransaction();
			session.delete(info);
			tx.commit();
		} catch (HibernateException e){
			e.printStackTrace();
			tx.rollback();
		} finally{
			HibernateUtils.closeSession(session);
		}
	}
	/**
	 * 查询指定用户所有的Email发送地址信息
	 * @param user
	 * @return 表示发送地址的实体对象集合
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<EmailInfo> selectAllEmailInfo(User user){
		List<EmailInfo> result = null;
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateUtils.getSession();
			tx = session.beginTransaction();
			result = session.createQuery("from EmailInfo where user_id=?")
						.setParameter(0, user.getId()).list();
			tx.commit();
		} catch (HibernateException e){
			e.printStackTrace();
			tx.rollback();
		} finally{
			HibernateUtils.closeSession(session);
		}
		return result;
	}
	/**
	 * 根据发送邮件的基本信息ID查询数据库获得基本信息发送邮件
	 * @param emailInfoId
	 * @param path 附件的路径
	 * @param enclosureName 发送时的附件名称
	 * @return 成功返回true 失败返回false
	 */
	@Override
	public boolean sendMail(String emailInfoId,String path,String enclosureName){
		EmailInfo emailInfo = null;
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateUtils.getSession();
			tx = session.beginTransaction();
			emailInfo = (EmailInfo) session.get(EmailInfo.class, emailInfoId);
			tx.commit();
			Map<String,String> map = new HashMap<String,String>();
			map.put("title", enclosureName);
			map.put("mailBody", "");
			map.put("sendTo", emailInfo.getSendTo());
			map.put("sendFrom", emailInfo.getSendFrom());
			map.put("copyTo", emailInfo.getCopyTo());
			map.put("fileName", enclosureName);
			Email email = new Email(emailInfo.getSmtpHost(),
						emailInfo.getUsername(),
						emailInfo.getPassword());
			//定位生成的文件(文件名为随机uuid码)
			File excelFile = new File(path);
			FileDataSource fileSource = new FileDataSource(excelFile);
			if(email.sendMail(map, fileSource)){
				//如果邮件发送成功, 则删除文件
				fileSource.getFile().delete();
				return true;
			}else {
				return false;
			}
		} catch (HibernateException e){
			e.printStackTrace();
			tx.rollback();
			return false;
		} finally{
			HibernateUtils.closeSession(session);
		}
	}
	/**
	 * 保存SMTP域名
	 * @param smtpHost 实体类
	 * @return 成功返回true, 失败返回false
	 */
	@Override
	public boolean saveSmtpHost(SmtpHost smtpHost){
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateUtils.getSession();
			tx = session.beginTransaction();
			session.save(smtpHost);
			tx.commit();
			return true;
		} catch (HibernateException e){
			e.printStackTrace();
			tx.rollback();
			return false;
		} finally{
			HibernateUtils.closeSession(session);
		}
	}
}
