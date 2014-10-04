package at.tomtasche.whistleapp.wave;

import java.io.IOException;
import java.io.InputStream;

import at.stefl.commons.io.ByteStreamUtil;
import at.stefl.commons.io.Endianness;
import at.stefl.commons.util.NumberUtil;
import at.stefl.commons.util.array.ArrayUtil;

public class PcmInputStreamReader extends PcmReader {

	private final InputStream in;
	private final byte[] buffer;
	private final Endianness endian;

	public PcmInputStreamReader(InputStream in, int bitsPerSample,
			Endianness endian) {
		if (!NumberUtil.isPowerOf2(bitsPerSample))
			throw new IllegalArgumentException();

		this.in = in;
		this.buffer = new byte[bitsPerSample / 8];
		this.endian = endian;
	}

	public double read() throws IOException {
		ByteStreamUtil.readFully(in, buffer);

		if (endian == Endianness.LITTLE) {
			ArrayUtil.swapAll(buffer);
		}

		return NumberUtil.mapIntegerToDouble(buffer);
	}

	@Override
	public void close() throws IOException {
		in.close();
	}

}
