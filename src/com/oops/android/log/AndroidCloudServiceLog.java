package com.oops.android.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.os.Environment;
import android.util.Log;

public class AndroidCloudServiceLog {

	protected static File logFile;
	private static AndroidCloudServiceLog log;

	protected AndroidCloudServiceLog() {
	}

	public static AndroidCloudServiceLog getInstance() {
		if (log == null) {
			log = new AndroidCloudServiceLog();
		}
		return log;
	}

	protected void setup() {

		if (!logFile.exists()) {
			try {
				logFile.createNewFile();
				BufferedWriter buf = new BufferedWriter(new FileWriter(logFile,
						true));
				buf.append("========= Android Cloud Service Log File Begin =========");
				buf.newLine();
				buf.flush();
				buf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void setLogPath(File logPath) {
		logFile = new File(logPath, "log.file");
		setup();
	}
}