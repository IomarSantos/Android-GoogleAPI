package com.oops.android.service;

import java.io.File;
import java.util.ArrayList;

import com.oops.android.log.AndroidCloudServiceLogWriter;

import android.os.Environment;

public class AndroidCloudServiceAdapter {

	private String folder;

	private AndroidCloudServiceLogWriter logWriter;
	private File folderLocation;
	private AndroidCloudServiceInterface service;

	// constructor takes service type

	// get method (name)
	public AndroidCloudServiceAdapter(AndroidCloudServiceInterface service,
			String folder) {

		this.folder = (Environment.getExternalStorageDirectory()).toString()
				+ "/" + folder + "/";

		folderLocation = new File(this.folder);
		folderLocation.mkdirs();
		this.service = service;
		this.service.setRequestManager(null);
		logWriter = new AndroidCloudServiceLogWriter(
				Environment.getExternalStorageDirectory());

	}

	public boolean doRequest(String[] input, String filename) {

		AndroidCloudServiceReturnStatus ret = null;
		AndroidCloudServiceInterface tempService = null;

		File fileSaveLocation = new File(folderLocation, filename);

		ret = service.doRequest(input, fileSaveLocation.toString());

		// logs the success or failure of the request, if failure, perhaps also
		// log the message that ret contains
		if (ret.isSuccess()) {
			logWriter.appendLog(AndroidCloudServiceAdapter.class.getName(),
					"Request succeeded");
		} else {
			String reason = ret.getReason();
			logWriter.appendLog(AndroidCloudServiceAdapter.class.getName(),
					"Request failed for reason: " + reason);
		}
		return ret.isSuccess();
	}
}