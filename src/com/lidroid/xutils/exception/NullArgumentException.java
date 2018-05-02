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

public class NullArgumentException extends MyException {
	public static final String USB_ERROR = "检查数据失败，所用参数不能为空！";

	public NullArgumentException() {
		super();
		message = USB_ERROR;
	}

	public NullArgumentException(String detailMessage) {
		super(detailMessage);
		// TODO Auto-generated constructor stub
	}


	public NullArgumentException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public NullArgumentException(String detailMessage, Throwable cause) {
		super(detailMessage, cause);
		// TODO Auto-generated constructor stub
	}



}
