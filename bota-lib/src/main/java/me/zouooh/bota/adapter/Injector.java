package me.zouooh.bota.adapter;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.nutz.castor.Castors;
import org.nutz.lang.Strings;

import java.util.Date;

import me.zouooh.slark.Slark;
import me.zouooh.utils.Record;

/**
 * Created by zouooh on 2016/8/2.
 */
public class Injector {

    public static Injector obtain(Record record){
        return  new Injector(record);
    }

    public static String createAt(String dataStr) {
        if (Strings.isBlank(dataStr))
            return "未知";
        Date date = Castors.me().castTo(dataStr, Date.class);
        long now = System.currentTimeMillis() / 1000;
        long t = date.getTime() / 1000;
        long diff = now - t;
        if (diff < 5) {
            return "刚刚";
        }
        if (diff < 60) {
            return diff + "秒前";
        }
        if (diff < 60 * 60) {
            return (diff / 60) + "分钟前";
        }
        if (diff < 24 * 60 * 60) {
            return (diff / 60 / 60) + "小时前";
        }
        if (diff < 3 * 24 * 60 * 60) {
            return (diff / 24 / 60 / 60) + "天前";
        }
        return dataStr;
    }

    public static void injectGlideBitmapWithUrl(ImageView view, String path,
                                                int defalutResId, RequestManager requestManager) {
        if (view == null) {
            return;
        }
        if (Strings.isBlank(path)) {
            view.setImageResource(defalutResId);
            return;
        }
        if (!path.startsWith("http://")) {
            path = Slark.urlWithHost(path);
        }
        if (requestManager == null){
            requestManager = Glide.with(view.getContext());
        }
        requestManager.load(Uri.parse(path)).centerCrop().crossFade().placeholder(defalutResId).into
                (view);
    }

    private Record record;

    public Injector(Record record) {
        this.record = record;
    }

    public Record record() {
        return this.record;
    }

    public void injectRatingBar(RatingBar ratingBar, String jsonkey) {
        double n = record.getDouble(jsonkey);
        ratingBar.setRating((float) n);
    }

    public void injectProgressBar(ProgressBar progressBar, String jsonkey) {
        int n = record.getInt(jsonkey);
        progressBar.setProgress(n);
    }

    public void injectTextView(TextView textView, String jsonkey) {
        String text = record().getString(jsonkey);
        textView.setText(text);
    }

    public void injectTextViewF(TextView textView, String jsonkey, String format) {
        String text = record().getString(jsonkey);
        if (format != null) {
            text = String.format(format, text);
        }
        textView.setText(text);
    }

    public void injectInt2TextView(TextView textView, String jsonkey) {
        String text = record().getString(jsonkey, "0");
        textView.setText(text);
    }

    public void injectDefalut2TextView(TextView textView, String jsonkey,
                                       String defalut) {
        String text = record().getString(jsonkey, defalut);
        textView.setText(text);
    }

    public void injectDouble2TextView(TextView textView, String jsonkey) {
        String text = record().getString(jsonkey, "0.00");
        textView.setText(text);
    }

    public void injectEazyDate2TextView(TextView textView, String jsonkey) {
        String text = record().getString(jsonkey);
        textView.setText(createAt(text));
    }

    public void injectVisibility(View view, String jsonkey) {
        boolean b = record().getBoolean(jsonkey);
        if (b) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.INVISIBLE);
        }
    }

    public void injectGone(View view, String jsonkey) {
        boolean b = record().getBoolean(jsonkey);
        if (b) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    public void injectGlideBitmap(ImageView view, String jsonkey,
                                  int defalutResId,RequestManager requestManager) {
        injectGlideBitmapWithUrl(view, record().getString(jsonkey),
                defalutResId,requestManager);
    }
}
