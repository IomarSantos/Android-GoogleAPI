package com.oops.android.service;

public interface AndroidCloudServiceRequestSenderInterface {
	AndroidCloudServiceReturnStatus sendRequestToCloud(
			AndroidCloudServiceRequest request);
}
