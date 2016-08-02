package me.zouooh.bota.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import me.zouooh.bota.R;
import me.zouooh.gum.Gum;
import me.zouooh.gum.Gums;

public class LoadMoreCell extends RecyclerView.ViewHolder {

	private ProgressBar progressBar;

	private TextView textView;

	public LoadMoreCell(View itemView) {
		super(itemView);
		progressBar = Gums.findView(itemView,R.id.progressbar1);
		textView = Gums.findView(itemView,R.id.textView1);
	}
	
	public void setHasMore(boolean has){
		if(has){
			progressBar.setVisibility(View.VISIBLE);
			textView.setText("加载更多中•••");
		}else {
			progressBar.setVisibility(View.GONE);
			textView.setText("没有更多数据了。");
		}
	}

	public ProgressBar getProgressBar() {
		return progressBar;
	}

	public void setProgressBar(ProgressBar progressBar) {
		this.progressBar = progressBar;
	}

	public TextView getTextView() {
		return textView;
	}

	public void setTextView(TextView textView) {
		this.textView = textView;
	}
}
