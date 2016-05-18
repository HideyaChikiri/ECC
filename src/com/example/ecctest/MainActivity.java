package com.example.ecctest;

import static com.example.ecctest.MethodSet.*;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends BaseActivity implements OnClickListener {

	/* モンスターせりふ */
	static final String DEFAULT_NAME = "どせい";
	static final int limitName = 5;
	static final String MONSTER_MAIN_SPEECH[] = { "◇はじめ、事前テスト\n　するよ～ん。", "◇学習をするんだよ～ん。", "◇いよいよ、テストだ。\n　ぽえ～ん。" };
	static final String ANIMATION_SPEECH[] = { "◇名前をつけてあげよう。\n※名前は" + limitName + "文字までだよ。", };

	private TextView animTextView;
	String put_txt = "◇ぼくらはいつもげんきですけど、こんにちは\n\n" + "◇すきなたべものはコーヒー、すきなのみものコーヒーよーん\n\n"
			+ "◇いまはだいすきになったおんなのことおしゃべりするために、いっしょうけんめいえいごのべんきょうするのです。\nぽえ～ん。";
	int i = 0;
	String put_word = "";
	String put_text = "";
	private static int TIMEOUT_MESSAGE = 1;
	private static int INTERVAL = 1;

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		/* BGM */
		mpChange(MAIN_MUSIC);

		/* 画面サイズ取得→機種対応 */
		Display display = getWindowManager().getDefaultDisplay();
		Point p = new Point();
		display.getSize(p);

		/* AQUOSを基準 */
		BIG_BUTTON_SIZE[0] = (int) (p.x / 1.3); //550
		BIG_BUTTON_SIZE[1] = (int) (p.y / 7.9); //150

		SPEAKER_BUTTON_SIZE[0] = (int) (p.x / 2.88); //250
		SPEAKER_BUTTON_SIZE[1] = SPEAKER_BUTTON_SIZE[0]; //250

		BACK_BUTTON_SIZE[0] = (int) (p.x / 4.8); //150
		BACK_BUTTON_SIZE[1] = (int) (p.y / 10.76); //110

		NEXT_BUTTON_SIZE[0] = (int) (p.x / 4.8); //150
		NEXT_BUTTON_SIZE[1] = NEXT_BUTTON_SIZE[0]; //150

		/* 文字サイズ指定 */
		WORD_SIZE = p.y / 48.0f;
		WORD_SIZE2 = p.y / 60.0f;
		WORD_SIZE3 = p.y / 80.0f;

		MONSTER_VIEW[0] = (int) (p.x / 21.6);
		MONSTER_VIEW[1] = MONSTER_VIEW[0];
		MONSTER_VIEW[2] = MONSTER_VIEW[0];
		MONSTER_VIEW[3] = (int) (p.x / 76.8);

		SUCCESS_SIZE = (int) (p.x / 5.54);
		//		MONSTER_SIZE1 = (int) (p.x / 1.8);
		MONSTER_SIZE1 = (int) (p.x / 2.3);
		SETTING_SIZE = p.x / 9;

		/* 音量の指定と調節 */
		// AudioManagerを取得する
		AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

		//LEARNING＿MP3からテスト配列作成
		for (int i = 0; i < LEARNIG_MP3.length; i++) {
			for (int j = 0; j < (LEARNIG_MP3[0][0].length - 1); j++) {
				int index = i * (LEARNIG_MP3[0][0].length - 1) + j;
				testWord[index][0] = LEARNIG_MP3[i][0][j + 1];
				testWord[index][1] = LEARNIG_MP3[i][1][j + 1];
			}
		}

		//作成した配列をシャッフル
		for (int i = testWord.length - 1; i > 0; i--) {
			int t = (int) (Math.random() * i); //0～i-1の中から適当に選ぶ

			//選ばれた値と交換する
			String tmp[] = testWord[i];
			testWord[i] = testWord[t];
			testWord[t] = tmp;
		}

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

		/* 画面レイアウト */
		// 親レイアウト生成
		LinearLayout liV1 = makeLinearLayout(BACK_COLOR5, LinearLayout.VERTICAL, null, this);

		Drawable drawable = getResources().getDrawable(BACK_GROUND_IMAGE);
		liV1.setBackground(drawable);
		setContentView(liV1);

		/* タイトル生成 */
		RelativeLayout re1 = makeRelativeLayout(BACK_COLOR1, liV1, null, this);
		makeTextView(APPLICATION_NAME, WORD_SIZE, WORD_COLOR2, NO_ID, re1, param1, this);

		//スクロールバー
		ScrollView sc1 = makeScrollView(BACK_COLOR5, liV1, this);
		LinearLayout liV1_2 = makeLinearLayout(BACK_COLOR5, LinearLayout.VERTICAL, sc1, this);

		/* モンスターの描画 */
		RelativeLayout.LayoutParams param4 = new RelativeLayout.LayoutParams((int) (MONSTER_SIZE1 * 0.8), MONSTER_SIZE1);
		param4.addRule(RelativeLayout.CENTER_HORIZONTAL);

		/* アプリ初回起動時のアニメーション設定1 */
		if (animation.equals("0")) {

			liV1.setBackgroundColor(BACK_COLOR5);

			RelativeLayout re2 = makeRelativeLayout(BACK_COLOR4, liV1_2, null, this);
			re2.setBackgroundColor(BACK_COLOR5);

			LayoutParams lp = re2.getLayoutParams();
			MarginLayoutParams mlp = (MarginLayoutParams) lp;
			mlp.setMargins(MONSTER_VIEW[0], MONSTER_VIEW[0], MONSTER_VIEW[0], 0);
			//マージンを設定
			re2.setLayoutParams(mlp);

			ImageView monster = new ImageView(this);
			monster.setPadding(0, (int) (p.y / 25), 0, (int) (p.y / 25));
			monster.setId(4);
			re2.addView(monster, param4);

			/* frameの描画 */
			RelativeLayout.LayoutParams param5 = new RelativeLayout.LayoutParams(p.x - MONSTER_VIEW[0] * 2, 200);
			param5.addRule(RelativeLayout.CENTER_HORIZONTAL);

			//3重相対レイアウト
			LinearLayout liV3_pp = makeLinearLayout(BACK_COLOR5, LinearLayout.VERTICAL, liV1_2, this);
			((MarginLayoutParams) (liV3_pp.getLayoutParams())).setMargins(MONSTER_VIEW[0], 0, MONSTER_VIEW[0],
					MONSTER_VIEW[3]);
			LinearLayout liV3_p = makeLinearLayout(BACK_COLOR5, LinearLayout.VERTICAL, liV3_pp, this);
			((MarginLayoutParams) (liV3_p.getLayoutParams())).setMargins(MONSTER_VIEW[0], 0, MONSTER_VIEW[0],
					MONSTER_VIEW[3]);
			liV3_p.setPadding(0, 0, 0, MONSTER_VIEW[0]);
			liV3_p.setBackgroundResource(R.drawable.layout_shape);

			RelativeLayout re3 = makeRelativeLayout(BACK_COLOR4, liV3_p, null, this);
			re3.setBackgroundColor(BACK_COLOR4);

			//マージンを設定
			((MarginLayoutParams) (re3.getLayoutParams())).setMargins(MONSTER_VIEW[0], 0, MONSTER_VIEW[0], 0);

			RelativeLayout.LayoutParams param6 = new RelativeLayout.LayoutParams(WC, WC);
			param6.addRule(RelativeLayout.CENTER_IN_PARENT);

			TextView t = makeTextView(ANIMATION_SPEECH[0], WORD_SIZE3, WORD_COLOR2, NO_ID, re3, param6, this);

			makeTextView("　", WORD_SIZE3, WORD_COLOR2, NO_ID, liV3_p, null, this);

			/* エディットテキスト（名前入力） */
			RelativeLayout.LayoutParams param_et = new RelativeLayout.LayoutParams(WC, WC);
			param_et.addRule(RelativeLayout.CENTER_IN_PARENT);

			int textsize = (int) (p.x / 28.8);
			EditText et = makeEditText(name, textsize, NAME_RECORD, NO_TAG,
					makeRelativeLayout(BACK_COLOR4, liV3_p, null, this), param_et, this);
			et.setWidth((int) (p.x / 1.44));

			//入力文字数制限
			InputFilter[] _inputFilter = new InputFilter[1];
			_inputFilter[0] = new InputFilter.LengthFilter(limitName); //文字数指定
			et.setFilters(_inputFilter);
			et.setBackgroundResource(R.drawable.layout_shape2); //XMLでフレーム定義

			makeTextView("　", WORD_SIZE3, WORD_COLOR2, NO_ID, liV3_p, null, this);

			RelativeLayout r = makeRelativeLayout(BACK_COLOR4, liV3_p, null, this);
			Button button = makeButton(EX_BUTTON_NAME[5], RECORD_BUTTON, NO_TAG, r, param2, this);
			button.setWidth(BIG_BUTTON_SIZE[0] * 2 / 3);
			button.setHeight(BIG_BUTTON_SIZE[1] * 2 / 3);
			button.setTextSize(WORD_SIZE2);

			/* アプリ初回起動時のアニメーション設定1 */
		} else if (animation.equals("1")) {

			//BACKボタン用
			RelativeLayout.LayoutParams param_skip = new RelativeLayout.LayoutParams(BACK_BUTTON_SIZE[0] * 2,
					BACK_BUTTON_SIZE[1]);// ボタン類
			param_skip.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

			makeButton(EX_BUTTON_NAME[6], SKIP_BUTTON, NO_TAG, re1, param_skip, this);

			liV1.setBackgroundColor(BACK_COLOR5);

			RelativeLayout re2 = makeRelativeLayout(BACK_COLOR4, liV1, null, this);
			re2.setBackgroundColor(BACK_COLOR5);

			LayoutParams lp = re2.getLayoutParams();
			MarginLayoutParams mlp = (MarginLayoutParams) lp;
			mlp.setMargins(MONSTER_VIEW[0], MONSTER_VIEW[0], MONSTER_VIEW[0], 0);
			//マージンを設定
			re2.setLayoutParams(mlp);

			ImageView monster = new ImageView(this);
			monster.setPadding(0, (int) (p.y / 25), 0, (int) (p.y / 25));
			monster.setId(4);
			re2.addView(monster, param4);

			/* frameの描画 */
			RelativeLayout.LayoutParams param5 = new RelativeLayout.LayoutParams(p.x - MONSTER_VIEW[0] * 2, 200);
			param5.addRule(RelativeLayout.CENTER_HORIZONTAL);

			//3重相対レイアウト
			LinearLayout liV3_pp = makeLinearLayout(BACK_COLOR5, LinearLayout.VERTICAL, liV1, this);
			((MarginLayoutParams) (liV3_pp.getLayoutParams())).setMargins(MONSTER_VIEW[0], 0, MONSTER_VIEW[0],
					MONSTER_VIEW[3]);
			LinearLayout liV3_p = makeLinearLayout(BACK_COLOR5, LinearLayout.VERTICAL, liV3_pp, this);
			((MarginLayoutParams) (liV3_p.getLayoutParams())).setMargins(MONSTER_VIEW[0], 0, MONSTER_VIEW[0],
					MONSTER_VIEW[3]);
			liV3_p.setPadding(0, 0, 0, MONSTER_VIEW[0]);
			liV3_p.setBackgroundResource(R.drawable.layout_shape);

			RelativeLayout re3 = makeRelativeLayout(BACK_COLOR4, liV3_p, null, this);
			re3.setBackgroundColor(BACK_COLOR4);

			//マージンを設定
			((MarginLayoutParams) (re3.getLayoutParams())).setMargins(MONSTER_VIEW[0], 0, MONSTER_VIEW[0], 0);

			RelativeLayout.LayoutParams param6 = new RelativeLayout.LayoutParams(WC, WC);
			param6.addRule(RelativeLayout.CENTER_IN_PARENT);

			makeTextView(name, WORD_SIZE2, WORD_COLOR3, NO_ID, liV3_p, param6, this);

			/* ドミノテキスト */
			animTextView = makeTextView("", WORD_SIZE3, WORD_COLOR2, NO_ID, liV3_p, param6, this);
			// ハンドラ実行
			handler.sendEmptyMessage(TIMEOUT_MESSAGE);

			makeTextView("　\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n", WORD_SIZE3,
					WORD_COLOR2, NO_ID, liV3_p, null, this);

			/* 初回以降の処理 */
		} else {

			RelativeLayout re2 = makeRelativeLayout(MONSTER_BACKCOLOR, liV1, null, this);

			LayoutParams lp = re2.getLayoutParams();
			MarginLayoutParams mlp = (MarginLayoutParams) lp;
			mlp.setMargins(MONSTER_VIEW[0], MONSTER_VIEW[0], MONSTER_VIEW[0], 0);
			//マージンを設定
			re2.setLayoutParams(mlp);

			ImageView monster = new ImageView(this);
			monster.setPadding(0, (int) (p.y / 25), 0, (int) (p.y / 25));
			monster.setId(4);
			re2.addView(monster, param4);

			/* 設定ボタン */
			RelativeLayout.LayoutParams param4_2 = new RelativeLayout.LayoutParams(SETTING_SIZE, SETTING_SIZE);
			param4_2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			param4_2.addRule(RelativeLayout.ALIGN_PARENT_TOP);

			Button setting = makeButton("", SETTING_BUTTON, NO_TAG, re2, param4_2, this);
			setting.setBackgroundColor(BACK_COLOR4);
			setting.setBackgroundResource(R.drawable.setting);

			RelativeLayout.LayoutParams param4_3 = new RelativeLayout.LayoutParams(SETTING_SIZE, SETTING_SIZE);
			param4_3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			param4_3.addRule(RelativeLayout.BELOW, SETTING_BUTTON);

			Button info = makeButton("", INFO_BUTTON, NO_TAG, re2, param4_3, this);
			info.setBackgroundColor(BACK_COLOR4);
			info.setBackgroundResource(R.drawable.info);

			/* frameの描画 */
			RelativeLayout.LayoutParams param5 = new RelativeLayout.LayoutParams(p.x - MONSTER_VIEW[0] * 2, 200);
			param5.addRule(RelativeLayout.CENTER_HORIZONTAL);

			//2重相対レイアウト
			RelativeLayout re3_p = makeRelativeLayout(MONSTER_BACKCOLOR, liV1, null, this);
			((MarginLayoutParams) (re3_p.getLayoutParams())).setMargins(MONSTER_VIEW[0], 0, MONSTER_VIEW[0],
					MONSTER_VIEW[3]);
			re3_p.setPadding(0, 0, 0, MONSTER_VIEW[0]);

			RelativeLayout re3 = makeRelativeLayout(BACK_COLOR1, re3_p, null, this);
			//			re3.setBackgroundColor(BACK_COLOR4);
			re3.setBackgroundResource(R.drawable.layout_shape); //XMLでフレーム定義

			//マージンを設定
			((MarginLayoutParams) (re3.getLayoutParams())).setMargins(MONSTER_VIEW[0], 0, MONSTER_VIEW[0], 0);

			RelativeLayout.LayoutParams param6 = new RelativeLayout.LayoutParams(WC, WC);
			param6.addRule(RelativeLayout.CENTER_IN_PARENT);

			String speech = "error";
			if (testtimes.equals("0")) {
				speech = MONSTER_MAIN_SPEECH[0];
			} else if (randl.equals("0") || bandv.equals("0") || arander.equals("0")) {
				speech = MONSTER_MAIN_SPEECH[1];
			} else {
				speech = MONSTER_MAIN_SPEECH[2];
			}
			TextView t = makeTextView(speech, WORD_SIZE3, WORD_COLOR2, NO_ID, re3, param6, this);
			t.setTypeface(tf);
			/* ボタン生成 */
			int button_num = SUB_TITLE.length;
			if (testtimes.equals("0")) {
				for (int i = 0; i < button_num; i++) {
					RelativeLayout r = makeRelativeLayout(BACK_COLOR4, liV1, null, this);
					Button button;
					if (i == 0) {
						button = makeButton(EX_BUTTON_NAME[0], i, NO_TAG, r, param2, this);
					} else {
						button = makeButton(SUB_TITLE[i], i, NO_TAG, r, param2, this);
						button.setEnabled(false);
					}
					button.setWidth(BIG_BUTTON_SIZE[0]);
					button.setHeight(BIG_BUTTON_SIZE[1]);
					button.setTextSize(WORD_SIZE2);
				}
			} else if (randl.equals("0") || bandv.equals("0") || arander.equals("0")) {
				for (int i = 0; i < button_num; i++) {
					RelativeLayout r = makeRelativeLayout(BACK_COLOR4, liV1, null, this);
					Button button;
					button = makeButton(SUB_TITLE[i], i, NO_TAG, r, param2, this);
					if (i == 0) {
						button.setEnabled(false);
					}
					button.setWidth(BIG_BUTTON_SIZE[0]);
					button.setHeight(BIG_BUTTON_SIZE[1]);
					button.setTextSize(WORD_SIZE2);
				}

			} else {
				for (int i = 0; i < button_num; i++) {
					RelativeLayout r = makeRelativeLayout(BACK_COLOR4, liV1, null, this);
					Button button = makeButton(SUB_TITLE[i], i, NO_TAG, r, param2, this);
					button.setWidth(BIG_BUTTON_SIZE[0]);
					button.setHeight(BIG_BUTTON_SIZE[1]);
					button.setTextSize(WORD_SIZE2);
				}
			}

		}
	}

	// 文字列を一文字ずつ出力するハンドラ
	private Handler handler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {

			// 文字列を配列に１文字ずつセット
			char data[] = put_txt.toCharArray();

			// 配列数を取得
			int arr_num = data.length;

			if (i < arr_num) {
				if (msg.what == TIMEOUT_MESSAGE) {
					put_word = String.valueOf(data[i]);
					put_text = put_text + put_word;

					animTextView.setText(put_text);
					handler.sendEmptyMessageDelayed(TIMEOUT_MESSAGE, INTERVAL * TEXT_SPEED);
					i++;
				} else {
					super.dispatchMessage(msg);
				}
			}
		}
	};

	/* oncreateやonresumeだとアニメーションが正しく動かない
	 * 画面遷移に合わせたアニメーションはこのメソッドで処理
	 */

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {

		if (hasFocus) {

			ImageView monster = (ImageView) findViewById(4);
			monster.setBackgroundResource(R.animator.parapara_animation);
			AnimationDrawable frameAnimation = (AnimationDrawable) monster.getBackground();
			frameAnimation.start();

			if (animation.equals("0")) {

				//特に特殊処理なし
			} else if (animation.equals("1")) {

			} else {
				if (testtimes.equals("0")) {
					Button button = (Button) findViewById(0);
					RotateAnimation a2 = new RotateAnimation(-2.0f, 2.0f, button.getWidth() / 2, button.getHeight() / 2);
					a2.setDuration(10);
					a2.setRepeatCount(15);
					a2.setRepeatMode(a2.REVERSE);
					a2.setStartOffset(10);

					button.startAnimation(a2);
				} else if (randl.equals("0") || bandv.equals("0") || arander.equals("0")) {
					Button button = (Button) findViewById(1);
					RotateAnimation a2 = new RotateAnimation(-2.0f, 2.0f, button.getWidth() / 2, button.getHeight() / 2);
					a2.setDuration(10);
					a2.setRepeatCount(15);
					a2.setRepeatMode(a2.REVERSE);
					a2.setStartOffset(10);

					button.startAnimation(a2);
				} else {
					Button button = (Button) findViewById(0);
					RotateAnimation a2 = new RotateAnimation(-2.0f, 2.0f, button.getWidth() / 2, button.getHeight() / 2);
					a2.setDuration(10);
					a2.setRepeatCount(15);
					a2.setRepeatMode(a2.REVERSE);
					a2.setStartOffset(10);

					button.startAnimation(a2);
				}
			}
		}
	}

	@Override
	public void onClick(View view) {// クリック時に呼ばれる
		int id = view.getId();
		Intent intent = new Intent(this, MainActivity.class);

		/* 遷移先の指定 */
		switch (id) {

		case RECORD_BUTTON:
			try {
				int anm = Integer.valueOf(animation);
				EditText et = (EditText) findViewById(NAME_RECORD); //30ok
				CharSequence test_c = et.getText();
				String test = test_c.toString();

				//				String test = ((EditText) findViewById(NAME_RECORD)).getText().toString();
				//				String test = "debug";
				Log.d("d", test);
				updateDB("name", test, DB_TABLE2, db);
				updateDB("animation", String.valueOf(++anm), DB_TABLE, db);
			} catch (Exception e) {
				Log.d("d", e.toString());
			}
			intent.setClassName("com.example.ecctest", "com.example.ecctest.MainActivity");
			startActivity(intent);
			MainActivity.this.finish();

			break;

		case SKIP_BUTTON:

			if (animation.equals("1")) {
				try {
					int anm = Integer.valueOf(animation);
					updateDB("animation", String.valueOf(++anm), DB_TABLE, db);

				} catch (Exception e) {
					Log.d("d", e.toString());
				}
				intent.setClassName("com.example.ecctest", "com.example.ecctest.MainActivity");
				startActivity(intent);
				MainActivity.this.finish();
			}

			break;

		case SETTING_BUTTON:
			intent.setClassName("com.example.ecctest", "com.example.ecctest.SettingActivity");
			startActivity(intent);
			MainActivity.this.finish();
			break;

		case INFO_BUTTON:
			intent.setClassName("com.example.ecctest", "com.example.ecctest.LomActivity");
			startActivity(intent);
			MainActivity.this.finish();
			break;
			
		case 0:
			// 次に明示的に呼び出すアクティビティを指定
			//			intent.putExtra("testAnswer", testAnswer);

			if (animation.equals("2") && testtimes.equals("0")) {
				intent.setClassName("com.example.ecctest", "com.example.ecctest.BeforeTestActivity");
			} else {
				intent.setClassName("com.example.ecctest", "com.example.ecctest.TestActivity");
			}
			startActivity(intent);
			MainActivity.this.finish();
			break;

		case 1:
			intent.setClassName("com.example.ecctest", "com.example.ecctest.LearningIndexActivity");
			startActivity(intent);
			MainActivity.this.finish();
			break;

		case 2:
			intent.setClassName("com.example.ecctest", "com.example.ecctest.AchievementActivity");
			startActivity(intent);
			MainActivity.this.finish();
			break;

		//		case 3:
		//
		//			ImageView monster = (ImageView) findViewById(4);
		//
		//			AnimationSet set = new AnimationSet(true);
		//
		//			RotateAnimation rotate = new RotateAnimation(-10.0f, 10.0f, monster.getWidth() / 2, monster.getHeight() / 2);
		//			rotate.setDuration(5);
		//			rotate.setRepeatCount(200);
		//			rotate.setRepeatMode(Animation.REVERSE);
		//
		//			TranslateAnimation translate = new TranslateAnimation(0, 0, -20, 50);
		//			translate.setDuration(200);
		//			translate.setRepeatCount(25);
		//			translate.setRepeatMode(Animation.REVERSE);
		//
		//			AlphaAnimation alpha = new AlphaAnimation(1, 0.1f);
		//			alpha.setDuration(5000);
		//			//			alpha.setFillAfter(true);
		//
		//			set.addAnimation(rotate);
		//			set.addAnimation(translate);
		//			//			set.addAnimation(alpha);
		//			set.setFillAfter(true);
		//			monster.startAnimation(set);
		//
		//			break;

		default:
			break;
		}

	}

	// BACKボタンで終了させる
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			new AlertDialog.Builder(this).setTitle("確認").setMessage(APPLICATION_NAME + "を終了してもよろしいですか？")
					.setPositiveButton("はい", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							MainActivity.this.finish();
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

	//		@Override
	//	    public boolean onTouchEvent(MotionEvent event) {
	//			Intent intent = new Intent(this, MainActivity.class);
	//
	//			if(animation.equals("1")) {
	//				try {
	//					int anm = Integer.valueOf(animation);
	//					updateDB("animation", String.valueOf(++anm), DB_TABLE, db);
	//
	//				} catch (Exception e) {
	//					Log.d("d", e.toString());
	//				}
	//				intent.setClassName("com.example.ecctest", "com.example.ecctest.MainActivity");
	//				startActivity(intent);
	//				MainActivity.this.finish();
	//			}
	//
	//			return true;
	//	    }
}
