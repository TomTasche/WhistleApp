package at.tomtasche.whistleapp;

public interface WhistleProcessor {

	public void initialize(Callback callback);

	public void process(short[] buffer, int length);

	public interface Callback {

		public void onProcessed(String text);
	}
}
