package me.zouooh.bota.adapter;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;

import com.bumptech.glide.RequestManager;

import me.zouooh.invoker.IndexPath;
import me.zouooh.invoker.OnItemListener;
import me.zouooh.slark.task.Queue;
import me.zouooh.utils.Record;


public abstract class RecordAdapter extends SectionAdapter {

	protected Queue queue;
	protected RequestManager requestManager;

	public RecordAdapter(OnItemListener onItemListener,Queue queue,RequestManager requestManager) {
		super(onItemListener);
		this.queue = queue;
		this.requestManager = requestManager;
	}
	
	@Override
	public void onBindCellViewHolder(ViewHolder viewHolder, IndexPath indexPath) {
		if (viewHolder instanceof RecordCell) {
			Record record = recordOfIndexPath(indexPath);
			((RecordCell)viewHolder).loadData(record);
		}
	}
	
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
		ViewHolder viewHolder = super.onCreateViewHolder(parent, type);
		if (viewHolder instanceof RecordCell) {
			RecordCell recordViewHolder = (RecordCell) viewHolder;
			recordViewHolder.setRecordAdapter(this);
		}
		return viewHolder;
	}

	public Queue queue() {
		return queue;
	}
	public RequestManager requestManager() {
		return requestManager;
	}
	public abstract Record recordOfIndexPath(IndexPath indexPath);
}
