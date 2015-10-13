package com.pledgeapp.pledge.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.pledgeapp.pledge.R;

public class LinkEmployerActivity extends PledgeBaseActivity {
    public static Intent getLaunchIntent(Context context) {
        return new Intent(context, LinkEmployerActivity.class);
    }

    protected void showSignedInUI() {
        // TODO: Implement
    }
}
