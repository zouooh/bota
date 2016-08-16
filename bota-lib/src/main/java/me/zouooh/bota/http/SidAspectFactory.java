package me.zouooh.bota.http;

import android.content.Context;

import me.zouooh.slark.request.Request;
import me.zouooh.slark.request.RequestAspect;
import me.zouooh.slark.task.RequestAspectFactory;

/**
 * Created by zouooh on 2016/8/16.
 */
public class SidAspectFactory implements RequestAspectFactory{

    private SidStore sidStore;

    public  SidAspectFactory(Context context){
        this.sidStore = new SidStore(context);
    }

    @Override
    public RequestAspect buildRequestAspect(Request request) {
        return new SidAspect(sidStore);
    }
}
