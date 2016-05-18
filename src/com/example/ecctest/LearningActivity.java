package com.example.ecctest;

import static com.example.ecctest.MethodSet.*;

import java.io.IOException;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class LearningActivity extends BaseActivity implements OnClickListener {

	/* アプり共有不変定数定義 */
	private final int MP = MainActivity.MP;
	private final int WC = MainActivity.WC;

	//文章
	private final String[] TEST_RESULT = MainActivity.TEST_RESULT;

	//ID（0以上使用禁止）
	private final int NO_ID = MainActivity.NO_ID;
	private final int BACK_BUTTON = MainActivity.BACK_BUTTON;
	private final int LEARNING_TEST_BUTTON = MainActivity.LEARNING_TEST_BUTTON;

	//ボタンタグ
	private final String NO_TAG = MainActivity.NO_TAG;

	//色
	private final int BACK_COLOR1 = MainActivity.BACK_COLOR1;
	private final int BACK_COLOR2 = MainActivity.BACK_COLOR2;
	private final int BACK_COLOR3 = MainActivity.BACK_COLOR3;
	private final int BACK_COLOR4 = MainActivity.BACK_COLOR4;
	private final int WORD_COLOR1 = MainActivity.WORD_COLOR1;
	private final int WORD_COLOR2 = MainActivity.WORD_COLOR2;
	private final int WORD_COLOR3 = MainActivity.WORD_COLOR3;

	//画像
	private final int BACK_GROUND_IMAGE = MainActivity.BACK_GROUND_IMAGE;

	//サイズ
	private final float WORD_SIZE = MainActivity.WORD_SIZE;
	private final float WORD_SIZE2 = MainActivity.WORD_SIZE2;
	private final float WORD_SIZE3 = MainActivity.WORD_SIZE3;
	private final int[] BIG_BUTTON_SIZE = MainActivity.BIG_BUTTON_SIZE;
	private final int[] BACK_BUTTON_SIZE = MainActivity.BACK_BUTTON_SIZE;

	private final String EX_BUTTON_NAME[] = MainActivity.EX_BUTTON_NAME;

	/* MUSIC */
	private final int MAIN_MUSIC = MainActivity.MAIN_MUSIC;
	private final int SUB_MUSIC = MainActivity.SUB_MUSIC;
	private final int QUIETE_MUSIC = MainActivity.QUIETE_MUSIC;
	
	/* 学習情報取得 */
	private final String leanigIndex[] = MainActivity.leanigIndex;
	private final int leaningIndexLength = MainActivity.leaningIndexLength;
	private final String LEARNING_SENTENCE[][] = MainActivity.LEARNING_SENTENCE;
	private final String LEARNING_MP3[][][] = MainActivity.LEARNIG_MP3;
	private final int MAX_LEN = MainActivity.MAX_LEN;

	/* 学習INDEX */
	private int learningIndex;

	/* テストの答え */
	private int testAnswer[] = new int[LEARNING_MP3[learningIndex][0].length - 1];

	/* 大域変数 */
	MediaPlayer mp[] = new MediaPlayer[MAX_LEN];

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		/* BGM */
		mpChange(QUIETE_MUSIC);
		
		/* intentの取得 */
		Intent intent = getIntent();
		learningIndex = intent.getIntExtra("learningIndex", -1);

		/* 画面サイズ取得→機種対応 */
		Display display = getWindowManager().getDefaultDisplay();
		Point p = new Point();
		display.getSize(p);
		
		/* テストの答えランダム作成 */

		for (int i = 0; i < LEARNING_MP3[learningIndex][0].length - 1; i++) {
//			testAnswer[i] = (int) (Math.random() * 2);
			testAnswer[i] = 1;
		}

		/* 相対レイアウトのパラメータ設定 */
		RelativeLayout.LayoutParams param1 = new RelativeLayout.LayoutParams(MP, WC);
		param1.addRule(RelativeLayout.CENTER_HORIZONTAL, 1);
		param1.addRule(RelativeLayout.CENTER_VERTICAL);

		//ボタン用
		RelativeLayout.LayoutParams param2 = new RelativeLayout.LayoutParams(WC, WC);
		param2.addRule(RelativeLayout.CENTER_HORIZONTAL);

		//コメント用
		RelativeLayout.LayoutParams param3 = new RelativeLayout.LayoutParams(MP, WC);
		param3.addRule(RelativeLayout.CENTER_IN_PARENT);

		//BACKボタン用
		RelativeLayout.LayoutParams param5 = new RelativeLayout.LayoutParams(BACK_BUTTON_SIZE[0], BACK_BUTTON_SIZE[1]);// ボタン類
		param5.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

		/* 画面レイアウト */
		// 親レイアウト生成
		LinearLayout liV1 = makeLinearLayout(BACK_COLOR4, LinearLayout.VERTICAL, null, this);
		Drawable drawable = getResources().getDrawable(BACK_GROUND_IMAGE);
		liV1.setBackground(drawable);
		setContentView(liV1);

		//タイトル作成
		RelativeLayout re1 = makeRelativeLayout(BACK_COLOR1, liV1, null, this);
		makeTextView(leanigIndex[learningIndex], WORD_SIZE, WORD_COLOR2, NO_ID, re1, param1, this);
		makeButton(EX_BUTTON_NAME[1], BACK_BUTTON, NO_TAG, re1, param5, this);

		//スクロールバー
		ScrollView sc1 = makeScrollView(BACK_COLOR4, liV1, this);
		LinearLayout liV2 = makeLinearLayout(BACK_COLOR3, LinearLayout.VERTICAL, sc1, this);

		makeTextView("解説", WORD_SIZE, WORD_COLOR2, NO_ID, liV2, param1, this);

		//解説画像
		ImageView learningImage = new ImageView(this);
		ImageView learningImage2 = new ImageView(this);
		switch (learningIndex) {
		case 0:
			learningImage.setImageResource(R.drawable.r);
			learningImage2.setImageResource(R.drawable.l);
			break;
		case 1:
			learningImage.setImageResource(R.drawable.b);
			learningImage2.setImageResource(R.drawable.v);
			break;
		case 2:
			learningImage.setImageResource(R.drawable.ar);
			learningImage2.setImageResource(R.drawable.er);
			break;

		}
		
		//イメージ画像用
		RelativeLayout.LayoutParams param4 = new RelativeLayout.LayoutParams((int)(p.y/1.92), (int)(p.y/1.92));
		param4.addRule(RelativeLayout.CENTER_HORIZONTAL);

		//小ボタン用
		RelativeLayout.LayoutParams param6 = new RelativeLayout.LayoutParams(WC, WC);
		param6.addRule(RelativeLayout.CENTER_HORIZONTAL);

		//解説①
		makeTextView(LEARNING_MP3[learningIndex][0][0] + "の発音", WORD_SIZE2, WORD_COLOR3, NO_ID, liV2, param1, this);

		RelativeLayout re2 = makeRelativeLayout(BACK_COLOR4, liV2, null, this);
		re2.setBackgroundColor(BACK_COLOR4);
		re2.addView(learningImage, param4);

		makeTextView(LEARNING_SENTENCE[learningIndex][0], WORD_SIZE3, WORD_COLOR2, NO_ID, liV2, param1, this);

		makeTextView("例", WORD_SIZE2, WORD_COLOR2, NO_ID, liV2, param1, this); //「R」「L」などの表示
		for (int i = 1; i < LEARNING_MP3[learningIndex][0].length; i++) {
			//ボタンIDは一意に(0~MAX_LEN)
			int id = (i - 1) + (0 * (LEARNING_MP3[learningIndex][0].length - 1));
			Button button = makeButton(LEARNING_MP3[learningIndex][0][i], id, NO_TAG,
					makeRelativeLayout(BACK_COLOR4, liV2, null, this), param6, this);
			//			button.setWidth(BIG_BUTTON_SIZE[0]);
			//			button.setHeight(BIG_BUTTON_SIZE[1]);
			//			button.setTextSize(WORD_SIZE2);
			Log.d("d", "a:" + String.valueOf(LEARNING_MP3[learningIndex][0].length));
			Log.d("d", String.valueOf(id));
			//音声の生成
			Uri url = Uri.parse("android.resource://com.example.ecctest/raw/" + LEARNING_MP3[learningIndex][0][i]);
			mp[id] = MediaPlayer.create(this, url);
		}

		makeTextView(" ", WORD_SIZE3, WORD_COLOR2, NO_ID, liV2, param1, this); //スペース

		//解説②
		makeTextView(LEARNING_MP3[learningIndex][1][0] + "の発音", WORD_SIZE2, WORD_COLOR3, NO_ID, liV2, param1, this);

		RelativeLayout re3 = makeRelativeLayout(BACK_COLOR4, liV2, null, this);
		re3.setBackgroundColor(BACK_COLOR4);
		re3.addView(learningImage2, param4);

		makeTextView(LEARNING_SENTENCE[learningIndex][1], WORD_SIZE3, WORD_COLOR2, NO_ID, liV2, param1, this);

		makeTextView("例", WORD_SIZE2, WORD_COLOR2, NO_ID, liV2, param1, this); //「R」「L」などの表示
		for (int i = 1; i < LEARNING_MP3[learningIndex][1].length; i++) {
			//ボタンIDは一意に(0~MAX_LEN)
			int id = (i - 1) + (1 * (LEARNING_MP3[learningIndex][1].length - 1));
			Button button = makeButton(LEARNING_MP3[learningIndex][1][i], id, NO_TAG,
					makeRelativeLayout(BACK_COLOR4, liV2, null, this), param6, this);
			//			button.setWidth(BIG_BUTTON_SIZE[0]);
			//			button.setHeight(BIG_BUTTON_SIZE[1]);
			//			button.setTextSize(WORD_SIZE2);
			Log.d("d", "a:" + String.valueOf(LEARNING_MP3[learningIndex][1].length));
			Log.d("d", String.valueOf(id));
			//音声の生成
			Uri url = Uri.parse("android.resource://com.example.ecctest/raw/" + LEARNING_MP3[learningIndex][1][i]);
			mp[id] = MediaPlayer.create(this, url);
		}

		makeTextView(" ", WORD_SIZE3, WORD_COLOR2, NO_ID, liV2, param1, this); //スペース

		Button button = makeButton("確認テスト", LEARNING_TEST_BUTTON, NO_TAG,
				makeRelativeLayout(BACK_COLOR4, liV2, null, this), param2, this);
		button.setWidth(BIG_BUTTON_SIZE[0]);
		button.setHeight(BIG_BUTTON_SIZE[1]);
		button.setTextSize(WORD_SIZE2);

	}

	// ボタンクリック時の処理
	public void onClick(View view) {
		int id = view.getId();
		Intent intent = new Intent();

		/* 遷移先の指定 */
		switch (id) {

		case BACK_BUTTON:
			intent.setClassName("com.example.ecctest", "com.example.ecctest.LearningIndexActivity");
			startActivity(intent);
			LearningActivity.this.finish();
			break;

		case LEARNING_TEST_BUTTON:
			intent.setClassName("com.example.ecctest", "com.example.ecctest.LearningTestActivity");
			intent.putExtra("learningIndex", learningIndex);
			intent.putExtra("testAnswer", testAnswer);
			startActivity(intent);
			LearningActivity.this.finish();

			break;

		case 0:
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
		case 7:
		case 8:
		case 9:

			if (mp[id].isPlaying()) { //再生中
				mp[id].stop();
				try {
					mp[id].prepare();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else { //停止中
				mp[id].start();
			}
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.setClassName("com.example.ecctest", "com.example.ecctest.LearningIndexActivity");
			startActivity(intent);
			LearningActivity.this.finish();
		}
		return false;
	}

}
