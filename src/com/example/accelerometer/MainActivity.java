package com.example.accelerometer;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public class MainActivity extends ActionBarActivity {
	private WakeLock mWakeLock;
	private static final String TAG = "com.example.accelerometer.MainActivity";

	SimulationView _Sview ;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		PowerManager _mPowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = _mPowerManager.newWakeLock(
				PowerManager.SCREEN_BRIGHT_WAKE_LOCK, TAG);
		_Sview = new SimulationView(MainActivity.this);
		setContentView(_Sview);
	}

	@Override
	public void onResume() {
		super.onResume(); // Acquire WakeLock
		this.mWakeLock.acquire();
        setContentView(_Sview);
		_Sview.startSimulation();
	}

	@Override
	public void onPause() {
		super.onPause(); // Release WakeLock
		_Sview.stopSimulation();
		this.mWakeLock.release();
	}
}
