package com.lidroid.xutils.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;

import android.content.Context;
import android.text.TextUtils;

/**
 * @title: 时间工具类
 * @description: 用户操作时间
 */
public final class DateUtil {

	// 格式：年－月－日 小时：分钟：秒
	public static final String FORMAT_ONE = "yyyy-MM-dd HH:mm:ss";
	// 格式：年－月－日 小时：分钟：秒
	public static final String FORMAT_ONE_T = "yyyy-MM-dd'T'HH:mm:ss";
	// 格式：年－月－日 小时：分钟
	public static final String FORMAT_TWO = "yyyy-MM-dd HH:mm";

	// 格式：年月日 小时分钟秒
	public static final String FORMAT_THREE = "yyyyMMdd-HHmmss";

	public static final String FORMAT_FOUR = "yyyyMMdd";

	// 格式：年－月－日
	public static final String LONG_DATE_FORMAT = "yyyy-MM-dd";

	// 格式：月－日
	public static final String SHORT_DATE_FORMAT = "MM-dd";

	public static final String LONG_LINE_FORMAT = "yyyyMMddHHmmssSSS";
	// 格式: 年月日时分秒
	public static final String SHORT_LINE_FORMAT = "yyyyMMddHHmmss";

	// 格式: 年月日时分秒
	public static final String SHORT_LINE_FORMAT_TWO = "yyyyMMddHHmm";
	// 格式：小时：分钟：秒
	public static final String LONG_TIME_FORMAT = "HH:mm:ss";

	// 格式：小时：分钟
	public static final String LONG_TIME_FORMAT_HH = "HH:mm";

	// 格式：年-月
	public static final String MONTG_DATE_FORMAT = "yyyy-MM";

	// 年的加减
	public static final int SUB_YEAR = Calendar.YEAR;

	// 月加减
	public static final int SUB_MONTH = Calendar.MONTH;

	// 天的加减
	public static final int SUB_DAY = Calendar.DATE;

	// 小时的加减
	public static final int SUB_HOUR = Calendar.HOUR_OF_DAY;

	// 分钟的加减
	public static final int SUB_MINUTE = Calendar.MINUTE;

	// 秒的加减
	public static final int SUB_SECOND = Calendar.SECOND;

	public static String FORMATTIMESTR = "yyyy年MM月dd日 HH时mm分ss秒"; // 时间格式化格式

	public static String getWeekOfDate(Date dt) {
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}

	/**
	 * 获取yyyyMMdd格式日期
	 * 
	 * @param time
	 * @return
	 * @throws ParseException
	 */
	public static Date getShortDate(String time) throws ParseException {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat format = new SimpleDateFormat(FORMAT_FOUR);
		date = format.parse(time);
		return date;
	}

	public static String formatDate(Context context, long date) {
		@SuppressWarnings("deprecation")
		int format_flags = android.text.format.DateUtils.FORMAT_NO_NOON_MIDNIGHT
				| android.text.format.DateUtils.FORMAT_ABBREV_ALL
				| android.text.format.DateUtils.FORMAT_CAP_AMPM
				| android.text.format.DateUtils.FORMAT_SHOW_DATE
				| android.text.format.DateUtils.FORMAT_SHOW_DATE
				| android.text.format.DateUtils.FORMAT_SHOW_TIME;
		return android.text.format.DateUtils.formatDateTime(context, date,
				format_flags);
	}

	/**
	 * 返回此时时间
	 * 
	 * @return String: XXX年XX月XX日 XX:XX:XX
	 */
	public static String getNowtime() {
		return new SimpleDateFormat(FORMATTIMESTR).format(new Date());
	}

	/**
	 * 格式化输出指定时间点与现在的差
	 * 
	 * @param paramTime
	 *            指定的时间点
	 * @return 格式化后的时间差，类似 X秒前、X小时前、X年前
	 * @throws ParseException
	 */
	public static String getBetweentime(String paramTime) throws ParseException {
		String returnStr = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat(FORMATTIMESTR);
		Date nowData = new Date();
		Date mDate = dateFormat.parse(paramTime);
		long betweenForSec = Math.abs(mDate.getTime() - nowData.getTime()) / 1000; // 秒
		if (betweenForSec < 60) {
			returnStr = betweenForSec + "秒前";
		} else if (betweenForSec < (60 * 60)) {
			returnStr = betweenForSec / 60 + "分钟前";
		} else if (betweenForSec < (60 * 60 * 24)) {
			returnStr = betweenForSec / (60 * 60) + "小时前";
		} else if (betweenForSec < (60 * 60 * 24 * 30)) {
			returnStr = betweenForSec / (60 * 60 * 24) + "天前";
		} else if (betweenForSec < (60 * 60 * 24 * 30 * 12)) {
			returnStr = betweenForSec / (60 * 60 * 24 * 30) + "个月前";
		} else
			returnStr = betweenForSec / (60 * 60 * 24 * 30 * 12) + "年前";
		return returnStr;
	}

	public static Date getDate(String time) throws ParseException {
		DateFormat fmtDateFormat = new SimpleDateFormat(FORMAT_TWO);
		Date date = fmtDateFormat.parse(time);
		return date;
	}

	public static String DateToTime(String time, String format)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_ONE);
		Date date = sdf.parse(time);
		SimpleDateFormat sdf2 = new SimpleDateFormat(format);
		return sdf2.format(date);
	}

	/**
	 * 把符合日期格式的字符串转换为日期类型
	 * 
	 * @param dateStr
	 * @return
	 * @throws ParseException
	 */
	public static java.util.Date stringtoDate(String dateStr, String format)
			throws ParseException {
		Date d = null;
		SimpleDateFormat formater = new SimpleDateFormat(format);
		formater.setLenient(false);
		return formater.parse(dateStr);
	}

	/**
	 * 把符合日期格式的字符串转换为日期类型
	 */
	public static java.util.Date stringtoDate(String dateStr, String format,
			ParsePosition pos) {
		SimpleDateFormat formater = new SimpleDateFormat(format);
		formater.setLenient(false);
		return formater.parse(dateStr, pos);
	}

	/**
	 * 把日期转换为字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String dateToString(java.util.Date date, String format) {
		String result = "";
		SimpleDateFormat formater = new SimpleDateFormat(format);
		return formater.format(date);
	}

	/**
	 * 获取当前时间的指定格式
	 * 
	 * @param format
	 * @return
	 */
	public static String getCurrDate(String format) {
		return dateToString(new Date(), format);
	}

	public static String formatTime(String strTime, String format)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern(format);
		Date ddDate = sdf.parse(strTime);
		return sdf.format(ddDate);
	}

	/**
	 * 
	 * @param dateStr
	 * @param amount
	 * @return
	 * @throws ParseException
	 */
	public static String dateSub(int dateKind, String dateStr, int amount)
			throws ParseException {
		Date date = stringtoDate(dateStr, FORMAT_ONE);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(dateKind, amount);
		return dateToString(calendar.getTime(), FORMAT_ONE);
	}

	/**
	 * 两个日期相减
	 * 
	 * @param firstTime
	 * @param secTime
	 * @return 相减得到的秒数
	 * @throws ParseException
	 */
	public static long timeSub(String firstTime, String secTime)
			throws ParseException {
		long first = stringtoDate(firstTime, FORMAT_ONE).getTime();
		long second = stringtoDate(secTime, FORMAT_ONE).getTime();
		return (second - first) / 1000;
	}

	/**
	 * 获得某月的天数
	 * 
	 * @param year
	 *            int
	 * @param month
	 *            int
	 * @return int
	 */
	public static int getDaysOfMonth(String year, String month) {
		int days = 0;
		if (month.equals("1") || month.equals("3") || month.equals("5")
				|| month.equals("7") || month.equals("8") || month.equals("10")
				|| month.equals("12")) {
			days = 31;
		} else if (month.equals("4") || month.equals("6") || month.equals("9")
				|| month.equals("11")) {
			days = 30;
		} else {
			if ((Integer.parseInt(year) % 4 == 0 && Integer.parseInt(year) % 100 != 0)
					|| Integer.parseInt(year) % 400 == 0) {
				days = 29;
			} else {
				days = 28;
			}
		}

		return days;
	}

	/**
	 * 获取某年某月的天数
	 * 
	 * @param year
	 *            int
	 * @param month
	 *            int 月份[1-12]
	 * @return int
	 */
	public static int getDaysOfMonth(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, 1);
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获得当前日期(几号)
	 * 
	 * @return int
	 */
	public static int getToday() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.DATE);
	}

	/**
	 * 获得当前月份
	 * 
	 * @return int
	 */
	public static int getToMonth() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.MONTH) + 1;
	}

	/**
	 * 获得当前年份
	 * 
	 * @return int
	 */
	public static int getToYear() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.YEAR);
	}

	/**
	 * 返回日期的天
	 * 
	 * @param date
	 *            Date
	 * @return int
	 */
	public static int getDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DATE);
	}

	/**
	 * 返回日期的年
	 * 
	 * @param date
	 *            Date
	 * @return int
	 */
	public static int getYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
	}

	/**
	 * 返回日期的月份，1-12
	 * 
	 * @param date
	 *            Date
	 * @return int
	 */
	public static int getMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MONTH) + 1;
	}

	/**
	 * 计算两个日期的差，精确到秒，如果after > before 返回正数，否则返回负数
	 * 
	 * @param before
	 *            Date
	 * @param after
	 *            Date
	 * @return long after - before剩余的天数
	 */
	public static long diffSecond(Date before, Date after) {
		return after.getTime() - before.getTime();
	}

	/**
	 * 返回的日期格式为年月日
	 * 
	 * @param date
	 * @return
	 */
	public static String getToDay(java.util.Date date) {
		String result = "";
		if (date == null) {
			return result;
		}
		SimpleDateFormat formater = new SimpleDateFormat(LONG_DATE_FORMAT);
		return formater.format(date);
	}

	/**
	 * 比较指定日期与当前日期的差
	 * 
	 * @param befor
	 * @param after
	 * @return
	 * @throws ParseException
	 */
	public static int yearDiffCurr(String after) throws ParseException {
		Date beforeDay = new Date();
		Date afterDay = stringtoDate(after, LONG_DATE_FORMAT);
		return getYear(beforeDay) - getYear(afterDay);
	}

	/**
	 * 比较指定日期与当前日期的差
	 * 
	 * @param before
	 * @return
	 * @throws ParseException
	 */
	public static long dayDiffCurr(String before) throws ParseException {
		Date currDate = DateUtil.stringtoDate(currDay(), LONG_DATE_FORMAT);
		Date beforeDate = stringtoDate(before, LONG_DATE_FORMAT);
		return currDate.getTime() - beforeDate.getTime();
	}

	/**
	 * 比较两个时间的大小
	 * 
	 * @update 2014-8-21 下午5:09:45<br>
	 * @author <a href="mailto:gaohaiyanghy@126.com">高海燕</a>
	 * @param firstTime
	 * @param lastTime
	 * @return
	 * @throws ParseException
	 */
	public static boolean compareSize(String firstTime, String lastTime)
			throws ParseException {
		long firstDate = stringtoDate(firstTime, FORMAT_ONE).getTime();
		long lastDate = stringtoDate(lastTime, FORMAT_ONE).getTime();
		if (lastDate - firstDate > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取每月的第一周
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getFirstWeekdayOfMonth(int year, int month) {
		Calendar c = Calendar.getInstance();
		c.setFirstDayOfWeek(Calendar.SATURDAY); // 星期天为第一天
		c.set(year, month - 1, 1);
		return c.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 获取每月的最后一周
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getLastWeekdayOfMonth(int year, int month) {
		Calendar c = Calendar.getInstance();
		c.setFirstDayOfWeek(Calendar.SATURDAY); // 星期天为第一天
		c.set(year, month - 1, getDaysOfMonth(year, month));
		return c.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 获得当前日期字符串，格式"yyyy-MM-dd HH:mm:ss"
	 * 
	 * @return
	 */
	public static String getNow() {
		Calendar today = Calendar.getInstance();
		return dateToString(today.getTime(), FORMAT_ONE);
	}

	/**
	 * 获得当前日期字符串，格式"yyyyMMddHHmmss"
	 * 
	 * @return
	 */
	public static String getLongDateFormat() {
		Calendar today = Calendar.getInstance();
		return dateToString(today.getTime(), LONG_LINE_FORMAT);
	}

	/**
	 * 获得当前日期字符串，格式"yyyyMMddHHmmss"
	 * 
	 * @return
	 */
	public static String getShortDateFormat() {
		Calendar today = Calendar.getInstance();
		return dateToString(today.getTime(), SHORT_LINE_FORMAT);
	}

	/**
	 * 获得当前日期字符串，格式"yyyyMMdd"
	 * 
	 * @return
	 */
	public static String getFormatFour() {
		Calendar today = Calendar.getInstance();
		return dateToString(today.getTime(), FORMAT_FOUR);
	}

	/**
	 * @update 2014-8-5 下午2:18:08<br>
	 * @author <a href="mailto:gaohaiyanghy@126.com">高海燕</a>
	 * @param strTime
	 * @param format
	 * @return
	 * @throws ParseException
	 */
	public static Date getDate(String strTime, String format)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.parse(strTime);
	}

	/**
	 * 根据生日获取星座
	 * 
	 * @param birth
	 *            YYYY-mm-dd
	 * @return
	 */
	public static String getAstro(String birth) {
		if (!isDate(birth)) {
			birth = "2000" + birth;
		}
		if (!isDate(birth)) {
			return "";
		}
		int month = Integer.parseInt(birth.substring(birth.indexOf("-") + 1,
				birth.lastIndexOf("-")));
		int day = Integer.parseInt(birth.substring(birth.lastIndexOf("-") + 1));
		String s = "魔羯水瓶双鱼牡羊金牛双子巨蟹狮子处女天秤天蝎射手魔羯";
		int[] arr = { 20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22 };
		int start = month * 2 - (day < arr[month - 1] ? 2 : 0);
		return s.substring(start, start + 2) + "座";
	}

	/**
	 * 判断日期是否有效,包括闰年的情况
	 * 
	 * @param date
	 *            YYYY-mm-dd
	 * @return
	 */
	public static boolean isDate(String date) {
		StringBuffer reg = new StringBuffer(
				"^((\\d{2}(([02468][048])|([13579][26]))-?((((0?");
		reg.append("[13578])|(1[02]))-?((0?[1-9])|([1-2][0-9])|(3[01])))");
		reg.append("|(((0?[469])|(11))-?((0?[1-9])|([1-2][0-9])|(30)))|");
		reg.append("(0?2-?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][12");
		reg.append("35679])|([13579][01345789]))-?((((0?[13578])|(1[02]))");
		reg.append("-?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))");
		reg.append("-?((0?[1-9])|([1-2][0-9])|(30)))|(0?2-?((0?[");
		reg.append("1-9])|(1[0-9])|(2[0-8]))))))");
		Pattern p = Pattern.compile(reg.toString());
		return p.matcher(date).matches();
	}

	/**
	 * 取得指定日期过 months 月后的日期 (当 months 为负数表示指定月之前);
	 * 
	 * @param date
	 *            日期 为null时表示当天
	 * @param month
	 *            相加(相减)的月数
	 */
	public static Date nextMonth(Date date, int months) {
		Calendar cal = Calendar.getInstance();
		if (date != null) {
			cal.setTime(date);
		}
		cal.add(Calendar.MONTH, months);
		return cal.getTime();
	}

	/**
	 * 取得指定日期过 day 天后的日期 (当 day 为负数表示指日期之前);
	 * 
	 * @param date
	 *            日期 为null时表示当天
	 * @param month
	 *            相加(相减)的月数
	 */
	public static Date nextDay(Date date, int day) {
		Calendar cal = Calendar.getInstance();
		if (date != null) {
			cal.setTime(date);
		}
		cal.add(Calendar.DAY_OF_YEAR, day);
		return cal.getTime();
	}

	/**
	 * 取得距离今天 day 日的日期
	 * 
	 * @param day
	 * @param format
	 * @return
	 */
	public static String nextDay(int day, String format) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_YEAR, day);
		return dateToString(cal.getTime(), format);
	}

	/**
	 * 取得指定日期过 day 周后的日期 (当 day 为负数表示指定月之前)
	 * 
	 * @param date
	 *            日期 为null时表示当天
	 */
	public static Date nextWeek(Date date, int week) {
		Calendar cal = Calendar.getInstance();
		if (date != null) {
			cal.setTime(date);
		}
		cal.add(Calendar.WEEK_OF_MONTH, week);
		return cal.getTime();
	}

	/**
	 * 获取当前的日期(yyyy-MM-dd)
	 */
	public static String currDay() {
		return DateUtil.dateToString(new Date(), DateUtil.LONG_DATE_FORMAT);
	}

	/**
	 * 获取昨天的日期
	 * 
	 * @return
	 */
	public static String befoDay() {
		return befoDay(DateUtil.LONG_DATE_FORMAT);
	}

	/**
	 * 根据时间类型获取昨天的日期
	 * 
	 * @param format
	 * @return
	 */
	public static String befoDay(String format) {
		return DateUtil.dateToString(DateUtil.nextDay(new Date(), -1), format);
	}

	/**
	 * 获取明天的日期
	 */
	public static String afterDay() {
		return DateUtil.dateToString(DateUtil.nextDay(new Date(), 1),
				DateUtil.LONG_DATE_FORMAT);
	}

	/**
	 * 取得当前时间距离1900/1/1的天数
	 * 
	 * @return
	 */
	public static int getDayNum() {
		int daynum = 0;
		GregorianCalendar gd = new GregorianCalendar();
		Date dt = gd.getTime();
		GregorianCalendar gd1 = new GregorianCalendar(1900, 1, 1);
		Date dt1 = gd1.getTime();
		daynum = (int) ((dt.getTime() - dt1.getTime()) / (24 * 60 * 60 * 1000));
		return daynum;
	}

	/**
	 * getDayNum的逆方法(用于处理Excel取出的日期格式数据等)
	 * 
	 * @param day
	 * @return
	 */
	public static Date getDateByNum(int day) {
		GregorianCalendar gd = new GregorianCalendar(1900, 1, 1);
		Date date = gd.getTime();
		date = nextDay(date, day);
		return date;
	}

	/** 针对yyyy-MM-dd HH:mm:ss格式,显示yyyymmdd */
	public static String getYmdDateCN(String datestr) {
		if (datestr == null)
			return "";
		if (datestr.length() < 10)
			return "";
		StringBuffer buf = new StringBuffer();
		buf.append(datestr.substring(0, 4)).append(datestr.substring(5, 7))
				.append(datestr.substring(8, 10));
		return buf.toString();
	}

	/**
	 * 获取本月第一天
	 * 
	 * @param format
	 * @return
	 */
	public static String getFirstDayOfMonth(String format) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DATE, 1);
		return dateToString(cal.getTime(), format);
	}

	/**
	 * 获取本月最后一天
	 * 
	 * @param format
	 * @return
	 */
	public static String getLastDayOfMonth(String format) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DATE, 1);
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.DATE, -1);
		return dateToString(cal.getTime(), format);
	}

	/**
	 * 获取yyyy年mm月dd日
	 * 
	 * @param date
	 * @return
	 */
	public static String getChineDate(Date date) {
		String strDate = DateUtil.dateToString(date, DateUtil.LONG_DATE_FORMAT);
		return strDate.split("-")[0] + "年" + strDate.split("-")[1] + "月"
				+ strDate.split("-")[2] + "日";
	}

	/**
	 * 获得yyyy年MM月dd日 hh:mm:ss
	 * 
	 * @param date
	 * @return
	 */
	public static String getChineLongDate(Date date) {
		String strDate = DateUtil.dateToString(date, DateUtil.LONG_DATE_FORMAT);
		return strDate.split("-")[0] + "年" + strDate.split("-")[1] + "月"
				+ strDate.split("-")[2] + "日" + " "
				+ dateToString(date, LONG_TIME_FORMAT);
	}

	/**
	 * 获取mm月dd日 hh:mm:ss
	 * 
	 * @param date
	 * @return
	 */
	public static String getChineShortDate(Date date) {
		String strDate = DateUtil.dateToString(date, DateUtil.LONG_DATE_FORMAT);
		return strDate.split("-")[1] + "月" + strDate.split("-")[2] + "日" + " "
				+ dateToString(date, LONG_TIME_FORMAT);
	}

	public static String getMilliToDate(String time) {
		if (!TextUtils.isEmpty(time)) {
			Date date = new Date(Long.valueOf(time));
			SimpleDateFormat formatter = new SimpleDateFormat(FORMAT_ONE);
			return formatter.format(date);
		} else {
			return time;
		}
	}

	/**
	 * 把符合日期格式的字符串转换为日期类型
	 * 
	 * @param dateStr
	 * @return
	 * @throws ParseException
	 */
	public static java.util.Date sqlStringToDate(String valueStr)
			throws ParseException {
		Date d = null;
		SimpleDateFormat formater = null;
		if (valueStr == null || valueStr.equals("")
				|| valueStr.trim().equals("null")) {
			return null;
		} else if (StringUtil.calculate(valueStr, '-') == 2
				&& StringUtil.calculate(valueStr, ':') == 2) {
			formater = new SimpleDateFormat(FORMAT_ONE);
		} else if (StringUtil.calculate(valueStr, '-') == 2) {
			formater = new SimpleDateFormat(LONG_DATE_FORMAT);
		} else if (StringUtil.calculate(valueStr, '-') == 1) {
			formater = new SimpleDateFormat(MONTG_DATE_FORMAT);
		} else {
			return new Date(Long.valueOf(valueStr));
		}
		formater.setLenient(false);
		return formater.parse(valueStr);
	}

	/**
	 * 把日期转换为字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String sqlDateToString(java.util.Date date) {
		String result = "";
		if (date == null) {
			return result;
		}
		SimpleDateFormat formater = new SimpleDateFormat(FORMAT_ONE);
		return formater.format(date);
	}

	/**
	 * 获得当前日期字符串，格式"yyyyMMdd" "yyyy-MM-dd 00:00:00"
	 * 
	 * @return
	 */
	public static String getDateZeroTime() {
		Calendar today = Calendar.getInstance();
		return DateUtil
				.dateToString(today.getTime(), DateUtil.LONG_DATE_FORMAT)
				+ " 00:00:00";
	}
	public static String getDateZeroTime(Date time) {
		return DateUtil
				.dateToString(time, DateUtil.LONG_DATE_FORMAT)
				+ " 00:00:00";
	}
}