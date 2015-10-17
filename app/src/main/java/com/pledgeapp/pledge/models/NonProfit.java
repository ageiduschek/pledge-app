package com.pledgeapp.pledge.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nikhil on 10/14/15.
 */
public class NonProfit {
    private String id;
    private String name;
//    private Category category;
    private int category;
    private String missionStatement;
    private String address;
    private String city;
    private String state;
    private String guideStarUrl;
    private String websiteUrl;
    private long ein;
//    FeaturedReason featuredReason

    public static NonProfit fromJson(JSONObject nonProfitJson) {
        NonProfit nonProfit = new NonProfit();

        try {
            nonProfit.id = nonProfitJson.getString("id");
            nonProfit.name = nonProfitJson.getString("name");
            nonProfit.category = nonProfitJson.getInt("category");
            nonProfit.missionStatement = nonProfitJson.getString("mission_statement");
            nonProfit.address = nonProfitJson.getString("address");
            nonProfit.city = nonProfitJson.getString("city");
            nonProfit.state = nonProfitJson.getString("state");
            nonProfit.guideStarUrl = nonProfitJson.getString("guidestar_url");
            nonProfit.websiteUrl = nonProfitJson.getString("website_url");
            nonProfit.ein = nonProfitJson.getLong("ein");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return nonProfit;
    }

    public static List<NonProfit> fromJSONArray(JSONArray array) {
        List<NonProfit> nonProfits = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject nonProfitJson = array.getJSONObject(i);
                nonProfits.add(NonProfit.fromJson(nonProfitJson));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return nonProfits;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCategory() {
        return category;
    }

    public String getMissionStatement() {
        return missionStatement;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getGuideStarUrl() {
        return guideStarUrl;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public long getEin() {
        return ein;
    }
}
