package com.oops.android.service;

import java.util.ArrayList;

import com.oops.android.log.AndroidCloudServiceLogReader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class OldMainActivity extends Activity implements OnClickListener {

	private TextView logTV;
	private Button spanishButton;
	private Button englishButton;
	private EditText spanishET;
	private EditText englishET;
	private AndroidCloudServiceAdapter engTTS;
	private AndroidCloudServiceAdapter spnTTS;
	private MediaPlayer mp;
	private Context myContext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		myContext = this;
		this.mp = new MediaPlayer();
		engTTS = new AndroidCloudServiceAdapter(
				new TextToSpeechServiceEnglish(), "newdir");
		spnTTS = new AndroidCloudServiceAdapter(
				new TextToSpeechServiceSpanish(), "newdir");
		spanishButton = (Button) findViewById(R.id.spanishButton);
		englishButton = (Button) findViewById(R.id.englishButton);
		spanishET = (EditText) findViewById(R.id.spanishET);
		englishET = (EditText) findViewById(R.id.englishET);

		logTV = (TextView) findViewById(R.id.logTV);
		logTV.setMovementMethod(new ScrollingMovementMethod());
		updateLogTV();
		createButtonListeners();

	}

	private void createButtonListeners() {
		spanishButton.setOnClickListener(this);
		englishButton.setOnClickListener(this);

	}

	private void updateLogTV() {
		AndroidCloudServiceLogReader testread = new AndroidCloudServiceLogReader(
				Environment.getExternalStorageDirectory());
		StringBuffer sb = new StringBuffer();
		ArrayList<String> all = testread.getAll();
		for (int i = 0; i < all.size(); i++) {
			sb.append(all.get(i));
			sb.append('\n');
		}
		logTV.setText(sb.toString());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.englishButton:
			new sendRequest().execute(new Integer(0));
			break;
		case R.id.spanishButton:
			new sendRequest().execute(new Integer(1));
			break;

		}
		updateLogTV();

	}

	public void playFromFile() {
		try {
			String audioFile = "/mnt/sdcard/newdir/testfile";
			this.mp.setDataSource(audioFile);
			System.out.println("source set succeeded");
			this.mp.prepare();
			this.mp.start();
			while (mp.isPlaying()) {

			}
			this.mp.reset();
		} catch (Exception e) {
			e.printStackTrace();
		}
		updateLogTV();
	}

	private class sendRequest extends AsyncTask<Integer, Void, Boolean> {
		private ProgressDialog dialog;

		protected void onPreExecute() {

			dialog = ProgressDialog.show(myContext, "",
					"Loading. Please wait...", true);
			dialog.setCancelable(true);

		}

		@Override
		protected Boolean doInBackground(Integer... ints) {
			int temp = ints[0];
			String[] text = new String[1];
			switch (temp) {
			case 0:
				Log.d("test", "english request");
				text[0] = englishET.getText().toString();
				return engTTS.doRequest(text, "testfile");
			case 1:
				Log.d("test", "spanish request");
				text[0] = spanishET.getText().toString();
				return spnTTS.doRequest(text, "testfile");
			default:
				return false;
			}

		}

		protected void onPostExecute(Boolean res) {
			dialog.cancel();
			if (res) {
				playFromFile();
			}
			updateLogTV();
		}
	}
}