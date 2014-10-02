package at.tomtasche.whistleapp.wave;

public class PCMLowPassFilter extends PCMFilter {

	private final double a;
	private final double b;

	private double lastOut = Double.NaN;

	public PCMLowPassFilter(int sampleRate, double cutoffFrequency) {
		double rc = 1 / (2 * Math.PI * cutoffFrequency);
		double sampleTime = 1.0 / sampleRate;
		this.a = sampleTime / (rc + sampleTime);
		this.b = 1 - a;
	}

	@Override
	public double filter(double pulse) {
		if (lastOut == Double.NaN)
			lastOut = pulse;
		else
			lastOut = (a * pulse + b * lastOut);
		return lastOut;
	}

}