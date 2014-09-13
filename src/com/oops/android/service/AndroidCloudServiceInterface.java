package com.oops.android.service;

import java.util.ArrayList;

public abstract class AndroidCloudServiceInterface {

	AndroidCloudServiceRequestManager requestManager;

	public abstract AndroidCloudServiceReturnStatus doRequest(String[] input,
			String fileLocation);

	public abstract void setRequestManager(
			AndroidCloudServiceRequestManager requestManager);
}
