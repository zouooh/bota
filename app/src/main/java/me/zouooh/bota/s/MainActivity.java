package me.zouooh.bota.s;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.zouooh.bota.TabActivity;
import me.zouooh.slark.Slark;

public class MainActivity extends TabActivity {

    private String[] mTitles = { "首页", "会话"};
    private int[] mIconUnselectIds = {R.mipmap.ic_launcher,R.mipmap.ic_launcher };
    private int[] mIconSelectIds = { R.mipmap.ic_launcher,R.mipmap.ic_launcher};


    @Override
    protected void init() {
        //test
        Slark.init(getApplicationContext());
        for (int i = 0; i < mTitles.length; i++) {
            tabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i],
                    mIconUnselectIds[i]));
            if (i==0){
                fragments.add(new GridFragment());
                continue;
            }
            fragments.add(PlaceholderFragment.newInstance(i));
        }
    }

    @Override
    public void onTabSelect(int position) {

    }

    @Override
    public void onTabReselect(int position) {

    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_maine, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }
}
