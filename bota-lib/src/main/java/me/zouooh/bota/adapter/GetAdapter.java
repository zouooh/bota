package me.zouooh.bota.adapter;

import com.bumptech.glide.RequestManager;

import me.zouooh.bota.http.JsonResponse;
import me.zouooh.invoker.OnItemListener;
import me.zouooh.slark.request.Request;
import me.zouooh.slark.request.RequestConfig;
import me.zouooh.slark.request.SlarkGet;
import me.zouooh.slark.response.Progress;
import me.zouooh.slark.task.Queue;
import me.zouooh.utils.Record;

public abstract class GetAdapter extends RecordAdapter {

    private Progress progress;
    protected SlarkGet slarkGet;
    private JsonResponse jsonResponse;

    public boolean hasThrowable() {
        return hasThrowable;
    }

    private boolean hasThrowable = false;

    public GetAdapter(OnItemListener onItemListener,
                      Queue queue, RequestManager requestManager, Progress progress) {
        super(onItemListener, queue, requestManager);
        jsonResponse = new JsonResponse() {
            @Override
            public void onRequestSuccess(Request request, Record record) {
                GetAdapter.this.onRequestSuccess(record);
            }

            @Override
            public void onRequestFailure(Request request, Throwable throwable) {
                GetAdapter.this.onRequestFailure(throwable);
            }
        };
        setProgress(progress);
    }

    public GetAdapter load() {
        if (isRequesting()){
            return this;
        }
        SlarkGet slarkGet = slarkGet();
        if (slarkGet == null) {
            if (getProgress() != null) {
                getProgress().onRequestEnd(null);
            }
            return this;
        }
        slarkGet.progress(getProgress());
        queue().submitRequest(slarkGet);
        return this;
    }

    protected void reset() {
        if (slarkGet!=null){
            queue().destory(slarkGet);
        }
    }

    protected abstract void adapterData(Record record);

    protected SlarkGet slarkGet() {
        SlarkGet slarkGet = (SlarkGet) newGet();
        if (slarkGet == null) {
            return null;
        }
        slarkGet.response(jsonResponse);
        this.slarkGet = slarkGet;
        return slarkGet;
    }

    protected  boolean isRequesting(){
        if (slarkGet == null){
            return  false;
        }
        return  !slarkGet.isFinish()&&!slarkGet.isPause();
    }

    public Progress getProgress() {
        return progress;
    }

    protected abstract RequestConfig newGet();

    public void refresh() {
        reset();
        load();
    }

    protected void onRequestFailure(Throwable t) {
        hasThrowable = true;
        notifySectionChanged();
    }

    protected void onRequestSuccess(Record t) {
        hasThrowable = false;
        adapterData(t);
        notifySectionChanged();
    }

    public void setProgress(Progress progress) {
        this.progress = progress;
    }

}
