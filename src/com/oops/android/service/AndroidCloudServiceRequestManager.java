package com.oops.android.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public abstract class AndroidCloudServiceRequestManager {

	public AndroidCloudServiceRequestManager() {
	}

	abstract AndroidCloudServiceReturnStatus sendRequest(
			AndroidCloudServiceRequest request);

	protected static boolean writeToSaveLocation(InputStream in, String filePath) {

		try {

			FileOutputStream fos = new FileOutputStream(new File(filePath));

			try {
				byte[] buffer = new byte[1024];
				int d = 0;

				while ((d = in.read(buffer)) > 0) {
					fos.write(buffer, 0, d);
				}

				fos.close();
				return true;

			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}