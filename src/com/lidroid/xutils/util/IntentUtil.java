package com.lidroid.xutils.util;

import java.io.File;

import com.lidroid.xutils.exception.NullArgumentException;

import android.content.Intent;
import android.net.Uri;

/**
 * 本地系统工具交互
 * 
 * @author 李昊翔
 * 
 */
public class IntentUtil {
	public static Intent getFileInent(File f) throws NullArgumentException {
		String way = f
				.getName()
				.substring(f.getName().lastIndexOf(".") + 1,
						f.getName().length()).toLowerCase();
		Intent it = null;
		if (way.equals("xls") || way.equals("xlsx")) {
			it = getExcelFileIntent(f);// Excel
		} else if (way.equals("doc") || way.equals("docx")) {
			it = getWordFileIntent(f);// Word
		} else if (way.equals("txt")) {
			it = getTextFileIntent(f, false);// TXT
		} else if (way.equals("pdf")) {
			it = getPdfFileIntent(f);// pdf
		} else if (way.equals("ppt") || way.equals("pptx")) {
			it = getPptFileIntent(f);// PPT
		} else if (way.equals("apk")) {
			it = installApplySoftware(f);// apk
		} else {
			it = new Intent();
			it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			it.setAction(android.content.Intent.ACTION_VIEW);
			String type = getMIMEType(f.getName());
			it.setDataAndType(Uri.fromFile(f), type);
		}
		return it;
	}

	// android获取一个用于打开HTML文件的intent
	public static Intent getHtmlFileIntent(String param) {
		Uri uri = Uri.parse(param).buildUpon()
				.encodedAuthority("com.android.htmlfileprovider")
				.scheme("content").encodedPath(param).build();
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.setDataAndType(uri, "text/html");
		return intent;
	}

	// 调用系统浏览器
	public static Intent getSystemBrowser(String param) {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		Uri content_url = Uri.parse(param);
		intent.setData(content_url);
		return intent;
	}

	// android获取一个用于打开图片文件的intent
	public static Intent getImageFileIntent(String param) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "image/*");
		return intent;
	}

	// android获取一个用于打开PDF文件的intent
	public static Intent getPdfFileIntent(File f) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(f);
		intent.setDataAndType(uri, "application/pdf");
		return intent;
	}

	// android获取一个用于打开文本文件的intent
	public static Intent getTextFileIntent(File f, boolean paramBoolean) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (paramBoolean) {
			Uri uri1 = Uri.parse(f.getPath());
			intent.setDataAndType(uri1, "text/plain");
		} else {
			Uri uri2 = Uri.fromFile(f);
			intent.setDataAndType(uri2, "text/plain");
		}
		return intent;
	}

	// android获取一个用于打开音频文件的intent
	public static Intent getAudioFileIntent(String param) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("oneshot", 0);
		intent.putExtra("configchange", 0);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "audio/*");
		return intent;
	}

	// android获取一个用于打开视频文件的intent
	public static Intent getVideoFileIntent(String param) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("oneshot", 0);
		intent.putExtra("configchange", 0);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "video/*");
		return intent;
	}

	// android获取一个用于打开CHM文件的intent
	public static Intent getChmFileIntent(String param) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/x-chm");
		return intent;
	}

	// android获取一个用于打开Word文件的intent
	public static Intent getWordFileIntent(File f) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(f);
		intent.setDataAndType(uri, "application/msword");
		return intent;
	}

	// android获取一个用于打开Excel文件的intent
	public static Intent getExcelFileIntent(File f) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(f);
		intent.setDataAndType(uri, "application/vnd.ms-excel");
		return intent;
	}

	// android获取一个用于打开PPT文件的intent
	public static Intent getPptFileIntent(File f) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(f);
		intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
		return intent;
	}

	// 安装应用程序
	public static Intent installApplySoftware(File f) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(f);
		intent.setDataAndType(uri, "application/vnd.android.package-archive");
		return intent;
	}

	// android打开系统发送短信界面
	public static Intent getSendSmsIntent(String phoneNum, String content) {
		Uri uri = Uri.parse("smsto:" + phoneNum);
		Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
		intent.putExtra("sms_body", content);
		return intent;
	}

	// android打开打电话界面
	public static Intent getTelPanelIntent(String phoneNum) {
		Uri telUri = Uri.parse("tel:" + phoneNum);
		Intent intent = new Intent(Intent.ACTION_DIAL, telUri);
		return intent;
	}

	// android直接打出去
	public static Intent getTelIntent(String phoneNum) {
		Uri callUri = Uri.parse("tel:" + phoneNum);
		Intent intent = new Intent(Intent.ACTION_CALL, callUri);
		return intent;
	}

	/** 获取MIME类型 
	 * @throws NullArgumentException **/
	public static String getMIMEType(String name) throws NullArgumentException {
		if(name==null){
			throw new NullArgumentException();
		}
		String type = "";
		String end = name.substring(name.lastIndexOf(".") + 1, name.length())
				.toLowerCase();
		if (end.equals("apk")) {
			return "application/vnd.android.package-archive";
		} else if (end.equals("mp4") || end.equals("avi") || end.equals("3gp")
				|| end.equals("rmvb")) {
			type = "video";
		} else if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
				|| end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
			type = "audio";
		} else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
				|| end.equals("jpeg") || end.equals("bmp")) {
			type = "image";
		} else if (end.equals("txt") || end.equals("log")) {
			type = "text";
		} else {
			type = "*";
		}
		type += "/*";
		return type;
	}
}