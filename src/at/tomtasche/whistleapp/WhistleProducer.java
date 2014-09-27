package at.tomtasche.whistleapp;

public class WhistleProducer {
	
	public void queueMessage(String text) {
		// TODO
	}

	public Whistle read() {
		double time = 1;
		double frequency = 15000;
		int samplingRate = 44100;

		double angularVelocity = 2 * Math.PI * frequency;
		short[] data = new short[(int) Math.ceil(samplingRate * time)];

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

	public static class Whistle {
		short[] buffer;
		int length;
	}
}
