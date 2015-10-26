package com.pledgeapp.pledge.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.pledgeapp.pledge.PledgeApplication;
import com.pledgeapp.pledge.R;
import com.pledgeapp.pledge.fragments.AddPaymentFragment;
import com.pledgeapp.pledge.fragments.EnterDonationAmountFragment;
import com.pledgeapp.pledge.helpers.PledgeModel;
import com.pledgeapp.pledge.models.NonProfit;
import com.pledgeapp.pledge.models.PledgeCard;

import java.util.List;

public class DonateActivity extends AppCompatActivity implements AddPaymentFragment.AddPaymentFragmentListener,
                                                                 EnterDonationAmountFragment.onDonationSuccessListener {
    private static final String KEY_NON_PROFIT = "key_non_profit";

    private NonProfit mNonProfit;
    private AddPaymentFragment mAddPaymentFragment;

    public static Intent getLaunchIntent(Context context, NonProfit nonProfit) {

        Intent intent = new Intent(context, DonateActivity.class);
        intent.putExtra(KEY_NON_PROFIT, nonProfit);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_nav);


        if (savedInstanceState == null) {
            mNonProfit = getIntent().getParcelableExtra(KEY_NON_PROFIT);

            PledgeApplication.getPledgeModel().getCreditCards(false, new PledgeModel.OnResultDelegate<List<PledgeCard>>(this, true) {
                @Override
                public void onQueryComplete(List<PledgeCard> result) {
                    super.onQueryComplete(result);

                    Fragment fragment;
                    if (!result.isEmpty()) {
                        NonProfit nonProfit = getIntent().getParcelableExtra(KEY_NON_PROFIT);
                        fragment = EnterDonationAmountFragment.newInstance(nonProfit);
                    } else {
                        mAddPaymentFragment = AddPaymentFragment.newInstance();
                        fragment = mAddPaymentFragment;
                    }

                    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                    setSupportActionBar(toolbar);
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    }

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.flFragmentContainer, fragment).commit();
                }
            });
        }

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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

    @Override
    public void onDonationSuccess() {
        finish();
    }

    @Override
    public void onScanCardRequested(Intent launchIntent) {
        startActivityForResult(launchIntent, AddPaymentFragment.REQUEST_CODE_SCAN_CREDIT_CARD);
    }

    @Override
    public void onPaymentSuccessfullyAdded() {
        Fragment fragment = EnterDonationAmountFragment.newInstance(mNonProfit);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flFragmentContainer, fragment).commit();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AddPaymentFragment.REQUEST_CODE_SCAN_CREDIT_CARD) {
            mAddPaymentFragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
