package com.oops.android.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
	private EditText sourceInput;
	private EditText destinationInput;
	private Button calculateRouteButton;

	private int currentDirection = 0;
	private ArrayList<String> directions;
	private boolean directionResult = false;
	private TextView routeText;
	private Button nextButton;

	private AndroidCloudServiceAdapter engTTS, dirServ;

	private MediaPlayer voicedDirectionsPlayer;

	private Context myContext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.spokendirectionsapp);
		myContext = this;

		directions = null;

		voicedDirectionsPlayer = new MediaPlayer();

		engTTS = new AndroidCloudServiceAdapter(
				new TextToSpeechServiceEnglish(), "spokendirections");
		dirServ = new AndroidCloudServiceAdapter(new DirectionsService(),
				"spokendirections");
		sourceInput = (EditText) findViewById(R.id.sourceInput);
		destinationInput = (EditText) findViewById(R.id.destinationInput);
		calculateRouteButton = (Button) findViewById(R.id.calculateRouteButton);
		routeText = (TextView) findViewById(R.id.routeText);
		nextButton = (Button) findViewById(R.id.nextButton);
		nextButton.setEnabled(false);

		createButtonListeners();
	}

	private void createButtonListeners() {
		calculateRouteButton.setOnClickListener(this);
		nextButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.calculateRouteButton:
			calculateRoute();
			break;
		case R.id.nextButton:
			nextDirection(false);
			break;
		}
	}

	private void calculateRoute() {
		new SendDirectionsRequest().execute(new Integer(0));

	}

	// Calculating is true if called from calculateRoute, guaranteeing that
	// there will eventually
	// be directions. It's false if called from the button press because if
	// there are not
	// already directions when the button is pressed, there won't be any
	private void nextDirection(boolean calculating) {
		if (directions == null) {
			if (calculating)
				// wait for directions
				while (directions == null)
					;
			else
				return;
		}

		currentDirection = (currentDirection + 1) % directions.size();
		routeText.setText(directions.get(currentDirection));

		new SendTTSRequest().execute(new Integer(currentDirection));

		nextButton.setEnabled(true);
		if (currentDirection == directions.size() - 1)
			nextButton.setText("Restart from beginning");
		else
			nextButton.setText("Next");
	}

	public void playFromFile() {
		try {
			String audioFile = "/mnt/sdcard/spokendirections/directionttsfile";
			voicedDirectionsPlayer.setDataSource(audioFile);
			voicedDirectionsPlayer.prepare();
			voicedDirectionsPlayer.start();
			while (voicedDirectionsPlayer.isPlaying()) {

			}
			voicedDirectionsPlayer.reset();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class SendDirectionsRequest extends
			AsyncTask<Integer, Void, Boolean> {
		private ProgressDialog dialog;

		protected void onPreExecute() {
			dialog = ProgressDialog.show(myContext, "",
					"Loading. Please wait...", true);
			dialog.setCancelable(true);
		}

		@Override
		protected Boolean doInBackground(Integer... ints) {
			Log.d("test", "fetch directions");
			String[] input = new String[2];
			input[0] = sourceInput.getText().toString();
			input[1] = destinationInput.getText().toString();
			directionResult = false;
			directions = new ArrayList<String>();
			boolean result = dirServ.doRequest(input, "dirs");
			if (result) {
				File file = new File("/mnt/sdcard/spokendirections/dirs");
				Scanner scanner;
				try {
					scanner = new Scanner(file);
					String line;
					while (scanner.hasNextLine()) {
						line = scanner.nextLine();
						directions.add(line);
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
			return result;
		}

		protected void onPostExecute(Boolean res) {
			dialog.cancel();
			directionResult = res;
			if (directionResult) {
				currentDirection = -1;
				nextDirection(true);
			} else {
				Log.d("fore", "Failed dir request");
			}
		}
	}

	private class SendTTSRequest extends AsyncTask<Integer, Void, Boolean> {
		private ProgressDialog dialog;

		protected void onPreExecute() {
			dialog = ProgressDialog.show(myContext, "",
					"Loading. Please wait...", true);
			dialog.setCancelable(true);
		}

		@Override
		protected Boolean doInBackground(Integer... ints) {
			Log.d("test", "read directions");
			String[] temp = new String[1];
			temp[0] = directions.get(ints[0]);
			return engTTS.doRequest(temp, "directionttsfile");
		}

		protected void onPostExecute(Boolean res) {
			dialog.cancel();
			if (res) {
				playFromFile();
			}
		}
	}
}