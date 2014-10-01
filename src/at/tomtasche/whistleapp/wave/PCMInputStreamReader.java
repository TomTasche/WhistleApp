package at.tomtasche.whistleapp.wave;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

import at.tomtasche.whistleapp.util.ArrayUtil;
import at.tomtasche.whistleapp.util.ByteStreamUtil;
import at.tomtasche.whistleapp.util.NumberUtil;

public class PCMInputStreamReader extends PCMReader {

	private final InputStream in;
	private final byte[] buffer;
	private final ByteOrder endian;

	public PCMInputStreamReader(InputStream in, int bitsPerSample,
			ByteOrder endian) {
		if (!NumberUtil.isPowerOf2(bitsPerSample))
			throw new IllegalArgumentException();

		this.in = in;
		this.buffer = new byte[bitsPerSample / 8];
		this.endian = endian;
	}

	public double read() throws IOException {
		ByteStreamUtil.readFully(in, buffer);

		if (endian == ByteOrder.LITTLE_ENDIAN) {
			ArrayUtil.swapAll(buffer);
		}

		return NumberUtil.mapInteger(buffer);
	}

	@Override
	public void close() throws IOException {
		in.close();
	}

}
