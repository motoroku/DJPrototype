package com.example.djprototype;

import java.util.List;
import com.example.djprototype.MusicPlayer.Mode;
import com.example.djprototype.UserAction.Move;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
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
	MotionHandler mMotionHandler;
	MusicPlayer mMusicPlayer;
	UserAction mUserAction;
	// Sensor
	SensorManager mSensorManager;
	List<Sensor> sensors;
	// View
	Button mPlay;
	Button mBack;
	Button mForward;
	TextView mCurrentMode;
	ImageView mSound1;
	ImageView mSound2;
	ImageView mSound3;
	// Drawable
	Drawable play;
	Drawable stop;
	Drawable rock;
	Drawable dj;
	Drawable japan;
	Drawable debug;
	// Variables
	ArrayAdapter<String> adapter;
	boolean sensorRun = true;
	int currentTime;
	Resources resources;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_play, container, false);
		Context context = getActivity();
		// インスタンス生成
		mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		mMusicPlayer = new MusicPlayer(context);
		mMotionHandler = new MotionHandler();
		mUserAction = new UserAction();
		resources = getResources();

		// お試しで正解を作成
		mUserAction.addCorrectAction(Move.sideSwing);
		mUserAction.addCorrectAction(Move.sideSwing);
		mUserAction.addCorrectAction(Move.frontSlide);

		// Viewの設定
		createView(v);
		reloadModeView();
		return v;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// Listenerの登録
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
		// Listenerの登録解除
		mSensorManager.unregisterListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
			case R.id.textView_currentMode:
				mMusicPlayer.changeMode();
				reloadModeView();
				break;
			case R.id.button_play_stop:
				if (mMusicPlayer.mediaPlayer.isPlaying()) {
					mMusicPlayer.stopMusic();
					mPlay.setBackgroundDrawable(play);
				} else {
					mMusicPlayer.startMusic();
					mPlay.setBackgroundDrawable(stop);
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
				// mMusicPlayer.soundRhytmMiddle();
				// mMusicPlayer.startDirection();
				mMusicPlayer.startDirection();
				mUserAction.isUserTurn = true;
				break;
			case R.id.imageView_sound3:
				// mMusicPlayer.soundRhythmLow();
				// mMusicPlayer.stopDirection();
				break;
			default:
				break;
		}
	}

	private void createView(View v) {
		mPlay = (Button) v.findViewById(R.id.button_play_stop);
		mBack = (Button) v.findViewById(R.id.button_back);
		mForward = (Button) v.findViewById(R.id.button_foward);
		mCurrentMode = (TextView) v.findViewById(R.id.textView_currentMode);
		mSound1 = (ImageView) v.findViewById(R.id.imageView_sound1);
		mSound2 = (ImageView) v.findViewById(R.id.imageView_sound2);
		mSound3 = (ImageView) v.findViewById(R.id.imageView_sound3);

		mCurrentMode.setOnClickListener(this);
		mPlay.setOnClickListener(this);
		mBack.setOnClickListener(this);
		mForward.setOnClickListener(this);
		mSound1.setOnClickListener(this);
		mSound2.setOnClickListener(this);
		mSound3.setOnClickListener(this);

		play = resources.getDrawable(R.drawable.play);
		stop = resources.getDrawable(R.drawable.stop);
		rock = resources.getDrawable(R.drawable.rock);
		dj = resources.getDrawable(R.drawable.dj);
		japan = resources.getDrawable(R.drawable.japan);
		debug = resources.getDrawable(R.drawable.debug);
	}

	private void reloadModeView() {
		// setBackground()だとAPI
		// 16からなので最小SDKバージョンに対応出来ないので、とりあえずsetBackgroudnDrawable()で対応
		switch (mMusicPlayer.mCurrentMode) {
			case rock:
				mCurrentMode.setBackgroundDrawable(rock);
				break;
			case dj:
				mCurrentMode.setBackgroundDrawable(dj);
				break;
			case japan:
				mCurrentMode.setBackgroundDrawable(japan);
				break;
			case debug:
				mCurrentMode.setBackgroundDrawable(debug);
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
				mUserAction.addUserAction(Move.frontSlide);
			}
			if (mMotionHandler.sideSwing(event)) {
				mMusicPlayer.soundSwing();
				mUserAction.addUserAction(Move.sideSwing);
			}
			if (mMotionHandler.verticallSlide(event)) {
				if (mMusicPlayer.mCurrentMode == Mode.dj) {
					mMusicPlayer.soundSlide();
					mUserAction.addUserAction(Move.verticalSlide);
				}
			}
			mMotionHandler.reloadData(event);

			if (mUserAction.isUserTurn && mUserAction.isFinishedUserAction()) {
				if (mUserAction.isCorrectUserAction()) {
					mMusicPlayer.soundCorrect();
				} else {
					mMusicPlayer.soundIncorrect();
				}
				mUserAction.reset();
			}
		}
	}
}
