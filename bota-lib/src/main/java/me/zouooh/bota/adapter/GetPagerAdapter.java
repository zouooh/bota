package me.zouooh.bota.adapter;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;

import me.zouooh.bota.R;
import me.zouooh.invoker.IndexPath;
import me.zouooh.invoker.InvokerCell;
import me.zouooh.invoker.OnItemListener;
import me.zouooh.slark.request.SlarkGet;
import me.zouooh.slark.response.Progress;
import me.zouooh.slark.task.Queue;
import me.zouooh.utils.Pager;
import me.zouooh.utils.Record;

import com.bumptech.glide.RequestManager;

public abstract class GetPagerAdapter extends GetListAdapter {

    public static int CT_LOAD_MORE = 2999;

    private boolean hideLoadMoreCellOnNoMoreDatas = true;
    private Pager pager = new Pager();

    public GetPagerAdapter(OnItemListener onItemListener,
                           Queue queue, RequestManager requestManager, Progress progress) {
        super(onItemListener, queue, requestManager, progress);
    }

    @Override
    protected void reset() {
        pager.reset();
        super.reset();
    }

    @Override
    protected void adapterData(Record record) {
        super.adapterData(record);
        if (isFirstPage()) {
           pagerInfo(record);
        }
    }

    protected void pagerInfo(Record record){
        pager.setTotal(record.getInt("total"));
    }

    @Override
    protected void clearDatas() {
        if (isFirstPage()) {
            getDatas().clear();
        }
    }

    @Override
    public boolean hasFootViewInSection(int sectionIndex) {
        if (sectionIndex + 1 == getSectionCount()) {
            if (pager.isUnkown()) {
                return super.hasFootViewInSection(sectionIndex);
            }
            if (pager.hasMore()) {
                return true;
            } else if (!isHideLoadMoreCellOnNoMoreDatas()) {
                return true;
            }
        }
        return super.hasFootViewInSection(sectionIndex);
    }

    @Override
    protected void initIndexPathFootType(IndexPath indexPath) {
        if (isEmpty()) {
            super.initIndexPathFootType(indexPath);
            return;
        }
        if (indexPath.getSection() + 1 == getSectionCount()) {
            indexPath.setType(CT_LOAD_MORE);
            return;
        }
    }

    public boolean isHideLoadMoreCellOnNoMoreDatas() {
        return hideLoadMoreCellOnNoMoreDatas;
    }

    public void loadMore() {
        nextPage();
        load();
    }

    protected void nextPage() {
        if (isRequesting()){
            return;
        }
        pager.nextPage();
    }

    @Override
    protected SlarkGet slarkGet() {
        SlarkGet slarkGet = super.slarkGet();
        if (slarkGet != null) {
            appendPageparam(slarkGet);
        }
        return slarkGet;
    }

    protected void appendPageparam(SlarkGet slarkGet) {
        if (slarkGet == null)
            return;
        slarkGet.param("pageSize", pager.getPs() + "");
        slarkGet.param("pageNumber", pager.getPn() + "");
    }

    @Override
    public void onBindFootViewHolder(ViewHolder viewHolder, IndexPath indexPath) {
        if (indexPath.isType(CT_LOAD_MORE)) {
            boolean hasMore = pager.hasMore();
            if (hasMore) {
                loadMore();
            }
            LoadMoreCell loadMoreCell = InvokerCell.viewHolder(viewHolder);
            loadMoreCell.setHasMore(hasMore);
            return;
        }
        super.onBindFootViewHolder(viewHolder, indexPath);
    }

    @Override
    public ViewHolder onCreateFootViewHolder(ViewGroup parent, int type) {
        if (type == CT_LOAD_MORE) {
            return InvokerCell.newViewHolder(LoadMoreCell.class, parent,
                    R.layout.burro___cell_load_more);
        }
        return super.onCreateFootViewHolder(parent, type);
    }

    public void setHideLoadMoreCellOnNoMoreDatas(
            boolean hideLoadMoreCellOnNoMoreDatas) {
        this.hideLoadMoreCellOnNoMoreDatas = hideLoadMoreCellOnNoMoreDatas;
    }

    public boolean isFirstPage() {
        return pager.getPn() == 1;
    }
}
