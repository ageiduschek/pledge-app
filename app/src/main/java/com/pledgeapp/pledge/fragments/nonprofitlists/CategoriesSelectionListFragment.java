package com.pledgeapp.pledge.fragments.nonprofitlists;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pledgeapp.pledge.PledgeApplication;
import com.pledgeapp.pledge.PledgeClient;
import com.pledgeapp.pledge.R;
import com.pledgeapp.pledge.adapters.CategoriesArrayAdapter;
import com.pledgeapp.pledge.models.NonProfit;

import java.util.ArrayList;

/**
 * List of all the Categories
 */
public class CategoriesSelectionListFragment extends Fragment {

    protected PledgeClient mClient;
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

        mClient = ((PledgeApplication)getActivity().getApplication()).getPledgeClient();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_generic_list, container, false);

        ListView lvNonProfits = (ListView) v.findViewById(R.id.lvList);
        lvNonProfits.setAdapter(mCategoriesAdapter);
        lvNonProfits.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListener.onCategorySelected(mCategoriesAdapter.getItem(position));
                //TODO: IMPLEMENT
            }
        });

        mCategoriesAdapter.clear();
        mCategoriesAdapter.addAll(NonProfit.getMajorCategoryNames());

        return v;
    }

    private OnCategorySelectedListener mListener;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnCategorySelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                                                 + " must implement OnCategorySelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnCategorySelectedListener{
        void onCategorySelected(NonProfit.CategoryInfo info);
    }
}
