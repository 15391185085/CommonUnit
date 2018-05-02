package com.lidroid.xutils.util;

import java.io.Serializable;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 简单调用存储类
 */
public abstract class PreferenceUtil {

	private SharedPreferences sp;
	protected Context mApp;

	public PreferenceUtil(String name, Context app) {
		this.mApp = app;
		sp = mApp.getSharedPreferences(name, 0);
	}

	public Editor edit() {
		return sp.edit();
	}

	public void putSerializable(String key, Object value) {
		try {
			Editor ed = sp.edit();
			if (value == null) {
				ed.putString(key, "");
			} else {
				ed.putString(key, SerializableUtil.SerToBase64(value));
			}
			ed.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Object getSerializable(String key) {
		try {
			String str = sp.getString(key, "");
			if (str.equals("")) {
				return null;
			}
			return SerializableUtil.base64ToSer(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void putBool(String key, boolean value) {
		Editor ed = sp.edit();
		ed.putBoolean(key, value);
		ed.commit();
	}

	public boolean getBool(String key, boolean defValue) {
		return sp.getBoolean(key, defValue);
	}

	public void putInt(String key, int value) {
		Editor ed = sp.edit();
		ed.putInt(key, value);
		ed.commit();
	}

	public int getInt(String key, int defValue) {
		return sp.getInt(key, defValue);
	}

	public void putLong(String key, long value) {
		Editor ed = sp.edit();
		ed.putLong(key, value);
		ed.commit();
	}

	public long getLong(String key, long defValue) {
		return sp.getLong(key, defValue);
	}

	public void putString(String key, String value) {
		Editor ed = sp.edit();
		ed.putString(key, value);
		ed.commit();
	}

	public String getString(String key, String defValue) {
		return sp.getString(key, defValue);
	}

}
