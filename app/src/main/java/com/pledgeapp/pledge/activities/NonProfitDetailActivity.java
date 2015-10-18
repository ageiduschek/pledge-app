package com.pledgeapp.pledge.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.pledgeapp.pledge.R;
import com.pledgeapp.pledge.models.NonProfit;

public class NonProfitDetailActivity extends AppCompatActivity {
    private static final String KEY_NON_PROFIT = "key_non_profit";

    public static Intent getLaunchIntent(Context context, NonProfit nonProfit) {

        Intent intent = new Intent(context, NonProfitDetailActivity.class);
        intent.putExtra(KEY_NON_PROFIT, nonProfit);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_profit_detail);
        // Set a Toolbar to replace the ActionBar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        NonProfit nonProfit = getIntent().getParcelableExtra(KEY_NON_PROFIT);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitleEnabled(true);
        collapsingToolbar.setTitle(nonProfit.getName());
    }
}
