package at.tomtasche.whistleapp;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import at.tomtasche.whistleapp.WhistleProcessor.Callback;

public class MainActivity extends Activity implements OnClickListener, Callback {

	private static final int BAUD = 10;
	private static final int DATA_FREQUENCY = 10000;
	private static final int SYNC_FREQUENCY = 15000;
	private static final int SAMPLING_RATE = 44100;

	private EditText outputEdit;
	private EditText inputEdit;

	private Handler mainHandler;

	private WhistleProcessor whistleProcessor;
	private WhistleReceiver whistleReceiver;
	private WhistleProducer whistleProducer;
	private WhistleSender whistleSender;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.button_clear_input).setOnClickListener(this);
		findViewById(R.id.button_clear_output).setOnClickListener(this);
		findViewById(R.id.button_whistle).setOnClickListener(this);

		inputEdit = (EditText) findViewById(R.id.edit_input);
		outputEdit = (EditText) findViewById(R.id.edit_output);

		mainHandler = new Handler();

		whistleProcessor = new WhistleProcessor(this, SAMPLING_RATE,
				DATA_FREQUENCY, BAUD, SYNC_FREQUENCY);

		whistleReceiver = new WhistleReceiver();
		whistleReceiver.start(whistleProcessor);

		whistleProducer = new WhistleProducer(SAMPLING_RATE, DATA_FREQUENCY,
				BAUD);

		whistleSender = new WhistleSender();
		whistleSender.start(whistleProducer);
	}

	@Override
	public void onProcessed(final String text) {
		mainHandler.post(new Runnable() {

			@Override
			public void run() {
				outputEdit.setText(text);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_clear_input:
			inputEdit.setText("");

			break;

		case R.id.button_clear_output:
			outputEdit.setText("");

			break;

		case R.id.button_whistle:
			String text = inputEdit.getText().toString();
			whistleProducer.queueMessage(text);

			break;
		}
	}

	@Override
	protected void onStop() {
		super.onStop();

		whistleReceiver.stop();

		whistleSender.stop();
	}

}
