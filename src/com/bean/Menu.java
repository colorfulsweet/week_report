package com.bean;

import java.util.ArrayList;
import java.util.List;
/**
 * 表示一级菜单的类
 * @author Administrator
 *
 */
public class Menu {
	private String name;
	private String icon;
	private List<Submenu> submenuList;
	public Menu(){
		submenuList = new ArrayList<Submenu>();
	}
	public Menu(String name){
		this();
		this.name = name;
	}
	/**
	 * 添加子菜单项
	 * @param name 菜单名
	 * @param url 菜单的URL地址
	 */
	public void addSubmenu(String name,String url){
		Submenu submenu = new Submenu(name,url);
		submenuList.add(submenu);
	}
	/**
	 * 表示二级菜单的类
	 * @author Administrator
	 *
	 */
	public class Submenu {
		private String name;
		private String url;
		public Submenu(){}
		public Submenu(String name,String url){
			this.name = name;
			this.url = url;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Submenu> getSubmenuList() {
		return submenuList;
	}

	public void setSubmenuList(List<Submenu> submenuList) {
		this.submenuList = submenuList;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
}