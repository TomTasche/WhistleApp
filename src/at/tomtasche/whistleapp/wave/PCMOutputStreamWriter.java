package at.tomtasche.whistleapp.wave;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteOrder;

import at.tomtasche.whistleapp.util.ArrayUtil;
import at.tomtasche.whistleapp.util.NumberUtil;

public class PCMOutputStreamWriter extends PCMWriter {

	private final OutputStream out;
	private final byte[] buffer;
	private final ByteOrder endian;

	public PCMOutputStreamWriter(OutputStream out, int bitsPerSample,
			ByteOrder endian) {
		if (!NumberUtil.isPowerOf2(bitsPerSample))
			throw new IllegalArgumentException();

		this.out = out;
		this.buffer = new byte[bitsPerSample / 8];
		this.endian = endian;
	}

	public void write(double pulse) throws IOException {
		NumberUtil.mapDouble(pulse, buffer, 0, buffer.length);

		if (endian == ByteOrder.LITTLE_ENDIAN) {
			ArrayUtil.swapAll(buffer);
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