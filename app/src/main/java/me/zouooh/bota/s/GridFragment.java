package me.zouooh.bota.s;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import me.zouooh.bota.adapter.GetListAdapter;
import me.zouooh.bota.adapter.GetPagerAdapter;
import me.zouooh.bota.fragment.PtrRecyclerViewFragment;
import me.zouooh.invoker.InvokerCell;
import me.zouooh.invoker.OnItemListener;
import me.zouooh.slark.request.RequestConfig;
import me.zouooh.slark.response.Progress;
import me.zouooh.slark.task.Queue;

public class GridFragment extends PtrRecyclerViewFragment {


    @Override
    protected void configRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                GridFragmentAdapter gridFragmentAdapter = getAdapterOf();
                return gridFragmentAdapter.getIndexPaths().get(position).isCell()?1:3;
            }
        });

        getRecyclerView().setLayoutManager(layoutManager);
    }

    @Override
    protected RecyclerView.Adapter<? extends RecyclerView.ViewHolder> newAdapter() {
        return new GridFragmentAdapter(this,queue(), Glide.with(this),this).load();
    }

    public class GridFragmentAdapter extends GetListAdapter {

        public GridFragmentAdapter(OnItemListener onItemListener, Queue queue, RequestManager requestManager, Progress progress) {
            super(onItemListener, queue, requestManager, progress);
        }

        @Override
        protected RequestConfig newGet() {
            return queue().get("http://www.seqi360.com/app/tb/topic/bar?topic_bar_id=15");
        }

        @Override
        protected RecyclerView.ViewHolder onCreateCellViewHolder(ViewGroup parent, int type) {
            return InvokerCell.viewHolder(parent,R.layout.burro___cell_empty);
        }
    }

}
