package me.zouooh.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * app工具
 * 
 * @author zouooh
 * 
 */
public final class AppUtils {

	private AppUtils() {
	}
	public static boolean hasExternalCacheDir() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
	}

	@SuppressLint("NewApi")
	public static File getExternalCacheDir(Context context) {
		File file = null;
		if (hasExternalCacheDir()) {
			file = context.getExternalCacheDir();
		}
		if (file == null) {
			final String cacheDir = "/Android/data/" + context.getPackageName()
					+ "/cache/";
			file = new File(Environment.getExternalStorageDirectory().getPath()
					+ cacheDir);
		}
		return file;
	}


	public static int getMemoryClass(Context context) {
		return ((ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
	}

	/**
	 * WiFi网络是否有效
	 * 
	 * @param context
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static boolean isWifiAvailable(Context context) {
		ConnectivityManager mConnMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = mConnMgr
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		boolean flag = false;
		if (mWifi != null && (mWifi.isAvailable())) {
			if (mWifi.isConnected()) {
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 当前活动的网络
	 * 
	 * @param cotext
	 * @return
	 */
	public static NetworkInfo activeNetworkInfo(Context cotext) {
		ConnectivityManager connectivityManager = (ConnectivityManager) cotext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager != null) {
			NetworkInfo activeNetworkInfo = connectivityManager
					.getActiveNetworkInfo();
			if (activeNetworkInfo != null && activeNetworkInfo.isAvailable()
					&& activeNetworkInfo.isConnected()) {
				return activeNetworkInfo;
			}
		}
		return null;
	}

	public static PackageInfo packageInfo(Context context) {
		PackageInfo nPackageInfo = null;
		try {
			nPackageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(),
							PackageManager.GET_CONFIGURATIONS);
		} catch (NameNotFoundException e) {
		}
		return nPackageInfo;
	}
	
	public static String androidVersionString(){
		return "android "+ Build.VERSION.RELEASE;
	}
	public static String androidPhoneTypeString(){
		return Build.BRAND+" "+ Build.MODEL;
	}
	
	
	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static int getScreenWidth(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(metrics);
		return metrics.widthPixels;
	}
	
	public static int getWidthOfScreen(Context context,int diff,int count) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(metrics);
		return (metrics.widthPixels-dip2px(context, diff))/count;
	}

	public static int getScreenHeight(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(metrics);
		return metrics.heightPixels;
	}

	public static Object deviceId(Context context) {
		final TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		final String tmDevice, tmSerial, androidId;
		tmDevice = "" + tm.getDeviceId();
		tmSerial = "" + tm.getSimSerialNumber();
		androidId = ""
				+ Secure.getString(context.getContentResolver(),
						Secure.ANDROID_ID);
		UUID deviceUuid = new UUID(androidId.hashCode(),
				((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
		String uniqueId = deviceUuid.toString();
		return uniqueId;
	}
	
	
	public static void toast(Context context,String msg){
		toast(context,msg,Gravity.CENTER);
	}
	public static void toast(Context context,String msg,int gravity){
		Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		toast.setGravity(gravity, 0, 0);
		toast.show();
	}
}
