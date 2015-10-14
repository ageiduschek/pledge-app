package com.pledgeapp.pledge.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class DonationHistoryActivity extends NavigationBarActivity {

    public static Intent getLaunchIntent(Context context) {
        return new Intent(context, DonationHistoryActivity.class);
    }

    @Override
    protected void onAuthGranted(Bundle savedInstanceState) {
        // TODO: Implement
    }
}
