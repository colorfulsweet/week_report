package com.service.report;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import com.bean.hibernate.Experience;
import com.bean.hibernate.Partner;
import com.bean.hibernate.User;

public interface IReportManager {
	
	/**
	 * 编辑Excel文件
	 * @param info 包含生成周报内容所需的文本信息和流对象
	 * 包含的键有:
	 * input 文件输入流
	 * name 作者姓名
	 * infos 周报中的具体信息集合
	 * expText 心得体会文本
	 * @param output 文件输出流
	 */
	public void conductExcel(Map<String,Object> info,OutputStream output);
	
	/**
	 * 添加用户对应的预定义心得体会文本
	 * @param text 心得体会
	 * @param user 用户实体对象
	 */
	public String addExperience(String text,User user);
	
	/**
	 * 删除心得体会文本
	 * @param id 心得体会id
	 */
	public void delExperience(String id);
	
	/**
	 * 查询当前用户的心得体会列表
	 * @param user
	 * @return 心得体会实体对象集合
	 */
	public List<Experience> selectAllExperience(User user);
	
	/**
	 * 根据id查询心得体会文本
	 * @param expId 心得体会的id(数据库表主键)
	 * @return 心得体会文本内容
	 */
	public String selectExperience(String expId);
	/**
	 * 添加参与者
	 * @param partner 表示该参与者的实体对象
	 * @return 由Hibernate生成的主键ID
	 */
	public String addPartner(Partner partner,User user);
	/**
	 * 删除参与者
	 * @param partner 表示该参与者的实体对象(只有主键ID信息)
	 */
	public void delPartner(Partner partner);
	
	/**
	 * 查询当前用户的参与者列表
	 * @param user 用户实体对象
	 * @return 该用户对应的所有参与者集合
	 */
	public List<Partner> selectAllPartner(User user);
	
	/**
	 * 根据id查询心得体会文本
	 * @param partnerIds 参与者的id(数据库表主键)
	 * @return 参与者姓名的数组
	 */
	public String[] selectPartner(Object[] partnerIds);
}
