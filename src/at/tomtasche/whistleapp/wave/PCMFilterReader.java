package at.tomtasche.whistleapp.wave;

import java.io.IOException;

public class PcmFilterReader extends PcmReader {

	private final PcmReader in;
	private final PcmFilter filter;

	public PcmFilterReader(PcmReader in, PcmFilter filter) {
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