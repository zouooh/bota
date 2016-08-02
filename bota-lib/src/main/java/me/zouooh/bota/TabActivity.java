package me.zouooh.bota;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

import me.zouooh.gum.Gums;

public abstract class TabActivity extends BaseActivity implements OnTabSelectListener {


    public static class TabEntity implements CustomTabEntity {
        public String title;
        public int selectedIcon;
        public int unSelectedIcon;

        public TabEntity(String title, int selectedIcon, int unSelectedIcon) {
            this.title = title;
            this.selectedIcon = selectedIcon;
            this.unSelectedIcon = unSelectedIcon;
        }

        @Override
        public String getTabTitle() {
            return title;
        }

        @Override
        public int getTabSelectedIcon() {
            return selectedIcon;
        }

        @Override
        public int getTabUnselectedIcon() {
            return unSelectedIcon;
        }
    }

    protected Toolbar toolbar;
    protected CommonTabLayout commonTabLayout;
    protected FrameLayout container;
    protected ArrayList<CustomTabEntity> tabEntities = new ArrayList<>();
    protected ArrayList<Fragment> fragments = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutFrameResId());
        toolbar = Gums.findView(this, R.id.burro__toolbar);
        commonTabLayout = Gums.findView(this, R.id.burro__tablayout);
        container = Gums.findView(this, R.id.burro__container);
        init();
        commonTabLayout.setOnTabSelectListener(this);
        commonTabLayout.setTabData(tabEntities,this,R.id.burro__container,fragments);
    }

    protected abstract void init();

    protected int layoutFrameResId() {
        return R.layout.burro___tab_content;
    }
}
