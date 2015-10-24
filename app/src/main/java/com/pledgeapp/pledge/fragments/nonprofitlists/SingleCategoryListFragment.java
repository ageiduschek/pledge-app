package com.pledgeapp.pledge.fragments.nonprofitlists;

import android.os.Bundle;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.pledgeapp.pledge.models.NonProfit;

public class SingleCategoryListFragment extends NonProfitListFragment {
    private static final String KEY_CATEGORY_INFO = "key_category_info";

    public static Bundle getNewInstanceArgs(NonProfit.CategoryInfo categoryInfo) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_CATEGORY_INFO, categoryInfo);
        return bundle;
    }

    public static SingleCategoryListFragment newInstance() {
        return new SingleCategoryListFragment();
    }

    @Override
    protected void fetchNonProfits(JsonHttpResponseHandler handler) {

        NonProfit.CategoryInfo categoryInfo = getArguments().getParcelable(KEY_CATEGORY_INFO);
        client.search("", categoryInfo, handler); // Do search with no keyword in the given category
    }
}
