package at.tomtasche.whistleapp;

import java.util.Arrays;

public class WhistlePrinter implements WhistleProcessor {

	private Callback callback;

	@Override
	public void initialize(Callback callback) {
		this.callback = callback;
	}

	@Override
	public void process(short[] buffer, int length) {
		System.out.println("beep: " + Arrays.toString(buffer));

		callback.onProcessed("hi");
	}
}
