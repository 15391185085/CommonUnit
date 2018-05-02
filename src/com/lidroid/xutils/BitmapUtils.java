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

package com.lidroid.xutils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.BitmapGlobalConfig;
import com.lidroid.xutils.bitmap.callback.ImageLoadCallBack;
import com.lidroid.xutils.bitmap.download.Downloader;
import com.lidroid.xutils.exception.NullArgumentException;
import com.lidroid.xutils.util.core.CompatibleAsyncTask;
import com.lidroid.xutils.util.core.LruDiskCache;

public class BitmapUtils {

	private boolean pauseTask = false;
	private final Object pauseTaskLock = new Object();

	private Context context;
	private BitmapGlobalConfig globalConfig;
	private BitmapDisplayConfig defaultDisplayConfig;

	public BitmapUtils(Context context) {
		this(context, null);
	}

	public BitmapUtils(Context context, String diskCachePath) {
		this.context = context;
		globalConfig = new BitmapGlobalConfig(context, diskCachePath);
		defaultDisplayConfig = new BitmapDisplayConfig(context);
	}

	public BitmapUtils(Context context, String diskCachePath,
			int memoryCacheSize) {
		this(context, diskCachePath);
		globalConfig.setMemoryCacheSize(memoryCacheSize);
	}

	public BitmapUtils(Context context, String diskCachePath,
			int memoryCacheSize, int diskCacheSize) {
		this(context, diskCachePath);
		globalConfig.setMemoryCacheSize(memoryCacheSize);
		globalConfig.setDiskCacheSize(diskCacheSize);
	}

	public BitmapUtils(Context context, String diskCachePath,
			float memoryCachePercent) {
		this(context, diskCachePath);
		globalConfig.setMemCacheSizePercent(memoryCachePercent);
	}

	public BitmapUtils(Context context, String diskCachePath,
			float memoryCachePercent, int diskCacheSize) {
		this(context, diskCachePath);
		globalConfig.setMemCacheSizePercent(memoryCachePercent);
		globalConfig.setDiskCacheSize(diskCacheSize);
	}

	public BitmapUtils configDefaultLoadingImage(Bitmap bitmap) {
		defaultDisplayConfig.setLoadingBitmap(bitmap);
		return this;
	}

	public BitmapUtils configDefaultLoadingImage(int resId) {
		defaultDisplayConfig.setLoadingBitmap(BitmapFactory.decodeResource(
				context.getResources(), resId));
		return this;
	}

	public BitmapUtils configDefaultLoadFailedImage(Bitmap bitmap) {
		defaultDisplayConfig.setLoadFailedBitmap(bitmap);
		return this;
	}

	public BitmapUtils configDefaultLoadFailedImage(int resId) {
		defaultDisplayConfig.setLoadFailedBitmap(BitmapFactory.decodeResource(
				context.getResources(), resId));
		return this;
	}

	public BitmapUtils configDefaultBitmapMaxWidth(int bitmapWidth) {
		defaultDisplayConfig.setBitmapMaxWidth(bitmapWidth);
		return this;
	}

	public BitmapUtils configDefaultBitmapMaxHeight(int bitmapHeight) {
		defaultDisplayConfig.setBitmapMaxHeight(bitmapHeight);
		return this;
	}

	public BitmapUtils configDefaultImageLoadAnimation(Animation animation) {
		defaultDisplayConfig.setAnimation(animation);
		return this;
	}

	public BitmapUtils configDefaultImageLoadCallBack(
			ImageLoadCallBack imageLoadCallBack) {
		defaultDisplayConfig.setImageLoadCallBack(imageLoadCallBack);
		return this;
	}

	public BitmapUtils configDefaultShowOriginal(boolean showOriginal) {
		defaultDisplayConfig.setShowOriginal(showOriginal);
		return this;
	}

	public BitmapUtils configDefaultBitmapConfig(Bitmap.Config config) {
		defaultDisplayConfig.setBitmapConfig(config);
		return this;
	}

	public BitmapUtils configDefaultDisplayConfig(
			BitmapDisplayConfig displayConfig) {
		defaultDisplayConfig = displayConfig;
		return this;
	}

	public BitmapUtils configDownloader(Downloader downloader) {
		globalConfig.setDownloader(downloader);
		return this;
	}

	public BitmapUtils configDefaultCacheExpiry(long defaultExpiry) {
		globalConfig.setDefaultCacheExpiry(defaultExpiry);
		return this;
	}

	public BitmapUtils configThreadPoolSize(int poolSize) {
		globalConfig.setThreadPoolSize(poolSize);
		return this;
	}

	public BitmapUtils configMemoryCacheEnabled(boolean enabled) {
		globalConfig.setMemoryCacheEnabled(enabled);
		return this;
	}

	public BitmapUtils configDiskCacheEnabled(boolean enabled) {
		globalConfig.setDiskCacheEnabled(enabled);
		return this;
	}

	public BitmapUtils configDiskCacheFileNameGenerator(
			LruDiskCache.DiskCacheFileNameGenerator diskCacheFileNameGenerator) {
		globalConfig.setDiskCacheFileNameGenerator(diskCacheFileNameGenerator);
		return this;
	}

	public BitmapUtils configGlobalConfig(BitmapGlobalConfig globalConfig) {
		this.globalConfig = globalConfig;
		return this;
	}

	// //////////////////////// display ////////////////////////////////////

	public void display(ImageView imageView, String uri) {
		display(imageView, uri, null);
	}

	public void display(ImageView imageView, String uri,
			BitmapDisplayConfig displayConfig) {
		if (imageView == null) {
			return;
		}

		if (displayConfig == null) {
			displayConfig = defaultDisplayConfig;
		}

		if (TextUtils.isEmpty(uri)) {
			displayConfig.getImageLoadCallBack().loadFailed(imageView,
					displayConfig.getLoadFailedBitmap());
			return;
		}

		Bitmap bitmap = null;

		bitmap = globalConfig.getBitmapCache().getBitmapFromMemCache(uri,
				displayConfig);

		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
		} else if (!bitmapLoadTaskExist(imageView, uri)) {

			final BitmapLoadTask loadTask = new BitmapLoadTask(imageView,
					displayConfig);
			// set loading image
			final AsyncBitmapDrawable asyncBitmapDrawable = new AsyncBitmapDrawable(
					context.getResources(), displayConfig.getLoadingBitmap(),
					loadTask);
			imageView.setImageDrawable(asyncBitmapDrawable);

			// load bitmap from uri or diskCache
			loadTask.executeOnExecutor(globalConfig.getBitmapLoadExecutor(),
					uri);
		}
	}

	// ///////////////////////////////////////////// cache
	// /////////////////////////////////////////////////////////////////

	public void clearCache() {
		globalConfig.clearCache();
	}

	public void clearMemoryCache() {
		globalConfig.clearMemoryCache();
	}

	public void clearDiskCache() {
		globalConfig.clearDiskCache();
	}

	public void clearCache(String uri, BitmapDisplayConfig config) {
		if (config == null) {
			config = defaultDisplayConfig;
		}
		globalConfig.clearCache(uri, config);
	}

	public void clearMemoryCache(String uri, BitmapDisplayConfig config) {
		if (config == null) {
			config = defaultDisplayConfig;
		}
		globalConfig.clearMemoryCache(uri, config);
	}

	public void clearDiskCache(String uri) {
		globalConfig.clearDiskCache(uri);
	}

	public void flushCache() {
		globalConfig.flushCache();
	}

	public void closeCache() {
		globalConfig.closeCache();
	}

	public File getBitmapFileFromDiskCache(String uri) {
		return globalConfig.getBitmapCache().getBitmapFileFromDiskCache(uri);
	}

	public Bitmap getBitmapFromMemCache(String uri,
			BitmapDisplayConfig displayConfig) {
		return globalConfig.getBitmapCache().getBitmapFromMemCache(uri,
				displayConfig);
	}

	// //////////////////////////////////////// tasks
	// //////////////////////////////////////////////////////////////////////

	public void resumeTasks() {
		pauseTask = false;
		synchronized (pauseTaskLock) {
			pauseTaskLock.notifyAll();
		}
	}

	public void pauseTasks() {
		pauseTask = true;
		flushCache();
	}

	public void stopTasks() {
		pauseTask = true;
		synchronized (pauseTaskLock) {
			pauseTaskLock.notifyAll();
		}
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private static BitmapLoadTask getBitmapTaskFromImageView(ImageView imageView) {
		if (imageView != null) {
			final Drawable drawable = imageView.getDrawable();
			if (drawable instanceof AsyncBitmapDrawable) {
				final AsyncBitmapDrawable asyncBitmapDrawable = (AsyncBitmapDrawable) drawable;
				return asyncBitmapDrawable.getBitmapWorkerTask();
			}
		}
		return null;
	}

	private static boolean bitmapLoadTaskExist(ImageView imageView, String uri) {
		final BitmapLoadTask oldLoadTask = getBitmapTaskFromImageView(imageView);

		if (oldLoadTask != null) {
			final String oldUri = oldLoadTask.uri;
			if (TextUtils.isEmpty(oldUri) || !oldUri.equals(uri)) {
				oldLoadTask.cancel(true);
			} else {
				return true;
			}
		}
		return false;
	}

	private class AsyncBitmapDrawable extends BitmapDrawable {

		private final WeakReference<BitmapLoadTask> bitmapLoadTaskReference;

		public AsyncBitmapDrawable(Resources res, Bitmap bitmap,
				BitmapLoadTask bitmapWorkerTask) {
			super(res, bitmap);
			bitmapLoadTaskReference = new WeakReference<BitmapLoadTask>(
					bitmapWorkerTask);
		}

		public BitmapLoadTask getBitmapWorkerTask() {
			return bitmapLoadTaskReference.get();
		}
	}

	private class BitmapLoadTask extends
			CompatibleAsyncTask<Object, Void, Bitmap> {
		private String uri;
		private final WeakReference<ImageView> targetImageViewReference;
		private final BitmapDisplayConfig displayConfig;

		public BitmapLoadTask(ImageView imageView, BitmapDisplayConfig config) {
			targetImageViewReference = new WeakReference<ImageView>(imageView);
			displayConfig = config;
		}

		@Override
		protected Bitmap doInBackground(Object... params) {
			if (params != null && params.length > 0) {
				uri = (String) params[0];
			} else {
				return null;
			}
			Bitmap bitmap = null;

			synchronized (pauseTaskLock) {
				while (pauseTask && !this.isCancelled()) {
					try {
						pauseTaskLock.wait();
					} catch (InterruptedException e) {
					}
				}
			}

			// get cache from disk cache
			if (!this.isCancelled() && this.getTargetImageView() != null) {
				bitmap = globalConfig.getBitmapCache().getBitmapFromDiskCache(
						uri, displayConfig);
			}

			// download image
			if (bitmap == null && !this.isCancelled()
					&& this.getTargetImageView() != null) {
				bitmap = globalConfig.getBitmapCache().downloadBitmap(uri,
						displayConfig);
			}

			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			final ImageView imageView = this.getTargetImageView();
			if (imageView != null) {
				if (bitmap != null) {
					displayConfig.getImageLoadCallBack().loadCompleted(
							imageView, bitmap, displayConfig);
				} else {
					displayConfig.getImageLoadCallBack().loadFailed(imageView,
							displayConfig.getLoadFailedBitmap());
				}
			}
		}

		@Override
		protected void onCancelled(Bitmap bitmap) {
			super.onCancelled(bitmap);
			synchronized (pauseTaskLock) {
				pauseTaskLock.notifyAll();
			}
		}

		private ImageView getTargetImageView() {
			final ImageView imageView = targetImageViewReference.get();
			final BitmapLoadTask bitmapWorkerTask = getBitmapTaskFromImageView(imageView);

			if (this == bitmapWorkerTask) {
				return imageView;
			}

			return null;
		}
	}

	/**
	 * 得到视频的缩略图
	 * 
	 * @update 2014-7-9 上午8:46:01<br>
	 * @author <a href="mailto:lihaoxiang@ieds.com.cn">李昊翔</a>
	 * 
	 * @param videoPath
	 *            缩略图属于哪个视频,视频地址
	 * @param width
	 *            缩略图的宽
	 * @param height
	 *            缩略图的宽
	 * @param kind
	 *            视频的种类 来自Images.Thumbnails的属性
	 * @return
	 */
	public static Bitmap getVideoThumbnail(String videoPath, int width,
			int height, int kind) {
		Bitmap bitmap = null;

		bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);

		return bitmap;
	}

	// 将byte[]转换成InputStream
	public static InputStream Byte2InputStream(byte[] b) {
		ByteArrayInputStream bais = new ByteArrayInputStream(b);
		return bais;
	}

	// 将InputStream转换成byte[]
	public static byte[] InputStream2Bytes(InputStream is) {
		String str = "";
		byte[] readByte = new byte[1024];
		int readCount = -1;
		try {
			while ((readCount = is.read(readByte, 0, 1024)) != -1) {
				str += new String(readByte).trim();
			}
			return str.getBytes();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 将Bitmap转换成InputStream
	public static InputStream Bitmap2InputStream(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		InputStream is = new ByteArrayInputStream(baos.toByteArray());
		return is;
	}

	// 将Bitmap转换成InputStream
	public static InputStream Bitmap2InputStream(Bitmap bm, int quality) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, quality, baos);
		InputStream is = new ByteArrayInputStream(baos.toByteArray());
		return is;
	}

	// 将InputStream转换成Bitmap
	public static Bitmap InputStream2Bitmap(InputStream is) {
		return BitmapFactory.decodeStream(is);
	}

	// Drawable转换成InputStream
	public static InputStream Drawable2InputStream(Drawable d) {
		Bitmap bitmap = drawable2Bitmap(d);
		return Bitmap2InputStream(bitmap);
	}

	// InputStream转换成Drawable
	public static Drawable InputStream2Drawable(InputStream is) {
		Bitmap bitmap = InputStream2Bitmap(is);
		return bitmap2Drawable(bitmap);
	}

	// Drawable转换成byte[]
	public static byte[] Drawable2Bytes(Drawable d) {
		Bitmap bitmap = drawable2Bitmap(d);
		return Bitmap2Bytes(bitmap);
	}

	// byte[]转换成Drawable
	public static Drawable Bytes2Drawable(byte[] b) {
		Bitmap bitmap = Bytes2Bitmap(b);
		return bitmap2Drawable(bitmap);
	}

	// Bitmap转换成byte[]
	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	// byte[]转换成Bitmap
	public static Bitmap Bytes2Bitmap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		}
		return null;
	}

	// Drawable转换成Bitmap
	public static Bitmap drawable2Bitmap(Drawable drawable) {
		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * 得到固定尺寸的图片
	 * 
	 * @author 李昊翔
	 * @param tile
	 * @param w
	 * @param h
	 * @return
	 * @since JDK 1.6
	 */
	public static Bitmap createImage(Drawable tile, int w, int h) {
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		tile.setBounds(0, 0, w, h);
		tile.draw(canvas);
		return bitmap;
	}

	/**
	 * 得到固定尺寸的图片
	 * 
	 * @author 李昊翔
	 * @param tile
	 * @param w
	 * @param h
	 * @return
	 * @since JDK 1.6
	 */
	public static Bitmap createImage(Bitmap bit, int w, int h) {
		Drawable tile = bitmap2Drawable(bit);
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		tile.setBounds(0, 0, w, h);
		tile.draw(canvas);
		return bitmap;
	}

	// Bitmap转换成Drawable
	public static Drawable bitmap2Drawable(Bitmap bitmap) {
		BitmapDrawable bd = new BitmapDrawable(bitmap);
		Drawable d = (Drawable) bd;
		return d;
	}

	/**
	 * 旋转图片
	 * 
	 * @param bitmap
	 * @param angle
	 */
	public static Drawable angleBitmap(Drawable drawable, float angle,
			int newWidth, int newHeight) {
		Bitmap bitmap = drawable2Bitmap(drawable); // drawable 转换成 bitmap
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		Matrix matrix = new Matrix();
		float scaleop = scaleWidth > scaleHeight ? scaleHeight : scaleWidth;
		matrix.postRotate(angle);
		matrix.postScale(scaleop, scaleop);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true); // 建立新的 bitmap ，其内容是对原 bitmap 的缩放后的图
		return new BitmapDrawable(newbmp); // 把 bitmap 转换成 drawable 并返回

	}

	/**
	 * 旋转图片
	 * 
	 * @param bitmap
	 * @param angle
	 */
	public static Drawable angleBitmap(Drawable drawable, float angle) {
		Bitmap bitmap = drawable2Bitmap(drawable); // drawable 转换成 bitmap
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true); // 建立新的 bitmap ，其内容是对原 bitmap 的缩放后的图
		return new BitmapDrawable(newbmp); // 把 bitmap 转换成 drawable 并返回

	}

	public static Drawable zoomDrawable(Drawable drawable, int newWidth,
			int newHeight) {
		Bitmap bitmap = drawable2Bitmap(drawable); // drawable 转换成 bitmap
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		Matrix matrix = new Matrix();
		float scaleop = scaleWidth > scaleHeight ? scaleHeight : scaleWidth;
		matrix.postScale(scaleop, scaleop);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true); // 建立新的 bitmap ，其内容是对原 bitmap 的缩放后的图
		return new BitmapDrawable(newbmp); // 把 bitmap 转换成 drawable 并返回

	}

	/**
	 * 获取照片缩略图
	 * 
	 * @update 2014-7-9 下午2:34:59<br>
	 * @author <a href="mailto:lihaoxiang@ieds.com.cn">李昊翔</a>
	 * 
	 * @param Imagepath
	 *            图片的位置
	 * @param width
	 *            缩略图的宽
	 * @param height
	 *            缩略图的宽
	 * @return
	 */
	public static Bitmap getImagerThumbnail(String Imagepath, int width,
			int height) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		bitmap = BitmapFactory.decodeFile(Imagepath, options);
		options.inJustDecodeBounds = false;

		int h = options.outHeight;
		int w = options.outWidth;
		if (h < w && height > width) {
			h = options.outWidth;
			w = options.outHeight;
		}
		int beWidth = w / width;
		int beHeight = h / height;
		int be = 1;
		if (beWidth < beHeight) {
			be = beWidth;
		} else {
			be = beHeight;
		}
		if (be <= 0) {
			be = 1;
		}
		options.inSampleSize = be;

		bitmap = BitmapFactory.decodeFile(Imagepath, options);

		bitmap = rotateBitmapByDegree(bitmap, width, height,
				getBitmapDegree(Imagepath));
		return bitmap;
	}

	/**
	 * 将图片按照某个角度进行旋转
	 * 
	 * @param bm
	 *            需要旋转的图片
	 * @param degree
	 *            旋转角度
	 * @return 旋转后的图片
	 */
	public static Bitmap rotateBitmapByDegree(Bitmap bm, int width, int height,
			int degree) {
		Bitmap returnBm = null;

		// 根据旋转角度，生成旋转矩阵
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);
		try {
			// 将原始图片按照旋转矩阵进行旋转，并得到新的图片
			returnBm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
					true);
		} catch (OutOfMemoryError e) {
		}
		if (returnBm == null) {
			returnBm = bm;
		}
		if (bm != returnBm) {
			bm.recycle();
		}
		return returnBm;
	}

	private static int getBitmapDegree(String path) {
		int degree = 0;
		try {
			// 从指定路径下读取图片，并获取其EXIF信息
			ExifInterface exifInterface = new ExifInterface(path);
			// 获取图片的旋转信息
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * 将YUV420SP格式解码成Bitmap
	 * 
	 * @update 2014-7-9 下午2:34:20<br>
	 * @author <a href="mailto:lihaoxiang@ieds.com.cn">李昊翔</a>
	 * 
	 * @param yuv420sp
	 * @param width
	 * @param height
	 * @return
	 */
	public static final Bitmap getYUV420SPBitmap(byte[] yuv420sp, int width,
			int height) {
		final int decodeWidth = 320;
		final int decodeHeight = 240;

		Bitmap bitmap = Bitmap.createBitmap(
				decodeYUV420SP(yuv420sp, width, height), width, height,
				Bitmap.Config.ARGB_8888);

		if (width > decodeWidth || height > decodeHeight) {
			bitmap = scaleBitmap(bitmap, decodeWidth, decodeHeight);
		}

		return bitmap;
	}

	private static Bitmap scaleBitmap(Bitmap bitmap, int newWidth, int newHeight) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		if (scaleWidth >= 1 && scaleHeight >= 1) {
			return bitmap;
		}

		Matrix matrix = new Matrix();
		float scaleop = scaleWidth > scaleHeight ? scaleHeight : scaleWidth;

		matrix.postScale(scaleop, scaleop);

		return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
	}

	private static int[] decodeYUV420SP(byte[] yuv420sp, int width, int height)
			throws NullPointerException, IllegalArgumentException {
		final int frameSize = width * height;
		if (yuv420sp == null) {
			throw new NullPointerException("buffer yuv420sp is null");
		}

		if (yuv420sp.length < frameSize) {
			throw new IllegalArgumentException("buffer yuv420sp is illegal");
		}

		int[] rgb = new int[frameSize];

		for (int j = 0, yp = 0; j < height; j++) {
			int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
			for (int i = 0; i < width; i++, yp++) {
				int y = (0xff & ((int) yuv420sp[yp])) - 16;
				if (y < 0)
					y = 0;
				if ((i & 1) == 0) {
					v = (0xff & yuv420sp[uvp++]) - 128;
					u = (0xff & yuv420sp[uvp++]) - 128;
				}
				int y1192 = 1192 * y;
				int r = (y1192 + 1634 * v);
				int g = (y1192 - 833 * v - 400 * u);
				int b = (y1192 + 2066 * u);

				if (r < 0)
					r = 0;
				else if (r > 262143)
					r = 262143;
				if (g < 0)
					g = 0;
				else if (g > 262143)
					g = 262143;
				if (b < 0)
					b = 0;
				else if (b > 262143)
					b = 262143;

				rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000)
						| ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
			}
		}

		return rgb;
	}

	public static String red(String arg) {
		return "<font color=red>" + arg + "</font>";
	}

	public static String green(String arg) {
		return "<font color=blue>" + arg + "</font>";
	}

	public static Spanned getHtmlText(String arg) {
		return Html.fromHtml(arg);

	}
}
