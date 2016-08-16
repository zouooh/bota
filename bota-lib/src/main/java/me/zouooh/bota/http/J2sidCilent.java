package me.zouooh.bota.http;

import android.content.Context;

import me.zouooh.slark.biuld.SlarkClientImpl;
import me.zouooh.slark.task.QueueAspect;
import me.zouooh.slark.task.RequestAspectFactory;

/**
 * Created by zouooh on 2016/8/16.
 */
public class J2sidCilent extends SlarkClientImpl{

    protected  Context context;

    public J2sidCilent(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public QueueAspect aspect() {
        return new AuthAspect();
    }

    @Override
    public RequestAspectFactory requestAspectFactory() {
        return new SidAspectFactory(context);
    }
}
