package com.lidroid.xutils.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 操作字符串相关的类
 * 
 * @author 李昊翔
 * @time 2013-3-23上午11:59:07
 * @version
 */
public class StringUtil {

	/**
	 * 删除StringBuffer中最后一个字符 （1个汉字字符存储需要2个字节，1个英文字符存储需要1个字节 非线程安全）
	 * 
	 * @param StringBuilder
	 *            sb 要被处理的值
	 * @return String 处理完后返回的值
	 */
	public static String deleteLastCharacter(StringBuilder sb) {

		if (sb.length() > 0) {
			sb.delete(sb.length() - 1, sb.length());
		}
		return sb.toString();

	}

	/**
	 * c在s中出现的次数
	 * 
	 * @update 2014-11-11 下午2:32:24<br>
	 * @author <a href="mailto:lihaoxiang@ieds.com.cn">李昊翔</a>
	 * 
	 * @param s
	 * @param c
	 * @return
	 */
	public static int calculate(String s, char c) {
		int count = 0;
		char[] cs = s.toCharArray();
		for (int i = 0; i < cs.length; i++) {
			if (cs[i] == c) {
				count++;
			}
		}
		return count;
	}

	public static String replaceOfNull(Object s) {
		if (s == null) {
			return "";
		}
		return s.toString().replaceAll("null", "");
	}

	/**
	 * 将字符对象中null转换为"" 返回，如果不为空，不做处理
	 * 
	 * @author wangjingtao
	 * @since 2012-7-7
	 * @param str
	 * @return String
	 */
	public static String null2Str(String str) {
		if (str == null || str.equals("") || str.equals("null")) {
			return null;
		}
		return str;
	}

	/**
	 * 删除StringBuffer中最后一个字符（ 1个汉字字符存储需要2个字节，1个英文字符存储需要1个字节 线程安全）
	 * 
	 * @param StringBuffer
	 *            sb 要被处理的值
	 * @return String 处理完后返回的值
	 */
	public static String deleteLastCharacter(StringBuffer sb) {
		if (sb.length() > 0) {
			sb.delete(sb.length() - 1, sb.length());
		}
		return sb.toString();
	}

	/**
	 * 判断字符串都是由整数0~9组成
	 * 
	 * @param String
	 *            str 要被判断的字符串
	 * @return boolean 是否符合要求，true 都是数字
	 */
	public static boolean isNumeric(String str) {
		if (str == null || str.trim().equals("")) {
			return false;
		}
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	/**
	 * 删除 （汉字字符冒号，格式：xx：xx）和它右边的全部字符，只保留左边内容
	 * 
	 * @param CharSequence
	 *            s 字符序列
	 * @return CharSequence 去除冒号之后的字符序列
	 */
	public static CharSequence cleanMaoHao(CharSequence s) {

		if (s != null) {

			String[] m = null;
			if (s.toString().indexOf(":") != -1) {
				m = s.toString().split(":");
			} else if (s.toString().indexOf("：") != -1) {
				m = s.toString().split("：");
			}
			if (m != null) {
				if (m.length > 0) {
					return m[0];
				} else {
					// length = 0 表达式两边没有数据
					return "";
				}
			} else {
				return s;
			}
		} else {
			return "";
		}
	}

	/**
	 * 转换车辆的出勤状态 把N代表待命， Y代表出勤
	 * 
	 * @param s
	 * @return
	 */
	public static String valueOfCarState(String s) {
		if (s != null) {
			if (s.equals("n") || s.equals("N")) {
				return "待命";
			} else if (s.equals("y") || s.equals("Y")) {
				return "出勤";
			}
		}
		return s;
	}

	/**
	 * 去掉字符串开头【】之间的文字，截取
	 * 
	 * @param String
	 *            待截取的字符串
	 * @return string 截取得到的字符串
	 */
	public static String removeString(String srcString) {
		if (srcString.indexOf("【") != -1 && srcString.indexOf("】") != -1) {
			int begin = srcString.indexOf("【");
			int end = srcString.indexOf("】");
			String tarString = "";

			String tempString = srcString.substring(begin, end + 1);
			tarString = srcString.replace(tempString, "");

			return tarString;
		} else {
			return srcString;
		}
	}

	/**
	 * 删除what和它左边的全部字符，只保留右边内容
	 * 
	 * @param s
	 * @return
	 */
	public static CharSequence cleanSringLeft(CharSequence s, String what) {

		if (s != null) {

			String[] m = null;
			if (s.toString().indexOf(what) != -1) {
				m = s.toString().split(what);
			}
			if (m != null) {
				if (m.length > 1) {
					return m[1];
				} else {
					// length = 0 表达式两边没有数据
					return "";
				}
			} else {
				return s;
			}
		} else {
			return "";
		}
	}

	/**
	 * 手动组装exception日志
	 * 
	 * @param e
	 * @return
	 */
	public static String getError(Exception e) {
		StringBuilder sb = new StringBuilder();
		sb.append(e.toString() + "\n");

		StackTraceElement[] ste = e.getStackTrace();
		for (StackTraceElement s : ste) {
			sb.append("类: " + s.getClassName() + " 函数：" + s.getMethodName()
					+ " 行数：" + s.getLineNumber() + "\n");
		}
		return sb.toString();
	}

}
