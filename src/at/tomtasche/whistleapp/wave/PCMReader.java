package at.tomtasche.whistleapp.wave;

import java.io.Closeable;
import java.io.IOException;

public abstract class PCMReader implements Closeable {

	public abstract double read() throws IOException;

}