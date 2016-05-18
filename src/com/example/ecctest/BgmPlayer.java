package com.example.ecctest;

import android.content.Context;
import android.media.MediaPlayer;

public class BgmPlayer {
	private MediaPlayer mediaPlayer;

	public BgmPlayer(Context context, int resource) {
		// BGMファイルを読み込む
		this.mediaPlayer = MediaPlayer.create(context, resource);
		// ループ再生
		this.mediaPlayer.setLooping(true);
		// 音量設定(R,L)(0~1.0)
		//左右のバランスだから音量には関係ない
		this.mediaPlayer.setVolume(0.2f, 0.2f);
	}

	/**
	 * BGMを再生する
	 */
	public void start() {
		if (!mediaPlayer.isPlaying()) {
			mediaPlayer.seekTo(0);
			mediaPlayer.start();
		}
	}

	/**
	 * BGMを停止する
	 */
	public void stop() {
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
			mediaPlayer.prepareAsync();
		}
	}
}