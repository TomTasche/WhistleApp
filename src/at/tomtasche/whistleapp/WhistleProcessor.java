package at.tomtasche.whistleapp;

import android.util.Log;

public class WhistleProcessor {

	public static interface Callback {
		public void onProcessed(String text);
	}

	public static final int DEFAULT_FREQUENCY_DELTA = 100;

	public static void dft(double[] in, int samplingRate, double[] out,
			double lowerFrequency, double upperFrequency) {
		double frequencyBand = upperFrequency - lowerFrequency;
		double sampleTime = 1.0 / samplingRate;

		for (int i = 0; i < out.length; i++) {
			double frequency = lowerFrequency + frequencyBand
					* ((double) i / out.length);

			for (int k = 0; k < in.length; k++) {
				double angle = 2 * Math.PI * frequency * sampleTime * k;
				out[i] += in[k] * Math.cos(angle);
			}
		}
	}

	private final Callback callback;

	private final int samplingRate;
	private final int frequency;
	private final int frequencyDelta;
	private final int baud;
	private final int syncFrequency;

	private final short[] buffer;
	private int bufferIndex;

	private int state = 0;

	public WhistleProcessor(Callback callback, int samplingRate, int frequency,
			int baud, int syncFrequency) {
		this(callback, samplingRate, frequency, baud, syncFrequency,
				DEFAULT_FREQUENCY_DELTA);
	}

	public WhistleProcessor(Callback callback, int samplingRate, int frequency,
			int baud, int syncFrequency, int frequencyDelta) {
		this.callback = callback;
		this.samplingRate = samplingRate;
		this.frequency = frequency;
		this.baud = baud;
		this.syncFrequency = syncFrequency;
		this.frequencyDelta = frequencyDelta;

		double bufferTime = 1.0 / frequency * 20;
		buffer = new short[(int) Math.ceil(bufferTime * samplingRate)];
	}

	public void process(short[] buf, int len) {
		int index = 0;

		while (index < buf.length) {
			int copyLength = Math.min(len, buffer.length - bufferIndex);
			System.arraycopy(buf, 0, buffer, bufferIndex, copyLength);
			index += copyLength;
			bufferIndex += copyLength;

			if (bufferIndex >= buffer.length) {
				processBuffer();
				bufferIndex = 0;
			}
		}
	}

	public void processBuffer() {
		switch (state) {
		case 0:
			processSync();
			break;
		case 1:
			processData();
			break;
		}
	}

	public void processSync() {
		double[] data = new double[buffer.length];
		for (int i = 0; i < data.length; i++) {
			data[i] = (double) buffer[i] / Short.MAX_VALUE;
		}

		double lowerFrequency = syncFrequency - frequencyDelta;
		double upperFrequency = syncFrequency + frequencyDelta;
		double[] out = new double[100];
		dft(data, samplingRate, out, lowerFrequency, upperFrequency);

		double avg = 0;
		for (int i = 0; i < out.length; i++) {
			avg += out[i];
		}
		avg /= out.length;

		// TODO: LOG
		callback.onProcessed("" + avg);
		if (avg > 0.1) {
			Log.d("WhistleProcessor", "" + avg);
		}
	}

	public void processData() {

	}

}
