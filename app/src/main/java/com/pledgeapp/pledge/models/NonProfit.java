package com.pledgeapp.pledge.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nikhil on 10/14/15.
 */
public class NonProfit implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.category);
        dest.writeString(this.missionStatement);
        dest.writeString(this.address);
        dest.writeString(this.city);
        dest.writeString(this.state);
        dest.writeString(this.guideStarUrl);
        dest.writeString(this.websiteUrl);
        dest.writeLong(this.ein);
    }

    public NonProfit() {
    }

    private NonProfit(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.category = in.readInt();
        this.missionStatement = in.readString();
        this.address = in.readString();
        this.city = in.readString();
        this.state = in.readString();
        this.guideStarUrl = in.readString();
        this.websiteUrl = in.readString();
        this.ein = in.readLong();
    }

    public static final Parcelable.Creator<NonProfit> CREATOR = new Parcelable.Creator<NonProfit>() {
        public NonProfit createFromParcel(Parcel source) {
            return new NonProfit(source);
        }

        public NonProfit[] newArray(int size) {
            return new NonProfit[size];
        }
    };
}
