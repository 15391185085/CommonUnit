package com.lidroid.xutils.exception;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import com.lidroid.xutils.data.FileData;
import com.lidroid.xutils.util.DateUtil;
import com.lidroid.xutils.util.FileUtil;

public class MyLog {
	/**
	 * 是否写日志到本地
	 */
	public static boolean isWrite = false;

	public static void setLogText(String message) {
		if (!isWrite) {
			return;
		}
		try {
			String path = FileData.getLogFilePath();
			int day = DateUtil.getDay(new Date());
			File f = new File(path + (day + 1) + ".txt");
			if (f.isFile()) {
				// 删除明天的记录
				f.delete();
			} else {
				// 明天的记录不存在
				f = new File(path + 1 + ".txt");
				if (f.isFile()) {
					// 删除当月第一天
					f.delete();
				}
			}
			f = new File(path + day + ".txt");
			FileUtil.createDirFile(f.getParent());
			FileWriter fileWrite = new FileWriter(f, true);
			fileWrite.write(message+"\n\n");
			if (fileWrite != null) {
				fileWrite.flush();
				fileWrite.close();
			}
		} catch (Exception e) {
		}
	}
}
