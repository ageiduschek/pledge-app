package com.pledgeapp.pledge.activities;

/**
 *
 */
public abstract class SignInRequiredBaseActivity extends AuthBaseActivity {
    protected void showSignedOutUI() {
        startActivity(LoginActivity.getLaunchIntent(this));
        finish();
    }
}
