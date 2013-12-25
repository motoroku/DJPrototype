package com.example.djprototype;

import java.util.ArrayList;
import java.util.List;

import com.example.djprototype.MusicPlayer.Mode;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainFragment extends Fragment implements OnClickListener, SensorEventListener {
	// Logic
	MotionHandler			mMotionHandler;
	MusicPlayer				mMusicPlayer;
	// Sensor
	SensorManager			mSensorManager;
	List<Sensor>			sensors;
	// view
	// Button
	Button					mdPlay;
	Button					mdStop;
	Button					mdGo;
	Button					mdBack;
	Button					mDrum;
	Button					sensorSwitch;
	Button					clear;
	// Variables
	float					accX;
	float					accY;
	float					accZ;
	float					gyroX;
	float					gyroY;
	float					gyroZ;
	Context					context;

	// accelerationList
	ListView				acceleroList;
	ArrayAdapter<String>	acceleroAdapter;
	List<String>			acceleroDataList	= new ArrayList<String>();
	// gyroList
	ListView				gyroList;
	ArrayAdapter<String>	gyroAdapter;
	List<String>			gyroDataList		= new ArrayList<String>();
	boolean					sensorRun			= true;
	TextView				gyroscopeText;
	TextView				accelerometerText;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_main, container, false);
		this.context = getActivity();

		// �C���X�^���X����
		mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		mMusicPlayer = new MusicPlayer(context);
		mMotionHandler = new MotionHandler();
		createView(v, context);
		return v;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// Listener�̓o�^
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
		mMusicPlayer = new MusicPlayer(context);
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		// Listener�̓o�^����
		mSensorManager.unregisterListener(this);
		mMusicPlayer = null;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		if (sensorRun) {
			if (mMotionHandler.frontSlide(event)) {
				mMusicPlayer.soundMove();
			}
			if (mMotionHandler.sideSwing(event)) {
				mMusicPlayer.soundSwing();
			}
			if (mMotionHandler.verticallSlide(event)) {
				if (mMusicPlayer.mCurrentMode == Mode.dj) {
					mMusicPlayer.soundSlide();
				}
			}
			mMotionHandler.reloadData(event);
			reloadData(event);
			reloadDisplay(event);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		// MediaPlayer
		// back
		case R.id.button1:
			break;
		// stop
		case R.id.button2:
			mMusicPlayer.stopMusic();
			break;
		// play
		case R.id.button3:
			mMusicPlayer.startMusic();
			break;
		// go
		case R.id.button4:
			break;
		// SoundPool
		// hi-hat
		case R.id.button5:
			mMusicPlayer.soundRhythmHigh();
			break;
		// sensor switch
		case R.id.button6:
			sensorRun = !sensorRun;
			break;
		// SensorDataList
		case R.id.button7:
			acceleroAdapter.clear();
			gyroAdapter.clear();
			break;
		default:
			break;
		}
	}

	private void reloadData(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
			// �ω��̂������l�����X�g�ɒǉ�
			if (-0.1 > event.values[SensorManager.DATA_X] || event.values[SensorManager.DATA_X] > 0.1) {
				accX = event.values[SensorManager.DATA_X];
			}
			if (-0.1 > event.values[SensorManager.DATA_Y] || event.values[SensorManager.DATA_Y] > 0.1) {
				accY = event.values[SensorManager.DATA_Y];
			}
			if (-0.1 > event.values[SensorManager.DATA_Z] || event.values[SensorManager.DATA_Z] > 0.1) {
				accZ = event.values[SensorManager.DATA_Z];
			}
		}
		if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
			if (-0.1 > event.values[SensorManager.DATA_X] || event.values[SensorManager.DATA_X] > 0.1) {
				gyroX = event.values[SensorManager.DATA_X];
			}
			if (-0.1 > event.values[SensorManager.DATA_Y] || event.values[SensorManager.DATA_Y] > 0.1) {
				gyroY = event.values[SensorManager.DATA_Y];
			}
			if (-0.1 > event.values[SensorManager.DATA_Z] || event.values[SensorManager.DATA_Z] > 0.1) {
				gyroZ = event.values[SensorManager.DATA_Z];
			}
		}
	}

	private void reloadDisplay(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
			// �ŐV�̒l�݂̂�\��
			String str1 = "�����x�Z���T�[�l:" + "\nX��:" + event.values[SensorManager.DATA_X] + "\nY��:" + event.values[SensorManager.DATA_Y] + "\nZ��:" + event.values[SensorManager.DATA_Z];
			accelerometerText.setText(str1);
			// ���X�g�ɃZ���T�[�̒l��\��
			acceleroAdapter.add(acceleroDataList.size() + "-X���F" + accX + "  Y���F" + accY + "  Z���F" + accZ);
			acceleroList.setSelection(acceleroDataList.size());
		}
		if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
			// �ŐV�̒l�݂̂�\��
			String str2 = "�W���C���Z���T�[�l:" + "\nX�����S:" + event.values[SensorManager.DATA_X] + "\nY�����S:" + event.values[SensorManager.DATA_Y] + "\nZ�����S:" + event.values[SensorManager.DATA_Z];
			gyroscopeText.setText(str2);
			// ���X�g�ɃZ���T�[�̒l��\��
			gyroAdapter.add(gyroDataList.size() + "-X���F" + gyroX + "  Y���F" + gyroY + "  Z���F" + gyroZ);
			gyroList.setSelection(gyroDataList.size());
		}
	}

	private void createView(View v, Context context) {
		mdBack = (Button) v.findViewById(R.id.button1);
		mdStop = (Button) v.findViewById(R.id.button2);
		mdPlay = (Button) v.findViewById(R.id.button3);
		mdGo = (Button) v.findViewById(R.id.button4);
		mDrum = (Button) v.findViewById(R.id.button5);
		sensorSwitch = (Button) v.findViewById(R.id.button6);
		clear = (Button) v.findViewById(R.id.button7);

		mdBack.setOnClickListener(this);
		mdStop.setOnClickListener(this);
		mdPlay.setOnClickListener(this);
		mdGo.setOnClickListener(this);
		mDrum.setOnClickListener(this);
		sensorSwitch.setOnClickListener(this);
		clear.setOnClickListener(this);

		acceleroList = (ListView) v.findViewById(R.id.listView1);
		acceleroAdapter = new ArrayAdapter<String>(context, R.layout.list_row, acceleroDataList);
		acceleroList.setAdapter(acceleroAdapter);

		gyroList = (ListView) v.findViewById(R.id.listView4);
		gyroAdapter = new ArrayAdapter<String>(context, R.layout.list_row, gyroDataList);
		gyroList.setAdapter(gyroAdapter);

		accelerometerText = (TextView) v.findViewById(R.id.textView6);
		gyroscopeText = (TextView) v.findViewById(R.id.textView5);

	}
}
