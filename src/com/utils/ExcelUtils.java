package com.utils;

import java.io.FileInputStream;
import java.io.IOException;
//import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
//import java.util.Properties;
//import java.util.Set;
//import java.util.Vector;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.bean.Infomation;
import com.bean.Task;
/**
 * Excel工具类
 * @author Administrator
 *
 */
public class ExcelUtils {
	/**
	 * 根据路径读取Excel文件
	 * @param fis 文件输入流
	 * @return 表示该文件的实体对象
	 */
	public static HSSFWorkbook readExcel(FileInputStream fis){
		HSSFWorkbook wb = null;
		try {
			POIFSFileSystem pfs = new POIFSFileSystem(fis);
			wb = new HSSFWorkbook(pfs);
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(fis != null){
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return wb;
	}
	
	/**
	 * 修改Excel中的表头和日期序列
	 * @param wb Excel表格对象
	 * @param name 作者姓名
	 * @param dates Date类型的日期序列
	 * @param dates_str 字符串类型的日期序列
	 */
	public static void updateExcel(HSSFWorkbook wb,String name,Date[] dates,String[] dates_str){
		String title = name + "的工作周报（"+dates_str[0]+"至"+dates_str[dates_str.length-1]+"）";
		
		//获取第一个工作表
		HSSFSheet sheet = wb.getSheetAt(0);
		//获取工作表的第一行
		HSSFRow row = sheet.getRow(0);
		//获取该行的第一个单元格
		HSSFCell cell = row.getCell(0);
		
		//将标题文本写入到(0,0)单元格
		cell.setCellValue(title);
		
		for(int i=0 ; i<dates_str.length ; i++){
			row = sheet.getRow(10+i);
			cell = row.getCell(2);
			cell.setCellValue(DateUtils.sdf2.format(dates[i]));
		}
	}
	/**
	 * 编辑表格的主体内容(本周工作和下周计划)
	 * @param wb
	 * @param infos
	 * @param tasks
	 */
	public static void editContent(HSSFWorkbook wb,List<Infomation> infos,List<Task> tasks){
		HSSFSheet sheet = wb.getSheetAt(0);
		HSSFRow row = null;
		HSSFCell cell = null;
		
		for(short i=0 ; i<infos.size() ; i++){
			Infomation info = infos.get(i);
			String[] texts = info.getTexts();
			row = sheet.getRow(10+i);
			for(short j=0 ; j<3 ; j++){
				cell = row.getCell(3+j);
				cell.setCellValue(texts[j]);
			}
			cell = row.getCell(10);
			cell.setCellValue(texts[3]);
		}
		for(short i=0 ; i<tasks.size() && i<5 ; i++){
			Task task = tasks.get(i);
			row = sheet.getRow(3+i);
			cell = row.getCell(6);
			cell.setCellValue(task.getTaskName());
			cell = row.getCell(10);
			cell.setCellValue(task.getTaskDate());
			cell = row.getCell(11);
			cell.setCellValue(task.getChargePerson());
			cell = row.getCell(12);
			cell.setCellValue(task.getOtherPartner());
		}
	}
	/**
	 * 将心得体会的文本加入到Excel表格当中
	 * @param wb
	 * @param expText
	 */
	public static void addExp2Excel(HSSFWorkbook wb,String expText){
		HSSFSheet sheet = wb.getSheetAt(0);
		HSSFRow row = sheet.getRow(3);
		HSSFCell cell = row.getCell(0);
		cell.setCellValue(expText);
	}
	/**
	 * 将缓存中的Excel表格信息写出到文件
	 * @param wb 缓存中的Excel表对象
	 * @param fileOut 文件输出流
	 */
	public static void outputFile(HSSFWorkbook wb,OutputStream fileOut){
		try {
			wb.write(fileOut);
		} catch (IOException e) {
			// TODO IO异常处理
			e.printStackTrace();
		} finally{
			//TODO 关闭输出流
			try{
				if(fileOut != null){
					fileOut.close();
				}
			} catch (IOException e){
				e.printStackTrace();
			}
		}
	}
}
