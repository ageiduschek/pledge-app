package com.pledgeapp.pledge.fragments.nonprofitlists;

import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * Created by nikhil on 10/14/15.
 */
public class FeaturedNonProfitsFragment extends NonProfitListFragment {
    @Override
    protected void fetchNonProfits(JsonHttpResponseHandler handler) {
        client.getFeatured(handler);
    }
}
