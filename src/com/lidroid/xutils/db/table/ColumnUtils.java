/*
 * Copyright (c) 2013. wyouflf (wyouflf@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lidroid.xutils.db.table;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.Date;

import javax.persistence.ColumnResult;

import android.text.TextUtils;

import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.DateUtil;
import com.lidroid.xutils.util.LogUtils;

public class ColumnUtils {

	private ColumnUtils() {
	}

	public static Method getColumnGetMethod(Class<?> entityType, Field field) {
		String fieldName = field.getName();
		Method getMethod = null;
		if (field.getType() == boolean.class) {
			getMethod = getBooleanColumnGetMethod(entityType, fieldName);
		}
		if (getMethod == null) {
			String methodName = "get" + fieldName.substring(0, 1).toUpperCase()
					+ fieldName.substring(1);
			try {
				getMethod = entityType.getDeclaredMethod(methodName);
			} catch (NoSuchMethodException e) {
				LogUtils.d(methodName + " not exist");
			}
		}

		if (getMethod == null
				&& !Object.class.equals(entityType.getSuperclass())) {
			return getColumnGetMethod(entityType.getSuperclass(), field);
		}
		return getMethod;
	}

	public static Method getColumnSetMethod(Class<?> entityType, Field field) {
		String fieldName = field.getName();
		Method setMethod = null;
		if (field.getType() == boolean.class) {
			setMethod = getBooleanColumnSetMethod(entityType, field);
		}
		if (setMethod == null) {
			String methodName = "set" + fieldName.substring(0, 1).toUpperCase()
					+ fieldName.substring(1);
			try {
				setMethod = entityType.getDeclaredMethod(methodName,
						field.getType());
			} catch (NoSuchMethodException e) {
				LogUtils.d(methodName + " not exist");
			}
		}

		if (setMethod == null
				&& !Object.class.equals(entityType.getSuperclass())) {
			return getColumnSetMethod(entityType.getSuperclass(), field);
		}
		return setMethod;
	}

	public static Object getColumnDefaultValue(Field field) throws DbException {
		ColumnResult column = field.getAnnotation(ColumnResult.class);
		if (column != null && !TextUtils.isEmpty(column.name())) {
			return valueStrSimpleTypeFieldValue(field, column.name());
		}
		return null;
	}

	public static boolean isSimpleColumnType(Field field) {
		Class<?> clazz = field.getType();
		return isSimpleColumnType(clazz);
	}

	public static boolean isSimpleColumnType(Class columnType) {
		return columnType.isPrimitive() || columnType.equals(String.class)
				|| columnType.equals(Integer.class)
				|| columnType.equals(Long.class)
				|| columnType.equals(Date.class)
				|| columnType.equals(java.sql.Date.class)
				|| columnType.equals(Boolean.class)
				|| columnType.equals(Float.class)
				|| columnType.equals(Double.class)
				|| columnType.equals(Byte.class)
				|| columnType.equals(Short.class)
				|| columnType.equals(CharSequence.class)
				|| columnType.equals(Character.class);
	}


	public static Object valueStrSimpleTypeFieldValue(Field mainField,
			final String valueStr) throws DbException {
		Class columnFieldType = mainField.getType();
		try {
			Object value = null;
			if (isSimpleColumnType(columnFieldType) && valueStr != null) {
				if (columnFieldType.equals(String.class)
						|| columnFieldType.equals(CharSequence.class)) {
					if (valueStr.trim().equals("null")) {
						value = null;
					} else {
						value = valueStr;
					}
				} else if (columnFieldType.equals(int.class)
						|| columnFieldType.equals(Integer.class)) {
					if (valueStr == null || valueStr.equals("")
							|| valueStr.trim().equals("null")) {
						value = null;
					} else {
						value = Integer.valueOf(valueStr);
					}
				} else if (columnFieldType.equals(long.class)
						|| columnFieldType.equals(Long.class)) {
					if (valueStr == null || valueStr.equals("")
							|| valueStr.trim().equals("null")) {
						value = null;
					} else {
						value = Long.valueOf(valueStr);
					}
				} else if (columnFieldType.equals(java.sql.Date.class)) {
					if (valueStr == null || valueStr.equals("")
							|| valueStr.trim().equals("null")) {
						value = null;
					} else {
						value = new java.sql.Date(Long.valueOf(valueStr));
					}
				} else if (columnFieldType.equals(Date.class)) {
					if (valueStr == null || valueStr.equals("")
							|| valueStr.trim().equals("null")) {
						value = null;
					} else if (valueStr.split("-").length > 2) {
						try {
							if (valueStr.split(":").length > 2) {
								value = DateUtil.stringtoDate(valueStr,
										DateUtil.FORMAT_ONE);
							} else {
								value = DateUtil.stringtoDate(valueStr,
										DateUtil.LONG_DATE_FORMAT);
							}
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							throw new DbException(e);
						}
					} else {
						value = new Date(Long.valueOf(valueStr));
					}
				} else if (columnFieldType.equals(boolean.class)
						|| columnFieldType.equals(Boolean.class)) {
					if (valueStr == null || valueStr.equals("")
							|| valueStr.trim().equals("null")) {
						value = null;
					} else {
						value = ColumnUtils.convert2Boolean(valueStr);
					}
				} else if (columnFieldType.equals(float.class)
						|| columnFieldType.equals(Float.class)) {
					if (valueStr == null || valueStr.equals("")
							|| valueStr.trim().equals("null")) {
						value = null;
					} else {
						if (valueStr.equals(".")) {
							value = 0.0f;
						} else {
							value = Float.valueOf(valueStr);
						}
					}
				} else if (columnFieldType.equals(double.class)
						|| columnFieldType.equals(Double.class)) {
					if (valueStr == null || valueStr.equals("")
							|| valueStr.trim().equals("null")) {
						value = null;
					} else {
						if (valueStr.equals(".")) {
							value = 0.0d;
						} else {
							value = Double.valueOf(valueStr);
						}
					}
				} else if (columnFieldType.equals(byte.class)
						|| columnFieldType.equals(Byte.class)) {
					if (valueStr == null || valueStr.equals("")
							|| valueStr.trim().equals("null")) {
						value = null;
					} else {
						value = Byte.valueOf(valueStr);
					}
				} else if (columnFieldType.equals(short.class)
						|| columnFieldType.equals(Short.class)) {
					if (valueStr == null || valueStr.equals("")
							|| valueStr.trim().equals("null")) {
						value = null;
					} else {
						value = Short.valueOf(valueStr);
					}
				} else if (columnFieldType.equals(char.class)
						|| columnFieldType.equals(Character.class)) {
					if (valueStr == null || valueStr.equals("")
							|| valueStr.trim().equals("null")) {
						value = null;
					} else {
						value = valueStr.charAt(0);
					}
				}
			}
			return value;
		} catch (Exception e) {
			throw new DbException(mainField.getName() + "数据=(" + valueStr
					+ "),类型=（" + columnFieldType.getSimpleName() + ")");
		}
	}

	public static Boolean convert2Boolean(final Object value) {
		if (value != null) {
			String valueStr = value.toString();
			return valueStr.length() == 1 ? "1".equals(valueStr) : Boolean
					.valueOf(valueStr);
		}
		return false;
	}

	public static Object convert2DbColumnValueIfNeeded(final Object value) {
		if (value != null) {
			if (value instanceof Boolean) {
				return ((Boolean) value) ? 1 : 0;
			} else if (value instanceof java.sql.Date) {
				return DateUtil.sqlDateToString((java.sql.Date) value);
			} else if (value instanceof Date) {
				return DateUtil.sqlDateToString((Date) value);
			}
		}
		return value;
	}

	public static String fieldType2DbType(Class<?> fieldType) {
		if (fieldType.equals(int.class) || fieldType.equals(Integer.class)
				|| fieldType.equals(boolean.class)
				|| fieldType.equals(Boolean.class)
				|| fieldType.equals(Date.class)
				|| fieldType.equals(java.sql.Date.class)
				|| fieldType.equals(long.class) || fieldType.equals(Long.class)
				|| fieldType.equals(byte.class) || fieldType.equals(Byte.class)
				|| fieldType.equals(short.class)
				|| fieldType.equals(Short.class)) {
			return "INTEGER";
		} else if (fieldType.equals(float.class)
				|| fieldType.equals(Float.class)
				|| fieldType.equals(double.class)
				|| fieldType.equals(Double.class)) {
			return "REAL";
		}
		return "TEXT";
	}

	private static boolean isStartWithIs(final String fieldName) {
		return fieldName != null && fieldName.startsWith("is");
	}

	private static Method getBooleanColumnGetMethod(Class<?> entityType,
			final String fieldName) {
		String methodName = "is" + fieldName.substring(0, 1).toUpperCase()
				+ fieldName.substring(1);
		if (isStartWithIs(fieldName)) {
			methodName = fieldName;
		}
		try {
			return entityType.getDeclaredMethod(methodName);
		} catch (NoSuchMethodException e) {
			LogUtils.d(methodName + " not exist");
		}
		return null;
	}

	private static Method getBooleanColumnSetMethod(Class<?> entityType,
			Field field) {
		String fieldName = field.getName();
		String methodName = null;
		if (isStartWithIs(field.getName())) {
			methodName = "set" + fieldName.substring(2, 3).toUpperCase()
					+ fieldName.substring(3);
		} else {
			methodName = "set" + fieldName.substring(0, 1).toUpperCase()
					+ fieldName.substring(1);
		}
		try {
			return entityType.getDeclaredMethod(methodName, field.getType());
		} catch (NoSuchMethodException e) {
			LogUtils.d(methodName + " not exist");
		}
		return null;
	}

}
