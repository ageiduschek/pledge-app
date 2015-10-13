package com.pledgeapp.pledge.activities;

import android.content.Context;
import android.content.Intent;

public class AddPaymentActivity extends NavigationBarActivity {

    public static Intent getLaunchIntent(Context context) {
        return new Intent(context, AddPaymentActivity.class);
    }

    protected void showSignedInUI() {
        // TODO: Implement
    }
}
