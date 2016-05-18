package com.example.ecctest;

import static com.example.ecctest.MethodSet.*;

import java.io.IOException;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LearningTestActivity extends BaseActivity implements OnClickListener {

	/* 保守パラメータ */
	private final int PAGE_ID = 0;


	/* テスト情報取得 */
	private int testLength;
	private int testAnswer[];
	private final String LEARNING_MP3[][][] = MainActivity.LEARNIG_MP3;

	/* テスト情報格納 */
	private int testResult = 0; //何問正解したか
	private boolean isPushed = false;

	/* 大域変数 */
	MediaPlayer mp[] = new MediaPlayer[2];
	static int testIndex = 1; //現在のテスト番号(学習テストでは1からスタート)

	/* 学習INDEX */
	private int learningIndex;

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		mpChange(QUIETE_MUSIC);

		Log.d("d", "LearningTestActivity");
		/* 変数の初期化 */
		testIndex = 0;

		/* intentの取得 */
		Intent intent = getIntent();
		learningIndex = intent.getIntExtra("learningIndex", -1);
		testAnswer = intent.getIntArrayExtra("testAnswer");
		testLength = LEARNING_MP3[learningIndex][0].length - 1;

		/* 相対レイアウトのパラメータ設定 */
		//タイトル用
		RelativeLayout.LayoutParams param1 = new RelativeLayout.LayoutParams(MP, WC);
		param1.addRule(RelativeLayout.CENTER_HORIZONTAL, 1);
		param1.addRule(RelativeLayout.CENTER_VERTICAL);

		//ボタン用
		RelativeLayout.LayoutParams param2 = new RelativeLayout.LayoutParams(WC, WC);
		param2.addRule(RelativeLayout.CENTER_HORIZONTAL);

		//コメント用
		RelativeLayout.LayoutParams param3 = new RelativeLayout.LayoutParams(MP, WC);
		param3.addRule(RelativeLayout.CENTER_IN_PARENT);

		//スピーカーボタン用
		RelativeLayout.LayoutParams param4 = new RelativeLayout.LayoutParams(SPEAKER_BUTTON_SIZE[0],
				SPEAKER_BUTTON_SIZE[1]);
		param4.addRule(RelativeLayout.CENTER_HORIZONTAL);

		//BACKボタン用
		RelativeLayout.LayoutParams param5 = new RelativeLayout.LayoutParams(BACK_BUTTON_SIZE[0], BACK_BUTTON_SIZE[1]);// ボタン類
		param5.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

		//NEXTボタン用
		RelativeLayout.LayoutParams param6 = new RelativeLayout.LayoutParams(NEXT_BUTTON_SIZE[0], NEXT_BUTTON_SIZE[1]);
		param6.addRule(RelativeLayout.CENTER_HORIZONTAL);

		//AMSWER_VIEWボタン用
		RelativeLayout.LayoutParams param7 = new RelativeLayout.LayoutParams(WC, WC);
		param7.addRule(RelativeLayout.CENTER_HORIZONTAL);

		/* 画面レイアウト */
		// 親レイアウト生成
		LinearLayout liV1 = makeLinearLayout(BACK_COLOR4, LinearLayout.VERTICAL, null, this);
		Drawable drawable = getResources().getDrawable(BACK_GROUND_IMAGE);
		liV1.setBackground(drawable);
		setContentView(liV1);

		Log.d("d", "LearningTestActivity3");

		//タイトル作成
		RelativeLayout re1 = makeRelativeLayout(BACK_COLOR1, liV1, null, this);
		makeTextView(MainActivity.leanigIndex[learningIndex] + MainActivity.SUB_TITLE[0], WORD_SIZE, WORD_COLOR2,
				NO_ID, re1, param1, this);
				makeButton(EX_BUTTON_NAME[1], BACK_BUTTON, NO_TAG, re1, param5, this);

				makeTextView(MainActivity.LETTERS_IN_TEST[0] + " " + (testIndex + 1) + "／" + (testLength), WORD_SIZE2,
						WORD_COLOR2, QUESTION_INDEX, liV1, null, this);

				makeTextView(" ", 16.0f, WORD_COLOR2, NO_ID, liV1, null, this); //スペース確保

				//スピーカーボタン生成
				makeImageButton(R.drawable.speaker3, SPEAKER_BUTTON, NO_TAG, makeRelativeLayout(BACK_COLOR4, liV1, null, this),
						param4, this);

				/* ここに◎×を置く */
				makeTextView("", 72.0f, WORD_COLOR2, ANSWER_VIEW, makeRelativeLayout(BACK_COLOR4, liV1, null, this), param7,
						this); //スペース確保

				// 上下ボタン生成
				for (int i = 0; i < 2; i++) {
					Button button = makeButton(LEARNING_MP3[learningIndex][i][testIndex+1], i, "0",
							makeRelativeLayout(BACK_COLOR4, liV1, null, this), param2, this);
					button.setWidth(BIG_BUTTON_SIZE[0]);
					button.setHeight(BIG_BUTTON_SIZE[1]);
					button.setTextSize(WORD_SIZE2);

					//音声の生成
					Uri url = Uri.parse("android.resource://com.example.ecctest/raw/" + LEARNING_MP3[learningIndex][i][testIndex+1]);
					mp[i] = MediaPlayer.create(this, url);
				}

				/* ここに次問題に遷移するボタン配置 */
				//NEXTボタン生成
				ImageButton ib = makeImageButton(R.drawable.next2, NEXT_BUTTON, NO_TAG,
						makeRelativeLayout(BACK_COLOR4, liV1, null, this), param6, this);
				ib.setVisibility(View.INVISIBLE);

				/* スレッド操作用ハンドラ */
				Handler mHandler = new Handler();
				mHandler.postDelayed(new Runnable() {
					public void run() {
						int rand = testAnswer[testIndex];
						if (mp[rand].isPlaying()) { //再生中
							mp[rand].stop();
							try {
								mp[rand].prepare();
							} catch (IllegalStateException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
						} else { //停止中
							mp[rand].start();
						}
					}
				}, 500);

	}

	// ボタンクリック時の処理
	public void onClick(View view) {
				int id = view.getId();


				/* 遷移先の指定 */
				switch (id) {

				case BACK_BUTTON:

					// アクティビティを消去することで戻るループをさせない
					new AlertDialog.Builder(this).setTitle("確認")
							.setMessage(MainActivity.SUB_TITLE[PAGE_ID] + "を途中で終了した場合データは失われますが終了してもよろしいですか？")
							.setPositiveButton("はい", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									Intent intent = new Intent();
									intent.setClassName("com.example.ecctest", "com.example.ecctest.MainActivity");
									startActivity(intent);
									LearningTestActivity.this.finish();
								}
							}).setNegativeButton("いいえ", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {

								}
							}).show();
					break;

				//上ボタン
				case ABOVE_BUTTON:
					((ImageButton) findViewById(NEXT_BUTTON)).setVisibility(View.VISIBLE);
					if (testAnswer[testIndex] == 0) {
						TextView tv = ((TextView) findViewById(ANSWER_VIEW));
						tv.setText("○");
						tv.setTextColor(Color.rgb(100, 100, 255));
						if (!isPushed)
							testResult++;
						isPushed = true;
					} else {
						TextView tv = ((TextView) findViewById(ANSWER_VIEW));
						tv.setText("×");
						tv.setTextColor(Color.rgb(255, 100, 100));

						Vibrator vibrator = (Vibrator) this.getSystemService(this.VIBRATOR_SERVICE);
						vibrator.vibrate(VIB_TIME);

					}
					//再クリック防止
					((Button) findViewById(1)).setEnabled(false);
					break;

				//下ボタン
				case BELOW_BUTTON:
					((ImageButton) findViewById(NEXT_BUTTON)).setVisibility(View.VISIBLE);
					if (testAnswer[testIndex] == 1) {
						TextView tv = ((TextView) findViewById(ANSWER_VIEW));
						tv.setText("○");
						tv.setTextColor(Color.rgb(100, 100, 255));
						if (!isPushed)
							testResult++;
						isPushed = true;
					} else {
						TextView tv = ((TextView) findViewById(ANSWER_VIEW));
						tv.setText("×");
						tv.setTextColor(Color.rgb(255, 100, 100));

						Vibrator vibrator = (Vibrator) this.getSystemService(this.VIBRATOR_SERVICE);
						vibrator.vibrate(VIB_TIME);

					}
					//再クリック防止
					((Button) findViewById(0)).setEnabled(false);
					break;

				//NEXTボタン
				case NEXT_BUTTON:

					//testIndexをtest数より多くしない
					testIndex++;
					isPushed = false;
					if (testIndex >= testLength) {

						Intent intent = new Intent(this, TestResultActivity.class);
						intent.setClassName("com.example.ecctest", "com.example.ecctest.LearningTestResultActivity");
						intent.putExtra("testResult", testResult);
						intent.putExtra("learningIndex", learningIndex);
						startActivity(intent);
						LearningTestActivity.this.finish();

					} else {

						/* スレッド操作用ハンドラ */
						Handler mHandler = new Handler();
						mHandler.postDelayed(new Runnable() {
							public void run() {
								int rand = testAnswer[testIndex];
								if (mp[rand].isPlaying()) { //再生中
									mp[rand].stop();
									try {
										mp[rand].prepare();
									} catch (IllegalStateException e) {
										e.printStackTrace();
									} catch (IOException e) {
										e.printStackTrace();
									}
								} else { //停止中
									mp[rand].start();
								}
							}
						}, 500);

						//答えとNEXTボタンの非表示
						((TextView) findViewById(ANSWER_VIEW)).setText("");
						((ImageButton) findViewById(NEXT_BUTTON)).setVisibility(View.INVISIBLE);

						//QUESTION番号の更新
						((TextView) findViewById(QUESTION_INDEX)).setText(MainActivity.LETTERS_IN_TEST[0] + " "
								+ (testIndex + 1) + "／" + (testLength));

						//ボタンの更新
						for (int i = 0; i < 2; i++) {
							((Button) findViewById(i)).setText(LEARNING_MP3[learningIndex][i][testIndex+1]);
//							音声の生成
							Uri url = Uri.parse("android.resource://com.example.ecctest/raw/" + LEARNING_MP3[learningIndex][i][testIndex+1]);
							mp[i] = MediaPlayer.create(this, url);
						}

						//クリック可能にする
						for (int i = 0; i < 2; i++) {
							((Button) findViewById(i)).setEnabled(true);
						}
					}
					break;

				//スピーカーボタン
				case SPEAKER_BUTTON:
					int rand = testAnswer[testIndex];
					if (mp[rand].isPlaying()) { //再生中
						mp[rand].stop();
						try {
							mp[rand].prepare();
						} catch (IllegalStateException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else { //停止中
						mp[rand].start();
					}
					break;

				default:
					break;
				}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			new AlertDialog.Builder(this).setTitle("確認")
					.setMessage(MainActivity.SUB_TITLE[PAGE_ID] + "を途中で終了した場合データは失われますが終了してもよろしいですか？")
					.setPositiveButton("はい", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent();
							intent.setClassName("com.example.ecctest", "com.example.ecctest.MainActivity");
							startActivity(intent);
							LearningTestActivity.this.finish();
						}
					}).setNegativeButton("いいえ", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					}).show();

			return true;
		}
		return false;
	}

}
