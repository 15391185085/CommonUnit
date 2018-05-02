package com.lidroid.xutils.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64;
import android.util.Log;

/**
 * 处理对象和字符串的转换 对象需要继承Serializable
 * 
 * @author 李昊翔
 * @time 2013-10-24下午03:57:30
 * @version
 */
public class SerializableUtil {
	/**
	 * 将串行对象转成可保存的base64字符串
	 * 
	 * @param object
	 * @return
	 */
	public static String SerToBase64(Object object) throws Exception {
		// 判断空值
		if (object == null) {
			return "";
		}
		// 创建字节输出流
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		String str = "";
		try {
			// 创建对象输出流，并封装字节流
			oos = new ObjectOutputStream(baos);
			// 将对象写入字节流
			oos.writeObject(object);
			// 将字节流编码成base64的字符窜
			str = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
		} finally {
			if (oos != null) {
				oos.close();
			}
		}
		return str;
	}

	/**
	 * 将base64字符串转成对象
	 * 
	 * @param object
	 * @return
	 */
	public static Object base64ToSer(String productBase64)
			throws Exception {
		// 判断空值
		if (productBase64.equals("")) {
			return null;
		}
		// 读取字节
		byte[] base64 = Base64.decode(productBase64.getBytes(), Base64.DEFAULT);
		Serializable ser = null;
		// 封装到字节流
		ByteArrayInputStream bais = new ByteArrayInputStream(base64);
		ObjectInputStream bis = null;
		try {
			// 再次封装
			bis = new ObjectInputStream(bais);
			// 读取对象
			ser = (Serializable) bis.readObject();
		} finally {
			if (bis != null) {
				bis.close();
			}
		}
		return ser;
	}
}
