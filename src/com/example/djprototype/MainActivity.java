package com.example.djprototype;

import java.io.IOException;
import java.util.ArrayList;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener, SensorEventListener {

	MediaPlayer				mMediaPlayer		= null;
	SoundPool				mSoundPool			= null;
	int						drumId;
	int						cymbalId;

	SensorManager			mSensorManager;
	List<Sensor>			sensors;

	Button					mdPlay;
	Button					mdStop;
	Button					mdGo;
	Button					mdBack;
	Button					mDrum;
	Button					sensorSwitch;
	Button					clear;

	TextView				sensorList;
	TextView				gyroscopeData;
	TextView				accelerometerData;

	ListView				acceleroXList;
	ListView				acceleroYList;
	ListView				acceleroZList;

	ArrayAdapter<String>	acceleroXAdapter;
	ArrayAdapter<String>	acceleroYAdapter;
	ArrayAdapter<String>	acceleroZAdapter;

	List<String>			acceleroXDataList	= new ArrayList<String>();
	List<String>			acceleroYDataList	= new ArrayList<String>();
	List<String>			acceleroZDataList	= new ArrayList<String>();

	int						totalTime;
	int						currentTime;
	static int				MOVE_TIME			= 1000;
	int						tempo				= 0;
	float					aX;
	float					aY;
	float					aZ;
	boolean					sensorRun			= true;
	int						count				= 0;

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
		drumId = mSoundPool.load(this, R.raw.se_maoudamashii_instruments_drum2_bassdrum, 1);
		cymbalId = mSoundPool.load(this, R.raw.se_maoudamashii_instruments_drum2_cymbal, 1);

		// view
		mdBack = (Button) findViewById(R.id.button1);
		mdStop = (Button) findViewById(R.id.button2);
		mdPlay = (Button) findViewById(R.id.button3);
		mdGo = (Button) findViewById(R.id.button4);
		mDrum = (Button) findViewById(R.id.button5);
		sensorSwitch = (Button) findViewById(R.id.button6);
		clear = (Button) findViewById(R.id.button7);

		// sensorList = (TextView) findViewById(R.id.textView4);
		// sensorList.setText(str);
		gyroscopeData = (TextView) findViewById(R.id.textView5);
		accelerometerData = (TextView) findViewById(R.id.textView6);

		acceleroXList = (ListView) findViewById(R.id.listView1);
		acceleroYList = (ListView) findViewById(R.id.listView2);
		acceleroZList = (ListView) findViewById(R.id.listView3);
		acceleroXAdapter = new ArrayAdapter<String>(this, R.layout.list_row, acceleroXDataList);
		acceleroYAdapter = new ArrayAdapter<String>(this, R.layout.list_row, acceleroYDataList);
		acceleroZAdapter = new ArrayAdapter<String>(this, R.layout.list_row, acceleroZDataList);
		acceleroXList.setAdapter(acceleroXAdapter);
		acceleroYList.setAdapter(acceleroYAdapter);
		acceleroZList.setAdapter(acceleroZAdapter);

		mdBack.setOnClickListener(this);
		mdStop.setOnClickListener(this);
		mdPlay.setOnClickListener(this);
		mdGo.setOnClickListener(this);
		mDrum.setOnClickListener(this);
		sensorSwitch.setOnClickListener(this);
		clear.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		List<Sensor> gyroSensors = mSensorManager.getSensorList(Sensor.TYPE_GYROSCOPE);
		if (gyroSensors.size() > 0) {
			Sensor sensor = gyroSensors.get(0);
			mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
		}
		List<Sensor> accelerometers = mSensorManager.getSensorList(Sensor.TYPE_LINEAR_ACCELERATION);
		if (accelerometers.size() > 0) {
			Sensor sensor = accelerometers.get(0);
			mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
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
			mSoundPool.play(drumId, 1.0f, 1.0f, 1, 0, 1.0f);
			break;
		// sensor switch
		case R.id.button6:
			sensorRun = !sensorRun;
			break;
		case R.id.button7:
			acceleroXAdapter.clear();
			acceleroYAdapter.clear();
			acceleroZAdapter.clear();
			break;
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
		if (sensorRun) {
			if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {

			}

			// 直線加速度センサー
			if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
				// 最新の値のみを表示
				String str = "加速度センサー値:" + "\nX軸:" + event.values[SensorManager.DATA_X] + "\nY軸:" + event.values[SensorManager.DATA_Y] + "\nZ軸:" + event.values[SensorManager.DATA_Z];
				accelerometerData.setText(str);
				// 端末を振った動きの値が出たら効果音を鳴らす
				if (event.values[SensorManager.DATA_Y] < -5 && aY - event.values[SensorManager.DATA_Y] > 10) {
					mSoundPool.play(cymbalId, 1.0f, 1.0f, 1, 0, 1.0f);
				}
				// 変化のあった値をリストに追加
				if (-0.1 > event.values[SensorManager.DATA_X] || event.values[SensorManager.DATA_X] > 0.1) {
					aX = event.values[SensorManager.DATA_X];
				}
				if (-0.1 > event.values[SensorManager.DATA_Y] || event.values[SensorManager.DATA_Y] > 0.1) {
					aY = event.values[SensorManager.DATA_Y];
				}
				if (-0.1 > event.values[SensorManager.DATA_Z] || event.values[SensorManager.DATA_Z] > 0.1) {
					aZ = event.values[SensorManager.DATA_Z];
				}
				// リストにセンサーの値を表示
				acceleroXAdapter.add("X軸：" + aX);
				acceleroYAdapter.add("Y軸：" + aY);
				acceleroZAdapter.add("Z軸：" + aZ);
				acceleroXList.setSelection(acceleroXDataList.size());
				acceleroYList.setSelection(acceleroYDataList.size());
				acceleroZList.setSelection(acceleroZDataList.size());
			}
			// ジャイロセンサー
			if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
				String str = "ジャイロセンサー値:" + "\nX軸中心:" + event.values[SensorManager.DATA_X] + "\nY軸中心:" + event.values[SensorManager.DATA_Y] + "\nZ軸中心:" + event.values[SensorManager.DATA_Z];
				gyroscopeData.setText(str);
			}
		}
	}
}
