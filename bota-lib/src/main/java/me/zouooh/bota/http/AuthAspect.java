package me.zouooh.bota.http;

import org.greenrobot.eventbus.EventBus;

import me.zouooh.bota.BurroEvent;
import me.zouooh.slark.http.HttpStatus;
import me.zouooh.slark.request.Request;
import me.zouooh.slark.request.StatusException;
import me.zouooh.slark.task.QueueAspect;

/**
 * Created by zouooh on 2016/8/16.
 */
public class AuthAspect implements QueueAspect {
    @Override
    public Request aspect(Request request) {
        return null;
    }

    @Override
    public void handle(Request request, Throwable t) {
        if (t instanceof StatusException) {
            int code = ((StatusException) t).getStatus();
            if (code == HttpStatus.SC_UNAUTHORIZED) {
                EventBus.getDefault().post(BurroEvent.event(BurroEvent.AUTH_401));
            }
        }
    }
}
