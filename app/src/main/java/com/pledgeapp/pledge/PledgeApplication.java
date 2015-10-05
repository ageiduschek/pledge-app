package com.pledgeapp.pledge;

import android.content.Context;

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

	@Override
	public void onCreate() {
		super.onCreate();
		PledgeApplication.context = this;
	}

	public static PledgeClient getPledgeClient() {
		return (PledgeClient) PledgeClient.getInstance(PledgeClient.class, PledgeApplication.context);
	}
}