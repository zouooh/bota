package me.zouooh.bota;

import org.nutz.lang.Strings;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.imid.SwipeBackActivityBase;
import com.imid.SwipeBackActivityHelper;
import com.imid.SwipeBackLayout;
import com.imid.Utils;

public abstract class ToolbarActivity extends BaseActivity implements SwipeBackActivityBase {

	private Toolbar toolbar;
	private ViewGroup container;
	private ViewGroup frame;
	private SwipeBackActivityHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();
        getSwipeBackLayout().setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null)
            return mHelper.findViewById(id);
        return v;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }

	@Override
	public void setContentView(int layoutResID) {
		View _iView = getLayoutInflater().inflate(layoutFrameResId(),
				null);
		toolbar = (Toolbar) _iView.findViewById(R.id.burro__toolbar);
		container= (ViewGroup) _iView.findViewById(R.id.burro__container);
		frame = (ViewGroup)_iView.findViewById(R.id.burro__frame);
		initToolbar();
		if (layoutResID > 0) {
			View append = getLayoutInflater().inflate(layoutResID, null);
			FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
					FrameLayout.LayoutParams.MATCH_PARENT,
					FrameLayout.LayoutParams.MATCH_PARENT);
			getContainer().addView(append, layoutParams);
		}
		super.setContentView(_iView);
		bindViews();
	}
	
	
	protected int layoutFrameResId() {
		return R.layout.burro___content;
	}
	
	protected void initToolbar(){
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		String title = getIntent().getStringExtra(_TITLE);
		if (!Strings.isBlank(title)) {
			getSupportActionBar().setTitle(title);
		}
	}


	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

	public ViewGroup getContainer() {
		return container;
	}


	public ViewGroup getFrame() {
		return frame;
	}	
}
