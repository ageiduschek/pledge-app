package com.pledgeapp.pledge.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.pledgeapp.pledge.PledgeApplication;
import com.pledgeapp.pledge.R;
import com.pledgeapp.pledge.adapters.CreditCardAdapter;
import com.pledgeapp.pledge.helpers.PledgeModel;
import com.pledgeapp.pledge.models.PledgeCard;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

public class AddPaymentFragment  extends Fragment {
    public interface AddPaymentFragmentListener {
        void onScanCardRequested(Intent launchIntent);
        void onPaymentSuccessfullyAdded();
    }

    public static final int REQUEST_CODE_SCAN_CREDIT_CARD = 4;
    private AddPaymentFragmentListener mListener;

    private FloatingActionButton fabAddCard;

    private List<PledgeCard> creditCards;
    private CreditCardAdapter aCreditCards;

    private PledgeModel mPledgeModel;

    private PledgeModel.OnResultDelegate<List<PledgeCard>> mGetCardsDelegate;
    private List<PledgeModel.OnResultDelegate> mDeleteCardDelegates = new ArrayList<>();

    public static AddPaymentFragment newInstance() {
        
        Bundle args = new Bundle();

        AddPaymentFragment fragment = new AddPaymentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPledgeModel = PledgeApplication.getPledgeModel();

        creditCards = new ArrayList<>();
        aCreditCards = new CreditCardAdapter(getContext(), creditCards);
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchCreditCards(false, false);
    }

    private void fetchCreditCards(boolean forceFetchFromServer, boolean suppressSpinner) {
        mGetCardsDelegate = new PledgeModel.OnResultDelegate<List<PledgeCard>>(getContext(), !suppressSpinner && getUserVisibleHint()) {
            @Override
            public void onQueryComplete(List<PledgeCard> result) {
                super.onQueryComplete(result);

                aCreditCards.clear();
                aCreditCards.addAll(result);
            }
        };

        mPledgeModel.getCreditCards(forceFetchFromServer, mGetCardsDelegate);
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
                // TODO(nikhilb): View the credit card in detail
            }
        });

        lvCreditCards.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final PledgeCard card = (PledgeCard) adapterView.getItemAtPosition(i);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                final CharSequence[] items = { "Yes", "No" };
                builder.setTitle("Delete Credit Card?");
                builder.setCancelable(true);
                builder.setPositiveButton("Delete card", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PledgeModel.OnResultDelegate delegate = new PledgeModel.OnResultDelegate(getContext(), true) {
                            @Override
                            public void onQueryComplete(Object result) {
                                super.onQueryComplete(result);
                                aCreditCards.remove(card);
                                fetchCreditCards(true, true);
                            }
                        };

                        mDeleteCardDelegates.add(delegate);
                        mPledgeModel.deleteCreditCard(card, delegate);
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

                return true;
            }
        });

        lvCreditCards.setEmptyView(v.findViewById(R.id.tvEmpty));

        fabAddCard = (FloatingActionButton) v.findViewById(R.id.fabAddCard);
        fabAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent scanIntent = new Intent(getContext(), CardIOActivity.class);

                scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true);
                scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, true);
                scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, true);
                scanIntent.putExtra(CardIOActivity.EXTRA_USE_PAYPAL_ACTIONBAR_ICON, false);
                scanIntent.putExtra(CardIOActivity.EXTRA_KEEP_APPLICATION_THEME, true);

                mListener.onScanCardRequested(scanIntent);
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
                addCreditCard((CreditCard) data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT));
            } else {
                resultDisplayStr = "Scan was canceled.";
            }
            // do something with resultDisplayStr, maybe display it in a textView
            // resultTextView.setText(resultStr);
        }
        // else handle other activity results
    }

    private void addCreditCard(CreditCard scanResult) {
        aCreditCards.add(PledgeCard.fromCreditCard(scanResult));
        mPledgeModel.addCreditCard(scanResult, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (mListener != null) {
                    // Force server fetch since we just added a new credit card
                    fetchCreditCards(true, true);
                    mListener.onPaymentSuccessfullyAdded();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });
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

    @Override
    public void onPause() {
        super.onPause();
        mGetCardsDelegate.cancel();
        for (PledgeModel.OnResultDelegate delegate : mDeleteCardDelegates) {
            delegate.cancel();
        }
        mDeleteCardDelegates.clear();
    }
}
