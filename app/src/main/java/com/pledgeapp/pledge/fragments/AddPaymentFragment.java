package com.pledgeapp.pledge.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.pledgeapp.pledge.R;

import io.card.payment.CardIOActivity;

public class AddPaymentFragment  extends Fragment {
    public static AddPaymentFragment newInstance() {
        
        Bundle args = new Bundle();
        
        AddPaymentFragment fragment = new AddPaymentFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_payment, container, false);

        Button scanButton = (Button) v.findViewById(R.id.btScanCreditCard);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent scanIntent = new Intent(getContext(), CardIOActivity.class);

                scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true);
                scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, true);
                scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, true);
                scanIntent.putExtra(CardIOActivity.EXTRA_USE_PAYPAL_ACTIONBAR_ICON, false);
                scanIntent.putExtra(CardIOActivity.EXTRA_KEEP_APPLICATION_THEME, true);

                getActivity().startActivityForResult(scanIntent, 4);
            }
        });

        return v;
    }
}
