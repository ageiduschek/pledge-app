package com.pledgeapp.pledge.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pledgeapp.pledge.R;
import com.pledgeapp.pledge.models.PledgeCard;

import java.util.List;

import io.card.payment.CardType;

/**
 * Created by nikhil on 11/1/15.
 */
public class CreditCardAdapter extends ArrayAdapter<PledgeCard> {

    private class CreditCardViewHolder {
        TextView tvCreditCardNumber;
        TextView tvExpirationDate;
        ImageView ivCardType;
    }

    public CreditCardAdapter(Context context, List<PledgeCard> objects) {
        super(context, 0, objects);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        PledgeCard card = getItem(position);

        CreditCardViewHolder viewHolder;
        if (convertView != null) {
            viewHolder = (CreditCardViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.credit_card_list_item, parent, false);
            viewHolder = new CreditCardViewHolder();
            viewHolder.tvCreditCardNumber = (TextView) convertView.findViewById(R.id.tvCreditCardNumber);
            viewHolder.tvExpirationDate = (TextView) convertView.findViewById(R.id.tvExpirationDate);
            viewHolder.ivCardType = (ImageView) convertView.findViewById(R.id.ivCardType);

            convertView.setTag(viewHolder);
        }

        viewHolder.tvCreditCardNumber.setText("**** **** **** " + card.getCardSuffix());

        String expirationDate = card.getExpiryMonth() + "/" + card.getExpiryYear();
        viewHolder.tvExpirationDate.setText(expirationDate);

        viewHolder.ivCardType.setImageResource(android.R.color.transparent);

        String cardType = card.getType();
        if (cardType.equals("Visa")) {
            viewHolder.ivCardType.setImageBitmap(CardType.VISA.imageBitmap(getContext()));
        } else if (card.getType().equals("Mastercard")) {
            viewHolder.ivCardType.setImageBitmap(CardType.MASTERCARD.imageBitmap(getContext()));
        } else if (card.getType().equals("Amex")) {
            viewHolder.ivCardType.setImageBitmap(CardType.AMEX.imageBitmap(getContext()));
        } else {
            viewHolder.ivCardType.setImageBitmap(CardType.UNKNOWN.imageBitmap(getContext()));
        }

        return convertView;
    }
}
