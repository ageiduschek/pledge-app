package com.pledgeapp.pledge.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.pledgeapp.pledge.helpers.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by nikhil on 10/14/15.
 */
public class NonProfit implements Parcelable {
    private String id;
    private String name;
    private String nteeCode;
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
            nonProfit.nteeCode = nonProfitJson.getString("category");
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
        return Util.titleProperCase(name);
    }

    public String getMissionStatement() {
        return missionStatement;
    }

    public String getAddress() {
        return Util.titleProperCase(address);
    }

    public String getCity() {
        return Util.titleProperCase(city);
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
        dest.writeString(this.nteeCode);
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
        this.nteeCode = in.readString();
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

    private static ArrayList<CategoryInfo> sMajorCategoryInfo = new ArrayList<>();
    public static void saveMajorCategoryInfoFromJSON(JSONArray response) {
        sMajorCategoryInfo.clear();
        for (int i = 0; i < response.length(); i++) {
            try {
                JSONObject categoryJson = response.getJSONObject(i);
                JSONArray subCategoriesJson = categoryJson.getJSONArray("subcategories");
                ArrayList<String> subCategories = new ArrayList<>();
                for(int j = 0; j < subCategoriesJson.length(); j++){
                    subCategories.add(subCategoriesJson.getString(j));
                }

                sMajorCategoryInfo.add(new CategoryInfo(categoryJson.getInt("index"),
                                                        categoryJson.getString("name"),
                                                        subCategories));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static HashMap<String, HashMap<String, String>> sNTEECodeToName = new HashMap<>();
    public static void saveSubCategoryInfoFromJSON(JSONObject response) {
        sNTEECodeToName.clear();
        Iterator<String> it = response.keys();
        while (it.hasNext()) {
            String subCategory = it.next();
            sNTEECodeToName.put(subCategory, new HashMap<String, String>());
            try {
                JSONObject subCategoryJson = response.getJSONObject(subCategory);
                Iterator<String> minorCategoryIt = subCategoryJson.keys();
                while (minorCategoryIt.hasNext()) {
                    String minorCategory = minorCategoryIt.next();
                    sNTEECodeToName.get(subCategory).put(minorCategory, subCategoryJson.getString(minorCategory));
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String getCategoryName() {
        if (nteeCode != null) {
            String subcategory = nteeCode.substring(0, 1).toUpperCase();
            // Strip all non-numeric chars
            String section = nteeCode.replaceAll("[^\\d]", "");
            // Trim leading zeros until we're at 2 chars
            section = section.length() > 2 ? section.replaceFirst("^0", "") : section;
            section = section.length() > 2 ? section.replaceFirst("0$", "") : section;
            if (sNTEECodeToName.get(subcategory) != null) {

                String minorCategory = sNTEECodeToName.get(subcategory).get(subcategory+section);
                if (minorCategory != null) {
                    return minorCategory;
                } else {
                    for (CategoryInfo categoryInfo : sMajorCategoryInfo) {
                        for (String s : categoryInfo.subCategories) {
                            if (s.equals(subcategory)) {
                                return categoryInfo.name;
                            }
                        }
                    }
                }
            }
        }

        return null;
    }


    public static ArrayList<CategoryInfo> getMajorCategoryInfo() {
        return sMajorCategoryInfo;
    }

    public String getFormattedAddress() {
        return getAddress() + "\n" + getCity() + ", " + getState();
    }

    // TODO: get a shorter name for each category for displaying in titles / search hint
    public static class CategoryInfo implements Parcelable {
        public int searchIndex;
        public String name;
        public ArrayList<String> subCategories;

        private CategoryInfo(int searchIndex, String name, ArrayList<String> subCategories) {
            this.searchIndex = searchIndex;
            this.name = name;
            this.subCategories = subCategories;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.searchIndex);
            dest.writeString(this.name);
            dest.writeSerializable(this.subCategories);
        }

        private CategoryInfo(Parcel in) {
            this.searchIndex = in.readInt();
            this.name = in.readString();
            this.subCategories = (ArrayList<String>) in.readSerializable();
        }

        public static final Creator<CategoryInfo> CREATOR = new Creator<CategoryInfo>() {
            public CategoryInfo createFromParcel(Parcel source) {
                return new CategoryInfo(source);
            }

            public CategoryInfo[] newArray(int size) {
                return new CategoryInfo[size];
            }
        };
    }
}
