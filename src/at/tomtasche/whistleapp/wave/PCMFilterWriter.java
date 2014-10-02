package at.tomtasche.whistleapp.wave;

import java.io.IOException;

public class PCMFilterWriter extends PCMWriter {

	private final PCMWriter out;
	private final PCMFilter filter;

	public PCMFilterWriter(PCMWriter out, PCMFilter filter) {
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