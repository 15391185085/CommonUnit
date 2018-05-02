package com.lidroid.xutils.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.NullArgumentException;

/**
 * 文件操作类，复制、删除等操作
 * 
 * @time 2013-03-14 22:35
 * @author 王庆磊
 * 
 */
public class FileUtil {
	public static final int UPDATE_SIZE = 1 * 1024 * 1024; // the size of buffer
	public static final String MEDIA_MOUNTED_ERROR = "没有发现可用磁盘，请检查数据是否断开，或磁盘是否加载！";
	// in
	public static final String TAG = "FileUpload";

	/**
	 * 判断SD是否可以
	 * 
	 * @return
	 */
	public static boolean isSdcardExist() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		}
		return false;
	}

	/**
	 * 创建该地址下（包括该地址所在）的所有目录
	 * 
	 * @param path
	 *            目录路径
	 */
	public static void createDirFile(String path) {
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
			Log.d(TAG, path);
		}
	}

	/**
	 * 创建文件
	 * 
	 * @param path
	 *            文件路径
	 * @return 创建的文件
	 * @throws IOException
	 */
	public static File createNewFile(String path) throws IOException {
		File file = new File(path);
		if (!file.exists()) {
			file.createNewFile();
		}
		return file;
	}

	/**
	 * 删除文件夹
	 * 
	 * @param folderPath
	 *            文件夹的路径
	 */
	public static void delFolder(String folderPath) {
		delAllFile(folderPath);
		String filePath = folderPath;
		filePath = filePath.toString();
		java.io.File myFilePath = new java.io.File(filePath);
		myFilePath.delete();
	}

	/**
	 * 删除文件
	 * 
	 * @param path
	 *            文件的路径
	 */
	public static void delAllFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		String[] tempList = file.list();
		File temp = null;
		int mLength = tempList.length;
		for (int i = 0; i < mLength; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);
				delFolder(path + "/" + tempList[i]);
			}
		}
	}

	/**
	 * 获取文件的Uri
	 * 
	 * @param path
	 *            文件的路径
	 * @return
	 */
	public static Uri getUriFromFile(String path) {
		File file = new File(path);
		return Uri.fromFile(file);
	}

	/**
	 * 换算文件大小
	 * 
	 * @param size
	 * @return
	 */
	public static String formatFileSize(long size) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "未知大小";
		if (size < 1024) {
			fileSizeString = df.format((double) size) + "B";
		} else if (size < 1048576) {
			fileSizeString = df.format((double) size / 1024) + "K";
		} else if (size < 1073741824) {
			fileSizeString = df.format((double) size / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) size / 1073741824) + "G";
		}
		return fileSizeString;
	}

	/**
	 * 通过路径获得文件名字
	 * 
	 * @param path
	 * @return
	 */
	public static String getNameByPath(String path) {
		return path.substring(path.lastIndexOf(File.separator) + 1);
	}

	/**
	 * 获得SD卡路径
	 * 
	 * @param
	 * @return String
	 * @throws DbException
	 * @throws NullArgumentException
	 */
	public static String getSdcardPath() throws NullArgumentException {
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		if (!sdCardExist) {
			throw new NullArgumentException(MEDIA_MOUNTED_ERROR);
		}
		if (android.os.Build.MODEL.equals("BOB T8")) { // 特殊机型
			return android.os.Environment.getExternalStorageDirectory()
					.getAbsolutePath() + 2;
		} else {
			return android.os.Environment.getExternalStorageDirectory()
					.getAbsolutePath();
		}

	}

	/**
	 * 写文件
	 */
	public static final void writeString(String write, File writeFile)
			throws IOException {
		PrintWriter os = null;
		try {
			// 写入
			os = new PrintWriter(writeFile);
			os.write(write);
			os.flush();
		} finally {
			if (os != null) {
				os.close();
			}
		}
	}

	/**
	 * 从assets目录下copy文件去data下
	 * 
	 * @param context
	 * @param writeName
	 *            文件名
	 * @throws IOException
	 */
	public static final void writeAssetsToData(Context context, String writeName)
			throws IOException {
		InputStream is = null;
		FileOutputStream os = null;
		try {
			// 读取
			is = context.getApplicationContext().getResources().getAssets()
					.open(writeName);
			// 写入
			os = context.openFileOutput(writeName, Activity.MODE_PRIVATE);

			byte[] buffer = new byte[UPDATE_SIZE];
			int length = 0;
			while ((length = is.read(buffer)) != -1) {
				os.write(buffer, 0, length);
			}
			os.flush();
		} finally {
			if (os != null) {
				os.close();
			}
			if (is != null) {
				is.close();
			}
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param filePath
	 *            文件路径
	 */
	public static void deleteFile(String filePath) throws IOException {
		File dir = new File(filePath);
		if (dir.isFile() && !dir.delete()) {
			throw new IOException("failed to delete " + dir);
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param filePath
	 *            文件路径
	 */
	public static void deleteFile(File dir) throws IOException {
		if (dir.isFile() && !dir.delete()) {
			throw new IOException("failed to delete " + dir);
		}
	}

	/**
	 * 获取sdcard指定目录
	 * 
	 * @return
	 */
	public static final String getLib(Context con) {
		return android.os.Environment.getDataDirectory().getAbsolutePath()
				+ "/data/" + con.getPackageName() + "/lib";

	}

	/**
	 * 得到资源文件, 没有文件夹创建，有同名文件删除，返回需要的文件地址
	 * 
	 * @author 李昊翔
	 * @param filePath
	 *            文件夹
	 * @param fileName
	 *            文件
	 * @return
	 * @since JDK 1.6
	 */
	public static File getResFile(String filePath, String fileName)
			throws IOException {
		File out = new File(filePath);
		if (!out.exists()) {
			out.mkdirs();
		}
		out = new File(filePath, fileName);
		deleteFile(out);
		return out;
	}

	/**
	 * 从assets目录下的文件copy去sd卡
	 * 
	 * @param context
	 * @param fileParent
	 *            文件夹路径
	 * @param fileName
	 *            Assets中的文件名，需要和拷贝后的文件同名
	 * @param force
	 *            是否替换已有
	 * @throws IOException
	 */
	public static final void writeAssetsFileToSDCard(Context context,
			File dbSDCardFile, String fileName, boolean force) throws Exception {
		// 读取
		InputStream is = null;
		try {
			// 删除同名文件
			is = context.getApplicationContext().getResources().getAssets()
					.open(fileName);
			writeFileTOSDCard(dbSDCardFile, is, force);
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}

	/**
	 * 写文件到指定位置
	 * 
	 * @update 2014-7-17 下午2:02:28<br>
	 * @author <a href="mailto:lihaoxiang@ieds.com.cn">李昊翔</a>
	 * 
	 * @param bitmap
	 * @param path
	 *            写到哪，文件全名
	 * @param force
	 * @return
	 */
	public static void writeBitmap(Bitmap bitmap, File path) throws IOException {
		// 不存在文件夹时，创建sdcard对应的文件夹
		FileUtil.createDirFile(path.getParent());
		// 删除同名文件
		deleteFile(path);
		FileOutputStream os = null;
		try {
			os = new FileOutputStream(path);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
			os.flush();
		} finally {
			if (os != null) {
				os.close();
			}
		}
	}

	public static void copyFile(File sourceFile, File targetFile)
			throws IOException {
		FileInputStream input = null;
		try {
			input = new FileInputStream(sourceFile);
			writeFileTOSDCard(targetFile, input, true);
		} finally {
			if (input != null) {
				input.close();
			}
		}
	}

	private static void writeFileTOSDCard(File dbSDCardFile, InputStream is,
			boolean force) throws IOException {
		// 不存在文件夹时，创建sdcard对应的文件夹
		FileUtil.createDirFile(dbSDCardFile.getParent());

		// 需要下载的文件
		if (force) {
			// 删除同名文件
			deleteFile(dbSDCardFile);
		} else {
			if (dbSDCardFile.exists()) {
				// 文件已存在
				return;
			}
		}
		FileOutputStream os = null;
		try {
			os = new FileOutputStream(dbSDCardFile);
			byte[] buffer = new byte[UPDATE_SIZE];
			int length = 0;
			while ((length = is.read(buffer)) != -1) {
				os.write(buffer, 0, length);
			}
			os.flush();
		} finally {
			if (os != null) {
				os.close();
			}
		}
	}

	/**
	 * 出发扫描 mtp下的文件，在保存文件到 sd卡下后，不能显示，故这里触发一下扫描机制，让手机连上电脑后，就可以读出文件了
	 * 
	 * @param fName
	 *            ，文件的完整路径名
	 */
	public static void fileScan(Activity act, String fName) {
		Uri data = Uri.parse("file:///" + fName);
		act.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
				data));
	}

	/**
	 * 把一个文件转化为字节
	 * 
	 * @param file
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] getByte(File file) throws Exception {
		byte[] bytes = null;
		if (file != null) {
			InputStream is = null;
			try {
				is = new FileInputStream(file);
				int length = (int) file.length();
				if (length > Integer.MAX_VALUE) // 当文件的长度超过了int的最大值
				{
					System.out.println("this file is max ");
					return null;
				}
				bytes = new byte[length];
				int offset = 0;
				int numRead = 0;
				while (offset < bytes.length
						&& (numRead = is.read(bytes, offset, bytes.length
								- offset)) >= 0) {
					offset += numRead;
				}
				// 如果得到的字节长度和file实际的长度不一致就可能出错了
				if (offset < bytes.length) {
					System.out.println("file length is error");
					return null;
				}
			} finally {
				if (is != null) {
					is.close();
				}
			}
		}
		return bytes;
	}

	/**
	 * 写入字符串到文件
	 * 
	 * @param dataName
	 *            文件名
	 * @param args
	 *            要写入的字符串
	 * @throws IOException
	 * @throws NullArgumentException
	 */
	public static void writeFileToData(Context context, String dataName,
			String args) throws IOException, NullArgumentException {
		if (args == null) {
			throw new NullArgumentException();
		}
		OutputStream out = null;
		try {

			byte[] bytes = args.getBytes();

			for (int i = 0; i < bytes.length; ++i) {
				if (bytes[i] < 0) {// 调整异常数据
					bytes[i] += 256;
				}
			}
			out = new FileOutputStream(new File(FileUtil.getSdcardPath() + "/"
					+ dataName));

			out.write(bytes);
			out.flush();

		} finally {
			if (out != null) {
				out.close();
			}
		}

	}

	/**
	 * @see 得到父目录下的所有子目录
	 * @return false 装入失败
	 */
	public static File[] getChildFiles(File fatherFileDir) {
		if (fatherFileDir.exists() && fatherFileDir.isDirectory()) {
			return fatherFileDir.listFiles();

		}
		return null;
	}

	/**
	 * 清空文件夹路径下所有
	 * 
	 * @param directory
	 *            (文件路径)
	 * @throws IOException
	 * @return void
	 */
	public static void cleanDirectory(File directory) throws IOException {
		if (!directory.exists()) {
			String message = directory + " does not exist";
			throw new IllegalArgumentException(message);
		}

		if (!directory.isDirectory()) {
			String message = directory + " is not a directory";
			throw new IllegalArgumentException(message);
		}

		File[] files = directory.listFiles();
		if (files == null) { // null if security restricted
			throw new IOException("Failed to list contents of " + directory);
		}

		IOException exception = null;
		for (File file : files) {
			forceDelete(file);
		}
	}

	/**
	 * 强制删除文件，当删除不掉时，向上查找父文件夹删除
	 * 
	 * @param File
	 *            file (文件路径)
	 * @throws IOException
	 * @return void
	 */
	public static void forceDelete(File file) throws IOException {
		if (file.isDirectory()) {
			deleteDirectory(file);
		} else {
			boolean filePresent = file.exists();
			if (!file.delete()) {
				if (!filePresent) {
					throw new FileNotFoundException("File does not exist: "
							+ file);
				}
				String message = "Unable to delete file: " + file;
				throw new IOException(message);
			}
		}
	}

	/**
	 * 删除文件目录
	 * 
	 * @param File
	 *            directory (文件路径)
	 * @throws IOException
	 * @return void
	 */
	public static void deleteDirectory(File directory) throws IOException {
		if (!directory.exists()) {
			return;
		}

		if (!isSymlink(directory)) {
			cleanDirectory(directory);
		}

		if (!directory.delete()) {
			String message = "Unable to delete directory " + directory + ".";
			throw new IOException(message);
		}
	}

	/**
	 * 删除文件目录
	 * 
	 * @param File
	 *            directory (文件路径)
	 * @throws IOException
	 * @return void
	 */
	public static void deleteDirectory(String path) throws IOException {
		deleteDirectory(new File(path));
	}

	/**
	 * 判断文件目录下是否存在系统文件或者目录
	 * 
	 * @param File
	 *            file (文件路径)
	 * @throws IOException
	 * @return boolean true 不存在系统关联文件，false 有
	 */
	public static boolean isSymlink(File file) throws IOException {
		if (file == null) {
			throw new NullPointerException("File must not be null");
		}
		File fileInCanonicalDir = null;
		if (file.getParent() == null) {
			fileInCanonicalDir = file;
		} else {
			File canonicalDir = file.getParentFile().getCanonicalFile();
			fileInCanonicalDir = new File(canonicalDir, file.getName());
		}

		if (fileInCanonicalDir.getCanonicalFile().equals(
				fileInCanonicalDir.getAbsoluteFile())) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 得到data/data下面私有file的路径
	 * 
	 * @param context
	 * @return
	 */
	public static String getDataFilderPath(Context context) {
		return android.os.Environment.getDataDirectory() + "/data/"
				+ context.getPackageName() + "/files";

	}

	public static String readFile(File f) throws Exception {
		// 读取
		InputStream is = new FileInputStream(f);

		return readFile(is);
	}

	private static String readFile(InputStream is) throws Exception {
		try {
			StringBuffer sb = new StringBuffer();

			byte[] buffer = new byte[UPDATE_SIZE];
			int length = 0;
			while ((length = is.read(buffer)) != -1) {
				sb.append(new String(buffer, 0, length));
			}
			return sb.toString();
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}

	/**
	 * 把assets中的文件转成String
	 * 
	 * @param assetsFileName
	 * @return
	 */
	public static String readFile(Context context, String assetsFileName)
			throws Exception {
		InputStream is = context.getApplicationContext().getResources()
				.getAssets().open(assetsFileName);
		return readFile(is);
	}
}
