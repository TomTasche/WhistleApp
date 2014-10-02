package at.tomtasche.whistleapp.wave;

public class PCMHighPassFilter extends PCMFilter {

	private final double a;

	private double lastIn = Double.NaN;
	private double lastOut = Double.NaN;

	public PCMHighPassFilter(int sampleRate, double cutoffFrequency) {
		double rc = 1 / (2 * Math.PI * cutoffFrequency);
		double sampleTime = 1.0 / sampleRate;
		this.a = rc / (rc + sampleTime);
	}

	@Override
	public double filter(double pulse) {
		if (lastOut == Double.NaN)
			lastOut = pulse;
		else
			lastOut = a * (lastOut + pulse - lastIn);
		lastIn = pulse;
		return lastOut;
	}

}