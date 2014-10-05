package at.tomtasche.whistleapp.wave;

import java.io.IOException;
import java.io.OutputStream;

import at.stefl.commons.io.Endianness;
import at.stefl.commons.util.NumberUtil;
import at.stefl.commons.util.array.ArrayUtil;

public class PcmOutputStreamWriter extends PcmWriter {

	private final OutputStream out;
	private final byte[] buffer;
	private final Endianness endian;

	public PcmOutputStreamWriter(OutputStream out, int bitsPerSample,
			Endianness endian) {
		if (!NumberUtil.isPowerOf2(bitsPerSample))
			throw new IllegalArgumentException();

		this.out = out;
		this.buffer = new byte[bitsPerSample / 8];
		this.endian = endian;
	}

	public void write(double pulse) throws IOException {
		NumberUtil.mapDoubleToInteger(pulse, buffer, 0, buffer.length);

		if (endian == Endianness.LITTLE) {
			ArrayUtil.turn(buffer);
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