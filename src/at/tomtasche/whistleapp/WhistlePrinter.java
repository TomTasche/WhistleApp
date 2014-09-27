package at.tomtasche.whistleapp;

import java.util.Arrays;

public class WhistlePrinter implements WhistleProcessor {

	@Override
	public void process(short[] buffer, int length) {
		System.out.println("beep: " + Arrays.toString(buffer));
	}
}
