package com.pledgeapp.pledge.fragments.nonprofitlists;

import android.os.Bundle;

import com.loopj.android.http.JsonHttpResponseHandler;

public class LocalNonProfitsFragment extends NonProfitListFragment {
    public static FeaturedNonProfitsFragment newInstance() {

        Bundle args = new Bundle();

        FeaturedNonProfitsFragment fragment = new FeaturedNonProfitsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void fetchNonProfits(JsonHttpResponseHandler handler) {
        client.getLocal(handler);
    }
}
