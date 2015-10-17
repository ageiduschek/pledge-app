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
import com.pledgeapp.pledge.models.SearchSuggestion;

import java.util.List;

public class SearchSuggestionsArrayAdapter extends ArrayAdapter<SearchSuggestion> implements Filterable {
    private static final int LIST_MAX_SIZE = 5;

    private final List<SearchSuggestion> mItems;
    private Filter mFilter;

    public class ViewHolder {
        public TextView suggestionText;
    }

    public SearchSuggestionsArrayAdapter(Context context,
                                         List<SearchSuggestion> objects,
                                         Filter suggestionsFilter) {
        super(context, R.layout.item_search_suggestion, objects);
        mFilter = suggestionsFilter;
        mItems = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_search_suggestion, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.suggestionText = (TextView) convertView.findViewById(R.id.tvSuggestionText);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        SearchSuggestion searchSuggestion = getItem(position);
        if (searchSuggestion.getQueryString() != null) {
            viewHolder.suggestionText.setText(searchSuggestion.getQueryString());
        } else {
            viewHolder.suggestionText.setText(searchSuggestion.getNonProfit().getName());
        }
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    @Override
    public int getCount() {
        return Math.min(LIST_MAX_SIZE, mItems.size());
    }
}
