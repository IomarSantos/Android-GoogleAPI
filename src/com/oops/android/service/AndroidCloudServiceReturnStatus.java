package com.oops.android.service;

import java.net.URI;

public class AndroidCloudServiceReturnStatus extends AndroidCloudServiceMessage {
	private final boolean success;

	// Empty string if the operation was successful
	private final String reason;

	protected AndroidCloudServiceReturnStatus(URI saveLocation,
			boolean success, String reason) {
		this.success = success;
		this.reason = reason;
	}

	public static AndroidCloudServiceReturnStatus returnSuccessful(
			URI saveLocation) {
		return new AndroidCloudServiceReturnStatus(saveLocation, true, "");
	}

	/*
	 * I don't know if you can use an empty string to create a URI, so I'm
	 * passing a null reference for the URI. I guess we'll have the convention
	 * that is success is false, the URI will be null, so it's clear nothing was
	 * saved.
	 */
	public static AndroidCloudServiceReturnStatus returnUnsuccessful(
			String reason) {
		return new AndroidCloudServiceReturnStatus(null, false, reason);
	}

	public boolean isSuccess() {
		return success;
	}

	public String getReason() {
		return reason;
	}
}