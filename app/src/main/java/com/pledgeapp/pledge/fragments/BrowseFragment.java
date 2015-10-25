package com.pledgeapp.pledge.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.pledgeapp.pledge.adapters.BrowsePagerAdapter;
import com.pledgeapp.pledge.R;

public class BrowseFragment extends Fragment {
    private PagerAdapter mPagerAdapter;
    private OnPageChangedListener mListener;

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
        if (mPagerAdapter == null) {
            mPagerAdapter = new BrowsePagerAdapter(getChildFragmentManager());
        }
        vpPager.setAdapter(mPagerAdapter);

        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mListener.onPageChanged(mPagerAdapter.getPageTitle(position).toString());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) thisView.findViewById(R.id.tabStrip);
        tabStrip.setViewPager(vpPager);
        mListener.onPageChanged(mPagerAdapter.getPageTitle(vpPager.getCurrentItem()).toString());

        return thisView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnPageChangedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                                                 + " must implement OnPaymentMethodSubmittedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnPageChangedListener {
        void onPageChanged(String currentTitle);
    }
}
