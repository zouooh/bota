package me.zouooh.bota.http;

import android.content.Context;
import android.content.SharedPreferences;

import org.nutz.lang.Strings;

/**
 * Created by zouooh on 2016/8/16.
 */
public class SidStore {

    private static final String COOKIE_PREFS = "_burro_cookies_";
    private static final String COOKIE = "_cookie_";

    private Context context;

    public SidStore(Context context) {
        this.context = context;
        if (context == null) {
            return;
        }
    }

    public void save(String host, String cookies) {
        if (Strings.isBlank(cookies)||context == null) {
            return;
        }
        if (Strings.isBlank(host)) {
            return;
        }
        String[] cs = cookies.split(";");
        cookies = null;
        for (String string : cs) {
            if (string.contains("=")) {
                if (string.split("=")[0].equalsIgnoreCase("sid")) {
                    cookies = string;
                    break;
                }
            }
        }
        if (cookies == null) {
            return;
        }
        if (cookies.contains("deleteMe")) {
            SharedPreferences cookiePrefs = context.getSharedPreferences(
                    COOKIE_PREFS, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = cookiePrefs.edit();
            editor.remove(COOKIE+host);
            editor.commit();
            return;
        }
        SharedPreferences cookiePrefs = context.getSharedPreferences(
                COOKIE_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = cookiePrefs.edit();
        editor.putString(COOKIE+host, cookies);
        editor.commit();
    }

    public String sidOf(String host) {
        if (Strings.isBlank(host)) {
            return null;
        }
        SharedPreferences cookiePrefs = context.getSharedPreferences(
                COOKIE_PREFS, Context.MODE_PRIVATE);
        return cookiePrefs.getString(COOKIE+host, null);
    }
}
