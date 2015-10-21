package com.pledgeapp.pledge.fragments.nonprofitlists;

import android.os.Bundle;

import com.loopj.android.http.JsonHttpResponseHandler;

public class SingleCategoryListFragment extends NonProfitListFragment {
    public static SingleCategoryListFragment newInstance() {

        Bundle args = new Bundle();

        SingleCategoryListFragment fragment = new SingleCategoryListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void fetchNonProfits(JsonHttpResponseHandler handler) {
        client.search("", handler);
    }
}
