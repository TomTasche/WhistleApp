package at.tomtasche.whistleapp;

import java.io.File;
import java.io.IOException;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder.AudioSource;
import android.os.Environment;
import at.tomtasche.whistleapp.wave.PcmAudioRecordReader;
import at.tomtasche.whistleapp.wave.PcmDftFilter;
import at.tomtasche.whistleapp.wave.PcmFilterReader;
import at.tomtasche.whistleapp.wave.PcmWriter;
import at.tomtasche.whistleapp.wave.WaveFile;
import at.tomtasche.whistleapp.wave.WaveHeader;

public class WhistleReceiver implements Runnable {

	private static final int ENCODING = AudioFormat.ENCODING_PCM_16BIT;
	private static final int CHANNEL = AudioFormat.CHANNEL_IN_MONO;

	private final int sampleRate;

	private WhistleProcessor processor;

	private Thread thread;
	private boolean stopped;

	private double[][] data;

	public WhistleReceiver(int sampleRate) {
		this.sampleRate = sampleRate;
	}

	public void start(WhistleProcessor processor) {
		this.processor = processor;

		thread = new Thread(this);
		thread.start();
	}

	// taken from:
	// http://stackoverflow.com/questions/4525206/android-audiorecord-class-process-live-mic-audio-quickly-set-up-callback-func
	@Override
	public void run() {
		int minBufferSize = AudioRecord.getMinBufferSize(sampleRate, CHANNEL,
				ENCODING);

		AudioRecord recorder = new AudioRecord(AudioSource.MIC, sampleRate,
				CHANNEL, ENCODING, minBufferSize * 10);
		recorder.startRecording();

		PcmAudioRecordReader in = new PcmAudioRecordReader(recorder);
		PcmDftFilter dft = new PcmDftFilter(sampleRate, 0, 10000, 100);
		data = dft.getData();
		PcmFilterReader fin = new PcmFilterReader(in, dft);

		try {
			File file = new File(Environment.getExternalStorageDirectory(),
					"test.wav");
			WaveFile waveFile = new WaveFile(file);

			waveFile.writeHeader(new WaveHeader((short) 1, sampleRate,
					(short) 16));

			PcmWriter writer = waveFile.getAppendWriter();
			while (!stopped) {
				double read = fin.read();
				writer.write(read);
			}

			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			recorder.stop();
			recorder.release();
		}
	}

	public void stop() {
		stopped = true;

		thread.interrupt();
	}

	public double[][] getData() {
		return data;
	}

}
