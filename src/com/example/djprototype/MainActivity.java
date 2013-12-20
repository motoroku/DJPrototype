package com.example.djprototype;

import java.io.IOException;
import java.util.List;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener, SensorEventListener {

	MediaPlayer		mMediaPlayer	= null;
	SoundPool		mSoundPool		= null;
	int				spId;

	SensorManager	mSensorManager;
	List<Sensor>	sensors;

	Button			mdPlay;
	Button			mdStop;
	Button			mdGo;
	Button			mdBack;
	Button			mDrum;

	TextView		sensorList;
	TextView		gyroscopeData;
	TextView		accelerometerData;

	int				totalTime;
	int				currentTime;
	static int		MOVE_TIME		= 1000;
	int				tempo			= 0;
	float			aX;
	float			aY;
	float			aZ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// sensormanager
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		// mediaplayer
		mMediaPlayer = MediaPlayer.create(this, R.raw.bgm_maoudamashii_neorock33);
		totalTime = mMediaPlayer.getDuration();

		// soundpool
		mSoundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		spId = mSoundPool.load(this, R.raw.se_maoudamashii_instruments_drum2_bassdrum, 1);

		// view
		mdBack = (Button) findViewById(R.id.button1);
		mdStop = (Button) findViewById(R.id.button2);
		mdPlay = (Button) findViewById(R.id.button3);
		mdGo = (Button) findViewById(R.id.button4);
		mDrum = (Button) findViewById(R.id.button5);

		// sensorList = (TextView) findViewById(R.id.textView4);
		// sensorList.setText(str);
		gyroscopeData = (TextView) findViewById(R.id.textView5);
		accelerometerData = (TextView) findViewById(R.id.textView6);

		mdBack.setOnClickListener(this);
		mdStop.setOnClickListener(this);
		mdPlay.setOnClickListener(this);
		mdGo.setOnClickListener(this);
		mDrum.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		List<Sensor> gyroSensors = mSensorManager.getSensorList(Sensor.TYPE_GYROSCOPE);
		if (gyroSensors.size() > 0) {
			Sensor sensor = gyroSensors.get(0);
			mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
		}
		List<Sensor> accelerometers = mSensorManager.getSensorList(Sensor.TYPE_LINEAR_ACCELERATION);
		if (accelerometers.size() > 0) {
			Sensor sensor = accelerometers.get(0);
			mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
		}
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

		// Listenerの登録解除
		mSensorManager.unregisterListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();

		switch (id) {
		// MediaPlayer
		// back
		case R.id.button1:
			currentTime = mMediaPlayer.getCurrentPosition();
			mMediaPlayer.seekTo(currentTime - MainActivity.MOVE_TIME);
			break;
		// stop
		case R.id.button2:
			mMediaPlayer.stop();
			try {
				mMediaPlayer.prepare();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		// play
		case R.id.button3:
			mMediaPlayer.start();
			break;
		// go
		case R.id.button4:
			currentTime = mMediaPlayer.getCurrentPosition();
			mMediaPlayer.seekTo(currentTime + MainActivity.MOVE_TIME);
			break;
		// SoundPool
		// Drum
		case R.id.button5:
			mSoundPool.play(spId, 1.0f, 1.0f, 1, 0, 1.0f);
		default:
			break;
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		tempo++;
		if (tempo == 5) {
			if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
				if (event.values[SensorManager.DATA_Y] - aY > 1) {
					currentTime = mMediaPlayer.getCurrentPosition();
					mMediaPlayer.seekTo(currentTime - MainActivity.MOVE_TIME);
					aY = event.values[SensorManager.DATA_Y];
				}
				tempo = 0;
			}
		}

		if (tempo == 15) {
			if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
				String str = "ジャイロセンサー値:" + "\nX軸中心:" + event.values[SensorManager.DATA_X] + "\nY軸中心:" + event.values[SensorManager.DATA_Y] + "\nZ軸中心:" + event.values[SensorManager.DATA_Z];

				gyroscopeData.setText(str);
			}
		}
		if (tempo == 20) {
			if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
				String str = "加速度センサー値:" + "\nX軸:" + event.values[SensorManager.DATA_X] + "\nY軸:" + event.values[SensorManager.DATA_Y] + "\nZ軸:" + event.values[SensorManager.DATA_Z];
				aX = event.values[SensorManager.DATA_X];
				aY = event.values[SensorManager.DATA_Y];
				aZ = event.values[SensorManager.DATA_Z];
				accelerometerData.setText(str);
			}
		}

		if (tempo == 25) {
			tempo = 0;
		}
	}
}
