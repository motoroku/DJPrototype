package com.example.djprototype;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener, Runnable, SensorEventListener {

	MediaPlayer		mMediaPlayer	= null;
	SoundPool		mSoundPool		= null;
	AudioTrack		mAudioTrack		= null;
	int				spId;
	int				bufferSize;
	byte[]			byteData;
	Thread			playThread;
	File			file;

	SensorManager	mSensorManager;
	List<Sensor>	sensors;

	Button			mdPlay;
	Button			mdStop;
	Button			mdGo;
	Button			mdBack;

	Button			spPlay;
	Button			spStop;
	Button			spGo;
	Button			spBack;
	Button			spTempoUp;
	Button			spTempoDown;

	Button			atPlay;
	Button			atStop;
	Button			atGo;
	Button			atBack;

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
		// sensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
		// String str = "実装されているセンサー一覧：\n";
		// if (sensors != null) {
		// for (Sensor s : sensors) {
		// str += s.getName() + "\n";
		// }
		// } else {
		// str += "センサーが存在しません";
		// }

		// audiotrack用ファイル読み込み
		file = new File("/sdcard/cm1_001.wav");
		byteData = new byte[(int) file.length()];
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			in.read(byteData);
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// audiotrack
		initialize();

		// mediaplayer
		mMediaPlayer = MediaPlayer.create(this, R.raw.bgm_maoudamashii_orchestra12);
		totalTime = mMediaPlayer.getDuration();

		// soundpool
		mSoundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		spId = mSoundPool.load(this, R.raw.se_maoudamashii_onepoint33, 1);

		// view
		mdBack = (Button) findViewById(R.id.button1);
		mdStop = (Button) findViewById(R.id.button2);
		mdPlay = (Button) findViewById(R.id.button3);
		mdGo = (Button) findViewById(R.id.button4);

		spBack = (Button) findViewById(R.id.button5);
		spStop = (Button) findViewById(R.id.button6);
		spPlay = (Button) findViewById(R.id.button7);
		spGo = (Button) findViewById(R.id.button8);
		spTempoUp = (Button) findViewById(R.id.button9);
		spTempoDown = (Button) findViewById(R.id.button10);

		atStop = (Button) findViewById(R.id.button11);
		atPlay = (Button) findViewById(R.id.button12);

		// sensorList = (TextView) findViewById(R.id.textView4);
		// sensorList.setText(str);
		gyroscopeData = (TextView) findViewById(R.id.textView5);
		accelerometerData = (TextView) findViewById(R.id.textView6);

		mdBack.setOnClickListener(this);
		mdStop.setOnClickListener(this);
		mdPlay.setOnClickListener(this);
		mdGo.setOnClickListener(this);

		spBack.setOnClickListener(this);
		spStop.setOnClickListener(this);
		spPlay.setOnClickListener(this);
		spGo.setOnClickListener(this);
		spTempoUp.setOnClickListener(this);
		spTempoDown.setOnClickListener(this);

		atStop.setOnClickListener(this);
		atPlay.setOnClickListener(this);

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
		int streamId = 0;

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
		// back
		case R.id.button5:
			break;
		// stop
		case R.id.button6:
			mSoundPool.stop(streamId);
			break;
		// play
		case R.id.button7:
			streamId = mSoundPool.play(spId, 1.0f, 1.0f, 1, 0, 1.0f);
			break;
		// go
		case R.id.button8:
			break;
		// tempoUp
		case R.id.button9:
			streamId = mSoundPool.play(spId, 1.0f, 1.0f, 1, 0, 2.0f);
			break;
		// tempoDown
		case R.id.button10:
			streamId = mSoundPool.play(spId, 1.0f, 1.0f, 1, 0, 0.5f);
			break;
		// stop
		case R.id.button11:
			if (mAudioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
				mAudioTrack.stop();
				playThread = null;
				initialize();
			}
			break;
		// play
		case R.id.button12:
			playThread.start();
			break;

		default:
			break;
		}
	}

	void initialize() {
		// 必要となるバッファサイズを計算
		bufferSize = android.media.AudioTrack.getMinBufferSize(44100, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT);

		// AudioTrackインスタンス作成
		mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize, AudioTrack.MODE_STREAM);

		// スレッドインスタンス作成
		playThread = new Thread(this);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (mAudioTrack != null) {
			// 再生開始
			mAudioTrack.play();
			mAudioTrack.write(byteData, 0, byteData.length);
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
		if (tempo == 15) {
			if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
				String str = "ジャイロセンサー値:" + "\nX軸中心:" + event.values[SensorManager.DATA_X] + "\nY軸中心:" + event.values[SensorManager.DATA_Y] + "\nZ軸中心:" + event.values[SensorManager.DATA_Z];
				aX = event.values[SensorManager.DATA_X];
				aY = event.values[SensorManager.DATA_Y];
				aZ = event.values[SensorManager.DATA_Z];
				gyroscopeData.setText(str);
			}
		}
		if (tempo == 30) {
			if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
				String str = "加速度センサー値:" + "\nX軸:" + event.values[SensorManager.DATA_X] + "\nY軸:" + event.values[SensorManager.DATA_Y] + "\nZ軸:" + event.values[SensorManager.DATA_Z];
				accelerometerData.setText(str);
			}
			tempo = 0;
		}
	}
}
