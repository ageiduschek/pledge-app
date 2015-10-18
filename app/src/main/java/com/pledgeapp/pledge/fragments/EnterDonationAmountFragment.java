package com.pledgeapp.pledge.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.pledgeapp.pledge.R;
import com.pledgeapp.pledge.models.NonProfit;

/**
 * Fragment for entering a donation amount
 */
public class EnterDonationAmountFragment extends Fragment {

    private onDonationSuccessListener mListener;

    public interface onDonationSuccessListener {
        void onDonationSuccess();
    }

    public static EnterDonationAmountFragment newInstance(NonProfit nonProfit) {

        Bundle args = new Bundle();

        EnterDonationAmountFragment fragment = new EnterDonationAmountFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: Create view for this fragment!
        View v =  inflater.inflate(R.layout.fragment_enter_amount, container, false);
        Button btnSubmitDonation = (Button) v.findViewById(R.id.btnSubmitDonation);
        btnSubmitDonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDonationSuccess();
            }
        });

        return v;
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
}
