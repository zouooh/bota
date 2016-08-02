package me.zouooh.bota.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.zouooh.bota.R;
import me.zouooh.invoker.IndexPath;
import me.zouooh.invoker.InvokerAdapter;
import me.zouooh.invoker.InvokerDecoration;
import me.zouooh.invoker.OnItemListener;
import me.zouooh.slark.request.Request;
import me.zouooh.slark.response.Progress;

public abstract class RecyclerViewFragment extends BaseFragment
		implements OnItemListener, Progress {

	private RecyclerView recyclerView;
	private Adapter<? extends ViewHolder> adapter;

	protected abstract void configRecyclerView();

	protected void decorationRecyclerView() {
		getRecyclerView().addItemDecoration(new InvokerDecoration(getContext()));
	}

	public Adapter<? extends ViewHolder> getAdapter() {
		if (adapter == null) {
			adapter = newAdapter();
		}
		return adapter;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Adapter<?extends ViewHolder>> T getAdapterOf() {
		Adapter<? extends ViewHolder> adapter = getAdapter();
		if (adapter instanceof InvokerAdapter) {
			return (T) adapter;
		}
		throw new RuntimeException("adapter is not sectionAdapter");
	}

	protected int layoutResId() {
		return R.layout.burro___recycler_view;
	}

	protected abstract Adapter<? extends ViewHolder> newAdapter();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(layoutResId(), container,false);
		setRecyclerView((RecyclerView) view.findViewById(R.id.burro__recycler_view));
		return view;
	}

	@Override
	public void onItemClick(IndexPath indexPath) {
	}

	@Override
	public boolean onItemLongClick(IndexPath indexPath) {
		return false;
	}

	@Override
	public void onLoading(int index, int current, int total) {
	}

	@Override
	public void onRequestEnd(Request antsRequest) {
	}

	@Override
	public void onRequestFailure(Request antsRequest, Throwable throwable) {
	}

	@Override
	public void onRequestStart(Request antsRequest) {
	}

	@Override
	public void onRequestSucess(Request antsRequest) {
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		getRecyclerView().setHasFixedSize(true);
		getRecyclerView().setItemAnimator(new DefaultItemAnimator());
		configRecyclerView();
		decorationRecyclerView();
		getRecyclerView().setAdapter(getAdapter());
	}

	public RecyclerView getRecyclerView() {
		return recyclerView;
	}

	protected void setRecyclerView(RecyclerView recyclerView) {
		this.recyclerView = recyclerView;
	}

}
