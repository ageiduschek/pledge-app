package com.pledgeapp.pledge.fragments.nonprofitlists;

import android.os.Bundle;

import com.pledgeapp.pledge.helpers.PledgeModel;
import com.pledgeapp.pledge.models.NonProfit;

import java.util.List;

public class LocalNonProfitsFragment extends NonProfitListFragment {
    public static LocalNonProfitsFragment newInstance() {

        Bundle args = new Bundle();

        LocalNonProfitsFragment fragment = new LocalNonProfitsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void fetchNonProfits(PledgeModel.OnResultDelegate<List<NonProfit>> delegate) {
        mPledgeModel.getLocal("CA", delegate);
    }
}
