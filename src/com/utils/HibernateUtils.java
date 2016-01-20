package com.utils;


import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
/**
 * Hibernate使用工具类(创建Session工厂)
 * @author Administrator
 *
 */
public class HibernateUtils {
	
	private static SessionFactory factory;
	private static Configuration cfg;
	
	static{
		if(cfg == null){
			buildConfiguration();
		}
	}
	
	public static void buildConfiguration(){
		cfg = new AnnotationConfiguration().configure();
		factory = cfg.buildSessionFactory();
	}
	
	public static Session getSession()
			throws HibernateException{
		return factory.openSession();
	}
	public static void closeSession(Session session){
		try{
			if(session != null && session.isOpen()){
				session.close();
			}
		} catch (HibernateException e){
			e.printStackTrace();
		}
	}
	public static SessionFactory getFactory(){
		return factory;
	}
	public static Configuration getConfiguration(){
		return cfg;
	}
}