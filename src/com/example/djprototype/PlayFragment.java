package com.example.djprototype;

import java.util.List;
import com.example.djprototype.MusicPlayer.Mode;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class PlayFragment extends Fragment implements OnClickListener, SensorEventListener {
	// Logic
	MotionHandler			mMotionHandler;
	MusicPlayer				mMusicPlayer;
	// Sensor
	SensorManager			mSensorManager;
	List<Sensor>			sensors;
	// View
	ImageButton				mModeChange;
	Button					mPlay;
	Button					mBack;
	Button					mForward;
	TextView				mCurrentMode;
	ImageView				mSound1;
	ImageView				mSound2;
	ImageView				mSound3;
	// Variables
	ArrayAdapter<String>	adapter;
	boolean					sensorRun	= true;
	int						currentTime;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_play, container, false);
		Context context = getActivity();
		// ÉCÉìÉXÉ^ÉìÉXê∂ê¨
		mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		mMusicPlayer = new MusicPlayer(context);
		mMotionHandler = new MotionHandler();
		// ViewÇÃê›íË
		createView(v);
		reloadModeView();
		return v;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// ListenerÇÃìoò^
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
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		// ListenerÇÃìoò^âèú
		mSensorManager.unregisterListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.button_modeChange:
			mMusicPlayer.changeMode();
			reloadModeView();
			break;
		case R.id.button_play_stop:
			if (mMusicPlayer.mediaPlayer.isPlaying()) {
				mMusicPlayer.stopMusic();
				mPlay.setText("Play");
			} else {
				mMusicPlayer.startMusic();
				mPlay.setText("Stop");
			}
			break;
		case R.id.button_back:
			currentTime = mMusicPlayer.mediaPlayer.getCurrentPosition();
			mMusicPlayer.mediaPlayer.seekTo(currentTime - 5000);
			break;
		case R.id.button_foward:
			currentTime = mMusicPlayer.mediaPlayer.getCurrentPosition();
			mMusicPlayer.mediaPlayer.seekTo(currentTime + 5000);
			break;
		case R.id.imageView_sound1:
			if (mMusicPlayer.mCurrentMode == Mode.debug) {
				FragmentManager fm = getFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				MainFragment fragment = new MainFragment();
				ft.replace(R.id.LinearLayout2, fragment);
				ft.addToBackStack(null);
				ft.commit();
			} else {
				mMusicPlayer.soundRhythmHigh();
			}
			break;
		case R.id.imageView_sound2:
			mMusicPlayer.soundRhytmMiddle();
			break;
		case R.id.imageView_sound3:
			mMusicPlayer.soundRhythmLow();
			break;
		default:
			break;
		}
	}

	private void createView(View v) {
		mModeChange = (ImageButton) v.findViewById(R.id.button_modeChange);
		mPlay = (Button) v.findViewById(R.id.button_play_stop);
		mBack = (Button) v.findViewById(R.id.button_back);
		mForward = (Button) v.findViewById(R.id.button_foward);
		mCurrentMode = (TextView) v.findViewById(R.id.textView_currentMode);
		mSound1 = (ImageView) v.findViewById(R.id.imageView_sound1);
		mSound2 = (ImageView) v.findViewById(R.id.imageView_sound2);
		mSound3 = (ImageView) v.findViewById(R.id.imageView_sound3);

		mModeChange.setOnClickListener(this);
		mPlay.setOnClickListener(this);
		mBack.setOnClickListener(this);
		mForward.setOnClickListener(this);
		mSound1.setOnClickListener(this);
		mSound2.setOnClickListener(this);
		mSound3.setOnClickListener(this);
	}

	private void reloadModeView() {
		switch (mMusicPlayer.mCurrentMode) {
		case rock:
			mCurrentMode.setText("ROCK");
			break;
		case dj:
			mCurrentMode.setText("DJ");
			break;
		case japan:
			mCurrentMode.setText("JAPAN");
			break;
		case debug:
			mCurrentMode.setText("DEBUG MODE");
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
		}
	}
}
