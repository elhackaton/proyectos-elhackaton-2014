package com.hackaton.carrot;

import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

public class CarrotActivity extends FragmentActivity {
	final static String CARROT_TAG = "carrot";

	void showMessage(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
	}

	void showErrorMessage(String msg) {
		showMessage("ERROR: " + msg);
	}
	
	static void logError(String msg) {
		Log.e(CARROT_TAG, msg);
	}
}
