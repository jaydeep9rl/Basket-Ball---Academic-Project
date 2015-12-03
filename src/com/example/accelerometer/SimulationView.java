package com.example.accelerometer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;

public class SimulationView extends View implements SensorEventListener {
	private Bitmap _mField;
	private Bitmap _mBasket;
	private Bitmap _mBitmap;

	private static final int BALL_SIZE = 50;
	private static final int BASKET_SIZE = 150;

	private Display _mDisplay;
	private SensorManager _mSensorManager;
	private Sensor _Sensor;

	private float _mXOrigin;
	private float _mYOrigin;

	private float _mHorizontalBound;
	private float _mVerticalBound;

	private float x, y, z;
	private long timestamps;

	Particle mBall = new Particle();

	public SimulationView(Context context) {
		super(context);

		Bitmap _Ball = BitmapFactory.decodeResource(getResources(),
				R.drawable.ball);
		_mBitmap = Bitmap.createScaledBitmap(_Ball, BALL_SIZE, BALL_SIZE, true);

		Bitmap _Basket = BitmapFactory.decodeResource(getResources(),
				R.drawable.basket);
		_mBasket = Bitmap.createScaledBitmap(_Basket, BASKET_SIZE, BASKET_SIZE,
				true);

		Options _Ops = new Options();
		_Ops.inDither = true;
		_Ops.inPreferredConfig = Bitmap.Config.RGB_565;
		_mField = BitmapFactory.decodeResource(getResources(),
				R.drawable.field, _Ops);

		WindowManager _mWindowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		_mDisplay = _mWindowManager.getDefaultDisplay();

		_mSensorManager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);
		_Sensor = _mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);

		_mXOrigin = w * 0.5f;
		_mYOrigin = h * 0.5f;

		_mHorizontalBound = (w - BALL_SIZE) * 0.5f;
		_mVerticalBound = (h - BALL_SIZE) * 0.5f;
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
			return;

		// Movement
		int _screenOrientation = _mDisplay.getRotation();

		switch (_screenOrientation) {
		default:
		case Surface.ROTATION_0:
			x = event.values[0];
			y = event.values[1];
			break;
		case Surface.ROTATION_90:
			x = -event.values[1];
			y = event.values[0];
			break;
		case Surface.ROTATION_180:
			x = -event.values[0];
			y = -event.values[1];
		case Surface.ROTATION_270:
			x = event.values[1];
			y = -event.values[0];
			break;
		}
		z = event.values[2];
		timestamps = event.timestamp;

	}

	void startSimulation() {
		this._mSensorManager.registerListener(this, _Sensor,
				SensorManager.SENSOR_DELAY_UI);
	}

	void stopSimulation() {
		this._mSensorManager.unregisterListener(this);
	}

	protected void onDraw(android.graphics.Canvas canvas) {
		super.onDraw(canvas);

		canvas.drawBitmap(_mField, 0, 0, null);
		canvas.drawBitmap(_mBasket, _mXOrigin - BASKET_SIZE / 2, _mYOrigin - BASKET_SIZE / 2, null);

		mBall.updatePosition(x, y, z, timestamps);
		mBall.resolveCollisionWithBounds(_mHorizontalBound, _mVerticalBound);

		canvas.drawBitmap(_mBitmap, (_mXOrigin - BALL_SIZE / 2) + mBall.mPosX, (_mYOrigin - BALL_SIZE / 2) - mBall.mPosY, null);
		invalidate();
	};

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
	}
}