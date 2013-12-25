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

	int			drumId;
	int			cymbalId;
	int			hiHatId;
	int			scratchId;

	enum Mode {
		rock, dj, debug;
		public Mode getNextMode(Mode mode) {
			if (mode == Mode.rock) {
				return Mode.dj;
			} else if (mode == Mode.dj) {
				return debug;
			} else {
				return Mode.rock;
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

	public void soundDrum() {
		soundPool.play(drumId, 2.0f, 2.0f, 1, 0, 1.0f);
	}

	public void soundCymbal() {
		soundPool.play(cymbalId, 1.5f, 1.5f, 1, 0, 1.0f);
	}

	public void soundHiHat() {
		soundPool.play(hiHatId, 2.0f, 2.0f, 1, 0, 1.0f);
	}

	public void soundScratch() {
		soundPool.play(scratchId, 1.0f, 1.0f, 1, 0, 1.0f);
	}

	public Mode changeMode() {
		mCurrentMode = mCurrentMode.getNextMode(mCurrentMode);
		setSound(mCurrentMode);
		return mCurrentMode;
	}

	private void setSound(Mode mode) {
		switch (mode) {
		case rock:
			drumId = soundPool.load(context, R.raw.se_maoudamashii_instruments_drum2_bassdrum, 1);
			cymbalId = soundPool.load(context, R.raw.se_maoudamashii_instruments_drum2_cymbal, 1);
			hiHatId = soundPool.load(context, R.raw.se_maoudamashii_instruments_drum2_hat, 1);
			break;
		case dj:
			scratchId = soundPool.load(context, R.raw.nc30614, 1);
			break;
		case debug:
			break;
		default:
			break;
		}
	}
}
