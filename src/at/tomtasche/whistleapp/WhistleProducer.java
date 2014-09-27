package at.tomtasche.whistleapp;

import java.nio.charset.Charset;
import java.util.BitSet;
import java.util.concurrent.LinkedBlockingQueue;

public class WhistleProducer {

	private static final double WHISTLE_LENGTH = 0.1;

	private static final int WHISTLE_FREQUENCY = 10000;
	private static final int EMPTY_FREQUENCY = 0;

	private static final int WHISTLE_SAMPLE_RATE = 44100;

	private LinkedBlockingQueue<String> messageQueue;

	private BitSet currentMessageBits;
	private int currentMessageBitsOffset;

	public WhistleProducer() {
		messageQueue = new LinkedBlockingQueue<String>();
	}

	public void queueMessage(String text) {
		messageQueue.add(text);
	}

	public Whistle read() {
		if (currentMessageBits == null
				|| currentMessageBitsOffset + 1 == currentMessageBits.size()) {
			String currentMessage;
			try {
				currentMessage = messageQueue.take();
			} catch (InterruptedException e) {
				e.printStackTrace();

				return null;
			}

			byte[] currentMessageBytes = currentMessage.getBytes(Charset
					.forName("ASCII"));
			currentMessageBits = BitSet.valueOf(currentMessageBytes);
			currentMessageBitsOffset = 0;
		} else {
			currentMessageBitsOffset++;
		}

		int frequency;
		if (currentMessageBits.get(currentMessageBitsOffset)) {
			frequency = WHISTLE_FREQUENCY;
		} else {
			frequency = EMPTY_FREQUENCY;
		}

		double angularVelocity = 2 * Math.PI * frequency;
		short[] data = new short[(int) Math.ceil(WHISTLE_SAMPLE_RATE
				* WHISTLE_LENGTH)];

		for (int i = 0; i < data.length; i++) {
			double t = (double) i / WHISTLE_SAMPLE_RATE;
			short amplitude = (short) (Short.MAX_VALUE * Math.sin(t
					* angularVelocity));
			data[i] = amplitude;
		}

		Whistle whistle = new Whistle();
		whistle.buffer = data;
		whistle.length = data.length;

		return whistle;
	}

	public static class Whistle {

		short[] buffer;
		int length;
	}
}
