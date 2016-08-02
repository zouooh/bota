package me.zouooh.bota.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import me.zouooh.bota.R;

public class HeadCell extends  RecyclerView.ViewHolder {

	TextView textView;

	public HeadCell(View itemView) {
		super(itemView);
		textView = (TextView) itemView.findViewById(R.id.textView1);
	}

	public void setHeadText(String text) {
		if (text==null) {
			textView.setVisibility(View.GONE);
		}else {
			textView.setVisibility(View.VISIBLE);
			textView.setText(text);
		}
	}
}
