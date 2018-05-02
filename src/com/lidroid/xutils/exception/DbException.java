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

package com.lidroid.xutils.exception;

import java.lang.reflect.InvocationTargetException;

public class DbException extends MyException {
	public static final String USB_ERROR = "读写失败，请断开USB连接，并退出系统重进?";

	public DbException() {
		super();
		message = USB_ERROR;
	}
 
	public DbException(String detailMessage, Throwable cause) {
		super(detailMessage, cause);
		// TODO Auto-generated constructor stub
	}

	public DbException(String detailMessage) {
		super(detailMessage);
		// TODO Auto-generated constructor stub
	}

	public DbException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
