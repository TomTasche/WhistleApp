package at.tomtasche.whistleapp.wave;

import java.io.IOException;

public class PcmFilterWriter extends PcmWriter {

	private final PcmWriter out;
	private final PcmFilter filter;

	public PcmFilterWriter(PcmWriter out, PcmFilter filter) {
		this.out = out;
		this.filter = filter;
	}

	@Override
	public void write(double pulse) throws IOException {
		out.write(filter.filter(pulse));
	}

	@Override
	public void flush() throws IOException {
		out.flush();
	}

	@Override
	public void close() throws IOException {
		out.close();
	}

}