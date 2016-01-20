package com.service.report;

import java.io.FileInputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.type.IntegerType;
import org.springframework.stereotype.Service;

import com.bean.Infomation;
import com.bean.Task;
import com.bean.hibernate.Experience;
import com.bean.hibernate.Partner;
import com.bean.hibernate.User;
import com.utils.ExcelUtils;
import com.utils.HibernateUtils;

@Service
public class ReportManager implements IReportManager {
	/**
	 * 编辑Excel文件
	 * @param info 包含生成周报内容所需的文本信息和流对象
	 * 包含的键有:
	 * input 文件输入流
	 * output 文件输出流
	 * name 作者姓名
	 * infos 周报中的具体信息集合
	 * expText 心得体会文本
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void conductExcel(Map<String,Object> info,OutputStream output){
		HSSFWorkbook wb = ExcelUtils.readExcel((FileInputStream)info.get("input"));
		ExcelUtils.updateExcel(wb, (String)info.get("name"), 
									(Date[])info.get("dates"),
									(String[])info.get("dates_str"));
		ExcelUtils.editContent(wb, (List<Infomation>)info.get("infos"),(List<Task>)info.get("tasks"));
		ExcelUtils.addExp2Excel(wb, (String)info.get("expText"));
		ExcelUtils.outputFile(wb, output);
	}
	/**
	 * 添加心得体会文本
	 * @param text 心得体会
	 * @param user 用户实体对象
	 */
	@Override
	public String addExperience(String text,User user){
		Experience exp = new Experience(text,user);
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateUtils.getSession();
			tx = session.beginTransaction();
			session.save(exp);
			tx.commit();
		} catch (HibernateException e){
			e.printStackTrace();
			tx.rollback();
		} finally{
			HibernateUtils.closeSession(session);
		}
		return exp.getId();
	}
	/**
	 * 删除心得体会文本
	 * @param id 心得体会id
	 */
	@Override
	public void delExperience(String id){
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateUtils.getSession();
			tx = session.beginTransaction();
			Experience exp = new Experience();
			exp.setId(id);
			session.delete(exp);
			tx.commit();
		} catch (HibernateException e){
			e.printStackTrace();
			tx.rollback();
		} finally{
			HibernateUtils.closeSession(session);
		}
	}
	/**
	 * 查询当前用户的心得体会列表
	 * @param user
	 * @return 心得体会实体对象集合
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Experience> selectAllExperience(User user){
		Session session = null;
		Transaction tx = null;
		List<Experience> result = null;
		try{
			session = HibernateUtils.getSession();
			tx = session.beginTransaction();
			result = session.createQuery("from Experience where user_id = ?")
						.setParameter(0, user.getId())
						.list();
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
	 * 根据id查询心得体会文本
	 * @param expId 心得体会的id(数据库表主键)
	 * @return 心得体会文本内容
	 */
	@Override
	public String selectExperience(String expId){
		Experience exp = null;
		String text = null;
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateUtils.getSession();
			tx = session.beginTransaction();
			exp = (Experience)session.load(Experience.class, Integer.parseInt(expId));
			text = exp.getText();
			tx.commit();
		} catch (HibernateException e){
			e.printStackTrace();
			tx.rollback();
		} finally{
			HibernateUtils.closeSession(session);
		}
		return text;
	}
	/**
	 * 添加参与者
	 * @param partner 表示该参与者的实体对象
	 */
	@Override
	public String addPartner(Partner partner,User user){
		partner.setUser(user);
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateUtils.getSession();
			tx = session.beginTransaction();
			session.save(partner);
			tx.commit();
		} catch (HibernateException e){
			e.printStackTrace();
			tx.rollback();
		} finally{
			HibernateUtils.closeSession(session);
		}
		return partner.getId();
	}
	/**
	 * 删除参与者
	 * @param partner 表示该参与者的实体对象(只有主键ID信息)
	 */
	@Override
	public void delPartner(Partner partner){
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateUtils.getSession();
			tx = session.beginTransaction();
			session.delete(partner);
			tx.commit();
		} catch (HibernateException e){
			e.printStackTrace();
			tx.rollback();
		} finally{
			HibernateUtils.closeSession(session);
		}
	}
	/**
	 * 查询当前用户的参与者列表
	 * @param user 用户实体对象
	 * @return 该用户对应的所有参与者集合
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Partner> selectAllPartner(User user){
		Session session = null;
		Transaction tx = null;
		List<Partner> result = null;
		try{
			session = HibernateUtils.getSession();
			tx = session.beginTransaction();
			result = session.createQuery("from Partner where user_id = ?")
						.setParameter(0, user.getId())
						.list();
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
	 * 根据id查询参与者姓名
	 * @param partnerIds 参与者的id(数据库表主键)
	 * @return 参与者姓名的数组
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String[] selectPartner(Object[] partnerIds){
		Session session = null;
		Transaction tx = null;
		List<Partner> result = null;
		try{
			session = HibernateUtils.getSession();
			tx = session.beginTransaction();
			result = session.createQuery("from Partner where id in (:partnerIds)")
						.setParameterList("partnerIds", partnerIds, new IntegerType()).list();
			
			tx.commit();
		} catch (HibernateException e){
			e.printStackTrace();
			tx.rollback();
		} finally{
			HibernateUtils.closeSession(session);
		}
		String[] partnerNames = new String[result.size()];
		for(int i=0 ; i<partnerNames.length ; i++){
			Array.set(partnerNames, i, result.get(i).getPartnerName());
		}
		return partnerNames;
	}
}
