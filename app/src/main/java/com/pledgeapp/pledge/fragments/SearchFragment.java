package com.pledgeapp.pledge.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.pledgeapp.pledge.PledgeApplication;
import com.pledgeapp.pledge.PledgeClient;
import com.pledgeapp.pledge.R;
import com.pledgeapp.pledge.adapters.NonProfitArrayAdapter;
import com.pledgeapp.pledge.adapters.SearchSuggestionsArrayAdapter;
import com.pledgeapp.pledge.models.NonProfit;
import com.pledgeapp.pledge.models.RecentQueriesHelper;
import com.pledgeapp.pledge.models.SearchSuggestion;

import org.apache.http.Header;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    public static SearchFragment newInstance() {
        
        Bundle args = new Bundle();
        
        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private SearchSuggestionsArrayAdapter mSuggestionsListAdapter;
    private ListView mLvSuggestions;
    private NonProfitArrayAdapter mResultsListAdapter;
    private ListView mLvResults;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        mLvSuggestions = (ListView) view.findViewById(R.id.lvSearchSuggestions);
        mSuggestionsListAdapter = new SearchSuggestionsArrayAdapter(getActivity(),
                                                                    new ArrayList<SearchSuggestion>(),
                                                                    new SuggestionsFilter());

        mLvSuggestions.setAdapter(mSuggestionsListAdapter);
        mLvSuggestions.setTextFilterEnabled(true);
        mSuggestionsListAdapter.getFilter().filter("");
        mLvSuggestions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SearchSuggestionsArrayAdapter.ViewHolder tag
                        = (SearchSuggestionsArrayAdapter.ViewHolder) view.getTag();
                doSearchQuery(view, tag.suggestionText.getText().toString());
                mSuggestionsListAdapter.clear();
            }
        });


        mLvResults = (ListView) view.findViewById(R.id.lvSearchResults);
        mResultsListAdapter = new NonProfitArrayAdapter(getContext(), new ArrayList<NonProfit>());
        mLvResults.setAdapter(mResultsListAdapter);
        return view;
    }

    // TODO (ageiduschek): Figure out how to register this listener only after onCreateView()
    public void registerSearchView(final SearchView searchView) {
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                doSearchQuery(searchView, searchView.getQuery().toString());
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (mLvSuggestions != null) {
                    if (newText.isEmpty()) {
                        mSuggestionsListAdapter.getFilter().filter("");
                    } else {
                        mSuggestionsListAdapter.getFilter().filter(newText);
                    }
                }
                return true;
            }
        });
    }

    private void hideSoftKeyboard(View view){
        InputMethodManager imm =
                (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void doSearchQuery(View view, String query) {
        RecentQueriesHelper.getInstance(getContext()).addOrUpdateQuery(query);
        PledgeClient client = ((PledgeApplication) getActivity().getApplication()).getPledgeClient();
        client.search(query, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                mResultsListAdapter.clear();
                List<NonProfit> nonProfits = NonProfit.fromJSONArray(response);
                mResultsListAdapter.addAll(nonProfits);
            }
        });
        hideSoftKeyboard(view);

    }

    private class SuggestionsFilter extends Filter {

        List<SearchSuggestion> mAllSuggestions;

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (mAllSuggestions == null) {
                //QueryDB for recent queries
                mAllSuggestions = RecentQueriesHelper.getInstance(getContext())
                                                     .getAllRecentQueries();
            }

            List<SearchSuggestion> filteredList = new ArrayList<>();
            for (SearchSuggestion suggestion : mAllSuggestions) {
                if (constraint.length() == 0 || suggestion.matchesConstraint(constraint)) {
                    filteredList.add(suggestion);
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mSuggestionsListAdapter.clear();
            List suggestions = (List)results.values;
            if (suggestions != null) {
                for (Object obj : suggestions) {
                    mSuggestionsListAdapter.add((SearchSuggestion) obj);
                }
            }
        }
    }
}
