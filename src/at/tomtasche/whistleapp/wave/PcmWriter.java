package at.tomtasche.whistleapp.wave;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;

public abstract class PcmWriter implements Closeable, Flushable {

	public abstract void write(double pulse) throws IOException;

	@Override
	public void flush() throws IOException {
	}

	@Override
	public void close() throws IOException {
	}

}