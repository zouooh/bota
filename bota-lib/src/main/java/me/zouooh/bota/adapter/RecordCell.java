package me.zouooh.bota.adapter;


import java.util.Date;

import org.nutz.castor.Castors;
import org.nutz.lang.Strings;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import me.zouooh.gum.Gums;
import me.zouooh.invoker.InvokerCell;
import me.zouooh.slark.task.Queue;
import me.zouooh.utils.Record;

public abstract class RecordCell extends InvokerCell implements
		Gums.OnBindViewListener {

	protected Record record;

	public RecordAdapter adapter() {
		return recordAdapter;
	}

	public void setRecordAdapter(RecordAdapter recordAdapter) {
		this.recordAdapter = recordAdapter;
	}

	protected RecordAdapter recordAdapter;

	public RecordCell(View itemView) {
		super(itemView);
		Gums.bindViews(this, getItemView(), this);
	}

	public Record record() {
		return record;
	}

	public void loadData(Record record) {
		this.record = record;
		inject(record,Injector.obtain(record));
	}

	public abstract void inject(Record record,Injector injector);

	@Override
	public void onBinded(View bindView) {
	}
}
