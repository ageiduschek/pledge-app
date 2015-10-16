package com.pledgeapp.pledge.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.Toast;

import com.pledgeapp.pledge.R;
import com.pledgeapp.pledge.adapters.SearchSuggestionsArrayAdapter;
import com.pledgeapp.pledge.models.SearchSuggestion;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    public static SearchFragment newInstance() {
        
        Bundle args = new Bundle();
        
        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private SearchSuggestionsArrayAdapter mResultListAdapter;
    private ListView mLvSuggestions;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        mLvSuggestions = (ListView) view.findViewById(R.id.lvSearchSuggestions);

        mResultListAdapter = new SearchSuggestionsArrayAdapter(getActivity(),
                                                               new ArrayList<SearchSuggestion>());

        mResultListAdapter.addAll(getRecentSearches(""));

        mLvSuggestions.setAdapter(mResultListAdapter);

        return view;
    }

    public void registerSearchView(final SearchView searchView) {
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                mCurrentQuery = mSearchView.getQuery().toString();
//                fetchQueryResults(0);
                hideSoftKeyboard(searchView);
                Toast.makeText(getContext(), "Searched!", Toast.LENGTH_LONG).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // TODO (ageiduschek): Figure out how to register this listener only after onCreateView()
                // TODO (ageiduschek): Make filtering actually work
                if (mLvSuggestions != null) {
                    if (newText.isEmpty()) {
                        mLvSuggestions.clearTextFilter();
                    } else {
                        mLvSuggestions.setFilterText(newText);
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

    private List<SearchSuggestion> getRecentSearches(String query) {
        ArrayList<SearchSuggestion> list = new ArrayList<>();
        list.add(new SearchSuggestion("apple ball"));
        list.add(new SearchSuggestion("banana alligator"));
        return list;
    }
}
