package at.tomtasche.whistleapp.wave;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import static at.tomtasche.whistleapp.wave.WaveConstants.*;
import static at.tomtasche.whistleapp.util.EndianUtil.*;

// TODO: implement super level RIFF header
// https://ccrma.stanford.edu/courses/422/projects/WaveFormat/
public class WaveHeader {

	public static WaveHeader read(byte[] in) {
		return read(in, 0);
	}

	public static WaveHeader read(byte[] in, int off) {
		try {
			return read(new ByteArrayInputStream(in, off, in.length));
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	public static WaveHeader read(InputStream in) throws IOException {
		WaveHeader result = new WaveHeader();

		DataInputStream din = new DataInputStream(in);
		result.chunkId = din.readInt();
		result.chunkSize = swapEndian(din.readInt());
		result.format = din.readInt();
		result.subchunk1Id = din.readInt();
		result.subchunk1Size = swapEndian(din.readInt());
		result.audioFormat = swapEndian(din.readShort());
		result.numChannels = swapEndian(din.readShort());
		result.sampleRate = swapEndian(din.readInt());
		result.byteRate = swapEndian(din.readInt());
		result.blockAlign = swapEndian(din.readShort());
		result.bitsPerSample = swapEndian(din.readShort());
		result.subchunk2Id = din.readInt();
		result.subchunk2Size = swapEndian(din.readInt());

		return result;
	}

	private int chunkId;
	private int chunkSize;
	private int format;

	private int subchunk1Id;
	private int subchunk1Size;
	private short audioFormat;
	private short numChannels;
	private int sampleRate;
	private int byteRate;
	private short blockAlign;
	private short bitsPerSample;

	private int subchunk2Id;
	private int subchunk2Size;

	public WaveHeader() {
	}

	public WaveHeader(boolean defaults) {
		if (!defaults)
			return;

		defaults();
	}

	public WaveHeader(short numChannels, int sampleRate, short bitsPerSample) {
		this(true);

		fill(numChannels, sampleRate, bitsPerSample);
	}

	public int getChunkId() {
		return chunkId;
	}

	public long getChunkSize() {
		return 0xffffffffl & chunkSize;
	}

	public int getFormat() {
		return format;
	}

	public int getSubchunk1Id() {
		return subchunk1Id;
	}

	public long getSubchunk1Size() {
		return 0xffffffffl & subchunk1Size;
	}

	public short getAudioFormat() {
		return audioFormat;
	}

	public short getNumChannels() {
		return numChannels;
	}

	public int getSampleRate() {
		return sampleRate;
	}

	public int getByteRate() {
		return byteRate;
	}

	public short getBlockAlign() {
		return blockAlign;
	}

	public short getBitsPerSample() {
		return bitsPerSample;
	}

	public int getSubchunk2Id() {
		return subchunk2Id;
	}

	public long getSubchunk2Size() {
		return 0xffffffffl & subchunk2Size;
	}

	public void setChunkId(int chunkId) {
		this.chunkId = chunkId;
	}

	public void setChunkSize(long chunkSize) {
		this.chunkSize = (int) (0xffffffff & chunkSize);
	}

	public void setFormat(int format) {
		this.format = format;
	}

	public void setSubchunk1Id(int subchunk1Id) {
		this.subchunk1Id = subchunk1Id;
	}

	public void setSubchunk1Size(long subchunk1Size) {
		this.subchunk1Size = (int) (0xffffffff & subchunk1Size);
	}

	public void setAudioFormat(short audioFormat) {
		this.audioFormat = audioFormat;
	}

	public void setNumChannels(short numChannes) {
		this.numChannels = numChannes;
	}

	public void setSampleRate(int sampleRate) {
		this.sampleRate = sampleRate;
	}

	public void setByteRate(int byteTate) {
		this.byteRate = byteTate;
	}

	public void setBlockAlign(short blockAlign) {
		this.blockAlign = blockAlign;
	}

	public void setBitsPerSample(short bitsPerSample) {
		this.bitsPerSample = bitsPerSample;
	}

	public void setSubchunk2Id(int subchunk2Id) {
		this.subchunk2Id = subchunk2Id;
	}

	public void setSubchunk2Size(int subchunk2Size) {
		this.subchunk2Size = (int) (0xffffffff & subchunk2Size);
	}

	public void defaults() {
		this.chunkId = CHUNK_ID;
		this.format = FORMAT;
		this.subchunk1Id = SUBCHUNK_1_ID;
		this.subchunk1Size = SUBCHUNK_1_SIZE;
		this.audioFormat = AUDIO_FORMAT;
		this.subchunk2Id = SUBCHUNK_2_ID;
	}

	public void fill(short numChannels, int sampleRate, short bitsPerSample) {
		this.numChannels = numChannels;
		this.sampleRate = sampleRate;
		this.byteRate = sampleRate * numChannels * bitsPerSample / 8;
		this.blockAlign = (short) (numChannels * bitsPerSample / 8);
		this.bitsPerSample = bitsPerSample;
	}

	public void fillSize(int subchunk2Size) {
		this.chunkSize = 4 + (8 + subchunk1Size) + (8 + subchunk2Size);
		this.subchunk2Size = subchunk2Size;
	}

	public byte[] write() {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream(HEADER_SIZE);
			write(out);
			return out.toByteArray();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	public void write(OutputStream out) throws IOException {
		DataOutputStream dout = new DataOutputStream(out);
		dout.writeInt(chunkId);
		dout.writeInt(swapEndian(chunkSize));
		dout.writeInt(format);
		dout.writeInt(subchunk1Id);
		dout.writeInt(swapEndian(subchunk1Size));
		dout.writeShort(swapEndian(audioFormat));
		dout.writeShort(swapEndian(numChannels));
		dout.writeInt(swapEndian(sampleRate));
		dout.writeInt(swapEndian(byteRate));
		dout.writeShort(swapEndian(blockAlign));
		dout.writeShort(swapEndian(bitsPerSample));
		dout.writeInt(subchunk2Id);
		dout.writeInt(swapEndian(subchunk2Size));
	}

}
