package com.pledgeapp.pledge.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.pledgeapp.pledge.fragments.AccountSettingsFragment;
import com.pledgeapp.pledge.fragments.nonprofitlists.FeaturedNonProfitsFragment;
import com.pledgeapp.pledge.fragments.nonprofitlists.LocalNonProfitsFragment;

/**
 * Created by nikhil on 10/14/15.
 */
public class BrowsePagerAdapter extends FragmentPagerAdapter {

    private static final String[] TAB_TITLES = {"Featured", "Categories", "Local"};

    public BrowsePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // TODO(nikhilb) actually load correct fragments
        if (position == 0) {
            return new FeaturedNonProfitsFragment();
        } else if (position == 1) {
            // Categories tab
        } else {
            return new LocalNonProfitsFragment();
        }

        // TODO(nikhilb) remove this line after implementing all fragments
        return new AccountSettingsFragment();
    }

    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TAB_TITLES[position];
    }
}
