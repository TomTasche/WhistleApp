package at.tomtasche.whistleapp.wave;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;

import at.stefl.commons.io.RandomAccessFileInputStream;
import at.stefl.commons.io.RandomAccessFileOutputStream;

public class WaveFile {

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

	public PcmOutputStreamWriter getAppendWriter() throws IOException {
		RandomAccessFile raf = new RandomAccessFile(file, "rw");
		InputStream in = new RandomAccessFileInputStream(raf);
		WaveHeader header = WaveHeader.read(in);
		OutputStream out = new RandomAccessFileOutputStream(raf);
		return new PcmOutputStreamWriter(out, header.getBitsPerSample(),
				WaveConstants.DATA_ENDIAN);
	}

	public PcmInputStreamReader getReader() throws IOException {
		InputStream in = new FileInputStream(file);
		WaveHeader result = WaveHeader.read(in);
		in.close();
		return new PcmInputStreamReader(in, result.getBitsPerSample(),
				WaveConstants.DATA_ENDIAN);
	}

}