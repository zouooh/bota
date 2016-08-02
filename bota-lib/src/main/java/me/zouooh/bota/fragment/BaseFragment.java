package me.zouooh.bota.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;

import me.zouooh.bota.BaseActivity;
import me.zouooh.bota.BurroEvent;
import me.zouooh.bota.ToolbarActivity;
import me.zouooh.gum.Gums;
import me.zouooh.slark.Slark;
import me.zouooh.slark.task.Queue;
import me.zouooh.utils.Record;


public class BaseFragment extends Fragment implements OnClickListener,
		Gums.OnBindViewListener {

	private Queue queue;

	public Queue queue() {
		if (queue == null) {
			queue = Slark.with(this);
		}
		return queue;
	}

	public void bindViews() {
		bindViews(getView());
	}

	public void bindViews(View view) {
		Gums.bindViews(this, view, this);
	}

	public BaseActivity getBaseActivity() {
		Activity acitivity = getActivity();
		if (acitivity instanceof BaseActivity) {
			return (BaseActivity) acitivity;
		}
		return null;
	}
	
	public ToolbarActivity getToolBarActivity() {
		Activity acitivity = getActivity();
		if (acitivity instanceof ToolbarActivity) {
			return (ToolbarActivity) acitivity;
		}
		return null;
	}

	@Override
	public void onBinded(View bindView) {
		if (bindView instanceof Button) {
			Button button = (Button) bindView;
			button.setOnClickListener(this);
		}
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (queue != null) {
			queue.destroy();
			queue = null;
		}
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
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		Gums.bindViews(this, view, this);
	}

	public void startActivity(Class<?> clazz) {
		Intent intent = new Intent();
		intent.setClass(getContext(), clazz);
		startActivity(intent);
	}

	public void startActivity(Class<?> clazz,
			HashMap<String, Object> data) {
		Intent intent = new Intent();
		intent.setClass(getContext(), clazz);
		intent.putExtra(BaseActivity._DATA, data);
		startActivity(intent);
	}

	public void startActivity(Class<?> clazz, String... data) {
		if (data.length % 2 == 1) {
			return;
		}
		Intent intent = new Intent();
		intent.setClass(getContext(), clazz);
		for (int i = 0; i < data.length / 2; i++) {
			intent.putExtra(data[i*2], data[i*2 + 1]);
		}
		startActivity(intent);
	}

	public Record getDataFromStr(){
		BaseActivity inttusActivity = getBaseActivity();
		Record record = null;
		if (inttusActivity!=null) {
			record = inttusActivity.getDataFromStr();
		}
		return record == null? new Record(): record;
	}
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
	}

	@Subscribe
	public void onEventMainThread(BurroEvent event) {
		
	}
}
