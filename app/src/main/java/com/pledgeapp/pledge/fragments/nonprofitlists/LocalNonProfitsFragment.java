package com.pledgeapp.pledge.fragments.nonprofitlists;

import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * Created by nikhil on 10/14/15.
 */
public class LocalNonProfitsFragment extends NonProfitListFragment {
    @Override
    protected void fetchNonProfits(JsonHttpResponseHandler handler) {
        client.getLocal(handler);
    }
}
