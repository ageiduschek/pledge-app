package com.pledgeapp.pledge.activities;

import android.content.Context;
import android.content.Intent;

public class BrowseActivity extends NavigationBarActivity {
    public static Intent getLaunchIntent(Context context) {
        return new Intent(context, BrowseActivity.class);
    }

    protected void showSignedInUI() {
        // TODO: Implement
    }
}
