package com.lidroid.xutils.util;

import java.lang.reflect.Field;

import com.lidroid.xutils.exception.NullArgumentException;

/**
 * 反射相关的工具类
 * 
 * @author 李昊翔
 * @time 2013-4-9上午09:08:32
 * @version
 */
public class ReflexUtil {
	/**
	 * 通过反射得到该类的私有变量值
	 * 
	 * @param nativeObject
	 *            ：当前类
	 * @param name
	 *            ：变量名
	 * @return 拥有该变量名的变量值
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws NullArgumentException 
	 */
	public static Object getPrivateVariable(Class nativeClass, Object nativeObject, String name) throws IllegalAccessException, IllegalArgumentException, NullArgumentException {
		Field targetField = findPrivateVariable(nativeClass, name);
		return getFieldValue(nativeObject, targetField);
	}

	/**
	 * @param nativeObject
	 * @param targetField
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public static Object getFieldValue(Object nativeObject, Field targetField)
			throws IllegalAccessException, IllegalArgumentException {
		Object targetObject = null;
		if(targetField==null){
			return null;
		} else {
			boolean accessFlag = targetField.isAccessible();
			targetField.setAccessible(true);
			targetObject = (Object) targetField.get(nativeObject);
			targetField.setAccessible(accessFlag);
		}
		return targetObject;
	}

	/**
	 * 通过反射得到该类的私有变量值
	 * 
	 * @param nativeObject
	 *            ：当前类
	 * @param name
	 *            ：变量名
	 * @return 拥有该变量名的变量值
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws NullArgumentException 
	 */
	public static Object getPrivateVariable(Object nativeObject, String name) throws IllegalAccessException, IllegalArgumentException, NullArgumentException {
		Field targetField = findPrivateVariable(nativeObject.getClass(), name);
		return getFieldValue(nativeObject, targetField);
	}

	private static Field findPrivateVariable(Class nativeClass, String name) {
		if (nativeClass != null) {
			System.out.println("当前解析的类 = " + nativeClass.getName());

			Field[] fields = nativeClass.getDeclaredFields();
			if (fields != null) {
				for (int i = 0; i < fields.length; i++) {

					if (fields[i].getName().equals(name)) {
						System.out.println("得到的私有变量 = " + fields[i].getName());

						return fields[i];
					}
				}

			}
			return findPrivateVariable(nativeClass.getSuperclass(), name);
		} else {
			return null;
		}

	}
}
