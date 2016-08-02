package me.zouooh.bota;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.nutz.json.Json;
import org.nutz.lang.Strings;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import me.zouooh.gum.Gums;
import me.zouooh.slark.Slark;
import me.zouooh.slark.task.Queue;
import me.zouooh.utils.Record;

public class BaseActivity extends AppCompatActivity implements
		OnClickListener {
	public static String _ID = "_burro_id_";
	public static String _NAME = "_burro_name_";
	public static String _TITLE = "_burro_title_";
	public static String _DATA = "_burro_data_";
	private Queue queue;

	@Override
	public void onClick(View v) {
	}

	public void startActivity(Class<? extends Activity> clazz) {
		Intent intent = new Intent();
		intent.setClass(this, clazz);
		startActivity(intent);
	}

	public void startActivity(Class<? extends Activity> clazz, String... data) {
		if (data.length % 2 == 1) {
			return;
		}
		Intent intent = new Intent();
		intent.setClass(this, clazz);
		for (int i = 0; i < data.length / 2; i++) {
			intent.putExtra(data[i*2], data[i*2 + 1]);
		}
		startActivity(intent);
	}

	public void startActivity(Class<? extends Activity> clazz,
			HashMap<String, Object> data) {
		Intent intent = new Intent();
		intent.setClass(this, clazz);
		intent.putExtra(_DATA, data);
		startActivity(intent);
	}

	public Map<String, Object> getData() {
		return getData(getIntent());
	}

	public Map<String, Object> getData(Intent intent) {
		Serializable data = intent.getSerializableExtra(_DATA);
		if (data instanceof Map) {
			return (Map<String, Object>) data;
		}
		return null;
	}

	public Record getDataFromStr(String key) {
		String temp = getIntent().getStringExtra(key);
		if (Strings.isBlank(temp)) {
			return new Record();
		}
		return new Record((Map<String, Object>) Json.fromJson(temp));
	}
	
	public Record getDataFromStr(){
		return getDataFromStr(_DATA);
	}
	
	public Queue queue() {
		if (queue == null) {
			queue = Slark.with(this);
		}
		return queue;
	}

	@Override
	public void onPause() {
		super.onPause();
		if (queue != null) {
			queue.pause();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (queue != null) {
			queue.resume();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (queue != null) {
			queue.destroy();
		}
		EventBus.getDefault().unregister(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
	}

	@Subscribe
	public void onEventMainThread(BurroEvent event) {
	}

	public void bindViews() {
		bindViews(null);
	}

	public void bindViews(View view) {
		Gums.bindViews(this, view, new Gums.OnBindViewListener() {
			@Override
			public void onBinded(View bindView) {
				if (bindView instanceof Button) {
					Button button = (Button) bindView;
					button.setOnClickListener(BaseActivity.this);
				}
			}
		});
	}

	
	public String _ID(){
		return getIntent().getStringExtra(_ID);
	}

}
