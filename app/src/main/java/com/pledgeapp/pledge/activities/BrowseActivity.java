package com.pledgeapp.pledge.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class BrowseActivity extends NavigationBarActivity {
    public static Intent getLaunchIntent(Context context) {
        return new Intent(context, BrowseActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
