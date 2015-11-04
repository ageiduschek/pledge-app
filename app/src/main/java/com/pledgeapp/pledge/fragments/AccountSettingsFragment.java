package com.pledgeapp.pledge.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pledgeapp.pledge.PledgeApplication;
import com.pledgeapp.pledge.R;
import com.pledgeapp.pledge.activities.LogoutActivity;
import com.pledgeapp.pledge.models.User;


public class AccountSettingsFragment extends Fragment {

    private Context mContext;

    public static AccountSettingsFragment newInstance() {

        Bundle args = new Bundle();

        AccountSettingsFragment fragment = new AccountSettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_account_settings, container, false);

        TextView tvName = (TextView) v.findViewById(R.id.tvName);
        User loggedInUser = PledgeApplication.getPledgeModel().getUser();
        tvName.setText(loggedInUser.getFirstName() + " " + loggedInUser.getLastName());

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button logoutButton = (Button) view.findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(LogoutActivity.getLaunchIntent(mContext));
                getActivity().finish();
            }
        });
    }
}
