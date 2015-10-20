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
import com.pledgeapp.pledge.helpers.Util;
import com.pledgeapp.pledge.models.NonProfit;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

/**
 * Created by nikhil on 10/14/15.
 */
public class NonProfitArrayAdapter extends ArrayAdapter<NonProfit> {
    private class NonProfitListItemViewHolder {
        private TextView tvName;
        private TextView tvLocation;
    }

    public NonProfitArrayAdapter(Context context, List<NonProfit> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final NonProfit nonProfit = getItem(position);

        NonProfitListItemViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.nonprofit_list_item, parent, false);

            viewHolder = new NonProfitListItemViewHolder();
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            viewHolder.tvLocation = (TextView) convertView.findViewById(R.id.tvLocation);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (NonProfitListItemViewHolder) convertView.getTag();
        }

        viewHolder.tvName.setText(Util.titleProperCase(nonProfit.getName()));
        viewHolder.tvLocation.setText(Util.titleProperCase(nonProfit.getCity()) + ", " + nonProfit.getState());

        String imageUrl = getImageUrl(position);

        final View v = convertView;
        Picasso.with(getContext())
               .load(imageUrl)
               .transform(new GaussianBlurTransformation(getContext()))
               .into(new Target() {
                   @Override
                   public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                       v.setBackground(new BitmapDrawable(getContext().getResources(), bitmap));
                   }

                   @Override
                   public void onBitmapFailed(Drawable errorDrawable) {
                       // use error drawable if desired
                   }

                   @Override
                   public void onPrepareLoad(Drawable placeHolderDrawable) {
                       // use placeholder drawable if desired
                   }
               });


        return convertView;
    }

    // TODO: Add more images? Maybe just solid color backgrounds would look better?
    private String getImageUrl(int position) {
        switch (position % 3) {
            case 0: return "http://tcktcktck.org/wp-content/uploads/2013/01/4996107756_020feea7f5_b.jpg";
            case 1: return "http://biofriendly.com/blog/wp-content/uploads/2012/10/2981387336_4b0ebb247f_z-e1349824175975.jpg";
            case 2: return "https://thecringeblog.files.wordpress.com/2014/11/mark-c-austin-crowd-surfing.jpg";
        }

        throw new RuntimeException("No image url corresponding to position = " + position);
    }
}
