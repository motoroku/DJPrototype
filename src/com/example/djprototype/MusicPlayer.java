package com.example.djprototype;

import java.io.IOException;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

public class MusicPlayer {
	MediaPlayer	mediaPlayer;
	SoundPool	soundPool;

	Context		context;

	int			swingId;
	int			moveId;
	int			rhythmId;
	int			slideId;

	enum Mode {
		rock, dj, debug, japan;
		public Mode getNextMode(Mode mode) {
			if (mode == Mode.rock) {
				return Mode.dj;
			} else if (mode == Mode.dj) {
				return japan;
			} else if (mode == Mode.japan) {
				return debug;
			} else {
				return rock;
			}
		}
	}

	Mode	mCurrentMode	= Mode.rock;

	public MusicPlayer(Context context) {
		this.context = context;
		mediaPlayer = MediaPlayer.create(context, R.raw.bgm_maoudamashii_neorock33);
		soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		setSound(mCurrentMode);
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

	public void soundSwing() {
		soundPool.play(swingId, 2.0f, 2.0f, 1, 0, 1.0f);
	}

	public void soundMove() {
		soundPool.play(moveId, 1.5f, 1.5f, 1, 0, 1.0f);
	}

	public void soundRhythm() {
		soundPool.play(rhythmId, 2.0f, 2.0f, 1, 0, 1.0f);
	}

	public void soundSlide() {
		soundPool.play(slideId, 1.0f, 1.0f, 1, 0, 1.0f);
	}

	public Mode changeMode() {
		mCurrentMode = mCurrentMode.getNextMode(mCurrentMode);
		setSound(mCurrentMode);
		return mCurrentMode;
	}

	private void setSound(Mode mode) {
		switch (mode) {
		case rock:
			swingId = soundPool.load(context, R.raw.se_maoudamashii_instruments_drum2_bassdrum, 1);
			moveId = soundPool.load(context, R.raw.se_maoudamashii_instruments_drum2_cymbal, 1);
			rhythmId = soundPool.load(context, R.raw.se_maoudamashii_instruments_drum2_hat, 1);
			break;
		case dj:
			swingId = soundPool.load(context, R.raw.ta_ge_tambourine02, 1);
			slideId = soundPool.load(context, R.raw.nc30614, 1);
			rhythmId = soundPool.load(context, R.raw.se_maoudamashii_voice_human03, 1);
			break;
		case japan:
			swingId = soundPool.load(context, R.raw.ta_ge_kotaiko02, 1);
			moveId = soundPool.load(context, R.raw.ta_ge_ootaiko02, 1);
			rhythmId = soundPool.load(context, R.raw.clappers01, 1);
			break;
		case debug:
			break;
		default:
			break;
		}
	}
}
