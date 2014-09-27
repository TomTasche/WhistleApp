package at.tomtasche.whistleapp;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

	private WhistleReceiver whistleReceiver;
	private WhistleSender whistleSender;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		whistleReceiver = new WhistleReceiver();
		whistleReceiver.startRecording(new WhistlePrinter());

		whistleSender = new WhistleSender();
		whistleSender.startPlaying(new WhistleProducer());
	}

	@Override
	protected void onStop() {
		super.onStop();

		whistleReceiver.cancelRecording();

		whistleSender.cancelPlaying();
	}
}
