package at.tomtasche.whistleapp;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import at.tomtasche.whistleapp.WhistleProducer.Whistle;

public class WhistleSender implements Runnable {

	private static final int SAMPLE_RATE_IN_HZ = 44100;

	private Thread thread;
	private boolean stopped;

	private WhistleProducer producer;

	public void startPlaying(WhistleProducer producer) {
		this.producer = producer;

		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		int minBufferSize = AudioTrack.getMinBufferSize(SAMPLE_RATE_IN_HZ,
				AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);

		AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
				SAMPLE_RATE_IN_HZ, AudioFormat.CHANNEL_OUT_MONO,
				AudioFormat.ENCODING_PCM_16BIT, minBufferSize,
				AudioTrack.MODE_STREAM);
		audioTrack.play();

		while (!stopped) {
			Whistle whistle = producer.read();

			audioTrack.write(whistle.buffer, 0, whistle.length);
		}

		audioTrack.pause();
		audioTrack.flush();
		audioTrack.stop();
		audioTrack.release();
	}

	public void cancelPlaying() {
		stopped = true;

		thread.interrupt();
	}
}
