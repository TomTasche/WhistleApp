package at.tomtasche.whistleapp;

import java.util.Arrays;

public class WhistleProcessor {

	private Callback callback;

	public WhistleProcessor(int samplingRate, double frequency, int baud) {
	}

	public void initialize(Callback callback) {
		this.callback = callback;
	}

	public void process(short[] buffer, int length) {
		System.out.println("beep: " + Arrays.toString(buffer));

		callback.onProcessed("hi");
	}

	public interface Callback {

		public void onProcessed(String text);
	}
}
