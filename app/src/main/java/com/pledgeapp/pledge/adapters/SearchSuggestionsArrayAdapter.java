package com.pledgeapp.pledge.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pledgeapp.pledge.R;
import com.pledgeapp.pledge.models.SearchSuggestion;

import java.util.List;

public class SearchSuggestionsArrayAdapter extends ArrayAdapter<SearchSuggestion> {
    private class ViewHolder {
        TextView suggestionText;
    }

    public SearchSuggestionsArrayAdapter(Context context, List<SearchSuggestion> objects) {
        super(context, R.layout.item_search_suggestion, objects);
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
}
