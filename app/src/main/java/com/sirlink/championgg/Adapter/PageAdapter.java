package com.sirlink.championgg.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.List;

/**
 * Created by compudep on 20/11/2017.
 */

public class PageAdapter extends FragmentPagerAdapter{

    private List<Fragment> fragments;

    public PageAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }
    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }
    @Override
    public int getCount() {
        return this.fragments.size();
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return fragments.get(position).getArguments().getString("param2");
    }
}
