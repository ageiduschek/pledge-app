package com.pledgeapp.pledge.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nikhil on 10/14/15.
 */
public class NonProfit {

    private String id;
    private String name;
//    private Category category;
    private int category;
    private String missionStatement;
    private double longitude;
    private double latitude;
//    Address address;
//    String guideStarUrl;
//    String websiteUrl
//    long ein;
//    FeaturedReason featuredReason

    public static NonProfit fromJson(JSONObject nonProfitJson) {
        NonProfit nonProfit = new NonProfit();

        try {
            nonProfit.id = nonProfitJson.getString("id");
            nonProfit.name = nonProfitJson.getString("name");
            nonProfit.category = nonProfitJson.getInt("category");
            nonProfit.missionStatement = nonProfitJson.getString("mission_statement");
            nonProfit.longitude = nonProfitJson.getDouble("longitude");
            nonProfit.latitude = nonProfitJson.getDouble("latitude");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return nonProfit;
    }
}
