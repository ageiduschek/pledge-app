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

import com.pledgeapp.pledge.EndlessScrollListener;
import com.pledgeapp.pledge.PledgeApplication;
import com.pledgeapp.pledge.R;
import com.pledgeapp.pledge.activities.NonProfitDetailActivity;
import com.pledgeapp.pledge.adapters.NonProfitArrayAdapter;
import com.pledgeapp.pledge.adapters.SearchSuggestionsArrayAdapter;
import com.pledgeapp.pledge.helpers.PledgeModel;
import com.pledgeapp.pledge.helpers.Util;
import com.pledgeapp.pledge.models.NonProfit;
import com.pledgeapp.pledge.models.RecentQueriesHelper;
import com.pledgeapp.pledge.models.SearchSuggestion;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    private static final String KEY_CATEGORY_INFO = "key_category_info";

    public static Bundle getNewInstanceArgs(NonProfit.CategoryInfo categoryInfo) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_CATEGORY_INFO, categoryInfo);
        return bundle;
    }

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

    private SearchView mSearchView;

    private PledgeModel mPledgeModel;
    // Optionally set if we are searching within a category
    private NonProfit.CategoryInfo mCategoryInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCategoryInfo = getArguments().getParcelable(KEY_CATEGORY_INFO);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        mPledgeModel = PledgeApplication.getPledgeModel();

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
                doSearchQuery(tag.suggestionText.getText().toString());
            }
        });

        mLvResults = (ListView) view.findViewById(R.id.lvSearchResults);
        mResultsListAdapter = new NonProfitArrayAdapter(getContext(), new ArrayList<NonProfit>());
        mLvResults.setAdapter(mResultsListAdapter);
        mLvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(NonProfitDetailActivity.getLaunchIntent(getContext(),
                        mResultsListAdapter.getItem(position)));
            }
        });

        mLvSuggestions.setVisibility(View.VISIBLE);
        mLvResults.setVisibility(View.GONE);

        if (mCategoryInfo != null) {
            mSearchView.setQueryHint("Search " + mCategoryInfo.name);
        }
        return view;
    }

    // TODO (ageiduschek): Figure out how to register this listener only after onCreateView()
    public void registerSearchView(final SearchView searchView) {
        mSearchView = searchView;
        mSearchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                doSearchQuery(mSearchView.getQuery().toString());
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
                    if (mResultsListAdapter != null) {
                        mResultsListAdapter.clear();
                    }

                    mLvSuggestions.setVisibility(View.VISIBLE);
                    mLvResults.setVisibility(View.GONE);
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

    private void doSearchQuery(final String query) {
        RecentQueriesHelper.getInstance(getContext()).addOrUpdateQuery(query);

        mSearchView.setQuery(query, false /*submit query*/);

        if (Util.isNetworkAvailable(getContext())) {
            mResultsListAdapter.clear();
            mLvResults.setOnScrollListener(new EndlessScrollListener() {
                @Override
                public boolean onLoadMore(int page, int totalItemCount) {
                    searchWithPageOffset(query, page);
                    return true;
                }
            });
            searchWithPageOffset(query, 1);
        } else {
            Util.displayNetworkErrorToast(getContext());
        }

        hideSoftKeyboard(mSearchView);
    }

    private void searchWithPageOffset(String query, int page) {
        // ProPublica 1-indexes their search results, so we need to convert to 1-indexing
        mPledgeModel.search(query, mCategoryInfo, page, new PledgeModel.OnResultDelegate<List<NonProfit>>() {
            @Override
            public void onQueryComplete(List<NonProfit> nonProfits) {
                mResultsListAdapter.addAll(nonProfits);
                mLvSuggestions.setVisibility(View.GONE);
                mLvResults.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNetworkFailure(List<NonProfit> results, int errorMessage) {
                Util.displayNetworkErrorToast(getContext());
            }
        });
    }

    private class SuggestionsFilter extends Filter {

        List<SearchSuggestion> mAllSuggestions;

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            //QueryDB for recent queries
            mAllSuggestions = RecentQueriesHelper.getInstance(getContext())
                                                 .getAllRecentQueries();

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
