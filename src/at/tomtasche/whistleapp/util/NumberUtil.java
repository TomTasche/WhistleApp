package at.tomtasche.whistleapp.util;

import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberUtil {

	public static boolean isPowerOf2(int i) {
		return (i != 0) && ((i & (i - 1)) == 0);
	}

	public static byte[] mapDouble(double d, int len) {
		byte[] result = new byte[len];
		mapDouble(d, result, 0, len);
		return result;
	}

	public static void mapDouble(double d, byte[] b, int off, int len) {
		BigInteger maxPositive = BigInteger.ZERO.setBit(len << 2).subtract(
				BigInteger.ONE);
		BigInteger value = BigDecimal.valueOf(d)
				.multiply(new BigDecimal(maxPositive)).toBigInteger();
		byte[] bytes = value.toByteArray();
		System.arraycopy(bytes, 0, b, off, Math.min(bytes.length, len));
	}

	private NumberUtil() {
	}

}