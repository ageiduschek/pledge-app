package com.pledgeapp.pledge.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nikhil on 10/24/15.
 */
public class PledgeCard {
    private String cardSuffix;
    private int expiryYear;
    private int expiryMonth;
    private String type;
    private String id;

    public static PledgeCard fromJson(JSONObject cardObject) {
        PledgeCard card = new PledgeCard();

        try {
            card.cardSuffix = cardObject.getString("Suffix");
            card.id = cardObject.getString("Id");
            card.type = cardObject.getString("Type");

            JSONObject expiration = cardObject.getJSONObject("Expiration");
            card.expiryYear = expiration.getInt("year");
            card.expiryMonth = expiration.getInt("month");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return card;
    }

    public String getCardSuffix() {
        return cardSuffix;
    }

    public int getExpiryYear() {
        return expiryYear;
    }

    public int getExpiryMonth() {
        return expiryMonth;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }
}
