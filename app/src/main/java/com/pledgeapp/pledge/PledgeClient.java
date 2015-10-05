package com.pledgeapp.pledge;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.GoogleApi;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 *
 * 
 */
public class PledgeClient extends OAuthBaseClient {
	public static final Class<? extends Api> AUTH_REST_API_CLASS = GoogleApi.class;
	public static final String PLEDGE_SERVICE_REST_URL = "https://api.pledgeapp.com"; // Change this, base API URL
	public static final String AUTH_REST_CONSUMER_KEY = "SOME_KEY";       // Change this
	public static final String AUTH_REST_CONSUMER_SECRET = "SOME_SECRET"; // Change this
	public static final String AUTH_REST_CALLBACK_URL = "oauth://pledgerest";

	public PledgeClient(Context context) {
		super(context, AUTH_REST_API_CLASS, PLEDGE_SERVICE_REST_URL, AUTH_REST_CONSUMER_KEY, AUTH_REST_CONSUMER_SECRET, AUTH_REST_CALLBACK_URL);
	}

	public void search(AsyncHttpResponseHandler handler) {
		// This should return the the api
		String apiUrl = getApiUrl("/search");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();


		client.get(apiUrl, params, handler);
	}

	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}