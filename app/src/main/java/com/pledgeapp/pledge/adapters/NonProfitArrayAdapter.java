package com.pledgeapp.pledge.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pledgeapp.pledge.R;
import com.pledgeapp.pledge.helpers.GaussianBlurTransformation;
import com.pledgeapp.pledge.models.NonProfit;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class NonProfitArrayAdapter extends ArrayAdapter<NonProfit> {
    private class NonProfitListItemViewHolder {
        private TextView tvName;
        private TextView tvLocation;
        private TextView tvCategory;
    }

    public NonProfitArrayAdapter(Context context, List<NonProfit> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final NonProfit nonProfit = getItem(position);

        NonProfitListItemViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_nonprofit_list, parent, false);

            viewHolder = new NonProfitListItemViewHolder();
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            viewHolder.tvLocation = (TextView) convertView.findViewById(R.id.tvLocation);
            viewHolder.tvCategory = (TextView) convertView.findViewById(R.id.tvCategory);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (NonProfitListItemViewHolder) convertView.getTag();
        }

        viewHolder.tvName.setText(nonProfit.getName());
        viewHolder.tvLocation.setText(nonProfit.getCity() + ", " + nonProfit.getState());
        viewHolder.tvCategory.setText(nonProfit.getCategoryName());

        return convertView;
    }
}
