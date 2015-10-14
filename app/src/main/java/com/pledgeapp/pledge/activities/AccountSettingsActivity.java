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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewStub stub = (ViewStub) findViewById(R.id.nav_bar_activity_main_layout_stub);
        stub.setLayoutResource(R.layout.activity_account_settings);
        View v = ((ViewStub) findViewById(R.id.nav_bar_activity_main_layout_stub)).inflate();
        Button logoutButton = (Button) v.findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(SignoutActivity.getLaunchIntent(AccountSettingsActivity.this));
                finish();
            }
        });
    }
}
