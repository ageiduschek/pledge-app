package com.pledgeapp.pledge.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.pledgeapp.pledge.adapters.BrowsePagerAdapter;
import com.pledgeapp.pledge.R;

public class BrowseFragment extends Fragment {
    public static BrowseFragment newInstance() {
        
        Bundle args = new Bundle();
        
        BrowseFragment fragment = new BrowseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View thisView = inflater.inflate(R.layout.fragment_browse, container, false);

        ViewPager vpPager = (ViewPager) thisView.findViewById(R.id.vpPager);
        vpPager.setAdapter(new BrowsePagerAdapter(getActivity().getSupportFragmentManager()));
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) thisView.findViewById(R.id.tabStrip);
        tabStrip.setViewPager(vpPager);

        return thisView;
    }
}
