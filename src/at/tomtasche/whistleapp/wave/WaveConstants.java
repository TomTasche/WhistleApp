package at.tomtasche.whistleapp.wave;

import at.stefl.commons.io.Endianness;

public class WaveConstants {

	public static final int CHUNK_ID = 0x52494646; // "RIFF"
	public static final int FORMAT = 0x57415645; // "WAVE"
	public static final int SUBCHUNK_1_ID = 0x666d7420; // "fmt "
	public static final int SUBCHUNK_1_SIZE = 16;
	public static final short AUDIO_FORMAT = 1;
	public static final int SUBCHUNK_2_ID = 0x64617461; // "data"
	public static final int HEADER_SIZE = 44;
	public static final Endianness DATA_ENDIAN = Endianness.BIG;

	private WaveConstants() {
	}

}