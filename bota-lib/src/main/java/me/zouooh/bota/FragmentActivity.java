package me.zouooh.bota;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public abstract class FragmentActivity extends ToolbarActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(0);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.burro__container, newFragment()).commit();
		}
	}
	
	
	protected abstract Fragment newFragment();
	
}
