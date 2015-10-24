package com.pledgeapp.pledge.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.pledgeapp.pledge.R;
import com.pledgeapp.pledge.fragments.SearchFragment;
import com.pledgeapp.pledge.fragments.nonprofitlists.SingleCategoryListFragment;
import com.pledgeapp.pledge.models.NonProfit;

import java.util.ArrayList;

public class CategoryBrowseActivity extends BaseActivity {
    private MenuItem mSearchMenuItem;
    private NonProfit.CategoryInfo mCategoryInfo;

    private static final String KEY_CATEGORY = "key_category";

    public static Intent getLaunchIntent(Context context, NonProfit.CategoryInfo category) {

        Intent intent = new Intent(context, CategoryBrowseActivity.class);
        intent.putExtra(KEY_CATEGORY, category);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_nav);

        mCategoryInfo = getIntent().getParcelableExtra(KEY_CATEGORY);

        // Set a Toolbar to replace the ActionBar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(mCategoryInfo.name);
        }

        attachFragmentToContainer(SingleCategoryListFragment.class,
                                  R.id.flFragmentContainer,
                                  SingleCategoryListFragment.getNewInstanceArgs(mCategoryInfo));

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        setupSearchBar(menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void setupSearchBar(Menu menu) {
        mSearchMenuItem = menu.findItem(R.id.action_search);

        final FragmentManager fragmentManager = getSupportFragmentManager();
        MenuItemCompat.setOnActionExpandListener(mSearchMenuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                attachFragmentToContainer(SingleCategoryListFragment.class,
                                          R.id.flFragmentContainer,
                                          SingleCategoryListFragment.getNewInstanceArgs(mCategoryInfo));
                return true; // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                SearchView searchView = (SearchView) MenuItemCompat.getActionView(mSearchMenuItem);
                SearchFragment searchFragment = attachFragmentToContainer(SearchFragment.class,
                                                                          R.id.flFragmentContainer,
                                                                          SearchFragment.getNewInstanceArgs(mCategoryInfo));
                searchFragment.registerSearchView(searchView);
                return true;
            }
        });
    }

    private static final ArrayList<Class<? extends Fragment>> mFragmentClasses = getFragmentClasses();

    private static ArrayList<Class<? extends Fragment>> getFragmentClasses() {
        ArrayList<Class<? extends Fragment>> fragmentClasses = new ArrayList<>();
        fragmentClasses.add(SingleCategoryListFragment.class);
        fragmentClasses.add(SearchFragment.class);
        return fragmentClasses;
    }

    @Override
    protected ArrayList<Class<? extends Fragment>> getFragmentClassesForContainer() {
        return mFragmentClasses;
    }
}
