package com.pledgeapp.pledge.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class LinkEmployerActivity extends NavigationBarActivity {
    public static Intent getLaunchIntent(Context context) {
        return new Intent(context, LinkEmployerActivity.class);
    }

    @Override
    protected void onAuthGranted(Bundle savedInstanceState) {
        // TODO: Implement
    }
}
