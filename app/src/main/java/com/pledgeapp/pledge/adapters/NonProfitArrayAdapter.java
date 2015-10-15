package com.pledgeapp.pledge.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.pledgeapp.pledge.models.NonProfit;

import java.util.List;

/**
 * Created by nikhil on 10/14/15.
 */
public class NonProfitArrayAdapter extends ArrayAdapter<NonProfit> {
    public NonProfitArrayAdapter(Context context, List<NonProfit> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }
}
