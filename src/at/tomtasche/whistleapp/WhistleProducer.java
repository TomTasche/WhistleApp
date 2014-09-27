package at.tomtasche.whistleapp;

import java.nio.charset.Charset;
import java.util.BitSet;
import java.util.concurrent.LinkedBlockingQueue;

public class WhistleProducer {

	private static final int EMPTY_FREQUENCY = 0;

	private LinkedBlockingQueue<String> messageQueue;

	private BitSet currentMessageBits;
	private int currentMessageBitsOffset;

	private int samplingRate;
	private int frequency;
	private double length;

	public WhistleProducer(int samplingRate, int frequency, int baud) {
		this.samplingRate = samplingRate;
		this.frequency = frequency;

		length = baud / 100.0;

		messageQueue = new LinkedBlockingQueue<String>();
	}

	public void queueMessage(String text) {
		messageQueue.add(text);
	}

	public Whistle produce() {
		if (currentMessageBits != null) {
			boolean empty = isEmpty(currentMessageBits,
					currentMessageBitsOffset);
			if (empty) {
				currentMessageBits = null;
			}
		}

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

		int frq;
		if (currentMessageBits.get(currentMessageBitsOffset)) {
			frq = frequency;
		} else {
			frq = EMPTY_FREQUENCY;
		}

		double angularVelocity = 2 * Math.PI * frq;
		short[] data = new short[(int) Math.ceil(samplingRate * length)];
		for (int i = 0; i < data.length; i++) {
			double t = (double) i / samplingRate;
			short amplitude = (short) (Short.MAX_VALUE * Math.sin(t
					* angularVelocity));
			data[i] = amplitude;
		}

		Whistle whistle = new Whistle();
		whistle.buffer = data;
		whistle.length = data.length;

		return whistle;
	}

	private static boolean isEmpty(BitSet bitSet, int offset) {
		BitSet tempSet = bitSet.get(offset, bitSet.size());
		return tempSet.isEmpty();
	}

	public static class Whistle {

		short[] buffer;
		int length;
	}
}
