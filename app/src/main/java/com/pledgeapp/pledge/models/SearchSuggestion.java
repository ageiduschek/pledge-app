package com.pledgeapp.pledge.models;

/**
 * Model that wraps the possible types that could be in a search result.
 */
public class SearchSuggestion {
    private NonProfit nonProfit;
    private String queryString;

    public SearchSuggestion(NonProfit nonProfit) {
        this.nonProfit = nonProfit;
    }

    public SearchSuggestion(String queryString) {
        this.queryString = queryString;
    }

    public NonProfit getNonProfit() {
        return nonProfit;
    }

    public String getQueryString() {
        return queryString;
    }

}
