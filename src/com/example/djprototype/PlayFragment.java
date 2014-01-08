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
	MotionHandler			mMotionHandler;
	MusicPlayer				mMusicPlayer;
	UserAction				mUserAction;
	LessonStage				mLessonStage;
	// Sensor
	SensorManager			mSensorManager;
	List<Sensor>			sensors;
	// View
	Button					mPlay;
	Button					mBack;
	Button					mForward;
	TextView				mCurrentMode;
	TextView				mLessonNo;
	ImageView				mSound1;
	ImageView				mSound2;
	ImageView				mSound3;
	// Drawable
	Drawable				play;
	Drawable				stop;
	Drawable				rock;
	Drawable				dj;
	Drawable				japan;
	Drawable				game;
	Drawable				debug;
	// Variables
	ArrayAdapter<String>	adapter;
	boolean					sensorRun	= true;
	int						currentLesson;
	Resources				resources;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_play, container, false);
		Context context = getActivity();
		// インスタンス生成
		mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		mMotionHandler = new MotionHandler();
		mUserAction = new UserAction();
		mLessonStage = new LessonStage(0); // TODO ステージ選択機能用の引数
		mMusicPlayer = new MusicPlayer(context, mLessonStage); // 各ステージ用のMusicPlayerという感じで
		resources = getResources();

		// Viewの設定
		mMusicPlayer.mCurrentMode = Mode.game;
		createView(v);
		reloadModeView();
		setLessonNo();
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
			break;
		case R.id.button_play_stop:
			if (mMusicPlayer.mediaPlayer.isPlaying()) {
				mPlay.setBackgroundDrawable(play);
			} else {
				mPlay.setBackgroundDrawable(stop);
				mMusicPlayer.startLesson();
				mUserAction.isUserTurn = true;
			}
			break;
		case R.id.button_back:
			currentLesson = mMusicPlayer.previousLesson(currentLesson);
			setLessonNo();
			break;
		case R.id.button_foward:
			currentLesson = mMusicPlayer.nextLesson(currentLesson);
			setLessonNo();
			break;
		case R.id.imageView_sound1:
			mMusicPlayer.soundRhythmHigh();
			mUserAction.addUserAction(Move.highTap);
			isClear();
			break;
		case R.id.imageView_sound2:
			mMusicPlayer.soundRhytmMiddle();
			mUserAction.addUserAction(Move.middleTap);
			isClear();
			break;
		case R.id.imageView_sound3:
			mMusicPlayer.soundRhythmLow();
			mUserAction.addUserAction(Move.lowTap);
			isClear();
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
			if (mMotionHandler.frontSlide(event)) {
				mMusicPlayer.soundFrontSlide();
				mUserAction.addUserAction(Move.frontSlide);
			}
			if (mMotionHandler.sideSwing(event)) {
				mMusicPlayer.soundSideSwing();
				mUserAction.addUserAction(Move.sideSwing);
			}
			if (mMotionHandler.verticallSlide(event)) {
				if (mMusicPlayer.mCurrentMode == Mode.dj) {
					mMusicPlayer.soundVerticalSlide();
					mUserAction.addUserAction(Move.verticalSlide);
				}
			}
			mMotionHandler.reloadData(event);
			isClear();
		}
	}

	private void isClear() {
		if (mUserAction.isFinishedUserAction(mLessonStage.getCorrectMove(currentLesson))) {
			if (mUserAction.isCorrectUserAction(mLessonStage.getCorrectMove(currentLesson))) {
				mMusicPlayer.soundCorrect();
				mUserAction.isUserTurn = false;
			} else {
				mMusicPlayer.soundIncorrect();
			}
			mUserAction.reset();
		}
	}

	private void createView(View v) {
		mPlay = (Button) v.findViewById(R.id.button_play_stop);
		mBack = (Button) v.findViewById(R.id.button_back);
		mForward = (Button) v.findViewById(R.id.button_foward);
		mCurrentMode = (TextView) v.findViewById(R.id.textView_currentMode);
		mLessonNo = (TextView) v.findViewById(R.id.textView_LessonNo);
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
		game = resources.getDrawable(R.drawable.game);
		debug = resources.getDrawable(R.drawable.debug);
	}

	private void reloadModeView() {
		// setBackground()だとAPI16からなので
		// 最小SDKバージョンに対応出来ないので、とりあえずsetBackgroudnDrawable()で対応
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
		case game:
			mCurrentMode.setBackgroundDrawable(game);
			break;
		case debug:
			mCurrentMode.setBackgroundDrawable(debug);
		default:
			break;
		}
		mUserAction.isUserTurn = false;
		mUserAction.reset();
	}

	private void setLessonNo() {
		mLessonNo.setText(currentLesson);
	}

}
