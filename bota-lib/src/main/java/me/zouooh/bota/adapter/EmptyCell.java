package me.zouooh.bota.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import me.zouooh.bota.R;

public class EmptyCell extends  RecyclerView.ViewHolder {

	TextView textView;

	public EmptyCell(View itemView) {
		super(itemView);
		textView = (TextView) itemView.findViewById(R.id.textView1);
	}

	public void setEmptyText(String text) {
		textView.setText(text);
	}
}
