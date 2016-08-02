package me.zouooh.bota.adapter;



import org.nutz.lang.Lang;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;

import com.bumptech.glide.RequestManager;

import java.util.LinkedList;
import java.util.List;

import me.zouooh.bota.R;
import me.zouooh.invoker.IndexPath;
import me.zouooh.invoker.InvokerCell;
import me.zouooh.invoker.OnItemListener;
import me.zouooh.slark.response.Progress;
import me.zouooh.slark.task.Queue;
import me.zouooh.utils.Record;

public abstract class GetListAdapter extends GetAdapter {

	public static int CT_EMPTY = 2998;

	private List<Record> datas = null;

	private boolean isShowEmptyCell = true;

	public GetListAdapter(OnItemListener onItemListener,
					  Queue queue, RequestManager requestManager, Progress progress) {
		super(onItemListener, queue,requestManager, progress);
	}

	@Override
	protected void adapterData(Record record) {
		clearDatas();
		List<Record> records = record.getRecordList("datas");
		addDatas(records);
	}

	protected void addDatas(List<Record> records) {
		getDatas().addAll(records);
	}


	protected void clearDatas() {
		getDatas().clear();
	}

	@Override
	protected void onRequestFailure(Throwable t) {
		clearDatas();
		super.onRequestFailure(t);
	}

	@Override
	public Record recordOfIndexPath(IndexPath indexPath) {
		return getDatas().get(indexPath.getRow());
	}

	@Override
	public int getRowCountInSection(int sectionIndex) {
		return getDatas().size();
	}

	@Override
	protected void initIndexPathType(IndexPath indexPath) {
	}

	@Override
	public boolean hasFootViewInSection(int sectionIndex) {
		if (sectionIndex + 1 == getSectionCount()&&isEmpty() && isShowEmptyCell()) {
			return true;
		}
		return false;
	}
	
	@Override
	protected void initIndexPathFootType(IndexPath indexPath) {
		if (indexPath.getSection() + 1 == getSectionCount()) {
			indexPath.setType(CT_EMPTY);
			return;
		}
		super.initIndexPathFootType(indexPath);
	}
	
	@Override
	public ViewHolder onCreateFootViewHolder(ViewGroup parent, int type) {
		if (type == CT_EMPTY) {
			return onCreateEmptyHolder(parent);
		}
		return super.onCreateFootViewHolder(parent, type);
	}

	private ViewHolder onCreateEmptyHolder(ViewGroup parent) {
		return InvokerCell.newViewHolder(EmptyCell.class, parent,
				R.layout.burro___cell_empty);
	}
	
	@Override
	public void onBindFootViewHolder(ViewHolder viewHolder, IndexPath indexPath) {
		if (indexPath.isType(CT_EMPTY)) {
			if (viewHolder instanceof EmptyCell) {
				String string = emptyStr();
				if (hasThrowable()){
					string = "加载数据失败";
				}
				((EmptyCell) viewHolder).setEmptyText(string);
			}
			return;
		}
		super.onBindFootViewHolder(viewHolder, indexPath);
	}
	
	protected  String emptyStr(){
		return "没有数据";
	}

	@Override
	public boolean hasHeadViewInSection(int sectionIndex) {
		return false;
	}

	public List<Record> getDatas() {
		if (datas == null) {
			datas = new LinkedList<>();
		}
		return datas;
	}

	public void remove(IndexPath indexPath) {
		int index = getIndexPaths().indexOf(indexPath);
		notifyItemRemoved(index);
		getDatas().remove(indexPath.getRow());
		getIndexPaths().clear();
		initIndexPath();
	}

	public boolean isShowEmptyCell() {
		return isShowEmptyCell;
	}

	public void setShowEmptyCell(boolean isShowEmptyCell) {
		this.isShowEmptyCell = isShowEmptyCell;
	}

	public boolean isEmpty() {
		return Lang.isEmpty(datas)||hasThrowable();
	}

}
