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

package com.lidroid.xutils.db.sqlite;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.database.Cursor;

import com.lidroid.xutils.db.table.MyColumn;
import com.lidroid.xutils.db.table.MyId;
import com.lidroid.xutils.db.table.MyTable;
import com.lidroid.xutils.util.LogUtils;

public class CursorUtils {
	@SuppressWarnings("unchecked")
	public static <T> T getEntity(HashMap<String, String> cursor,
			Class<T> entityType) {
		if (cursor == null) {
			return null;
		}

		try {
			MyTable table = MyTable.get(entityType);
			T entity = entityType.newInstance();
			Set s = cursor.keySet();
			for (Iterator iterator = s.iterator(); iterator.hasNext();) {
				String key = (String) iterator.next();
				MyColumn column = table.columnMap.get(key);
				if (column != null) {
					column.setValue2Entity(entity, cursor.get(key));
				} else {
					List<MyId> idList = table.getId();
					for (Iterator iterator2 = idList.iterator(); iterator2
							.hasNext();) {
						MyId id = (MyId) iterator2.next();
						if (key.equals(id.getColumnName())) {
							id.setValue2Entity(entity, cursor.get(key));
						}
					}

				}
			}
			return entity;
		} catch (Exception e) {
			LogUtils.e(e.getMessage(), e);
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getEntity(Cursor cursor, Class<T> entityType) {
		if (cursor == null) {
			return null;
		}

		try {
			MyTable table = MyTable.get(entityType);
			T entity = entityType.newInstance();
			int columnCount = cursor.getColumnCount();
			for (int i = 0; i < columnCount; i++) {
				String columnName = cursor.getColumnName(i);
				MyColumn column = table.columnMap.get(columnName);
				if (column != null) {
					column.setValue2Entity(entity,
							getCursorString(cursor, i, entityType));
				} else {
					List<MyId> idList = table.getId();
					for (Iterator iterator = idList.iterator(); iterator
							.hasNext();) {
						MyId id = (MyId) iterator.next();
						if (columnName.equals(id.getColumnName())) {
							id.setValue2Entity(entity,
									getCursorString(cursor, i, entityType));
						}
					}

				}
			}
			return entity;
		} catch (Exception e) {
			LogUtils.e(e.getMessage(), e);
		}

		return null;
	}


	private static String getCursorString(Cursor cursor, int i, Class classType) {
		try {
			Field field = classType.getDeclaredField(cursor.getColumnName(i));
			if (field == null) {
				return null;
			}
			Class<?> fieldType = field.getType();
			if ((Integer.TYPE == fieldType) || (Integer.class == fieldType)) {
				return "" + cursor.getInt(i);
			} else if ((Long.TYPE == fieldType) || (Long.class == fieldType)) {
				return "" + cursor.getLong(i);
			} else if ((Float.TYPE == fieldType) || (Float.class == fieldType)) {
				return "" + cursor.getFloat(i);
			} else if ((Short.TYPE == fieldType) || (Short.class == fieldType)) {
				return "" + cursor.getShort(i);
			} else if ((Double.TYPE == fieldType)
					|| (Double.class == fieldType)) {
				return "" + cursor.getDouble(i);
			} else if (Byte.class == fieldType) {
				return new String(cursor.getBlob(i));
			} else {
				return cursor.getString(i);
			}
		} catch (NoSuchFieldException e) {
		}
		return null;
	}

}
