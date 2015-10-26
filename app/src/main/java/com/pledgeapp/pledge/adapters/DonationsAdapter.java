package com.pledgeapp.pledge.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pledgeapp.pledge.R;
import com.pledgeapp.pledge.models.Donation;

import java.text.DateFormat;
import java.util.List;

/**
 * Created by nikhil on 10/25/15.
 */
public class DonationsAdapter extends ArrayAdapter<Donation> {
    private class DonationViewHolder {
        TextView tvDonationAmount;
        TextView tvDonationDate;
        TextView tvNonProfitName;
    }

    public DonationsAdapter(Context context, List<Donation> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Donation donation = getItem(position);

        DonationViewHolder donationViewHolder;
        if (convertView != null) {
            donationViewHolder = (DonationViewHolder) convertView.getTag();
        } else {
            donationViewHolder = new DonationViewHolder();

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.donation_list_item, parent, false);
            donationViewHolder.tvDonationAmount = (TextView) convertView.findViewById(R.id.tvDonationAmount);
            donationViewHolder.tvDonationDate = (TextView) convertView.findViewById(R.id.tvDonationDate);
            donationViewHolder.tvNonProfitName = (TextView) convertView.findViewById(R.id.tvNonProfitName);

            convertView.setTag(donationViewHolder);
        }

        donationViewHolder.tvDonationAmount.setText("$" + String.format("%.2f", donation.getAmount()));
        donationViewHolder.tvDonationDate.setText(DateFormat.getDateInstance().format(donation.getDate().getTime()));
        donationViewHolder.tvNonProfitName.setText(donation.getNonProfitName());

        return convertView;
    }
}
