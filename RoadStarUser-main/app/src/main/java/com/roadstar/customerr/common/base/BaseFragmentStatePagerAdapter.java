package com.roadstar.customerr.common.base;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.SparseArray;
import android.view.ViewGroup;

import java.util.List;

/**
 * Responsible to add/destroy fragment in a given {@link ViewPager}
 * We must override {@link FragmentStatePagerAdapter#getItem(int)} and  {@link FragmentStatePagerAdapter#getCount()}
 * in all our child classes
 */
abstract public class BaseFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

    protected SparseArray<Fragment> fragments = new SparseArray<>();
    protected List<String> titles = null;
    protected int[] tabIcons = null;
    protected boolean hasIcons = false;

    public BaseFragmentStatePagerAdapter(FragmentManager fm, List<String> titles) {
        super(fm);
        this.titles = titles;
    }

    public BaseFragmentStatePagerAdapter(FragmentManager fm, int[] tabResourceItems) {
        super(fm);
        this.tabIcons = tabResourceItems;
        hasIcons = true;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (fragments.size() >= position)
            fragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public void clearResources() {
        if (titles != null)
            titles.clear();
        titles = null;
        tabIcons = null;
    }

    /**
     * @param position Return fragment from {@link #fragments} at given position
     * @return Fragment
     */
    public Fragment getItemAt(int position) {
        if (position >= fragments.size()) return null;
        return fragments.get(position);
    }

    /**
     * @param position if we are using icons get resource id of icon from {@link #tabIcons} at given position
     * @return resource id of icon
     */
    public int getTabIconAt(int position) {
        // return hasIcons() ? tabIcons[position] : R.drawable.ic_launcher;
        return -1;
    }

    /**
     * @return true we'r using icons/images in tabs else not
     */
    public boolean hasIcons() {
        return hasIcons;
    }
}
