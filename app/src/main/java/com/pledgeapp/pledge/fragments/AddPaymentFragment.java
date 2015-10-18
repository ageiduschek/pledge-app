package com.pledgeapp.pledge.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pledgeapp.pledge.R;
import com.pledgeapp.pledge.adapters.CreditCardAdapter;

import java.util.ArrayList;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

public class AddPaymentFragment  extends Fragment {
    public interface AddPaymentFragmentListener {
        void onScanCardRequested(Intent launchIntent);
        void onPaymentSuccessfullyAdded();
    }

    public static final int REQUEST_CODE_SCAN_CREDIT_CARD = 4;
    private AddPaymentFragmentListener mListener;

    private ArrayList<CreditCard> creditCards;
    private CreditCardAdapter aCreditCards;

    public static AddPaymentFragment newInstance() {
        
        Bundle args = new Bundle();

        AddPaymentFragment fragment = new AddPaymentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO(nikhilb): fetch from server instead
        creditCards = new ArrayList<>();

        creditCards.add(new CreditCard("4111111111111111", 11, 11, "111", "11111"));

        aCreditCards = new CreditCardAdapter(getContext(), creditCards);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_payment, container, false);

        final ListView lvCreditCards = (ListView) v.findViewById(R.id.lvCreditCards);
        lvCreditCards.setAdapter(aCreditCards);
        lvCreditCards.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == lvCreditCards.getAdapter().getCount() - 1) {
                    // It's the final item, we should add a new credit card
                    Intent scanIntent = new Intent(getContext(), CardIOActivity.class);

                    scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true);
                    scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, true);
                    scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, true);
                    scanIntent.putExtra(CardIOActivity.EXTRA_USE_PAYPAL_ACTIONBAR_ICON, false);
                    scanIntent.putExtra(CardIOActivity.EXTRA_KEEP_APPLICATION_THEME, true);

                    mListener.onScanCardRequested(scanIntent);
                } else {
                    // TODO(nikhilb): View the credit card in detail
                }
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SCAN_CREDIT_CARD) {
            String resultDisplayStr;
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

//                // Never log a raw card number. Avoid displaying it, but if necessary use getFormattedCardNumber()
//                resultDisplayStr = "Card Number: " + scanResult.getRedactedCardNumber() + "\n";
//
//                // Do something with the raw number, e.g.:
//                // myService.setCardNumber( scanResult.cardNumber );
//
//                if (scanResult.isExpiryValid()) {
//                    resultDisplayStr += "Expiration Date: " + scanResult.expiryMonth + "/" + scanResult.expiryYear + "\n";
//                }
//
//                if (scanResult.cvv != null) {
//                    // Never log or display a CVV
//                    resultDisplayStr += "CVV has " + scanResult.cvv.length() + " digits.\n";
//                }
//
//                if (scanResult.postalCode != null) {
//                    resultDisplayStr += "Postal Code: " + scanResult.postalCode + "\n";
//                }

                aCreditCards.add(scanResult);

                mListener.onPaymentSuccessfullyAdded();
            } else {
                resultDisplayStr = "Scan was canceled.";
            }
            // do something with resultDisplayStr, maybe display it in a textView
            // resultTextView.setText(resultStr);
        }
        // else handle other activity results
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (AddPaymentFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                                                 + " must implement OnPaymentMethodSubmittedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
