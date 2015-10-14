package com.pledgeapp.pledge.activities;

import android.os.Bundle;

/**
 *
 */
public abstract class SignInRequiredBaseActivity extends AuthBaseActivity {
    @Override
    protected void onAuthDenied(Bundle savedInstanceState) {
        startActivity(LoginActivity.getLaunchIntent(this));
        finish();
    }
}
