package me.zouooh.bota.adapter;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.zouooh.bota.R;
import me.zouooh.invoker.IndexPath;
import me.zouooh.invoker.InvokerAdapter;
import me.zouooh.invoker.OnItemListener;


public abstract class SectionAdapter extends InvokerAdapter {

	public SectionAdapter(OnItemListener onItemListener) {
		super(onItemListener);
	}

	@Override
	public ViewHolder onCreateHeadViewHolder(ViewGroup parent, int type) {
		View view = LayoutInflater.from(parent.getContext()).inflate(
				R.layout.burro___cell_head_view, parent, false);
		return new HeadCell(view);
	}

	@Override
	public ViewHolder onCreateFootViewHolder(ViewGroup parent, int type) {
		View view = LayoutInflater.from(parent.getContext()).inflate(
				R.layout.burro___cell_head_view, parent, false);
		return new HeadCell(view);
	}

	@Override
	public void onBindHeadViewHolder(ViewHolder viewHolder, IndexPath indexPath) {
		if (viewHolder instanceof HeadCell) {
			((HeadCell) viewHolder).setHeadText(getHeadTextOfSection(indexPath));
		}
	}

	@Override
	public void onBindFootViewHolder(ViewHolder viewHolder, IndexPath indexPath) {
		if (viewHolder instanceof HeadCell) {
			((HeadCell) viewHolder).setHeadText(null);
		}
	}

	public String getHeadTextOfSection(IndexPath indexPath) {
		return null;
	}
	
	@Override
	public boolean hasFootViewInSection(int sectionIndex) {
		int sn = getSectionCount();
		return sn == sectionIndex;
	}
	
	@Override
	public boolean hasHeadViewInSection(int sectionIndex) {
		return sectionIndex > 0;
	}
}
