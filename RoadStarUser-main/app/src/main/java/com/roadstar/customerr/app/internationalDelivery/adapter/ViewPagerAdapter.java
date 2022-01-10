package com.roadstar.customerr.app.internationalDelivery.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.roadstar.customerr.app.internationalDelivery.fragments.AvailableTrips;
import com.roadstar.customerr.app.internationalDelivery.fragments.PostTrip;

import org.jetbrains.annotations.NotNull;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final Fragment[] childFragments;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        childFragments = new Fragment[] {
                new PostTrip(), //0
                new AvailableTrips(), //1
        };
    }

    @NotNull
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
            return "POST TRIP";
        else
            return "AVAILABLE TRIPS";
    }
}