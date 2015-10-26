package com.pledgeapp.pledge.fragments.nonprofitlists;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pledgeapp.pledge.R;
import com.pledgeapp.pledge.activities.CategoryBrowseActivity;
import com.pledgeapp.pledge.adapters.CategoriesArrayAdapter;
import com.pledgeapp.pledge.models.NonProfit;

import java.util.ArrayList;

/**
 * List of all the Categories
 */
public class CategoriesSelectionListFragment extends Fragment {
    private CategoriesArrayAdapter mCategoriesAdapter;

    public static CategoriesSelectionListFragment newInstance() {

        Bundle args = new Bundle();

        CategoriesSelectionListFragment fragment = new CategoriesSelectionListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<NonProfit.CategoryInfo> nonProfits = new ArrayList<>();
        mCategoriesAdapter = new CategoriesArrayAdapter(getActivity(), nonProfits);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_generic_list, container, false);

        ListView lvCategories = (ListView) v.findViewById(R.id.lvList);
        lvCategories.setAdapter(mCategoriesAdapter);
        lvCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(CategoryBrowseActivity.getLaunchIntent(getContext(),
                                                                     mCategoriesAdapter.getItem(position)));
            }
        });
        lvCategories.setEmptyView(v.findViewById(R.id.tvEmpty));

        mCategoriesAdapter.clear();
        mCategoriesAdapter.addAll(NonProfit.getMajorCategoryInfo());

        return v;
    }
}
