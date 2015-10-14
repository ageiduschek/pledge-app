package com.pledgeapp.pledge.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;

import com.pledgeapp.pledge.R;

public class AccountSettingsActivity extends NavigationBarActivity {

    public static Intent getLaunchIntent(Context context) {
        return new Intent(context, AccountSettingsActivity.class);
    }

    @Override
    protected void onAuthGranted(Bundle savedInstanceState) {
        // TODO: Implement
        ViewStub stub = (ViewStub) findViewById(R.id.nav_bar_activity_main_layout_stub);
        stub.setLayoutResource(R.layout.activity_account_settings);
        Button logoutButton = (Button) findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSignOutClicked();
            }
        });
    }
}
