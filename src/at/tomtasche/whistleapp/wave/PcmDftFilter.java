package at.tomtasche.whistleapp.wave;

public class PcmDftFilter extends PcmFilter {

	private final int sampleRate;
	private final double bandStart;
	private final double bandEnd;
	private final double[] buffer;
	private int bufferIndex;
	private int bufferSize;

	private final double[][] dftBuffer;
	private final double[] lastResults;

	public PcmDftFilter(int sampleRate, double bandStart, double bandEnd,
			int resolution) {
		this.sampleRate = sampleRate;
		this.bandStart = bandStart;
		this.bandEnd = bandEnd;
		int bufferSize = (int) (sampleRate * (1 / bandEnd) * 5);
		this.buffer = new double[bufferSize];
		this.dftBuffer = new double[bufferSize][2];
		this.lastResults = new double[resolution];
	}

	@Override
	public double filter(double pulse) {
		buffer[bufferIndex] = pulse;
		bufferIndex = (++bufferIndex) % buffer.length;
		if (bufferSize < buffer.length)
			bufferSize++;
		else
			dft();
		return 0;
	}

	private void dft() {
		double band = bandEnd - bandStart;
		double sampleTime = 1.0 / sampleRate;

		for (int i = 0; i < lastResults.length; i++) {
			double frequency = bandStart + band
					* ((double) i / (lastResults.length - 1));
			dftBuffer[i][0] = 0;
			dftBuffer[i][1] = 0;

			for (int k = 0; k < buffer.length; k++) {
				int j = (bufferIndex + k) % buffer.length;
				double angle = 2 * Math.PI * frequency * sampleTime * k;
				dftBuffer[i][0] += buffer[j] * Math.cos(angle);
				dftBuffer[i][1] += buffer[j] * Math.sin(angle);
			}

			lastResults[i] = Math.sqrt(dftBuffer[i][0] * dftBuffer[i][0]
					+ dftBuffer[i][1] * dftBuffer[i][1])
					/ buffer.length;
		}
	}

}