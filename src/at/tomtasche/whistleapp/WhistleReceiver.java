package at.tomtasche.whistleapp;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder.AudioSource;

public class WhistleReceiver implements Runnable {

	private static final int SAMPLE_RATE_IN_HZ = 8000;

	private WhistleProcessor processor;

	private Thread thread;
	private boolean stopped;

	public void start(WhistleProcessor processor) {
		this.processor = processor;

		thread = new Thread(this);
		thread.start();
	}

	// taken from:
	// http://stackoverflow.com/questions/4525206/android-audiorecord-class-process-live-mic-audio-quickly-set-up-callback-func
	@Override
	public void run() {
		int minBufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ,
				AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);

		AudioRecord recorder = new AudioRecord(AudioSource.MIC,
				SAMPLE_RATE_IN_HZ, AudioFormat.CHANNEL_IN_MONO,
				AudioFormat.ENCODING_PCM_16BIT, minBufferSize * 10);
		recorder.startRecording();

		short[] buffer = new short[256];
		while (!stopped) {
			int shortsRead = recorder.read(buffer, 0, buffer.length);

			processor.process(buffer, shortsRead);
		}

		recorder.stop();
		recorder.release();
	}

	public void stop() {
		stopped = true;

		thread.interrupt();
	}
}
