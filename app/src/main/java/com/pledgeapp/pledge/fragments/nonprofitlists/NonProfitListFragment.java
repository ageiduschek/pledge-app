package com.pledgeapp.pledge.fragments.nonprofitlists;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.pledgeapp.pledge.PledgeApplication;
import com.pledgeapp.pledge.PledgeClient;
import com.pledgeapp.pledge.R;
import com.pledgeapp.pledge.adapters.NonProfitArrayAdapter;
import com.pledgeapp.pledge.models.NonProfit;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by nikhil on 10/14/15.
 */
public abstract class NonProfitListFragment extends Fragment {

    protected PledgeClient client;
    private ArrayList<NonProfit> nonProfits;
    private NonProfitArrayAdapter aNonProfits;

    private ListView lvNonProfits;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nonProfits = new ArrayList<>();
        aNonProfits = new NonProfitArrayAdapter(getActivity(), nonProfits);

        client = PledgeApplication.getPledgeClient();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_nonprofit_list, container, false);

        lvNonProfits = (ListView) v.findViewById(R.id.lvNonProfits);
        lvNonProfits.setAdapter(aNonProfits);

        // TODO(nikhilb): Add EndlessScrollListener when the server supports paging

        fetchNonProfits(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                aNonProfits.clear();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject nonProfitJson = response.getJSONObject(i);
                        aNonProfits.add(NonProfit.fromJson(nonProfitJson));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });

        return v;
    }

    protected abstract void fetchNonProfits(JsonHttpResponseHandler handler);
}
