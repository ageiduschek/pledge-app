package com.pledgeapp.pledge.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.pledgeapp.pledge.R;

public class PledgeBaseActivity extends AppCompatActivity {

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
        // Create a new fragment and specify the planet to show based on
        // position
        Fragment fragment = null;

        Intent launchIntent;
        switch(menuItem.getItemId()) {
            case R.id.nav_browse:
                launchIntent = BrowseActivity.getLaunchIntent(this);
                break;
            case R.id.nav_donation_history:
                launchIntent = DonationHistoryActivity.getLaunchIntent(this);
                break;
            case R.id.nav_payment_method:
                launchIntent = AddPaymentActivity.getLaunchIntent(this);
                break;
            case R.id.nav_link_employer:
                launchIntent = LinkEmployerActivity.getLaunchIntent(this);
                break;

            case R.id.nav_account_settings:
                launchIntent = AccountSettingsActivity.getLaunchIntent(this);
                break;
            default:
                throw new RuntimeException("Unknown menu item");
        }

        // Highlight the selected item, update the title, and close the drawer
        mNavDrawer.closeDrawers();

        startActivity(launchIntent);
        // Remove the current activity from the back stack
        finish();
    }


}
