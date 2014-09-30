package at.tomtasche.whistleapp;

import java.nio.charset.Charset;
import java.util.BitSet;
import java.util.concurrent.LinkedBlockingQueue;

public class WhistleProducer {

	private LinkedBlockingQueue<String> messageQueue;

	private BitSet currentMessageBits;
	private int currentMessageBitsOffset;

	private final int samplingRate;
	private final int frequency;
	private final int syncFrequency;
	private final double syncTime;
	private final double length;

	public WhistleProducer(int samplingRate, int frequency, int baud,
			int syncFrequency, double syncTime) {
		this.samplingRate = samplingRate;
		this.frequency = frequency;
		this.syncFrequency = syncFrequency;
		this.syncTime = syncTime;

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
				|| currentMessageBitsOffset >= currentMessageBits.size()) {
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

			// send sync
			double angularVelocity = 2 * Math.PI * syncFrequency;
			short[] data = new short[(int) Math.ceil(samplingRate * syncTime)];
			for (int i = 0; i < data.length; i++) {
				double t = (double) i / samplingRate;
				short value = (short) (Short.MAX_VALUE * Math.sin(t
						* angularVelocity));
				data[i] = value;
			}

			Whistle whistle = new Whistle();
			whistle.buffer = data;
			whistle.length = data.length;

			return whistle;
		} else {
			currentMessageBitsOffset++;
		}

		int amplitude;
		if (currentMessageBits.get(currentMessageBitsOffset)) {
			amplitude = Short.MAX_VALUE;
		} else {
			amplitude = 0;
		}

		double angularVelocity = 2 * Math.PI * frequency;
		short[] data = new short[(int) Math.ceil(samplingRate * length)];
		for (int i = 0; i < data.length; i++) {
			double t = (double) i / samplingRate;
			short value = (short) (amplitude * Math.sin(t * angularVelocity));
			data[i] = value;
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
