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

import java.util.HashMap;
import java.util.List;

import com.lidroid.xutils.exception.DbException;


public class MyTable {

    private String tableName;

    private List<MyId> id;


    /**
     * key: columnName
     */
    public final HashMap<String, MyColumn> columnMap;

    /**
     * key: className
     */
    private static final HashMap<String, MyTable> tableMap = new HashMap<String, MyTable>();

    private MyTable(Class entityType) throws DbException {
        this.tableName = TableUtils.getTableName(entityType);
        this.id = TableUtils.getId(entityType);
        this.columnMap = TableUtils.getColumnMap(entityType);
    }

    public static synchronized MyTable get(Class entityType) throws DbException {

        MyTable table = tableMap.get(entityType.getCanonicalName());
        if (table == null) {
            table = new MyTable(entityType);
            tableMap.put(entityType.getCanonicalName(), table);
        }

        return table;
    }

    public String getTableName() {
        return tableName;
    }

    public List<MyId> getId() {
        return id;
    }
    private boolean checkDatabase;

    public boolean isCheckDatabase() {
        return checkDatabase;
    }

    public void setCheckDatabase(boolean checkDatabase) {
        this.checkDatabase = checkDatabase;
    }

}
