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
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;

public class TableUtils {

	private TableUtils() {
	}

	public static String getTableName(Class<?> entityType) {
		Table t = entityType.getAnnotation(Table.class);
		if (t != null) {
			return t.name();
		} else {
			return entityType.getSimpleName();
		}
	}

	/**
	 * key: entityType.canonicalName
	 */
	private static ConcurrentHashMap<String, HashMap<String, MyColumn>> entityColumnsMap = new ConcurrentHashMap<String, HashMap<String, MyColumn>>();

	/**
	 * @param entityType
	 * @return key: columnName
	 * @throws DbException
	 */
	public static synchronized HashMap<String, MyColumn> getColumnMap(
			Class<?> entityType) throws DbException {

		if (entityColumnsMap.containsKey(entityType.getCanonicalName())) {
			return entityColumnsMap.get(entityType.getCanonicalName());
		}

		HashMap<String, MyColumn> columnMap = new HashMap<String, MyColumn>();
		List<String> primaryKeyFieldName = getPrimaryKeyFieldName(entityType);
		for (Iterator iterator = primaryKeyFieldName.iterator(); iterator
				.hasNext();) {
			String fieldName = (String) iterator.next();
			addColumns2Map(entityType, primaryKeyFieldName, columnMap);
		}
		entityColumnsMap.put(entityType.getCanonicalName(), columnMap);

		return columnMap;
	}

	private static void addColumns2Map(Class<?> entityType,
			List<String> primaryKeyList, HashMap<String, MyColumn> columnMap) {
		if (Object.class.equals(entityType))
			return;
		try {
			Field[] fields = entityType.getDeclaredFields();
			for (Field field : fields) {
				if (Modifier.isStatic(field.getModifiers())) {
					continue;
				}
				if (ColumnUtils.isSimpleColumnType(field)) {
					if (!primaryKeyList.contains(field.getName())) {
						MyColumn column = new MyColumn(entityType, field);
						if (!columnMap.containsKey(column.getColumnName())) {
							columnMap.put(column.getColumnName(), column);
						}
					}
				}
			}

			if (!Object.class.equals(entityType.getSuperclass())) {
				addColumns2Map(entityType.getSuperclass(), primaryKeyList,
						columnMap);
			}
		} catch (Exception e) {
			LogUtils.e(e.getMessage(), e);
		}
	}

	/**
	 * key: entityType.canonicalName
	 */
	private static ConcurrentHashMap<String, List<MyId>> entityIdMap = new ConcurrentHashMap<String, List<MyId>>();

	public static synchronized List<MyId> getId(Class<?> entityType)
			throws DbException {
		if (Object.class.equals(entityType)) {
			throw new RuntimeException("模块[" + entityType.getSimpleName()
					+ "] 没有发现");
		}

		if (entityIdMap.containsKey(entityType.getCanonicalName())) {
			return entityIdMap.get(entityType.getCanonicalName());
		}

		List<Field> primaryKeyField = new ArrayList<Field>();
		Field[] fields = entityType.getDeclaredFields();
		if (fields != null) {

			for (Field field : fields) {
				if (field.getAnnotation(Id.class) != null) {
					primaryKeyField.add(field);
				}
			}

			if (primaryKeyField.size() == 0) {
				for (Field field : fields) {
					if ("id".equals(field.getName())
							|| "_id".equals(field.getName())) {
						primaryKeyField.add(field);
					}
				}
			}
		}

		if (primaryKeyField.size() == 0) {
			return getId(entityType.getSuperclass());
		}
		List<MyId> idList = new ArrayList<MyId>();
		for (Iterator iterator = primaryKeyField.iterator(); iterator.hasNext();) {
			Field field = (Field) iterator.next();
			idList.add(new MyId(entityType, field));
		}
		entityIdMap.put(entityType.getCanonicalName(), idList);
		return idList;
	}

	private static List<String> getPrimaryKeyFieldName(Class<?> entityType)
			throws DbException {
		List<String> list = new ArrayList<String>();
		List<MyId> idList = getId(entityType);
		for (Iterator iterator = idList.iterator(); iterator.hasNext();) {
			MyId id = (MyId) iterator.next();
			if (id != null) {
				list.add(id.getColumnField().getName());
			}
		}
		return list;
	}

	private static List<String> getPrimaryKeyColumnName(Class<?> entityType)
			throws DbException {
		List<String> list = new ArrayList<String>();
		List<MyId> idList = getId(entityType);
		for (Iterator iterator = idList.iterator(); iterator.hasNext();) {
			MyId id = (MyId) iterator.next();
			if (id != null) {
				list.add(id.getColumnName());
			}
		}
		return list;
	}

	public static List<Object> getIdValue(Object entity) {
		if (entity == null)
			return null;

		try {
			List<MyId> id = getId(entity.getClass());
			if (id.isEmpty())
				return null;
			List<Object> oList = new ArrayList<Object>();
			for (Iterator iterator = id.iterator(); iterator.hasNext();) {
				MyId id2 = (MyId) iterator.next();
				Object idValue = id2.getColumnValue(entity);
				if (idValue != null && !idValue.equals(0)
						&& idValue.toString().length() > 0) {
					oList.add(idValue);
				}
			}
		} catch (Exception e) {
			LogUtils.e(e.getMessage(), e);
		}
		return null;
	}

}
