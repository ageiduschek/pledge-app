package com.pledgeapp.pledge.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.pledgeapp.pledge.R;

public class AddPaymentActivity extends PledgeBaseActivity {

    public static Intent getLaunchIntent(Context context) {
        return new Intent(context, AddPaymentActivity.class);
    }

    protected void showSignedInUI() {
        // TODO: Implement
    }
}
