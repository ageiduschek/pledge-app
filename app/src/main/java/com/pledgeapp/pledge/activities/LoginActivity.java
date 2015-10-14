package com.pledgeapp.pledge.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.google.android.gms.common.SignInButton;
import com.pledgeapp.pledge.R;


/**
 * Minimal activity demonstrating basic Google Sign-In.
 */
public class LoginActivity extends AuthBaseActivity implements View.OnClickListener {

    private static final String TAG = LoginActivity.class.getSimpleName();

    public static Intent getLaunchIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @Override
    protected void onAuthGranted(Bundle savedInstanceState) {
        startActivity(BrowseActivity.getLaunchIntent(this));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up button click listeners
        findViewById(R.id.sign_in_button).setOnClickListener(this);

        // Large sign-in
        ((SignInButton) findViewById(R.id.sign_in_button)).setSize(SignInButton.SIZE_WIDE);

        // Start with sign-in button disabled until sign-in either succeeds or fails
        findViewById(R.id.sign_in_button).setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                onSignInClicked();
                break;
        }
    }
}