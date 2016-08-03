package me.zouooh.bota.s;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import me.zouooh.bota.LayoutManagers;
import me.zouooh.bota.adapter.GetListAdapter;
import me.zouooh.bota.adapter.GetPagerAdapter;
import me.zouooh.bota.fragment.PtrRecyclerViewFragment;
import me.zouooh.invoker.InvokerCell;
import me.zouooh.invoker.InvokerDecoration;
import me.zouooh.invoker.OnItemListener;
import me.zouooh.slark.request.Request;
import me.zouooh.slark.request.RequestConfig;
import me.zouooh.slark.response.Progress;
import me.zouooh.slark.task.Queue;
import me.zouooh.utils.AppUtils;

public class GridFragment extends PtrRecyclerViewFragment {


    @Override
    protected void configRecyclerView() {
        getRecyclerView().setLayoutManager(LayoutManagers.grid(getContext(),invokerAdapter(),3));
    }

    @Override
    protected RecyclerView.Adapter<? extends RecyclerView.ViewHolder> newAdapter() {
        return new GridFragmentAdapter(this,queue(), Glide.with(this),this).load();
    }

    @Override
    public void onRequestSucess(Request antsRequest) {
        super.onRequestSucess(antsRequest);
        AppUtils.toast(getContext(),antsRequest.requestURL().toString());
    }

    public class GridFragmentAdapter extends GetPagerAdapter {

        public GridFragmentAdapter(OnItemListener onItemListener, Queue queue, RequestManager requestManager, Progress progress) {
            super(onItemListener, queue, requestManager, progress);
        }

        @Override
        protected RequestConfig newGet() {
            return queue().g("/app/tb/topic/bar?topic_bar_id=15");
        }

        @Override
        protected RecyclerView.ViewHolder onCreateCellViewHolder(ViewGroup parent, int type) {
            return InvokerCell.newViewHolder(TestCell.class,parent,R.layout.cell_test);
        }
    }

}
