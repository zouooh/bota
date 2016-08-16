package me.zouooh.bota.http;

import me.zouooh.slark.DataResponse;
import me.zouooh.slark.request.Request;
import me.zouooh.slark.request.RequestAspect;

/**
 * Created by zouooh on 2016/8/16.
 */
public class SidAspect implements RequestAspect {

    SidStore sidStore;

    public SidAspect(SidStore sidStore) {
        this.sidStore = sidStore;
    }

    @Override
    public void beforeOnBack(Request request) {
        if (sidStore != null) {
            String sid = sidStore.sidOf(request.requestURL().getHost());
            if (sid != null) {
                request.header("Cookie", sid);
            }
        }
    }

    @Override
    public void afterOnBack(Request request) {

    }

    @Override
    public void aspect(Request request, DataResponse networkResponse) {
        if (networkResponse.headers == null || sidStore == null)
            return;
        String cookie = networkResponse.headers.get("Set-Cookie");
        sidStore.save(request.requestURL().getHost(), cookie);
    }
}
