package com.pledgeapp.pledge.activities;

import android.content.Context;
import android.content.Intent;

public class AccountSettingsActivity extends NavigationBarActivity {

    public static Intent getLaunchIntent(Context context) {
        return new Intent(context, AccountSettingsActivity.class);
    }

    protected void showSignedInUI() {
        // TODO: Implement
    }
}
