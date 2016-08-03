package me.zouooh.bota.s;

import android.view.View;
import android.widget.TextView;

import me.zouooh.bota.adapter.Injector;
import me.zouooh.bota.adapter.RecordCell;
import me.zouooh.gum.Gum;
import me.zouooh.utils.Record;

/**
 * Created by zouooh on 2016/8/3.
 */
public class TestCell extends RecordCell{

    @Gum(resId = R.id.textView1)
    TextView textView;

    public TestCell(View itemView) {
        super(itemView);
    }

    @Override
    public void inject(Record record, Injector injector) {
        injector.injectTextView(textView,"topic_title");
    }
}
