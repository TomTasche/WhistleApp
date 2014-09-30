package at.tomtasche.whistleapp.util;

public class EndianUtil {

	public static short swapEndian(short s) {
		return (short) (((s & 0x00ff) << 8) | ((s & 0xff00) >> 8));
	}

	public static int swapEndian(int i) {
		return ((i & 0x000000ff) << 24) | ((i & 0x0000ff00) << 8)
				| ((i & 0x00ff0000) >>> 8) | ((i & 0xff000000) >>> 24);
	}

	private EndianUtil() {
	}

}