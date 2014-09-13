package com.oops.android.service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class AndroidCloudServiceRequestManagerDirection extends
		AndroidCloudServiceRequestManager {

	@Override
	AndroidCloudServiceReturnStatus sendRequest(
			AndroidCloudServiceRequest request) {
		try {
			URL u = new URL(request.getResourceLocation().toString());

			URLConnection c = u.openConnection();
			c.addRequestProperty("User-Agent", "Mozilla");
			InputStream data = c.getInputStream();
			BufferedInputStream BIS = new BufferedInputStream(data);
			try {
				// reading the stream from server and storing it in string
				// format.
				InputStream in = c.getInputStream();

				BufferedReader BR = new BufferedReader(new InputStreamReader(
						BIS));
				StringBuilder SB = new StringBuilder();
				String line1 = null;
				while ((line1 = BR.readLine()) != null) {
					SB.append(line1 + "\n");
				}
				BR.close();
				String line = SB.toString();

				// parsing the JSONObject to get the route.
				String finalString = new String("");
				JSONObject json = new JSONObject(line);
				JSONArray gson = json.getJSONArray("routes");
				JSONObject kson = gson.getJSONObject(0);
				JSONArray lson = kson.getJSONArray("legs");
				JSONObject ason = lson.getJSONObject(0);
				JSONArray bson = ason.getJSONArray("steps");
				for (int i = 0; i < bson.length(); i++) {
					JSONObject cson = bson.getJSONObject(i);
					String s = cson.getString("html_instructions");
					int k = 0;

					// removing special characters from string.

					for (int j = 0; j < s.length(); j++) {
						if (s.charAt(j) == '<')
							k = j;
						if (s.charAt(j) == '>') {
							s = s.substring(0, k) + " " + s.substring(j + 1);
							j = -1;
						}
					}
					finalString += s + "\n";
				}

				in = new ByteArrayInputStream(finalString.getBytes("UTF-8"));

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