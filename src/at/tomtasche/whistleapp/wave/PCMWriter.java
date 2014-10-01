package at.tomtasche.whistleapp.wave;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteOrder;

import at.tomtasche.whistleapp.util.NumberUtil;

public class PCMWriter implements Closeable, Flushable {

	private final OutputStream out;
	private final byte[] buffer;
	private final ByteOrder endian;

	public PCMWriter(OutputStream out, int bitsPerSample, ByteOrder endian) {
		if (!NumberUtil.isPowerOf2(bitsPerSample))
			throw new IllegalArgumentException();

		this.out = out;
		this.buffer = new byte[bitsPerSample / 8];
		this.endian = endian;
	}

	public void write(double pulse) throws IOException {
		NumberUtil.mapDouble(pulse, buffer, 0, buffer.length);

		if (endian == ByteOrder.LITTLE_ENDIAN) {
			for (int i = 0, j = buffer.length - 1; i < j; i++, j--) {
				byte tmp = buffer[i];
				buffer[i] = buffer[j];
				buffer[j] = tmp;
			}
		}

		out.write(buffer);
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