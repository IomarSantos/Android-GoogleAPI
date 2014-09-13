package com.oops.android.service;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

public class AndroidCloudServiceRequestManagerTTS extends
		AndroidCloudServiceRequestManager {

	@Override
	AndroidCloudServiceReturnStatus sendRequest(
			AndroidCloudServiceRequest request) {
		try {
			URL u = new URL(request.getResourceLocation().toString());
			HttpURLConnection c = (HttpURLConnection) u.openConnection();
			c.setRequestMethod("GET");
			c.setDoOutput(true);
			c.connect();
			try {
				InputStream in = c.getInputStream();
				if (writeToSaveLocation(in, request.getSaveLocation()
						.toString())) {
					return AndroidCloudServiceReturnStatus
							.returnSuccessful(request.getSaveLocation());
				}
				return AndroidCloudServiceReturnStatus
						.returnUnsuccessful("TTS: failed to write data to file");
			} catch (Exception e) {
				e.printStackTrace();
				Log.d("netY", e.getMessage(), e);
				return AndroidCloudServiceReturnStatus
						.returnUnsuccessful("TTS: failed to get HTTP input stream ");
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.d("netY", e.getMessage(), e);
			return AndroidCloudServiceReturnStatus
					.returnUnsuccessful("TTS: failed to connect to URL location via HTTP");
		}
	}
}