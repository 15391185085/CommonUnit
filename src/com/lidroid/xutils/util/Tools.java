package com.lidroid.xutils.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.UUID;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * 
 * @Description:全局常量及常用方法
 * @see:
 */
public class Tools {

	private static TelephonyManager teleManager;

	private Tools() {
	}

	/**
	 * 得到一个32位随机数
	 * 
	 * @return String
	 */
	public static String getUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replace("-", "").toUpperCase();
	}

	/**
	 * 返回去除下列字符的字符串
	 * \ / \s : * ? \ " < > |
	 * 
	 * @param name
	 * @return
	 */
	public static String getFileName(String temp) {
		return temp.replaceAll("[\\\\/\\s:*?\"<>|]", "");
	}

	/**
	 * 判断是否连接到网络
	 * 
	 * @Description:
	 * @param context
	 * @param showDialog
	 *            if true show the dialog else show toast
	 * @return true表示有网络，false表示无网络
	 */
	public static boolean isNetworkConnected(final Context context,
			boolean showDialog) {
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connManager.getActiveNetworkInfo() != null) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是否连接到网络
	 * 
	 * @Description:
	 * @param context
	 * @return true表示有网络，false表示无网络
	 * @see:
	 */
	public static boolean isNetworkConnected(final Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connManager.getActiveNetworkInfo() != null) {
			return true;
		}
		return false;
	}

	/**
	 * 使用32位MD5加密算法进行加密
	 * 
	 * @Description:
	 * @param text
	 *            要加密的字符串
	 * @return 加密后字符串
	 * @throws NoSuchAlgorithmException
	 * @see:
	 * @since:
	 */
	public static String md5Encrypt(String text)
			throws NoSuchAlgorithmException {
		// 空串就不用加密了
		if (text == null) {
			return text;
		}
		MessageDigest md5 = MessageDigest.getInstance("md5");
		char[] charArr = text.toCharArray();
		byte[] byteArr = new byte[charArr.length];
		for (int i = 0; i < charArr.length; i++) {
			byteArr[i] = (byte) charArr[i];
		}
		byte[] md5Bytes = md5.digest(byteArr);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16) {
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}

	/**
	 * 
	 * @Description: dip 到 px 的转换
	 * @param context
	 * @param dipValue
	 */
	public static int dipToPx(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/***
	 * 
	 * @Description:软键盘显示时被成功隐藏 返回true
	 * @param context
	 * @return
	 */
	public static void hideSoftInput(Activity context) {
		InputMethodManager inputMethodManager = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (inputMethodManager != null && context.getCurrentFocus() != null
				&& context.getCurrentFocus().getWindowToken() != null) {
			inputMethodManager.hideSoftInputFromWindow(context
					.getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	/**
	 * 
	 * @Description:切换显示输入法
	 * @param context
	 * @see:
	 */
	public static void toggleSoftInput(Context context) {
		InputMethodManager inputMethodManager = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.toggleSoftInput(0,
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

	public static void hideSoftInput(Context context, IBinder binder) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(binder, 0);

	}

	/**
	 * 
	 * @Description: 判断一个是否为空 如果为空返回默认值
	 * @param value
	 *            检测的值
	 * @param defaultValue
	 *            返回的默认值
	 * @return
	 * @see:
	 */
	public static String isBank(String value, String defaultValue) {
		return TextUtils.isEmpty(value) ? defaultValue : value;
	}

	/**
	 * 
	 * @Description: px 到 dip的转换
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int pxToDip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 比较两个数大小
	 */
	public static int getBiggerInt(int x, int y) {
		return x > y ? x : y;
	}

	/**
	 * 
	 * @Description: 判断两个集合中数据是否完全一致
	 * @param <T>
	 * @param c1
	 * @param c2
	 * @see:
	 */
	public static <T> boolean isFullMatch(Collection<T> c1, Collection<T> c2) {
		if (c1 == c2) {
			return true;
		}
		if (c1 == null || c2 == null) {
			return false;
		}
		if (c1.size() != c2.size()) {
			return false;
		}
		return c1.containsAll(c2);
	}

	/***
	 * 
	 * @Description: 回收ImagView图形
	 * @param imageView
	 * @see:
	 */
	public static void recycleImageViewBitmap(ImageView imageView) {

		if (imageView == null) {
			return;
		}

		BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
		if (drawable != null) {
			Bitmap bitmap = drawable.getBitmap();
			if (bitmap != null) {
				imageView.setImageBitmap(null);
				bitmap.recycle();
				bitmap = null;
			}

		}
	}

	/**
	 * 
	 * @Description: 为listview 设置最大高度
	 * @param listView
	 *            目标listview
	 * @param maxChildCount
	 *            最大显示的条目数量
	 * @see:
	 * @since:
	 */
	public static void setListViewHeightWithMaxChildCount(ListView listView,
			int maxChildCount) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		// 如果listview现在显示的条目数量小于 指定maxChildCount条目数 高度设置为自适应
		if (listAdapter.getCount() <= maxChildCount) {
			ViewGroup.LayoutParams params = listView.getLayoutParams();
			params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
			listView.setLayoutParams(params);
			return;
		}

		// listview 现在的总条目数量大于指定的条目数量 则设置高度
		int totalHeight = 0;
		// 计算出listivew高度 根据最大条目数量
		for (int i = 0; i < maxChildCount; i++) {
			View listItem = listAdapter.getView(i, null, listView);// 得到item的高度
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		// 设置listivew高度
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * maxChildCount);
		listView.setLayoutParams(params);
	}

	/**
	 * 唯一的设备ID： GSM手机的 IMEI 和 CDMA手机的 MEID
	 * 
	 * @Description:
	 * @return
	 */
	public static String getIMEI(Context context) {
		if (teleManager == null) {
			teleManager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
		}
		return teleManager.getDeviceId();
	}

	public static String getCallNum(Context context) {
		if (teleManager == null) {
			teleManager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
		}
		return teleManager.getLine1Number();
	}

	/**
	 * 获取版本号
	 * 
	 * @throws NameNotFoundException
	 * 
	 * @Description:
	 */
	public static String getVersion(Context context)
			throws NameNotFoundException {
		String version = "";
		PackageInfo info = context.getPackageManager().getPackageInfo(
				context.getPackageName(), 0);
		version = info.versionName;
		if (version == null || version.length() <= 0) {
			return "";
		}
		return version;
	}

	/**
	 * 获取version code
	 * 
	 * @Description:
	 * @return
	 * @throws NameNotFoundException
	 */
	public static int getVersionCode(Context context)
			throws NameNotFoundException {
		return context.getPackageManager().getPackageInfo(
				context.getPackageName(), 0).versionCode;
	}

	/**
	 * 半角转全角
	 * 
	 * @param input
	 *            String.
	 * @return 全角字符串.
	 */
	public static String ToSBC(String input) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == ' ') {
				c[i] = '\u3000';
			} else if (c[i] < '\177') {
				c[i] = (char) (c[i] + 65248);

			}
		}
		return new String(c);
	}

	/**
	 * 全角转半角
	 * 
	 * @param input
	 *            String.
	 * @return 半角字符串
	 */
	public static String ToDBC(String input) {

		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == '\u3000') {
				c[i] = ' ';
			} else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
				c[i] = (char) (c[i] - 65248);

			}
		}
		String returnString = new String(c);

		return returnString;
	}
}
