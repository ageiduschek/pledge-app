package com.pledgeapp.pledge.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by nikhil on 10/25/15.
 */
public class Donation {
    private double amount;
    private GregorianCalendar date;
    private String nonProfitName;

    public static Donation fromJson(JSONObject donationObject) {
        Donation donation = new Donation();

        try {
            donation.amount = donationObject.getDouble("amount");

            String date = donationObject.getString("donationDate");
            int firstSeparator = date.indexOf('-');
            int secondSeparator = date.indexOf('-', firstSeparator+1);
            int thirdSeparator = date.indexOf('T', secondSeparator + 1);
            int year = Integer.parseInt(date.substring(0, firstSeparator));
            int month = Integer.parseInt(date.substring(firstSeparator + 1, secondSeparator)) - 1;
            int day = Integer.parseInt((date.substring(secondSeparator + 1, thirdSeparator)));

            donation.date = new GregorianCalendar(year, month, day);

            JSONObject organization = donationObject.getJSONObject("organization");
            donation.nonProfitName = organization.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return donation;
    }

    public static List<Donation> fromJsonArray(JSONArray resultJSON) {
        ArrayList<Donation> donations = new ArrayList<>();

        for (int i = 0; i < resultJSON.length(); i++) {
            try {
                donations.add(Donation.fromJson(resultJSON.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return donations;
    }

    public double getAmount() {
        return amount;
    }

    public GregorianCalendar getDate() {
        return date;
    }

    public String getNonProfitName() {
        return nonProfitName;
    }
}
