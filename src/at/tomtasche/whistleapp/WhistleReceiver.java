package at.tomtasche.whistleapp;

import java.io.IOException;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder.AudioSource;
import at.tomtasche.whistleapp.wave.PcmAudioRecordReader;
import at.tomtasche.whistleapp.wave.PcmDftFilter;
import at.tomtasche.whistleapp.wave.PcmFilterReader;

public class WhistleReceiver implements Runnable {

	private static final int SAMPLE_RATE_IN_HZ = 8000;

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
		int minBufferSize = AudioRecord.getMinBufferSize(sampleRate,
				AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);

		AudioRecord recorder = new AudioRecord(AudioSource.MIC,
				SAMPLE_RATE_IN_HZ, AudioFormat.CHANNEL_IN_MONO,
				AudioFormat.ENCODING_PCM_16BIT, minBufferSize * 10);
		recorder.startRecording();

		PcmAudioRecordReader in = new PcmAudioRecordReader(recorder);
		PcmDftFilter dft = new PcmDftFilter(sampleRate, 0, 10000, 100);
		data = dft.getData();
		PcmFilterReader fin = new PcmFilterReader(in, dft);

		try {
			while (true)
				fin.read();
		} catch (IOException e) {
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
