package me.zouooh.bota.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import me.zouooh.bota.R;
import me.zouooh.bota.adapter.GetAdapter;
import me.zouooh.bota.adapter.GetPagerAdapter;
import me.zouooh.slark.request.Request;
import me.zouooh.utils.AppUtils;

public abstract class PtrRecyclerViewFragment extends
		RecyclerViewFragment {

	protected PtrFrameLayout ptrFrameLayout;
	protected ViewGroup listContainer;
	protected View listToTop;
	protected View loadingView;
	protected boolean isComplete = false;
	protected int delayShow = 350;
	private Handler handler;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState);
		ptrFrameLayout = (PtrFrameLayout) view.findViewById(R.id.burro__ptrl);
		listContainer = (ViewGroup) view
				.findViewById(R.id.burro__list_container);
		loadingView = view.findViewById(R.id.burro__list_loading);
		return view;
	}

	public void showLoadingView() {
		isComplete = false;
		if (loadingView == null) {
			return;
		}
		if (handler != null) {
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					if (!isComplete) {
						loadingView.setVisibility(View.VISIBLE);
					}
				}
			}, delayShow);
		}
	}

	public void hideLoadingView() {
		isComplete = true;
		if (loadingView != null) {
			loadingView.setVisibility(View.INVISIBLE);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		configPtr();
		if (loadingView != null) {
			handler = new Handler();
		}
		if (listToTop != null) {
			listToTop.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					RecyclerView recyclerView = getRecyclerView();
					if (recyclerView != null) {
						recyclerView.scrollToPosition(0);
						doScrollChage(false);
					}
				}
			});

			RecyclerView recyclerView = getRecyclerView();

			recyclerView.setOnScrollListener(new OnScrollListener() {

				@Override
				public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
					super.onScrolled(recyclerView, dx, dy);
					if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
						LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
						int postion = linearLayoutManager.findFirstVisibleItemPosition();
						if (postion<10) {
							doScrollChage(false);
							return;
						}
					}
					
					if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
						GridLayoutManager linearLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
						int postion = linearLayoutManager.findFirstVisibleItemPosition();
						if (postion<20) {
							doScrollChage(false);
							return;
						}
					}
					
					boolean isSignificantDelta = Math.abs(dy) > 30;
					if (isSignificantDelta) {
						if (dy > 0) {
							doScrollChage(false);
						} else {
							doScrollChage(true);
						}
					}
				}
			});
			
		}
		super.onViewCreated(view, savedInstanceState);
	}

	protected void doScrollChage(boolean show) {
		listToTop.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
	}

	protected void listTopExtMarginChage(int dp) {
		if (listToTop == null) {
			return;
		}
		LayoutParams layoutParams = listToTop.getLayoutParams();
		if (layoutParams instanceof FrameLayout.LayoutParams) {
			((FrameLayout.LayoutParams) layoutParams).bottomMargin = AppUtils
					.dip2px(getContext(), dp + 10);
		}
	}

	protected int layoutResId() {
		return R.layout.burro___ptr_recycler_view;
	}

	protected void configPtr() {
		if (ptrFrameLayout instanceof PtrClassicFrameLayout) {
			PtrClassicFrameLayout ptrClassicFrameLayout = (PtrClassicFrameLayout) ptrFrameLayout;
			ptrClassicFrameLayout.setLastUpdateTimeRelateObject(this);
		}
		ptrFrameLayout.setPtrHandler(new PtrHandler() {
			@Override
			public void onRefreshBegin(final PtrFrameLayout frame) {
				PtrRecyclerViewFragment.this.refresh();
			}

			@Override
			public boolean checkCanDoRefresh(PtrFrameLayout frame,
					View content, View header) {
				return PtrDefaultHandler.checkContentCanBePulledDown(frame,
						content, header);
			}
		});

		ptrFrameLayout.setResistance(2.3f);
		ptrFrameLayout.setRatioOfHeaderHeightToRefresh(1.2f);
		ptrFrameLayout.setDurationToClose(300);
		ptrFrameLayout.setDurationToCloseHeader(2000);
		ptrFrameLayout.setPullToRefresh(false);
		ptrFrameLayout.setKeepHeaderWhenRefresh(true);
	}

	protected void refresh() {
		if (getAdapter() instanceof GetAdapter) {
			GetAdapter getAdapter = (GetAdapter) getAdapter();
			getAdapter.refresh();
		}
	}

	@Override
	public void onRequestEnd(Request antsRequest) {
		if (!isComplete) {
			hideLoadingView();
		}
		if (ptrFrameLayout == null) {
			return;
		}
		if ( ptrFrameLayout.isRefreshing()) {
			ptrFrameLayout.refreshComplete();
		}
	}
	
	@Override
	public void onRequestStart(Request antsRequest) {
		if (ptrFrameLayout!=null && !ptrFrameLayout.isRefreshing()) {
			if (getAdapter() instanceof GetPagerAdapter) {
				GetPagerAdapter getPagerAdapter = getAdapterOf();
				if (getPagerAdapter.isFirstPage()) {
					showLoadingView();
				}
				return;
			}
			if (getAdapter() instanceof GetAdapter) {
				showLoadingView();
				return;
			}
		}
	}
}
