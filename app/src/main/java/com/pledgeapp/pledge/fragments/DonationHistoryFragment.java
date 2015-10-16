package com.pledgeapp.pledge.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pledgeapp.pledge.R;

public class DonationHistoryFragment extends Fragment {
    public static DonationHistoryFragment newInstance() {
        
        Bundle args = new Bundle();
        
        DonationHistoryFragment fragment = new DonationHistoryFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_donation_history, container, false);
    }
}
