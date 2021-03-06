package at.tomtasche.whistleapp.wave;

public class PcmDftFilter extends PcmFilter {

	private final double sampleTime;
	private final double bandStart;
	private final double band;
	private final double[] buffer;
	private int bufferIndex;
	private int bufferSize;

	private final double[][] dftBuffer;
	private final double[] lastResults;
	private final double[] frequencies;
	private final double[][] lastData;

	public PcmDftFilter(int sampleRate, double bandStart, double bandEnd,
			int resolution) {
		this.sampleTime = 1.0 / sampleRate;
		this.bandStart = bandStart;
		this.band = bandEnd - bandStart;
		int bufferSize = (int) (sampleRate * (1 / bandEnd) * 5);
		this.buffer = new double[bufferSize];
		this.dftBuffer = new double[resolution][2];
		this.lastResults = new double[resolution];

		this.frequencies = new double[resolution];
		for (int i = 0; i < resolution; i++) {
			double frequency = bandStart + band
					* ((double) i / (lastResults.length - 1));
			frequencies[i] = frequency;
		}
		this.lastData = new double[][] { frequencies, lastResults };
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

	public double[][] getData() {
		return lastData;
	}

}