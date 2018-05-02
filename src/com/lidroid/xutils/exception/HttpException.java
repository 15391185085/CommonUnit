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

import android.annotation.SuppressLint;
import java.io.IOException;

@SuppressLint("NewApi")
public class HttpException extends MyException {
	public static final String NET_ERROR = "网络连接失败，请稍后再试!";


	public HttpException(String detailMessage, Throwable cause) {
		super(detailMessage, cause);
		// TODO Auto-generated constructor stub
	}


	public HttpException(String detailMessage) {
		super(detailMessage);
		// TODO Auto-generated constructor stub
	}


	public HttpException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public HttpException(int code, String detailMessage) {
		super(code, detailMessage);
		// TODO Auto-generated constructor stub
	}



	public HttpException() {
		super();
		message = NET_ERROR;
	}


}
