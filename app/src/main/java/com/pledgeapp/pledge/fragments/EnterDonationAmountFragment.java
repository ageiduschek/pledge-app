package com.pledgeapp.pledge.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.pledgeapp.pledge.PledgeApplication;
import com.pledgeapp.pledge.R;
import com.pledgeapp.pledge.models.NonProfit;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Fragment for entering a donation amount
 */
public class EnterDonationAmountFragment extends Fragment {

    private static final String KEY_NON_PROFIT = "key_non_profit";
    private static final double MIN_DONATION_AMOUNT = 10.00; // In dollars
    private static final double MAX_DONATION_AMOUNT = 1000.00;
    private onDonationSuccessListener mListener;

    private EditText etDonationAmount;

    public interface onDonationSuccessListener {
        void onDonationSuccess();
    }

    public static EnterDonationAmountFragment newInstance(NonProfit nonProfit) {

        Bundle args = new Bundle();
        args.putParcelable(KEY_NON_PROFIT, nonProfit);

        EnterDonationAmountFragment fragment = new EnterDonationAmountFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: Create view for this fragment!
        View v =  inflater.inflate(R.layout.fragment_enter_amount, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.enter_amount_plea);

        final NonProfit nonProfit = getArguments().getParcelable(KEY_NON_PROFIT);
        if (nonProfit == null) {
            throw new RuntimeException("Must pass nonprofit to this fragment");
        }

        etDonationAmount = (EditText) v.findViewById(R.id.etDonationAmount);
        final Button btnSubmitDonation = (Button) v.findViewById(R.id.btnSubmitDonation);
        btnSubmitDonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSubmitDonation.setEnabled(false);
                final Double donationAmount = getDollarValue(etDonationAmount.getText().toString());

                final ProgressDialog pd = new ProgressDialog(getContext());
                pd.setMessage("Donating $" + String.format("%.2f", donationAmount) + "...");
                pd.setTitle("Donation");
                pd.setCancelable(false);
                pd.show();

                PledgeApplication.getPledgeModel().donate(donationAmount, nonProfit, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        pd.dismiss();
                        Toast.makeText(getContext(), "You donated $" + String.format("%.2f", donationAmount) + "!", Toast.LENGTH_SHORT).show();
                        mListener.onDonationSuccess();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        String errorStr;
                        if (errorResponse != null) {
                            Log.d("NETWORK ERROR", errorResponse.toString());
                            errorStr = "This nonprofit isn't processing donations";
                        } else {
                            errorStr = "Network error";
                        }
                        Toast.makeText(getContext(), errorStr, Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                        btnSubmitDonation.setEnabled(true);
                    }
                });
            }
        });
        btnSubmitDonation.setEnabled(true);

        etDonationAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                etDonationAmount.removeTextChangedListener(this);

                String str = s.toString();
                // Remove commas
                str = str.replaceAll("[$,]", "");

                if (str.length() > 0 && str.charAt(0) == '.') {
                    str = "0.";
                } else if (!str.startsWith("0.")) {
                    // Remove leading 0s, except not the last one if it is all zero
                    str = str.replaceFirst("^0+", "");
                }

                // Always prepend $
                str = "$" + str;

                Log.d("ASDF", "str: " + str);

                int periodIndex = str.indexOf('.');
                if (periodIndex != -1 && periodIndex + 2 < str.length() - 1) {
                    str = str.substring(0, periodIndex + 3);
                }


                etDonationAmount.setText(str);
                etDonationAmount.setSelection(str.length());

                etDonationAmount.addTextChangedListener(this);

                double dollarValue = getDollarValue(str);
                // Disable if it ends with period
                boolean endsWithPeriod = periodIndex == str.length();
                btnSubmitDonation.setEnabled(!endsWithPeriod && dollarValue >= MIN_DONATION_AMOUNT
                        && dollarValue <= MAX_DONATION_AMOUNT);

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        etDonationAmount.setText("$");
        if(etDonationAmount.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }

        TextView tvNonProfitName = (TextView) v.findViewById(R.id.tvNonProfitName);
        tvNonProfitName.setText(nonProfit.getName());
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        etDonationAmount.setFocusableInTouchMode(true);
        etDonationAmount.requestFocus();
    }

    public double getDollarValue(String formattedString) {
        String cleanStringDollars = formattedString.replaceAll("[$,]", "");
        double dollarValue;
        if (!cleanStringDollars.isEmpty()) {
            dollarValue = Double.parseDouble(cleanStringDollars);
        } else {
            dollarValue = 0;
        }

        return Math.round(dollarValue*100.0)/100.0;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (onDonationSuccessListener) activity;
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

    private void showSoftKeyboard(View view){
        InputMethodManager imm =
                (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }
}
