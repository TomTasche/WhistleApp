package at.tomtasche.whistleapp;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceView;

public class FrequencyView extends SurfaceView {

	public FrequencyView(Context context) {
		super(context);
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);

		postDelayed(new Runnable() {
			@Override
			public void run() {
				invalidate();
			}
		}, 10);
	}

}