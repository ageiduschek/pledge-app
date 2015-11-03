package com.pledgeapp.pledge.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.pledgeapp.pledge.R;
import com.pledgeapp.pledge.models.NonProfit;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoriesArrayAdapter extends ArrayAdapter<NonProfit.CategoryInfo> implements Filterable {
    private final List<NonProfit.CategoryInfo> mItems;

    private final int[] CATEGORY_IMAGES = {
            R.drawable.art_culture,
            R.drawable.education,
            R.drawable.environment,
            R.drawable.health,
            R.drawable.human_services,
            R.drawable.foreign_affairs,
            R.drawable.public_benefit,
            R.drawable.religion,
            R.drawable.membership,
    };

    public class ViewHolder {
        TextView categoryName;
        ImageView ivCategoryImage;
    }

    public CategoriesArrayAdapter(Context context,
                                  List<NonProfit.CategoryInfo> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        mItems = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_category_list, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.categoryName = (TextView) convertView.findViewById(R.id.tvCategoryName);
            viewHolder.ivCategoryImage = (ImageView) convertView.findViewById(R.id.ivCategoryImage);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        NonProfit.CategoryInfo info = getItem(position);
        viewHolder.categoryName.setText(info.name);
        Picasso.with(getContext())
                .load(CATEGORY_IMAGES[position % CATEGORY_IMAGES.length])
                .into(viewHolder.ivCategoryImage);
        return convertView;
    }
}
