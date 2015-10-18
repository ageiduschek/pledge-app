package com.pledgeapp.pledge.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.pledgeapp.pledge.R;
import com.pledgeapp.pledge.fragments.AddPaymentFragment;
import com.pledgeapp.pledge.fragments.EnterDonationAmountFragment;
import com.pledgeapp.pledge.models.NonProfit;

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
        setContentView(R.layout.activity_donate);


        if (savedInstanceState == null) {
            mNonProfit = getIntent().getParcelableExtra(KEY_NON_PROFIT);
            boolean userHasEnteredCreditCardInfo = false; // TODO: change to actually check
            Fragment fragment;
            if (userHasEnteredCreditCardInfo) {
                NonProfit nonProfit = getIntent().getParcelableExtra(KEY_NON_PROFIT);
                fragment = EnterDonationAmountFragment.newInstance(nonProfit);
            } else {
                mAddPaymentFragment = AddPaymentFragment.newInstance();
                fragment = mAddPaymentFragment;
            }

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flFragmentContainer, fragment).commit();
        }

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
    public void onPaymentSuccessfullySubmitted() {
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
