package com.service.admin;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.springframework.stereotype.Service;

import com.bean.Admin;
import com.bean.hibernate.EmailInfo;
import com.bean.hibernate.Experience;
import com.bean.hibernate.Partner;
import com.bean.hibernate.SmtpHost;
import com.bean.hibernate.User;
import com.text.Config;
import com.text.TextInfo;
import com.utils.HibernateUtils;

@Service
public class AdminManager implements IAdminManager {
	/**
	 * 验证管理员账户的正确性
	 * @param admin
	 * @return 验证为正确返回true,错误返回false
	 */
	@Override
	public boolean checkAdmin(Admin admin) {
		Admin now = new Admin(Config.ADMIN_NAME,Config.ADMIN_PASSWORD);
		return now.equals(admin);
	}
	
	/**
	 * 初始化数据库
	 */
	@Override
	public void initDatabase() throws HibernateException{
		Configuration cfg = HibernateUtils.getConfiguration();
		SchemaExport export = new SchemaExport(cfg);
		export.create(true, true);
	}
	/**
	 * 获取所有用户实体对象集合
	 * @param page 分页查询-页码
	 * @param pageSize 每页的记录数量
	 * @return 所有用户对象的集合
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<User> getUserList(int page,int pageSize){
		int firstResult = pageSize * (page-1);
		
		Session session = null;
		Transaction tx = null;
		List<User> list = null;
		try{
			session = HibernateUtils.getSession();
			tx = session.beginTransaction();
			list = session.createQuery("from User")
					.setFirstResult(firstResult)
					.setMaxResults(pageSize).list();
			tx.commit();
		} catch (HibernateException e){
			e.printStackTrace();
			tx.rollback();
		} finally{
			HibernateUtils.closeSession(session);
		}
		return list;
	}
	/**
	 * 统计用户的总数
	 * @return 用户总数
	 */
	@Override
	public long getUserCount(){
		Session session = null;
		Transaction tx = null;
		long cnt = 0;
		try{
			session = HibernateUtils.getSession();
			tx = session.beginTransaction();
			cnt = (Long)session.createQuery("select count(*) from User").uniqueResult();
			tx.commit();
		} catch (HibernateException e){
			e.printStackTrace();
			tx.rollback();
		} finally{
			HibernateUtils.closeSession(session);
		}
		return cnt;
	}
	/**
	 * 获取SMTP服务器列表
	 * @param page 分页查询-页码
	 * @param pageSize 每页的记录数量
	 * @return SmtpHost集合
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SmtpHost> getSmtpList(int page,int pageSize){
		int firstResult = pageSize * (page-1);
		Session session = null;
		Transaction tx = null;
		List<SmtpHost> list = null;
		try{
			session = HibernateUtils.getSession();
			tx = session.beginTransaction();
			list = session.createQuery("from SmtpHost")
					.setFirstResult(firstResult)
					.setMaxResults(pageSize).list();
			tx.commit();
		} catch (HibernateException e){
			e.printStackTrace();
			tx.rollback();
		} finally{
			HibernateUtils.closeSession(session);
		}
		return list;
	}
	/**
	 * SMTP表的记录总数
	 * @return 总数
	 */
	@Override
	public long getSmtpCount(){
		Session session = null;
		Transaction tx = null;
		long cnt = 0;
		try{
			session = HibernateUtils.getSession();
			tx = session.beginTransaction();
			//Hibernate3.2以上版本结果为Long类型, 更早版本为Integer类型
			cnt = (Long)session.createQuery("select count(*) from SmtpHost").uniqueResult();
			tx.commit();
		} catch (HibernateException e){
			e.printStackTrace();
			tx.rollback();
		} finally{
			HibernateUtils.closeSession(session);
		}
		return cnt;
	}
	/**
	 * 删除用户
	 * @param user
	 * @return 成功返回"success" 失败返回"failed"
	 */
	@Override
	public String delUser(User user){
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateUtils.getSession();
			tx = session.beginTransaction();
			user = (User) session.get(User.class, user.getId());
			//首先删除与该用户关联的其他表中数据
			for(Experience exp : user.getExps()){
				session.delete(exp);
			}
			for(Partner partner : user.getPartners()){
				session.delete(partner);
			}
			for(EmailInfo email : user.getEmailInfos()){
				session.delete(email);
			}
			//最后删除该用户
			session.delete(user);
			tx.commit();
		} catch (HibernateException e){
			e.printStackTrace();
			tx.rollback();
			return TextInfo.Admin.FAILED;
		} finally{
			HibernateUtils.closeSession(session);
		}
		return TextInfo.Admin.SUCCESS;
	}
	
	/**
	 * 修改用户的状态(可用改为禁用,禁用改为可用)
	 * @param user 用户对象(只包含ID的值)
	 * @return 成功启用返回"user_unlock" 成功禁用返回"user_lock" 操作失败返回"failed"
	 */
	@Override
	public String lockUser(User user){
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateUtils.getSession();
			tx = session.beginTransaction();
			user = (User) session.load(User.class, user.getId());
			user.setStatus(!user.getStatus());
			session.update(user);
			tx.commit();
		} catch (HibernateException e){
			e.printStackTrace();
			tx.rollback();
			return TextInfo.Admin.FAILED;
		} finally{
			HibernateUtils.closeSession(session);
		}
		return user.getStatus()?TextInfo.Admin.USER_UNLOCK:TextInfo.Admin.USER_LOCK;
	}
}
