package com.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
/**
 * 日期工具类
 * @author Administrator
 *
 */
public class DateUtils {
	private static Calendar now;
	private static Date[] dateSequence;
	public static SimpleDateFormat sdf1;
	public static SimpleDateFormat sdf2;
	static {
		sdf1 = new SimpleDateFormat("yyyy-M-d");
		sdf2 = new SimpleDateFormat("yyyy/M/d");
		dateSequence = new Date[7];
	}
	/**
	 * 获取Date类型的日期序列
	 * @return Date数组
	 */
	public static Date[] getDateSequence(){
		now = Calendar.getInstance();
		now.setFirstDayOfWeek(Calendar.MONDAY);
		int day_of_week = now.get(Calendar.DAY_OF_WEEK);
		if(day_of_week == 1){
			//每周的第一天默认设定为周天,将其校准为周一
			now.setTimeInMillis(now.getTimeInMillis()-24*3600*1000);
		}
		long start = now.getTimeInMillis()-(now.get(Calendar.DAY_OF_WEEK)*24*3600*1000);
		for(int i=0 ; i<dateSequence.length ; i++){
			Calendar temp = Calendar.getInstance();
			temp.setTimeInMillis(start + 24*3600*1000 * i);
			dateSequence[i] = temp.getTime();
		}
		return dateSequence;
	}
	/**
	 * 获取字符串类型的日期序列
	 * @return 字符串数组
	 */
	public static String[] getDateSequenceStr(){
		getDateSequence();
		String[] dates = new String[dateSequence.length];
		for(int i=0 ; i<dateSequence.length ; i++){
			dates[i] = sdf1.format(dateSequence[i]);
		}
		return dates;
	}
}
