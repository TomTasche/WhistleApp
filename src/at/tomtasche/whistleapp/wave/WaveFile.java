package at.tomtasche.whistleapp.wave;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;

import at.stefl.commons.io.CountingOutputStream;
import at.stefl.commons.io.RandomAccessFileInputStream;
import at.stefl.commons.io.RandomAccessFileOutputStream;

public class WaveFile {

	private class UpdateHeaderWriter extends PcmWriter {
		private final PcmWriter out;
		private final CountingOutputStream cout;

		public UpdateHeaderWriter(PcmWriter out, CountingOutputStream cout) {
			this.out = out;
			this.cout = cout;
		}

		@Override
		public void write(double pulse) throws IOException {
			out.write(pulse);
		}

		@Override
		public void flush() throws IOException {
			out.flush();
		}

		@Override
		public void close() throws IOException {
			flush();
			out.close();

			WaveHeader header = readHeader();
			header.fillSize((int) (header.getSubchunk2Size() + cout.count()));
			writeHeader(header);
		}
	}

	private final File file;

	public WaveFile(File file) {
		this.file = file;
	}

	public WaveHeader readHeader() throws IOException {
		InputStream in = new FileInputStream(file);
		WaveHeader result = WaveHeader.read(in);
		in.close();
		return result;
	}

	public void writeHeader(WaveHeader header) throws IOException {
		RandomAccessFile raf = new RandomAccessFile(file, "rw");
		OutputStream out = new RandomAccessFileOutputStream(raf);
		header.write(out);
		out.close();
		raf.close();
	}

	public PcmWriter getAppendWriter() throws IOException {
		RandomAccessFile raf = new RandomAccessFile(file, "rw");
		InputStream in = new RandomAccessFileInputStream(raf);
		WaveHeader header = WaveHeader.read(in);
		OutputStream out = new RandomAccessFileOutputStream(raf);
		CountingOutputStream cout = new CountingOutputStream(out);
		PcmWriter pout = new PcmOutputStreamWriter(cout,
				header.getBitsPerSample(), WaveConstants.DATA_ENDIAN);
		return new UpdateHeaderWriter(pout, cout);
	}

	public PcmReader getReader() throws IOException {
		InputStream in = new FileInputStream(file);
		WaveHeader result = WaveHeader.read(in);
		in.close();
		return new PcmInputStreamReader(in, result.getBitsPerSample(),
				WaveConstants.DATA_ENDIAN);
	}

}