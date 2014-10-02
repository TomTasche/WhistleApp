package at.tomtasche.whistleapp.wave;

import java.io.IOException;

public class PCMFilterReader extends PCMReader {

	private final PCMReader in;
	private final PCMFilter filter;

	public PCMFilterReader(PCMReader in, PCMFilter filter) {
		this.in = in;
		this.filter = filter;
	}

	@Override
	public double read() throws IOException {
		double pulse = in.read();
		return filter.filter(pulse);
	}

	@Override
	public void close() throws IOException {
		in.close();
	}

}