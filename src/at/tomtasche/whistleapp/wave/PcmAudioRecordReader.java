package at.tomtasche.whistleapp.wave;

import android.media.AudioFormat;
import android.media.AudioRecord;
import at.stefl.commons.util.InaccessibleSectionException;
import at.stefl.commons.util.NumberUtil;

public class PcmAudioRecordReader extends PcmReader {

	private static final int BUFFER_SIZE = 100;

	private final AudioRecord recorder;
	private final int sampleSize;
	private final byte[] inBuffer;
	private final double[] outBuffer;
	private int outBufferIndex;
	private int outBufferSize;
	private final byte[] tmpBuffer;

	public PcmAudioRecordReader(AudioRecord recorder) {
		switch (recorder.getAudioFormat()) {
		case AudioFormat.ENCODING_PCM_8BIT:
			sampleSize = 1;
			break;
		case AudioFormat.ENCODING_PCM_16BIT:
			sampleSize = 2;
			break;
		default:
			throw new InaccessibleSectionException();
		}

		this.recorder = recorder;
		this.inBuffer = new byte[BUFFER_SIZE * sampleSize];
		this.outBuffer = new double[BUFFER_SIZE];
		this.outBufferIndex = outBuffer.length;
		this.tmpBuffer = new byte[sampleSize];
	}

	private void readBuffer() {
		outBufferSize = recorder.read(inBuffer, 0, inBuffer.length)
				/ sampleSize;
		outBufferIndex = 0;

		for (int i = 0, j = 0; i < outBufferSize; i++, j += sampleSize) {
			System.arraycopy(inBuffer, j, tmpBuffer, 0, sampleSize);
			outBuffer[i] = NumberUtil.mapIntegerToDouble(tmpBuffer);
		}
	}

	@Override
	public double read() {
		while (outBufferIndex >= outBufferSize) {
			readBuffer();
		}

		return outBuffer[outBufferIndex++];
	}

}