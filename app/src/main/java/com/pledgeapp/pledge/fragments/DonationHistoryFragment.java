package com.pledgeapp.pledge.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.pledgeapp.pledge.PledgeApplication;
import com.pledgeapp.pledge.R;
import com.pledgeapp.pledge.adapters.DonationsAdapter;
import com.pledgeapp.pledge.helpers.PledgeModel;
import com.pledgeapp.pledge.models.Donation;

import java.util.ArrayList;
import java.util.List;

public class DonationHistoryFragment extends Fragment {

    private List<Donation> donations;
    private DonationsAdapter aDonations;

    public static DonationHistoryFragment newInstance() {
        
        Bundle args = new Bundle();
        
        DonationHistoryFragment fragment = new DonationHistoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        donations = new ArrayList<>();
        aDonations = new DonationsAdapter(getActivity(), donations);

        PledgeApplication.getPledgeModel().getDonationHistory(new PledgeModel.OnResultDelegate<List<Donation>>(getContext(), getUserVisibleHint()) {
            @Override
            public void onQueryComplete(List<Donation> result) {
                super.onQueryComplete(result);
                aDonations.addAll(result);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_donation_history, container, false);

        ListView lvDonations = (ListView) v.findViewById(R.id.lvDonations);
        lvDonations.setAdapter(aDonations);

        return v;
    }
}
