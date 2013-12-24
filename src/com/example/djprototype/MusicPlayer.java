package com.example.djprototype;

import java.io.IOException;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

public class MusicPlayer {
	MediaPlayer	mediaPlayer;
	SoundPool	soundPool;

	int			drumId;
	int			cymbalId;

	public MusicPlayer(Context context) {
		mediaPlayer = MediaPlayer.create(context, R.raw.bgm_maoudamashii_neorock33);
		soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		drumId = soundPool.load(context, R.raw.se_maoudamashii_instruments_drum2_bassdrum, 1);
		cymbalId = soundPool.load(context, R.raw.se_maoudamashii_instruments_drum2_cymbal, 1);
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
		soundPool.play(cymbalId, 1.0f, 1.0f, 1, 0, 1.0f);
	}
}
