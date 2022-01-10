package com.roadstar.customerr.app.module.ui.your_package.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.roadstar.customerr.app.module.ui.your_package.fragments.InternationPackages;
import com.roadstar.customerr.app.module.ui.your_package.fragments.LocalPackages;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private Fragment[] childFragments;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        childFragments = new Fragment[] {
                new LocalPackages(), //0
                new InternationPackages(), //1
        };
    }

    @Override
    public Fragment getItem(int position) {
        return childFragments[position];
    }

    @Override
    public int getCount() {
        return childFragments.length; //3 items
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0)
            return "Local";
        else
            return "International";
    }
}