/*
 * Copyright (c) 2019-2021, LaKaLa.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.lakala.dynamicreport.common.utils;

import com.lakala.dynamicreport.common.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 公共日期工具类
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public class DateUtils {

	public static final String DATE_FORMAT = "yyyy-MM-dd";

	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	public static final String DATE_TIME_FORMAT2 = "yyyyMMddHHmmss";

	public static final String DATE_FORMAT_CHINESE = "yyyy年M月d日";

	private DateUtils(){}
	/**
	 * 获得当前的日期毫秒
	 * 
	 * @return
	 */
	public static long nowTimeMillis() {
		return System.currentTimeMillis();
	}

	/**
	 * 获得当前的时间戳
	 * 
	 * @return
	 */
	public static Timestamp nowTimeStamp() {
		return new Timestamp(nowTimeMillis());
	}

	/**
	 * 根据String返回Timestamp
	 * 
	 * @param value
	 * @return Timestamp
	 */
	public static Timestamp formatFromYYYYMMDD(String value) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		Date date = null;
		try {
			date = sdf.parse(value);
		} catch (ParseException e) {
			return null;
		}
		return new Timestamp(date.getTime());
	}

	public static Timestamp formatFromYYYYMMDDhhmmss(String value) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = sdf.parse(value);
		} catch (ParseException e) {
			return null;
		}
		return new Timestamp(date.getTime());
	}

	/**
	 * 获取timestamp 转换成 String
	 *
	 * @param timestamp
	 * @return
	 */
	public static String getTimestamp(Timestamp timestamp) {
		if (null == timestamp) {
			return "";
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(timestamp);
	}

	/**
	 * 获取当前日期
	 *
	 * @return
	 * 
	 */
	public static String getCurrentDate() {
		SimpleDateFormat df = new SimpleDateFormat(DateUtils.DATE_FORMAT);
		return df.format(new Date());
	}

	/**
	 * 获取当前日期时间
	 *
	 * @return
	 * 
	 */
	public static String getCurrentDateTime() {
		SimpleDateFormat df = new SimpleDateFormat(DateUtils.DATE_TIME_FORMAT);
		return df.format(new Date());
	}

	/**
	 * 获取当前日期时间
	 *
	 * @return
	 * 
	 */
	public static String getCurrentDateTime(String dateformat) {
		SimpleDateFormat df = new SimpleDateFormat(dateformat);
		return df.format(new Date());
	}

	public static String dateToDateTime(Date date) {
		SimpleDateFormat df = new SimpleDateFormat(DateUtils.DATE_TIME_FORMAT);
		return df.format(date);
	}

	/**
	 * 将字符串日期转换为日期格式
	 *
	 * @param datestr
	 * @return
	 * 
	 */
	public static Date stringToDate(String datestr) {

		if (StringUtils.isBlank(datestr)) {
			return null;
		}
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat(DateUtils.DATE_FORMAT);
		try {
			date = df.parse(datestr);
		} catch (ParseException e) {
			date = DateUtils.stringToDate(datestr, "yyyyMMdd");
		}
		return date;
	}

	/**
	 * 将字符串日期转换为日期格式 自定義格式
	 * 
	 * @param datestr
	 * @return
	 * 
	 */
	public static Date stringToDate(String datestr, String dateformat) {
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat(dateformat);
		try {
			date = df.parse(datestr);
		} catch (ParseException e) {
			throw new BusinessException("请输入正确日期格式:"+dateformat);
		}
		return date;
	}

	/**
	 * 将日期格式日期转换为字符串格式
	 *
	 * @param date
	 * @return
	 * 
	 */
	public static String dateToString(Date date) {
		SimpleDateFormat df = new SimpleDateFormat(DateUtils.DATE_FORMAT);
		return df.format(date);
	}

	/**
	 * 将日期格式日期转换为字符串格式 自定義格式
	 * 
	 * @param date
	 * @param dateformat
	 * @return
	 */
	public static String dateToString(Date date, String dateformat) {
		SimpleDateFormat df = new SimpleDateFormat(dateformat);
		return df.format(date);
	}

	/**
	 * 获取日期的DAY值
	 * 
	 * 
	 * @param date
	 *            输入日期
	 * @return
	 * 
	 */
	public static int getDayOfDate(Date date) {
		Calendar cd = Calendar.getInstance();
		cd.setTime(date);
		return cd.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获取日期的MONTH值
	 * 
	 * 
	 * @param date
	 *            输入日期
	 * @return
	 * 
	 */
	public static int getMonthOfDate(Date date) {
		Calendar cd = Calendar.getInstance();
		cd.setTime(date);
		return cd.get(Calendar.MONTH) + 1;
	}

	/**
	 * 获取日期的YEAR值
	 * 
	 * 
	 * @param date
	 *            输入日期
	 * @return
	 * 
	 */
	public static int getYearOfDate(Date date) {
		Calendar cd = Calendar.getInstance();
		cd.setTime(date);
		return cd.get(Calendar.YEAR);
	}

	/**
	 * 获取星期几
	 * 
	 * 
	 * @param date
	 *            输入日期
	 * @return
	 * 
	 */
	public static int getWeekOfDate(Date date) {
		Calendar cd = Calendar.getInstance();
		cd.setTime(date);
		return cd.get(Calendar.DAY_OF_WEEK) - 1;
	}

	/**
	 * 获得当天的起始时间
	 *
	 * @param date
	 * @return
	 */
	public static Date getStartDate(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY,0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 获取输入日期的当周第一天
	 *
	 * @param date
	 * @return
	 */
	public static Date getFirstDayOfWeek(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		calendar.set(Calendar.HOUR_OF_DAY,0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 获取输入日期的当周最后一天
	 *
	 * @param date
	 * @return
	 */
	public static Calendar getLastDayOfWeek(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		return calendar;
	}

	/**
	 * 获取输入日期的当月第一天
	 *
	 * @param date
	 *            输入日期
	 * @return
	 * 
	 */
	public static Date getFirstDayOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY,0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 获得输入日期的当月最后一天
	 *
	 * @param date
	 * @return
	 */
	public static Date getLastDayOfMonth(Date date) {
		return DateUtils.addDay(
				DateUtils.getFirstDayOfMonth(DateUtils.addMonth(date, 1)), -1);
	}

	/**
	 * 获取输入日期的当季第一天
	 *
	 * @param date
	 * @return
	 */
	public static Date getFirstDayOfQuarter(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int currentMonth = calendar.get(Calendar.MONTH) + 1;
		try {
			if(currentMonth >= 1 && currentMonth <= 3) {
				calendar.set(Calendar.MONTH, 0);
			}else if(currentMonth >= 4 && currentMonth <= 6) {
				calendar.set(Calendar.MONTH, 3);
			}else if(currentMonth >= 7 && currentMonth <= 9) {
				calendar.set(Calendar.MONTH, 4);
			}else if(currentMonth >= 10 && currentMonth <= 12) {
				calendar.set(Calendar.MONTH, 9);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return calendar.getTime();
	}

	/**
	 * 获得输入日期的当月最后一天
	 *
	 * @param date
	 * @return
	 */
	public static Date getLastDatOfQuarter(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int currentMonth = calendar.get(Calendar.MONTH) + 1;
		try {
			if (currentMonth >= 1 && currentMonth <= 3) {
				calendar.set(Calendar.MONTH, 2);
				calendar.set(Calendar.DATE, 31);
			} else if (currentMonth >= 4 && currentMonth <= 6) {
				calendar.set(Calendar.MONTH, 5);
				calendar.set(Calendar.DATE, 30);
			} else if (currentMonth >= 7 && currentMonth <= 9) {
				calendar.set(Calendar.MONTH,8);
				calendar.set(Calendar.DATE, 30);
			} else if (currentMonth >= 10 && currentMonth <= 12) {
				calendar.set(Calendar.MONTH, 11);
				calendar.set(Calendar.DATE, 31);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return calendar.getTime();
	}

	/**
	 * 获得输入日期的当年第一天
	 *
	 * @param date
	 * @return
	 */
	public static Date getFirstDayOfYear(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		try {
			calendar.set(Calendar.MONTH, 0);
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
			calendar.set(Calendar.HOUR_OF_DAY,0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return calendar.getTime();
	}

	/**
	 * 获得输入日期的当年最后一天
	 *
	 * @param date
	 * @return
	 */
	public static Date getLastDayOfYear(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		try {
			calendar.set(Calendar.MONTH, 11);
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getMaximum(Calendar.DAY_OF_MONTH));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return calendar.getTime();
	}

	/**
	 * 判断是否是闰年
	 * 
	 * 
	 * @param date
	 *            输入日期
	 * @return 是true 否false
	 * 
	 */
	public static boolean isLeapYEAR(Date date) {

		Calendar cd = Calendar.getInstance();
		cd.setTime(date);
		int year = cd.get(Calendar.YEAR);

		if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	/**
	 * 根据整型数表示的年月日，生成日期类型格式
	 * 
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @param day
	 *            日
	 * @return
	 * 
	 */
	public static Date getDateByYMD(int year, int month, int day) {
		Calendar cd = Calendar.getInstance();
		cd.set(year, month - 1, day);
		return cd.getTime();
	}

	/**
	 * 获取月周期对应日
	 * 
	 * @param date
	 *            输入日期
	 * @param i
	 * @return
	 * 
	 */
	public static Date getMonthCycleOfDate(Date date, int i) {
		Calendar cd = Calendar.getInstance();
		cd.setTime(date);

		cd.add(Calendar.MONTH, i);

		return cd.getTime();
	}

	/**
	 * 计算 fromDate 到 toDate 相差多少年
	 * 
	 * @param fromDate
	 * @param toDate
	 * @return 年数
	 * 
	 */
	public static int getYearByMinusDate(Date fromDate, Date toDate) {
		Calendar df = Calendar.getInstance();
		df.setTime(fromDate);

		Calendar dt = Calendar.getInstance();
		dt.setTime(toDate);

		return dt.get(Calendar.YEAR) - df.get(Calendar.YEAR);
	}

	/**
	 * 计算 fromDate 到 toDate 相差多少个月
	 * 
	 * @param fromDate
	 * @param toDate
	 * @return 月数
	 * 
	 */
	public static int getMonthByMinusDate(Date fromDate, Date toDate) {
		Calendar df = Calendar.getInstance();
		df.setTime(fromDate);

		Calendar dt = Calendar.getInstance();
		dt.setTime(toDate);

		return dt.get(Calendar.YEAR) * 12 + dt.get(Calendar.MONTH)
				- (df.get(Calendar.YEAR) * 12 + df.get(Calendar.MONTH));
	}

	/**
	 * 计算 fromDate 到 toDate 相差多少天
	 * 
	 * @param fromDate
	 * @param toDate
	 * @return 天数
	 * 
	 */
	public static long getDayByMinusDate(Object fromDate, Object toDate) {
		if (null != fromDate && null!= toDate) {
			Date f = DateUtils.chgObject(fromDate);
			Date t = DateUtils.chgObject(toDate);
			long fd = f.getTime();
			long td = t.getTime();
			return (td - fd) / (24L * 60L * 60L * 1000L);
		} else {
			return 0L;
		}
	}

	/**
	 * 计算年龄
	 * 
	 * @param birthday
	 *            生日日期
	 * @param calcDate
	 *            要计算的日期点
	 * @return
	 * 
	 */
	public static int calcAge(Date birthday, Date calcDate) {

		int cYear = DateUtils.getYearOfDate(calcDate);
		int cMonth = DateUtils.getMonthOfDate(calcDate);
		int cDay = DateUtils.getDayOfDate(calcDate);
		int bYear = DateUtils.getYearOfDate(birthday);
		int bMonth = DateUtils.getMonthOfDate(birthday);
		int bDay = DateUtils.getDayOfDate(birthday);

		if (cMonth > bMonth || (cMonth == bMonth && cDay > bDay)) {
			return cYear - bYear;
		} else {
			return cYear - 1 - bYear;
		}
	}

	/**
	 * 从身份证中获取出生日期
	 * 
	 * @param idno
	 *            身份证号码
	 * @return
	 * 
	 */
	public static String getBirthDayFromIDCard(String idno) {
		Calendar cd = Calendar.getInstance();
		if (idno.length() == 15) {
			cd.set(Calendar.YEAR, Integer.parseInt("19" + idno.substring(6, 8)));
			cd.set(Calendar.MONTH, Integer.parseInt(idno.substring(8, 10)) - 1);
			cd.set(Calendar.DAY_OF_MONTH, Integer.parseInt(idno.substring(10, 12)));
		} else if (idno.length() == 18) {
			cd.set(Calendar.YEAR, Integer.parseInt(idno.substring(6, 10)));
			cd.set(Calendar.MONTH, Integer.parseInt(idno.substring(10, 12)) - 1);
			cd.set(Calendar.DAY_OF_MONTH, Integer.parseInt(idno.substring(12, 14)));
		}
		return DateUtils.dateToString(cd.getTime());
	}

	/**
	 * 在输入日期上增加（+）或减去（-）天数
	 * 
	 * @param date
	 *            输入日期
	 * @param imonth
	 *            要增加或减少的天数
	 */
	public static Date addDay(Date date, int iday) {
		Calendar cd = Calendar.getInstance();

		cd.setTime(date);

		cd.add(Calendar.DAY_OF_MONTH, iday);

		return cd.getTime();
	}

	/**
	 * 在输入日期上增加（+）或减去（-）月份
	 * 
	 * @param date
	 *            输入日期
	 * @param imonth
	 *            要增加或减少的月分数
	 */
	public static Date addMonth(Date date, int imonth) {
		Calendar cd = Calendar.getInstance();

		cd.setTime(date);

		cd.add(Calendar.MONTH, imonth);

		return cd.getTime();
	}

	/**
	 * 在输入日期上增加（+）或减去（-）年份
	 * 
	 * @param date
	 *            输入日期
	 * @param imonth
	 *            要增加或减少的年数
	 */
	public static Date addYear(Date date, int iyear) {
		Calendar cd = Calendar.getInstance();

		cd.setTime(date);

		cd.add(Calendar.YEAR, iyear);

		return cd.getTime();
	}

	/**
	 * 将Object类型转换为Date
	 * 
	 * @param date
	 * @return
	 */
	public static Date chgObject(Object date) {
		if (date instanceof Date) {
			return (Date) date;
		}
		if (date instanceof String) {
			return DateUtils.stringToDate((String) date);
		}
		return null;
	}

	public static long getAgeByBirthday(String date) {

		Date birthday = stringToDate(date, DATE_FORMAT);
		long sec = new Date().getTime() - birthday.getTime();

		return sec / (1000 * 60 * 60 * 24) / 365;
	}

	public static boolean isDate(String date) {
		Pattern patternDate = Pattern.compile("(\\d){4}-(\\d){2}-(\\d){2}");
		Matcher matcher = patternDate.matcher(date);
		return matcher.find();
	}
	
	/**
     * 获取当前日期时间
     * 
     * @return
     * 
     */
    public static String getCurrentDateTimeFormat() {
        SimpleDateFormat df = new SimpleDateFormat(DateUtils.DATE_TIME_FORMAT2);
        return df.format(new Date());
    }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/**
		 * String temp = DateUtil.dateToString(getLastDayOfMonth(new Date()),
		 * DateUtil.DATE_FORMAT_CHINESE);
		 * String s=DateUtil.dateToString(DateUtil.addDay(DateUtil.addYear(new
		 * Date(),1),-1));
		 *long s = DateUtils.getDayByMinusDate("2012-01-01", "2012-12-31");
		 *System.err.println(s);
		 */
	}

}
