package at.tomtasche.whistleapp;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import at.tomtasche.whistleapp.WhistleProcessor.Callback;

import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;

public class MainActivity extends Activity implements OnClickListener, Callback {

	private static final int BAUD = 10;
	private static final int DATA_FREQUENCY = 19000;
	private static final int SYNC_FREQUENCY = 100;
	private static final double SYNC_TIME = 0.5;
	private static final int SAMPLING_RATE = 44100;

	private EditText outputEdit;
	private EditText inputEdit;

	private XYPlot plot;
	private LineAndPointFormatter seriesFormat;

	private boolean stop;

	private Handler mainHandler;

	private SimpleXYSeries series;

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

		whistleReceiver = new WhistleReceiver(SAMPLING_RATE);
		whistleReceiver.start(whistleProcessor);

		whistleProducer = new WhistleProducer(SAMPLING_RATE, DATA_FREQUENCY,
				BAUD, SYNC_FREQUENCY, SYNC_TIME);

		whistleSender = new WhistleSender();
		whistleSender.start(whistleProducer);

		plot = (XYPlot) findViewById(R.id.frequencyPlot);
		plot.setRangeBoundaries(0, 1, BoundaryMode.FIXED);
		plot.setRangeStepValue(5);
		plot.setDomainStepValue(5);

		seriesFormat = new LineAndPointFormatter(Color.RED, Color.GREEN,
				Color.BLUE, null);

		mainHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				if (stop) {
					return;
				}

				updateAmplitudes();

				mainHandler.postDelayed(this, BAUD);
			}
		}, BAUD);
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

	public void updateAmplitudes() {
		double[][] amplitudes = whistleReceiver.getData();
		if (amplitudes == null) {
			Log.d("Whistle", "amplitude data not ready for graph");

			return;
		}

		List<Number> xyInterleavedList = new LinkedList<Number>();
		for (int i = 0; i < amplitudes[0].length; i++) {
			double x = amplitudes[0][i];
			double y = amplitudes[1][i];

			xyInterleavedList.add(x);
			xyInterleavedList.add(y);
		}

		if (series != null) {
			plot.removeSeries(series);
		}

		series = new SimpleXYSeries(xyInterleavedList,
				SimpleXYSeries.ArrayFormat.XY_VALS_INTERLEAVED, "series");

		plot.addSeries(series, seriesFormat);

		plot.redraw();
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

		stop = true;

		whistleReceiver.stop();

		whistleSender.stop();
	}
}
