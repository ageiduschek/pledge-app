package com.pledgeapp.pledge.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.pledgeapp.pledge.R;

public abstract class NavigationBarActivity extends SignInRequiredBaseActivity {

    ActionBarDrawerToggle drawerToggle;
    DrawerLayout mNavDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Set a Toolbar to replace the ActionBar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Find our drawer view
        mNavDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle(mNavDrawer, toolbar);

        // Tie DrawerLayout events to the ActionBarToggle
        mNavDrawer.setDrawerListener(drawerToggle);

        NavigationView nvDrawer = (NavigationView) findViewById(R.id.nvView);
        setupDrawerContent(nvDrawer);

        // Set default to first item
//        MenuItem menuItem = nvDrawer.getMenu().getItem(0);
//        menuItem.setChecked(true);
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.flContent, new FamilyGuyFragment()).commit();
//        setTitle(R.string.family_guy);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
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
                mNavDrawer.closeDrawer(GravityCompat.START);
                return true;
        }

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void selectDrawerItem(MenuItem menuItem) {

        Intent launchIntent = getLaunchIntentFromSelectedItem(menuItem.getItemId());

        // Highlight the selected item, update the title, and close the drawer
        mNavDrawer.closeDrawers();


        if (launchIntent != null) {
            startActivity(launchIntent);
            // Remove the current activity from the back stack
            finish();
        }
    }

    public Intent getLaunchIntentFromSelectedItem(int itemId) {
        Intent launchIntent;
        Class<?> activityClass;
        switch(itemId) {
            case R.id.nav_browse:
                activityClass = BrowseActivity.class;
                launchIntent = BrowseActivity.getLaunchIntent(this);
                break;
            case R.id.nav_donation_history:
                activityClass = DonationHistoryActivity.class;
                launchIntent = DonationHistoryActivity.getLaunchIntent(this);
                break;
            case R.id.nav_payment_method:
                activityClass = AddPaymentActivity.class;
                launchIntent = AddPaymentActivity.getLaunchIntent(this);
                break;
            case R.id.nav_link_employer:
                activityClass = LinkEmployerActivity.class;
                launchIntent = LinkEmployerActivity.getLaunchIntent(this);
                break;
            case R.id.nav_account_settings:
                activityClass = AccountSettingsActivity.class;
                launchIntent = AccountSettingsActivity.getLaunchIntent(this);
                break;
            default:
                throw new RuntimeException("Unknown menu item");
        }

        if (this.getClass() == activityClass) {
            return null;
        }

        return launchIntent;
    }


}
