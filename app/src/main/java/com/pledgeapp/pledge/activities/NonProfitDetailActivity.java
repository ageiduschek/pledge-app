package com.pledgeapp.pledge.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

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
            getSupportActionBar().setElevation(R.dimen.no_elevation);
        }

        final NonProfit nonProfit = getIntent().getParcelableExtra(KEY_NON_PROFIT);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitleEnabled(true);
        collapsingToolbar.setTitle(nonProfit.getName());

        FloatingActionButton btnDonate = (FloatingActionButton) findViewById(R.id.btnDonate);
        btnDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(DonateActivity.getLaunchIntent(NonProfitDetailActivity.this, nonProfit));
            }
        });

        fillViewWithData(nonProfit);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private void fillViewWithData(final NonProfit nonProfit) {
        TextView tvMissionStatement = (TextView) findViewById(R.id.tvMissionStatement);
        tvMissionStatement.setText(nonProfit.getMissionStatement());

        String formattedAddress = nonProfit.getFormattedAddress();
        TextView tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvAddress.setText(formattedAddress);

        CardView cvGuidestarLink = (CardView) findViewById(R.id.cvGuideStarLink);

        if (!TextUtils.isEmpty(nonProfit.getGuideStarUrl())) {
            cvGuidestarLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse(nonProfit.getGuideStarUrl()); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
        } else {
            cvGuidestarLink.setVisibility(View.GONE);
        }

        TextView tvCategory = (TextView) findViewById(R.id.tvCategory);
        if (nonProfit.getCategoryName() != null) {
            tvCategory.setText(nonProfit.getCategoryName());
        } else {
            findViewById(R.id.tvCategoryTitle).setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
