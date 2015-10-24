package com.pledgeapp.pledge.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pledgeapp.pledge.R;
import com.pledgeapp.pledge.models.PledgeCard;

import java.util.List;

import io.card.payment.CardType;

/**
 * Created by nikhil on 10/18/15.
 */
public class CreditCardAdapter extends BaseAdapter {

    private class CreditCardViewHolder {
        TextView tvCreditCardNumber;
        TextView tvExpirationDate;
        ImageView ivCardType;
    }

    private List<PledgeCard> creditCardList;
    private Context mContext;

    public CreditCardAdapter(Context context, List<PledgeCard> objects) {
        mContext = context;
        creditCardList = objects;
    }

    @Override
    public int getCount() {
        return creditCardList.size() + 1;
    }

    @Override
    public Object getItem(int i) {
        if (i < creditCardList.size()) {
            return creditCardList.get(i);
        }

        return null;
    }

    @Override
    public long getItemId(int i) {
        // Not meaningfully needed

        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (getItemViewType(position) == 1) {
            // This is the add credit card button
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.request_credit_card, parent, false);
            }
        } else {
            // This is the credit card view
            PledgeCard card = (PledgeCard) getItem(position);

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
        }

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return position < creditCardList.size() ? 0 : 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    public Context getContext() {
        return mContext;
    }

    public void addAll(List<PledgeCard> result) {
        creditCardList.addAll(result);
        notifyDataSetChanged();
    }

    public void clear() {
        creditCardList.clear();
        notifyDataSetChanged();
    }
}
