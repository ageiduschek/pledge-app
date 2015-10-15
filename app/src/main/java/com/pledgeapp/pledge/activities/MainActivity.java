package com.pledgeapp.pledge.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


import com.pledgeapp.pledge.R;
import com.pledgeapp.pledge.fragments.AccountSettingsFragment;
import com.pledgeapp.pledge.fragments.AddPaymentFragment;
import com.pledgeapp.pledge.fragments.BrowseFragment;
import com.pledgeapp.pledge.fragments.DonationHistoryFragment;
import com.pledgeapp.pledge.fragments.LinkEmployerFragment;

public class MainActivity extends AppCompatActivity {

    ActionBarDrawerToggle drawerToggle;
    DrawerLayout mDrawerLayout;
    NavigationView mNvDrawer;

    public static Intent getLaunchIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Set a Toolbar to replace the ActionBar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Find our drawer view
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle(mDrawerLayout, toolbar);

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawerLayout.setDrawerListener(drawerToggle);

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
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The back button should close the nav drawer
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
        }

        if (drawerToggle.onOptionsItemSelected(item)) {
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
