package com.pledgeapp.pledge.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class DonationHistoryActivity extends PledgeBaseActivity {

    public static Intent getLaunchIntent(Context context) {
        return new Intent(context, DonationHistoryActivity.class);
    }

    protected void showSignedInUI() {
        // TODO: Implement
    }
}
