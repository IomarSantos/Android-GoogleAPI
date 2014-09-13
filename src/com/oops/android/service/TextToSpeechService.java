package com.oops.android.service;

import java.net.URI;
import java.net.URISyntaxException;

public abstract class TextToSpeechService extends AndroidCloudServiceInterface {

	protected String baseURL;

	public TextToSpeechService() {
	}

	private String createModifiedURL(String text) {
		text = text.replace(" ", "+");
		return baseURL + text;
	}

	public AndroidCloudServiceReturnStatus sendTTSRequest(String modifiedURL,
			String saveLocation) {
		try {
			URI requestURI = new URI(modifiedURL);
			URI saveURI = new URI(saveLocation);
			AndroidCloudServiceRequest ttsRequest = new AndroidCloudServiceRequest(
					requestURI, saveURI);
			return requestManager.sendRequest(ttsRequest);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return AndroidCloudServiceReturnStatus
					.returnUnsuccessful("TTS: bad file save location");
		}
	}

	@Override
	public AndroidCloudServiceReturnStatus doRequest(String[] input,
			String fileLocation) {
		String modifiedURL = createModifiedURL(input[0]);
		return sendTTSRequest(modifiedURL, fileLocation);
	}

	@Override
	public void setRequestManager(
			AndroidCloudServiceRequestManager requestManager) {
		this.requestManager = new AndroidCloudServiceRequestManagerTTS();
	}
}