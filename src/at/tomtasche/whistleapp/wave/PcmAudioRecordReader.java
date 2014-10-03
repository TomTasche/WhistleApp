package at.tomtasche.whistleapp.wave;

import java.io.IOException;

import android.media.AudioFormat;
import android.media.AudioRecord;
import at.stefl.commons.util.InaccessibleSectionException;
import at.stefl.commons.util.NumberUtil;
import at.stefl.commons.util.array.ArrayUtil;

public class PcmAudioRecordReader extends PcmReader {

	private static final int BUFFER_SIZE = 1;

	private final AudioRecord recorder;
	private final int sampleSize;
	private final byte[] inBuffer;
	private final double[] outBuffer;
	private int outBufferIndex;
	private int outBufferSize;

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
		int inBufferSize = sampleSize * BUFFER_SIZE;
		this.inBuffer = new byte[inBufferSize];
		this.outBuffer = new double[BUFFER_SIZE];
		this.outBufferIndex = outBuffer.length;
	}
	
	private void readBuffer() {
		outBufferSize = recorder.read(inBuffer, 0, inBuffer.length) / sampleSize;
		byte[] tmp = ArrayUtil.copyOf(inBuffer, outBufferSize);
		
		for (int i = 0; i < outBufferSize; i += sampleSize) {
			outBuffer[i] = NumberUtil.mapIntegerToDouble(tmp);
		}
	}

	@Override
	public double read() throws IOException {
		return 0;
	}

}