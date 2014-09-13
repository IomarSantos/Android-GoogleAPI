package com.oops.android.service;

import java.net.URI;

/*
 * Since a URI can refer to any location, being a URL or a file directory, I think this class is generic
 * enough to use for any request we may need for any future service --Alberto
 */
public class AndroidCloudServiceRequest extends AndroidCloudServiceMessage {

	private URI resourceLocation;
	private URI saveLocation;

	public URI getResourceLocation() {
		return resourceLocation;
	}

	public URI getSaveLocation() {
		return saveLocation;
	}

	public AndroidCloudServiceRequest(URI resourceLocation, URI saveLocation) {
		this.resourceLocation = resourceLocation;
		this.saveLocation = saveLocation;
	}
}
