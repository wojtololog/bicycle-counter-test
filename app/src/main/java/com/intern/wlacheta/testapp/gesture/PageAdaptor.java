package com.intern.wlacheta.testapp.gesture;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.intern.wlacheta.testapp.activites.fragments.TrackerFragment;
import com.intern.wlacheta.testapp.activites.fragments.TripsFragment;

public class PageAdaptor extends FragmentPagerAdapter {
    private int numberOfTabs;

    public PageAdaptor(FragmentManager fragmentManager, int numberOfTabs) {
        super(fragmentManager);
        this.numberOfTabs = numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new TrackerFragment();
            case 1:
                return new TripsFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
