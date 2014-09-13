package com.oops.android.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import android.os.Environment;
import android.text.format.Time;

public class AndroidCloudServiceLogWriter extends AndroidCloudServiceLog {
	public AndroidCloudServiceLogWriter(File logPath) {
		this.setLogPath(logPath);
		this.setup();
	}

	public void appendLog(String className, String text) {

		if (!logFile.exists()) {
			this.setup();
		}
		try {
			// BufferedWriter for performance, true to set append to file flag
			GregorianCalendar now = new GregorianCalendar();

			StringBuilder sb = new StringBuilder();
			sb.append(now.get(Calendar.HOUR_OF_DAY));
			sb.append(":");
			sb.append(now.get(Calendar.MINUTE));
			sb.append(":");
			sb.append(now.get(Calendar.SECOND));

			sb.append(" -- ");
			sb.append(className);
			sb.append(": ");
			sb.append(text);
			BufferedWriter buf = new BufferedWriter(new FileWriter(logFile,
					true));
			buf.append(sb.toString());
			buf.newLine();
			buf.flush();
			buf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
