package com.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.bean.Menu;

public class XmlUtils {
	private static Logger log = Logger.getLogger(XmlUtils.class);
	private List<Menu> menuList = new ArrayList<Menu>();
	
	@SuppressWarnings("unchecked")
	public List<Menu> readMenuXml(String fileName){
		SAXReader saxReader = new SAXReader();
		String xmlPath = getClass().getClassLoader().getResource(fileName).getPath();
		log.info("-----开始解析菜单配置文件-----");
		Document doc = null;
		try {
			doc = saxReader.read(xmlPath);
		} catch (DocumentException e) {
			log.error("解析菜单配置文件出错!");
			e.printStackTrace();
		}
		//获取根节点
		if(doc != null){
			Element root = doc.getRootElement();
			List<Element> nodeList = root.elements();
			for(Element node : nodeList){
				readMenuNode(node);
			}
			log.info("-----结束解析菜单配置文件-----");
			return menuList;
		} else {
			log.error("解析菜单配置文件出错!");
			return null;
		}
		
	}
	/**
	 * 遍历子节点
	 * @param node 子节点对象
	 */
	@SuppressWarnings("unchecked")
	private void readMenuNode(Element node){
		Menu menu = new Menu();
		menuList.add(menu);
		List<Attribute> attrList = node.attributes();
		for(Attribute attr : attrList){
			switch(attr.getName().toLowerCase()){
				case "name":menu.setName(attr.getValue());break;
				case "icon":menu.setIcon(attr.getValue());break;
			}
		}
		log.info("一级菜单:" + menu.getName()+",icon:"+menu.getIcon());
		List<Element> nodeList = node.elements();
		for(Element childNode : nodeList){
			String submenu_name = null;
			String submenu_url = null;
			List<Element> attrs = childNode.elements();
			for(Element attr : attrs){
				switch(attr.getName().toLowerCase()){
					case "name":submenu_name = attr.getTextTrim();break;
					case "url":submenu_url = attr.getTextTrim();break;
				}
			}
			log.info("-->二级菜单:"+submenu_name+",url:"+submenu_url);
			menu.addSubmenu(submenu_name, submenu_url);
		}
	}
}
