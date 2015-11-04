package com.pledgeapp.pledge.fragments.nonprofitlists;

import android.os.Bundle;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.pledgeapp.pledge.helpers.PledgeModel;
import com.pledgeapp.pledge.models.NonProfit;

import java.util.List;

public class LocalNonProfitsFragment extends NonProfitListFragment {
    public static FeaturedNonProfitsFragment newInstance() {

        Bundle args = new Bundle();

        FeaturedNonProfitsFragment fragment = new FeaturedNonProfitsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void fetchNonProfits(PledgeModel.OnResultDelegate<List<NonProfit>> delegate) {
        mPledgeModel.getLocal(delegate);
    }
}
