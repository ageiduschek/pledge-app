package com.pledgeapp.pledge;

import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;
import com.pledgeapp.pledge.helpers.PledgeModel;

/*
 * This is the Android application itself and is used to configure various settings
 * including the image cache in memory and on disk. This also adds a singleton
 * for accessing the relevant rest client.
 *
 *     RestClient client = RestApplication.getPledgeClient();
 *     // use client to send requests to API
 *
 */
public class PledgeApplication extends com.activeandroid.app.Application {
	private static Context context;
	private static PledgeModel pledgeModel;

	@Override
	public void onCreate() {
		super.onCreate();
		PledgeApplication.context = this;
		PledgeApplication.pledgeModel = new PledgeModel(context);
	}

	private static GoogleApiClient mGoogleApiClient;
	public static void setGoogleApiClient(GoogleApiClient client) {
		mGoogleApiClient = client;
	}

	public static GoogleApiClient getGoogleApiClient() {
		return mGoogleApiClient;
	}

	public static PledgeModel getPledgeModel() {
		return pledgeModel;
	}
}