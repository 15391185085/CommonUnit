package com.lidroid.xutils.data;

import com.lidroid.xutils.exception.NullArgumentException;
import com.lidroid.xutils.util.FileUtil;

public class FileData {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	/**
	 * 得到存放异常日志的目录
	 * @return
	 * @throws NullArgumentException
	 */
	public static String getLogFilePath() throws NullArgumentException {
		String path = FileUtil.getSdcardPath() + "/com.ieds.gis.base/工程软件日志/";
		return path;
	}

}
