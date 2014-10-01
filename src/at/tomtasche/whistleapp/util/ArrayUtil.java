package at.tomtasche.whistleapp.util;

public class ArrayUtil {

	public static void swapAll(Object[] array) {
		Object tmp;

		for (int i = 0, j = array.length - 1; i < j; i++, j--) {
			tmp = array[i];
			array[i] = array[j];
			array[j] = tmp;
		}
	}

	public static void swapAll(boolean[] array) {
		boolean tmp;

		for (int i = 0, j = array.length - 1; i < j; i++, j--) {
			tmp = array[i];
			array[i] = array[j];
			array[j] = tmp;
		}
	}

	public static void swapAll(byte[] array) {
		byte tmp;

		for (int i = 0, j = array.length - 1; i < j; i++, j--) {
			tmp = array[i];
			array[i] = array[j];
			array[j] = tmp;
		}
	}

	public static void swapAll(char[] array) {
		char tmp;

		for (int i = 0, j = array.length - 1; i < j; i++, j--) {
			tmp = array[i];
			array[i] = array[j];
			array[j] = tmp;
		}
	}

	public static void swapAll(short[] array) {
		short tmp;

		for (int i = 0, j = array.length - 1; i < j; i++, j--) {
			tmp = array[i];
			array[i] = array[j];
			array[j] = tmp;
		}
	}

	public static void swapAll(int[] array) {
		int tmp;

		for (int i = 0, j = array.length - 1; i < j; i++, j--) {
			tmp = array[i];
			array[i] = array[j];
			array[j] = tmp;
		}
	}

	public static void swapAll(long[] array) {
		long tmp;

		for (int i = 0, j = array.length - 1; i < j; i++, j--) {
			tmp = array[i];
			array[i] = array[j];
			array[j] = tmp;
		}
	}

	public static void swapAll(float[] array) {
		float tmp;

		for (int i = 0, j = array.length - 1; i < j; i++, j--) {
			tmp = array[i];
			array[i] = array[j];
			array[j] = tmp;
		}
	}

	public static void swapAll(double[] array) {
		double tmp;

		for (int i = 0, j = array.length - 1; i < j; i++, j--) {
			tmp = array[i];
			array[i] = array[j];
			array[j] = tmp;
		}
	}

	public static void swapAll(Object[] array, int off, int len) {
		int last = off + len - 1;
		Object tmp;

		for (int i = off, j = last; i < j; i++, j--) {
			tmp = array[i];
			array[i] = array[j];
			array[j] = tmp;
		}
	}

	public static void swapAll(boolean[] array, int off, int len) {
		int last = off + len - 1;
		boolean tmp;

		for (int i = off, j = last; i < j; i++, j--) {
			tmp = array[i];
			array[i] = array[j];
			array[j] = tmp;
		}
	}

	public static void swapAll(byte[] array, int off, int len) {
		int last = off + len - 1;
		byte tmp;

		for (int i = off, j = last; i < j; i++, j--) {
			tmp = array[i];
			array[i] = array[j];
			array[j] = tmp;
		}
	}

	public static void swapAll(char[] array, int off, int len) {
		int last = off + len - 1;
		char tmp;

		for (int i = off, j = last; i < j; i++, j--) {
			tmp = array[i];
			array[i] = array[j];
			array[j] = tmp;
		}
	}

	public static void swapAll(short[] array, int off, int len) {
		int last = off + len - 1;
		short tmp;

		for (int i = off, j = last; i < j; i++, j--) {
			tmp = array[i];
			array[i] = array[j];
			array[j] = tmp;
		}
	}

	public static void swapAll(int[] array, int off, int len) {
		int last = off + len - 1;
		int tmp;

		for (int i = off, j = last; i < j; i++, j--) {
			tmp = array[i];
			array[i] = array[j];
			array[j] = tmp;
		}
	}

	public static void swapAll(long[] array, int off, int len) {
		int last = off + len - 1;
		long tmp;

		for (int i = off, j = last; i < j; i++, j--) {
			tmp = array[i];
			array[i] = array[j];
			array[j] = tmp;
		}
	}

	public static void swapAll(float[] array, int off, int len) {
		int last = off + len - 1;
		float tmp;

		for (int i = off, j = last; i < j; i++, j--) {
			tmp = array[i];
			array[i] = array[j];
			array[j] = tmp;
		}
	}

	public static void swapAll(double[] array, int off, int len) {
		int last = off + len - 1;
		double tmp;

		for (int i = off, j = last; i < j; i++, j--) {
			tmp = array[i];
			array[i] = array[j];
			array[j] = tmp;
		}
	}

	private ArrayUtil() {
	}

}