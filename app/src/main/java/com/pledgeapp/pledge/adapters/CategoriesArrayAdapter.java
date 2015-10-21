package com.pledgeapp.pledge.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.pledgeapp.pledge.R;
import com.pledgeapp.pledge.models.NonProfit;
import com.pledgeapp.pledge.models.SearchSuggestion;

import java.util.List;

public class CategoriesArrayAdapter extends ArrayAdapter<NonProfit.CategoryInfo> implements Filterable {
    private final List<NonProfit.CategoryInfo> mItems;

    public class ViewHolder {
        public TextView categoryName;
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
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        NonProfit.CategoryInfo info = getItem(position);
        viewHolder.categoryName.setText(info.name);
        return convertView;
    }
}
