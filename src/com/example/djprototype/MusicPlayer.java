package com.example.djprototype;

import java.io.IOException;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.SoundPool;

public class MusicPlayer implements OnCompletionListener {
	MediaPlayer	mediaPlayer;
	MediaPlayer	lessonPlayer;
	SoundPool	soundPool;
	SoundPool	gameSound;

	Context		context;

	int			sideSwingId;
	int			frontSlideId;
	int			verticalSlideId;
	int			rhythmHighId;
	int			rhythmMiddleId;
	int			rhythmLowId;

	int			correctId;
	int			incorrectId;

	enum Mode {
		rock, dj, debug, japan, game;
		public Mode getNextMode(Mode mode) {
			if (mode == rock) {
				return Mode.dj;
			} else if (mode == dj) {
				return japan;
			} else if (mode == japan) {
				return game;
			} else if (mode == game) {
				return debug;
			} else if (mode == debug) {
				return rock;
			} else {
				return rock;
			}
		}
	}

	Mode	mCurrentMode	= Mode.rock;

	public MusicPlayer(Context context) {
		this.context = context;
		mediaPlayer = MediaPlayer.create(context, R.raw.bgm_maoudamashii_acoustic04);
		lessonPlayer = MediaPlayer.create(context, R.raw.lesson1);
		soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		gameSound = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		setSound(mCurrentMode);

		mediaPlayer.setOnCompletionListener(this);
		lessonPlayer.setOnCompletionListener(this);
	}

	public void startMusic() {
		mediaPlayer.start();
	}

	public void stopMusic() {
		mediaPlayer.stop();
		try {
			mediaPlayer.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void startLesson() {
		lessonPlayer.start();
	}

	public void stopLesson() {
		lessonPlayer.stop();
		try {
			lessonPlayer.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void soundSideSwing() {
		soundPool.play(sideSwingId, 2.0f, 2.0f, 1, 0, 1.0f);
	}

	public void soundFrontSlide() {
		soundPool.play(frontSlideId, 1.5f, 1.5f, 1, 0, 1.0f);
	}

	public void soundRhythmHigh() {
		soundPool.play(rhythmHighId, 2.0f, 2.0f, 1, 0, 1.0f);
	}

	public void soundRhytmMiddle() {
		soundPool.play(rhythmMiddleId, 1.0f, 1.0f, 1, 0, 1.0f);
	}

	public void soundRhythmLow() {
		soundPool.play(rhythmLowId, 1.0f, 1.0f, 1, 0, 1.0f);
	}

	public void soundVerticalSlide() {
		soundPool.play(verticalSlideId, 1.0f, 1.0f, 1, 0, 1.0f);
	}

	public void soundCorrect() {
		gameSound.play(correctId, 1.0f, 1.0f, 1, 0, 1.0f);
	}

	public void soundIncorrect() {
		gameSound.play(incorrectId, 1.0f, 1.0f, 1, 0, 1.0f);
	}

	public Mode changeMode() {
		mCurrentMode = mCurrentMode.getNextMode(mCurrentMode);
		setSound(mCurrentMode);
		return mCurrentMode;
	}

	private void setSound(Mode mode) {
		switch (mode) {
		case rock:
			setRockSound();
			break;
		case dj:
			setDjSound();
			break;
		case japan:
			setJapanSound();
			break;
		case game:
			setRockSound();
			break;
		case debug:
			break;
		default:
			break;
		}
		correctId = gameSound.load(context, R.raw.se_maoudamashii_onepoint15_maru, 1);
		incorrectId = gameSound.load(context, R.raw.se_maoudamashii_onepoint14_batu, 1);
	}

	private void setRockSound() {
		sideSwingId = soundPool.load(context, R.raw.se_maoudamashii_instruments_drum2_bassdrum, 1);
		frontSlideId = soundPool.load(context, R.raw.se_maoudamashii_instruments_drum2_cymbal, 1);
		rhythmHighId = soundPool.load(context, R.raw.se_maoudamashii_instruments_drum2_hat, 1);
		rhythmMiddleId = soundPool.load(context, R.raw.se_maoudamashii_instruments_bass11, 1);
		rhythmLowId = soundPool.load(context, R.raw.se_maoudamashii_instruments_bass13, 1);
	}

	private void setDjSound() {
		sideSwingId = soundPool.load(context, R.raw.ta_ge_tambourine02, 1);
		verticalSlideId = soundPool.load(context, R.raw.nc30614, 1);
		rhythmHighId = soundPool.load(context, R.raw.se_maoudamashii_voice_human03, 1);
		rhythmMiddleId = soundPool.load(context, R.raw.se_maoudamashii_instruments_bass11, 1);
		rhythmLowId = soundPool.load(context, R.raw.se_maoudamashii_instruments_bass13, 1);
	}

	private void setJapanSound() {
		sideSwingId = soundPool.load(context, R.raw.ta_ge_kotaiko02, 1);
		frontSlideId = soundPool.load(context, R.raw.ta_ge_ootaiko02, 1);
		rhythmHighId = soundPool.load(context, R.raw.clappers01, 1);
		rhythmMiddleId = soundPool.load(context, R.raw.se_maoudamashii_instruments_bass11, 1);
		rhythmLowId = soundPool.load(context, R.raw.se_maoudamashii_instruments_bass13, 1);
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
	}
}
