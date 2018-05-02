package com.lidroid.xutils.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 处理数据格式 描述：工具类<br>
 * 最近修改时间：2013-3-9<br>
 * 
 * @author 李昊翔
 * @since 2013-3-9
 */
public class FormatUtil {
	/**
	 * 
	 * @param value
	 * @return true null 或者 “”
	 */
	public static boolean isNull(String value) {
		return value == null || value.trim().equals("");
	}
	/**
	 * 数据没有含义
	 * @param value
	 * @return true null 或者 “”
	 */
	public static boolean isNull(Collection value) {
		return value==null || value.isEmpty();
	}
	
	public static List fromMapToList(Map m) {
		List l = new ArrayList();
		Object[] a = m.keySet().toArray();
		Arrays.sort(a);
		for (int i = 0; i < a.length; i++) {
			l.add(m.get(a[i]));
		}
		return l;
	}

	/**
	 * 判断是否是手机号码
	 * 
	 * @param mobiles
	 *            发来的字符串
	 * @return boolean 是否是手机号
	 */
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 电话
	 * 
	 * @param phone
	 * @return
	 */
	public static boolean isPhoneNO(String phone) {
		Pattern p = Pattern.compile("(\\(\\d{3,4}\\)|\\d{3,4}-|\\s)?\\d{7,14}");
		Matcher m = p.matcher(phone);
		return m.matches();
	}

	/**
	 * 判断是否是整数
	 * 
	 * @author 李昊翔
	 * @param number
	 * @return
	 * @since JDK 1.6
	 */
	public static boolean isInt(String number) {
		Pattern p = Pattern.compile("^-?\\d+$");
		Matcher m = p.matcher(number);
		return m.matches();
	}
	
	
	/**
	 * 是否是版本号0.1.1
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isVersionNO(String value) {
		String n[] = value.split("\\.");
		if (n.length != 3) {
			return false;
		}
		for (String s : n) {
			Pattern p = Pattern.compile("^\\d+$");
			// Pattern p=Pattern.compile("\\.");
			Matcher m = p.matcher(s);
			if (!m.matches()) {
				return false;
			}
		}
		return true;
	}

	/*
	 * 正则表达式快速查找 
	 * "^\d+$"　　//非负整数（正整数 + 0） 
	 * "^[0-9]*[1-9][0-9]*$"　　//正整数
	 * "^((-\d+)|(0+))$"　　//非正整数（负整数 + 0） 
	 * "^-[0-9]*[1-9][0-9]*$"　　//负整数
	 * "^-?\d+$"　　　　//整数 
	 * "^\d+(\.\d+)?$"　　//非负浮点数（正浮点数 + 0）
	 * "^(([0-9]+\.[0-9]*[1-
	 * 9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*))$"　　//正浮点数
	 * "^((-\d+(\.\d+)?)|(0+(\.0+)?))$"　　//非正浮点数（负浮点数 + 0）
	 * "^(-(([0-9]+\.[0-9]*[1
	 * -9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*)))$"　　//负浮点 数
	 * "^(-?\d+)(\.\d+)?$"　　//浮点数 "^[A-Za-z]+$"　　//由26个英文字母组成的字符串
	 * "^[A-Z]+$"　　//由26个英文字母的大写组成的字符串 "^[a-z]+$"　　//由26个英文字母的小写组成的字符串
	 * "^[A-Za-z0-9]+$"　　//由数字和26个英文字母组成的字符串 "^\w+$"　　//由数字、26个英文字母或者下划线组成的字符串
	 * "^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$"　　　　//email地址
	 * "^[a-zA-z]+://(\w+(-\w+)*)(\.(\w+(-\w+)*))*(\?\S*)?$"　　//url
	 * /^13\d{9}$/gi手机号正则表达式 正则表达式--验证手机号码:13[0-9]{9}
	 * 实现手机号前带86或是+86的情况:^((\+86)|(86))?(13)\d{9}$
	 * 电话号码与手机号码同时验证:(^(\d{3,4}-)?\d{7,8})$|(13[0-9]{9})
	 * 提取信息中的网络链接:(h|H)(r|R)(e|E)(f|F) *= *('|")?(\w|\\|\/|\.)+('|"| *|>)?
	 * 提取信息中的邮件地址:\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*
	 * 提取信息中的图片链接:(s|S)(r|R)(c|C) *= *('|")?(\w|\\|\/|\.)+('|"| *|>)?
	 * 提取信息中的IP地址:(\d+)\.(\d+)\.(\d+)\.(\d+) 提取信息中的中国手机号码:(86)*0*13\d{9}
	 * 提取信息中的中国固定电话号码:(\(\d{3,4}\)|\d{3,4}-|\s)?\d{8}
	 * 提取信息中的中国电话号码（包括移动和固定电话）:(\(\d{3,4}\)|\d{3,4}-|\s)?\d{7,14}
	 * 提取信息中的中国邮政编码:[1-9]{1}(\d+){5} 提取信息中的中国身份证号码:\d{18}|\d{15} 提取信息中的整数：\d+
	 * 提取信息中的浮点数（即小数）：(-?\d*)\.?\d+ 提取信息中的任何数字 ：(-?\d*)(\.\d+)?
	 * 提取信息中的中文字符串：[\u4e00-\u9fa5]* 提取信息中的双字节字符串 (汉字)：[^\x00-\xff]*
	 */
}
