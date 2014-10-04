package at.tomtasche.whistleapp;

import java.io.IOException;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder.AudioSource;
import at.tomtasche.whistleapp.wave.PcmAudioRecordReader;
import at.tomtasche.whistleapp.wave.PcmDftFilter;
import at.tomtasche.whistleapp.wave.PcmFilterReader;

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
				CHANNEL, ENCODING, minBufferSize);
		recorder.startRecording();

		PcmAudioRecordReader in = new PcmAudioRecordReader(recorder);
		PcmDftFilter dft = new PcmDftFilter(sampleRate, 12000, 22000, 100);
		data = dft.getData();
		PcmFilterReader fin = new PcmFilterReader(in, dft);

		try {
			while (!stopped) {
				double read = fin.read();
			}
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
