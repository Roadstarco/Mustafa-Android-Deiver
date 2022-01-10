package com.roadstar.customerr.app.module.ui.available_booking.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.roadstar.customerr.app.module.ui.available_booking.fragments.InternationTrips;
import com.roadstar.customerr.app.module.ui.available_booking.fragments.LocalTrips;
import com.roadstar.customerr.app.module.ui.your_package.fragments.InternationPackages;
import com.roadstar.customerr.app.module.ui.your_package.fragments.LocalPackages;

public class MyTripsPagerAdapter extends FragmentPagerAdapter {

    private Fragment[] childFragments;

    public MyTripsPagerAdapter(FragmentManager fm) {
        super(fm);
        childFragments = new Fragment[] {
                new LocalTrips(), //0
                new InternationTrips(), //1
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