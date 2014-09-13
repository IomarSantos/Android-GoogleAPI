package com.oops.android.service;

public class TextToSpeechServiceEnglish extends TextToSpeechService {

	public TextToSpeechServiceEnglish() {
		this.baseURL = "http://translate.google.com/translate_tts?tl=en&q=";
	}
}
