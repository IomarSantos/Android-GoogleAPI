package com.oops.android.log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import android.os.Environment;

public class AndroidCloudServiceLogReader extends AndroidCloudServiceLog {
	public AndroidCloudServiceLogReader(File logPath) {
		setLogPath(logPath);
		this.setup();
	}

	public ArrayList<String> getAll() {

		ArrayList<String> ret = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(logFile));
			String line;

			while ((line = br.readLine()) != null) {
				ret.add(line);
			}
		} catch (IOException e) {
		}

		return ret;
	}

	public ArrayList<String> getLastTen() {

		ArrayList<String> ret = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(logFile));
			String line;
			int i = 0;
			while ((line = br.readLine()) != null) {
				if (i > 9) {
					ret.remove(i - 10);
					ret.add(i - 1, line);
				} else {
					ret.add(i, line);
					i++;
				}
			}
		} catch (IOException e) {
		}
		return ret;
	}
}