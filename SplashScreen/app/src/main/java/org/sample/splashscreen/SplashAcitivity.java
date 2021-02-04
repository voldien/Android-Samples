package org.sample.splashscreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class SplashAcitivity extends Activity {
	static public final String TAG = "Splash";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/*  Create main activity.   */
		Log.d(TAG, "Creating Activity intent.");

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				startActivityForResult(intent, 1);
			}
		}, 5000);
		Log.d(TAG, "Finishing Splash.");

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/*  Close activity. */
		finish();
	}
}
