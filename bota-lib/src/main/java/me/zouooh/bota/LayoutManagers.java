package me.zouooh.bota;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import me.zouooh.invoker.InvokerAdapter;

/**
 * Created by zouooh on 2016/8/3.
 */
public final class LayoutManagers {
    private LayoutManagers(){}

    public static  RecyclerView.LayoutManager list(Context context){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        return  linearLayoutManager;
    }

    public static  RecyclerView.LayoutManager grid(Context context,final InvokerAdapter invokerAdapter,final int span){
        GridLayoutManager layoutManager = new GridLayoutManager(context, span);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return invokerAdapter.getIndexPaths().get(position).isCell()?1:span;
            }
        });
        return  layoutManager;
    }
}
