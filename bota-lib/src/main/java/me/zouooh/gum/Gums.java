package me.zouooh.gum;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Field;

/**
 * 胶水工具
 * 
 * @author zouooh
 * 
 */
public abstract class Gums {
	
	public static interface OnBindViewListener {
		/**
		 * 绑定完成
		 * @param bindView
		 */
		void onBinded(View bindView);
	}

	public static  <T> T findView(Activity activity,int resId){
		return  (T)activity.findViewById(resId);
	}

	public static  <T> T findView(View view,int resId){
		return  (T)view.findViewById(resId);
	}

	/**
	 * Gum注解找到view
	 * 
	 * @param contaner
	 * @param fromView
	 * @param onBindViewListener
	 */
	public static void bindViews(Object contaner, View fromView,
			OnBindViewListener onBindViewListener) {
		if (contaner == null) {
			return;
		}
		if (fromView == null) {
			if (!Activity.class.isAssignableFrom(contaner.getClass())) {
				return;
			}
		}
		Class<?> clazz = contaner.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			Gum gum = field.getAnnotation(Gum.class);
			if (gum != null) {
				int resId = gum.resId();
				if (resId > 0) {
					if (!field.isAccessible()) {
						field.setAccessible(true);
					}

					View sub = null;
					if (fromView != null) {
						sub = fromView.findViewById(resId);
					} else {
						sub = ((Activity) contaner).findViewById(resId);
					}
					if (sub == null || !field.getType().isAssignableFrom(sub.getClass()) ) {
						continue;
					}
					try {
						field.set(contaner, sub);
						if (onBindViewListener != null) {
							onBindViewListener.onBinded(sub);
						}
					} catch (Exception e) {
					}
				}
			}
		}
	}
}
