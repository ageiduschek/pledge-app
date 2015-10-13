package com.pledgeapp.pledge.activities;

import android.content.Context;
import android.content.Intent;

public class LinkEmployerActivity extends NavigationBarActivity {
    public static Intent getLaunchIntent(Context context) {
        return new Intent(context, LinkEmployerActivity.class);
    }

    protected void showSignedInUI() {
        // TODO: Implement
    }
}
