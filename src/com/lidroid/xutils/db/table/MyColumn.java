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

import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MyColumn {

	protected String columnName;
	private Object defaultValue;

	protected Method getMethod;
	protected Method setMethod;

	protected Field columnField;

	protected MyColumn(Class entityType, Field field) throws DbException {
		this.columnField = field;
		this.columnName = field.getName();
		this.defaultValue = ColumnUtils.getColumnDefaultValue(field);
		this.getMethod = ColumnUtils.getColumnGetMethod(entityType, field);
		this.setMethod = ColumnUtils.getColumnSetMethod(entityType, field);
	}

	@SuppressWarnings("unchecked")
	public void setValue2Entity(Object entity, String valueStr)
			throws DbException {

		Object value = null;
		if (valueStr != null) {
			value = ColumnUtils.valueStrSimpleTypeFieldValue(columnField,
					valueStr);
		}

		if (setMethod != null) {
			try {
				setMethod.invoke(entity, value == null ? defaultValue : value);
			} catch (Exception e) {
				throw new DbException(e.getMessage());
			}
		} else {
			try {
				this.columnField.setAccessible(true);
				this.columnField.set(entity, value == null ? defaultValue
						: value);
			} catch (Exception e) {
				throw new DbException(e.getMessage());
			}
		}
	}

	@SuppressWarnings("unchecked")
	public Object getColumnValue(Object entity) throws DbException {
		Object resultObj = null;
		if (entity != null) {
			if (getMethod != null) {
				try {
					resultObj = getMethod.invoke(entity);
				} catch (Exception e) {
					throw new DbException(e.getMessage());
				}
			} else {
				try {
					this.columnField.setAccessible(true);
					resultObj = this.columnField.get(entity);
				} catch (Exception e) {
					throw new DbException(e.getMessage());
				}
			}
		}
		return ColumnUtils.convert2DbColumnValueIfNeeded(resultObj);
	}

	public String getColumnName() {
		return columnName;
	}

	public Object getDefaultValue() {
		return defaultValue;
	}

	public Field getColumnField() {
		return columnField;
	}

	public String getColumnDbType() {
		return ColumnUtils.fieldType2DbType(columnField.getType());
	}
}
