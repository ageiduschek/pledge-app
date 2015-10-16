package com.pledgeapp.pledge.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


import com.pledgeapp.pledge.R;
import com.pledgeapp.pledge.fragments.AccountSettingsFragment;
import com.pledgeapp.pledge.fragments.AddPaymentFragment;
import com.pledgeapp.pledge.fragments.BrowseFragment;
import com.pledgeapp.pledge.fragments.DonationHistoryFragment;
import com.pledgeapp.pledge.fragments.LinkEmployerFragment;
import com.pledgeapp.pledge.fragments.SearchFragment;

public class MainActivity extends AppCompatActivity {

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNvDrawer;
    private SearchView mSearchView;
    private MenuItem mSearchMenuItem;

    public static Intent getLaunchIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO(ageiduschek): Fix screen rotation bug with this info http://stackoverflow.com/a/27690095
        setContentView(R.layout.activity_main);
        // Set a Toolbar to replace the ActionBar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Find our drawer view
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = setupDrawerToggle(mDrawerLayout, toolbar);

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mNvDrawer = (NavigationView) findViewById(R.id.nvView);
        setupDrawerContent();

        // Set default to first item
        selectDrawerItem(R.id.nav_browse);
    }

    private void setupDrawerContent() {
        mNvDrawer.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem.getItemId());
                        return true;
                    }
                });
    }

    private ActionBarDrawerToggle setupDrawerToggle(DrawerLayout dlDrawer, Toolbar toolbar) {
        return new ActionBarDrawerToggle(this, dlDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
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
                fragmentManager.beginTransaction().replace(R.id.flFragmentContainer,
                                                           BrowseFragment.newInstance()).commit();
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                return true; // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                SearchView searchView = (SearchView) MenuItemCompat.getActionView(mSearchMenuItem);
                SearchFragment searchFragment = SearchFragment.newInstance();
                searchFragment.registerSearchView(searchView);
                fragmentManager.beginTransaction().replace(R.id.flFragmentContainer,
                                                           searchFragment).commit();
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                return true;
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void selectDrawerItem(int menuItemId) {
        Class<? extends Fragment> fragmentClass = getClassFromId(menuItemId);

        Fragment fragment = null;
        try {
            fragment = fragmentClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flFragmentContainer, fragment).commit();



        // Highlight the selected item, update the title, and close the drawer
        MenuItem menuItem = mNvDrawer.getMenu().findItem(menuItemId);
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawerLayout.closeDrawers();

        if (mSearchMenuItem != null) {
            if (menuItemId == R.id.nav_browse) {
                mSearchMenuItem.setVisible(true);
            } else {
                mSearchMenuItem.setVisible(false);
            }
        }
    }

    public Class<? extends Fragment> getClassFromId(int itemId) {
        Class<? extends Fragment> fragmentClass;
        switch(itemId) {
            case R.id.nav_browse:
                fragmentClass = BrowseFragment.class;
                break;
            case R.id.nav_donation_history:
                fragmentClass = DonationHistoryFragment.class;
                break;
            case R.id.nav_payment_method:
                fragmentClass = AddPaymentFragment.class;
                break;
            case R.id.nav_link_employer:
                fragmentClass = LinkEmployerFragment.class;
                break;
            case R.id.nav_account_settings:
                fragmentClass = AccountSettingsFragment.class;
                break;
            default:
                throw new RuntimeException("Unknown menu item");
        }

        return fragmentClass;
    }
}
