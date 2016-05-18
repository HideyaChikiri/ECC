package com.example.ecctest;

import static com.example.ecctest.MethodSet.*;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BeforeTestActivity extends BaseActivity implements OnClickListener {

	/* モンスターせりふ */
//	static final String MONSTER_MAIN_SPEECH[] = { "◇はじめ、事前テスト\n　するよーん。", "◇学習をするんだよーん。", "◇いよいよ、テストだ。\n　ぽえ～ん。" };
//	static final String ANIMATION_SPEECH[] = { "◇名前をつけてあげよう。\n※名前は" + limitName + "文字までだよ。", };

	private TextView animTextView;
	String put_txt = "◇これから事前テストをはじめるよ～ん。\n\n◇学習が終わったらまたテストをするよ。"
			+ "\n\n◇事前テストではできなくてもしょうがないから点数が低くてもおちこむなよ～ん。";
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

//		/* AQUOSを基準 */
//		BIG_BUTTON_SIZE[0] = (int) (p.x / 1.3); //550
//		BIG_BUTTON_SIZE[1] = (int) (p.y / 7.9); //150
//
//		SPEAKER_BUTTON_SIZE[0] = (int) (p.x / 2.88); //250
//		SPEAKER_BUTTON_SIZE[1] = SPEAKER_BUTTON_SIZE[0]; //250
//
//		BACK_BUTTON_SIZE[0] = (int) (p.x / 4.8); //150
//		BACK_BUTTON_SIZE[1] = (int) (p.y / 10.76); //110
//
//		NEXT_BUTTON_SIZE[0] = (int) (p.x / 4.8); //150
//		NEXT_BUTTON_SIZE[1] = NEXT_BUTTON_SIZE[0]; //150
//
//		/* 文字サイズ指定 */
//		WORD_SIZE = p.y / 48.0f;
//		WORD_SIZE2 = p.y / 60.0f;
//		WORD_SIZE3 = p.y / 80.0f;
//
//		MONSTER_VIEW[0] = (int) (p.x / 21.6);
//		MONSTER_VIEW[1] = MONSTER_VIEW[0];
//		MONSTER_VIEW[2] = MONSTER_VIEW[0];
//		MONSTER_VIEW[3] = (int) (p.x / 76.8);
//
//		SUCCESS_SIZE = (int) (p.x / 5.54);
//		//		MONSTER_SIZE1 = (int) (p.x / 1.8);
//		MONSTER_SIZE1 = (int) (p.x / 2.3);
//		SETTING_SIZE = p.x / 9;

		/* 音量の指定と調節 */
		// AudioManagerを取得する
		AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

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
		LinearLayout liV1 = makeLinearLayout(BACK_COLOR4, LinearLayout.VERTICAL, null, this);

		Drawable drawable = getResources().getDrawable(BACK_GROUND_IMAGE);
		liV1.setBackground(drawable);
		setContentView(liV1);

		/* タイトル生成 */
		RelativeLayout re1 = makeRelativeLayout(BACK_COLOR1, liV1, null, this);
		makeTextView(APPLICATION_NAME, WORD_SIZE, WORD_COLOR2, NO_ID, re1, param1, this);

		/* モンスターの描画 */
		RelativeLayout.LayoutParams param4 = new RelativeLayout.LayoutParams((int) (MONSTER_SIZE1 * 0.8), MONSTER_SIZE1);
		param4.addRule(RelativeLayout.CENTER_HORIZONTAL);

		if (animation.equals("2") && testtimes.equals("0")) {

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
		}
	}

	@Override
	public void onClick(View view) {// クリック時に呼ばれる
		int id = view.getId();
		Intent intent = new Intent(this, MainActivity.class);

		/* 遷移先の指定 */
		switch (id) {

		case SKIP_BUTTON:

//			if (animation.equals("1")) {
				try {
					int anm = Integer.valueOf(animation);
					updateDB("animation", String.valueOf(++anm), DB_TABLE, db);

				} catch (Exception e) {
					Log.d("d", e.toString());
				}
				intent.setClassName("com.example.ecctest", "com.example.ecctest.TestActivity");
				startActivity(intent);
				BeforeTestActivity.this.finish();
//			}

			break;

		default:
			break;
		}

	}

}
