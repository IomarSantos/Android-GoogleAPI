package com.oops.android.service;

import java.net.URI;
import java.net.URISyntaxException;

public class DirectionsService extends AndroidCloudServiceInterface {

	private final String baseURL = "http://maps.googleapis.com/maps/api/directions/json?origin=$&destination=@&sensor=false";

	public DirectionsService() {
	}

	private String createModifiedURL(String origin, String destination) {

		origin = origin.replace(" ", "+");
		destination = destination.replace(" ", "+");
		String temp = baseURL.replace("$", origin);
		temp = temp.replace("@", destination);
		return temp;
	}

	@Override
	public AndroidCloudServiceReturnStatus doRequest(String[] input,
			String fileLocation) {
		String modifiedURL = createModifiedURL(input[0], input[1]);
		return sendDirectionsRequest(modifiedURL, fileLocation);

	}

	public AndroidCloudServiceReturnStatus sendDirectionsRequest(
			String modifiedURL, String saveLocation) {
		try {
			URI requestURI = new URI(modifiedURL);
			URI saveURI = new URI(saveLocation);
			AndroidCloudServiceRequest directionsRequest = new AndroidCloudServiceRequest(
					requestURI, saveURI);
			return requestManager.sendRequest(directionsRequest);
		} catch (URISyntaxException e) {
			e.printStackTrace();

			return AndroidCloudServiceReturnStatus
					.returnUnsuccessful("TTS: bad file save location");
		}
	}

	@Override
	public void setRequestManager(
			AndroidCloudServiceRequestManager requestManager) {
		this.requestManager = new AndroidCloudServiceRequestManagerDirection();
	}
}