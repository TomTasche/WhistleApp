package at.tomtasche.whistleapp;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

	private WhistleReceiver whistleReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		whistleReceiver = new WhistleReceiver();
		whistleReceiver.startRecording(new WhistlePrinter());
	}

	@Override
	protected void onStop() {
		whistleReceiver.cancelRecording();
	}
}
