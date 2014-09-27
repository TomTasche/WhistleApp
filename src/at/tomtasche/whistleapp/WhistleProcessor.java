package at.tomtasche.whistleapp;

public class WhistleProcessor {

	public static final double DEFAULT_FREQUENCY_DELTA = 100;

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
	private final double frequency;
	private final double frequencyDelta;
	private final int baud;

	private final short[] buffer;
	private int bufferIndex;

	public WhistleProcessor(Callback callback, int samplingRate,
			double frequency, int baud) {
		this(callback, samplingRate, frequency, baud, DEFAULT_FREQUENCY_DELTA);
	}

	public WhistleProcessor(Callback callback, int samplingRate,
			double frequency, int baud, double frequencyDelta) {
		this.callback = callback;
		this.samplingRate = samplingRate;
		this.frequency = frequency;
		this.baud = baud;
		this.frequencyDelta = frequencyDelta;

		double optimalTimeSlice = 1.0 / baud / 3;
		buffer = new short[(int) Math.ceil(optimalTimeSlice * samplingRate)];
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
		double[] data = new double[buffer.length];
		for (int i = 0; i < data.length; i++) {
			data[i] = (double) buffer[i] / Short.MAX_VALUE;
		}

		double lowerFrequency = frequency - frequencyDelta;
		double upperFrequency = frequency + frequencyDelta;
		double frequencyBand = upperFrequency - lowerFrequency;
		double[] out = new double[100];
		dft(data, samplingRate, out, lowerFrequency, upperFrequency);

		double sum = 0;
		for (int i = 0; i < out.length; i++) {
			sum += out[i];
		}

		// TODO: LOG
		callback.onProcessed("" + sum);
	}

	public interface Callback {

		public void onProcessed(String text);
	}
}
