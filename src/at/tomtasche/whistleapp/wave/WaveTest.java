package at.tomtasche.whistleapp.wave;

import java.io.DataOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.io.RandomAccessFile;

import at.tomtasche.whistleapp.util.CountingOutputStream;
import at.tomtasche.whistleapp.util.EndianUtil;
import at.tomtasche.whistleapp.util.RandomAccessFileOutputStream;

public class WaveTest {

	public static void main(String[] args) throws Throwable {
		double time = 1;
		int sampleRate = 44100;
		double frequency = 440;
		double volume = 1;

		File f = new File("test.wav");
		RandomAccessFile raf = new RandomAccessFile(f, "rw");
		OutputStream out = new RandomAccessFileOutputStream(raf);
		CountingOutputStream cout = new CountingOutputStream(out);
		DataOutputStream dout = new DataOutputStream(cout);

		WaveHeader header = new WaveHeader((short) 1, sampleRate, (short) 16);
		header.write(out);

		int samples = (int) Math.ceil(time * sampleRate);
		double sampleTime = 1.0 / sampleRate;
		double angleVelocity = 2 * Math.PI * frequency;
		for (int i = 0; i < samples; i++) {
			double t = i * sampleTime;
			double value = volume * Math.sin(t * angleVelocity);
			short sample = (short) (Short.MAX_VALUE * value);
			dout.writeShort(EndianUtil.swapEndian(sample));
		}

		header.fillSize((int) cout.count());
		raf.seek(0);
		header.write(out);

		dout.close();
		raf.close();
	}

}