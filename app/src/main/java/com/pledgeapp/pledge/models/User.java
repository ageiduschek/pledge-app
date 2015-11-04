package com.pledgeapp.pledge.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nikhil on 10/24/15.
 */
public class User {
    private String userId;
    private String email;
    private String firstName;
    private String lastName;

    public static User fromJson(JSONObject userJson) {
        User user = new User();
        try {
            user.userId = userJson.getString("_id");
            user.email = userJson.getString("email");
            user.firstName = userJson.getString("first_name");
            user.lastName = userJson.getString("last_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
