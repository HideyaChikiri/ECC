package com.example.ecctest;

import static com.example.ecctest.MethodSet.*;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LearningTestResultActivity extends BaseActivity implements OnClickListener {

	/* 保守パラメータ */
	private final int PAGE_ID = 0; //testActivityと同じ

	/* テスト情報取得 */
	private final String testWord[][] = MainActivity.testWord;
	private int testLength;
	private int testResult;
	private int learningIndex;
	private int testScore;

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		/* BGM */
		mpChange(SUB_MUSIC);

		/* testActivityからのデータ受け渡し */
		Intent intent = getIntent();
		testResult = intent.getIntExtra("testResult", 99);
		learningIndex = intent.getIntExtra("learningIndex", -1);
		testLength = MainActivity.LEARNIG_MP3[learningIndex][0].length - 1;

		/* 計算処理 */
		testScore = (int) (((double) testResult / testLength) * 100);

		/* データベースオブジェクトの取得 */
		DBHelper dbHelper = new DBHelper(this);
		db = dbHelper.getWritableDatabase();

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
		RelativeLayout.LayoutParams param4 = new RelativeLayout.LayoutParams(250, 250);
		param4.addRule(RelativeLayout.CENTER_HORIZONTAL);

		//BACKボタン用
		RelativeLayout.LayoutParams param5 = new RelativeLayout.LayoutParams(150, 110);// ボタン類
		param5.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

		//NEXTボタン用
		RelativeLayout.LayoutParams param6 = new RelativeLayout.LayoutParams(150, 150);
		param6.addRule(RelativeLayout.CENTER_HORIZONTAL);

		//NEXTボタン用
		RelativeLayout.LayoutParams param7 = new RelativeLayout.LayoutParams(WC, WC);
		param7.addRule(RelativeLayout.CENTER_HORIZONTAL);

		/* 画面レイアウト */
		// 親レイアウト生成
		LinearLayout liV1 = makeLinearLayout(BACK_COLOR4, LinearLayout.VERTICAL, null, this);
		Drawable drawable = getResources().getDrawable(BACK_GROUND_IMAGE);
		liV1.setBackground(drawable);
		setContentView(liV1);

		//タイトル作成
		RelativeLayout re1 = makeRelativeLayout(BACK_COLOR1, liV1, null, this);
		if (!Build.MODEL.equals("SH-02E")) {
			re1.setTranslationZ(100); //前に表示
		}

		makeTextView(MainActivity.SUB_TITLE[PAGE_ID], WORD_SIZE, WORD_COLOR2, NO_ID, re1, param1, this);

		//マージンを設定
		LinearLayout liV2 = makeLinearLayout(BACK_COLOR3, LinearLayout.VERTICAL, liV1, this);
		LayoutParams lp = liV2.getLayoutParams();

		if (!Build.MODEL.equals("SH-02E")) {
			liV2.setTranslationZ(-10); //前に表示
		}

		MarginLayoutParams mlp = (MarginLayoutParams) lp;
		mlp.setMargins(20, 20, 20, 20);
		liV2.setLayoutParams(mlp);

		makeTextView(TEST_RESULT[0], WORD_SIZE, WORD_COLOR2, NO_ID, liV2, null, this).setGravity(Gravity.CENTER);
		makeTextView(" ", WORD_SIZE2, WORD_COLOR2, NO_ID, liV2, null, this);

		//マージンを設定
		LinearLayout liV3 = makeLinearLayout(BACK_COLOR4, LinearLayout.VERTICAL, liV2, this);
		liV3.setId(10);
		MarginLayoutParams mlp2 = (MarginLayoutParams) liV3.getLayoutParams();
		mlp2.setMargins(200, 0, 200, 0);
		liV3.setLayoutParams(mlp2);

		TextView t;

		//合否
		if (testScore >= 60) {
			t = makeTextView("合格", WORD_SIZE, Color.rgb(255, 100, 100), NO_ID, liV3, null, this);
		} else {
			t = makeTextView("不合格", WORD_SIZE, Color.rgb(255, 100, 100), NO_ID, liV3, null, this);
		}

		t.setPadding(100, 0, 100, 0);
		t.setGravity(Gravity.CENTER);
//		t.setBackgroundResource(R.drawable.layout_shape3);

		makeTextView(" ", WORD_SIZE2, WORD_COLOR2, NO_ID, liV2, null, this);
		makeTextView(TEST_RESULT[1] + " : " + testResult + "／" + testLength, WORD_SIZE2, WORD_COLOR2, NO_ID, liV2,
				null, this).setGravity(Gravity.CENTER);
		makeTextView(TEST_RESULT[2] + " : " + (testLength - testResult) + "／" + testLength, WORD_SIZE2, WORD_COLOR2,
				NO_ID, liV2, null, this).setGravity(Gravity.CENTER);

		makeTextView(" \n", WORD_SIZE2, WORD_COLOR2, NO_ID, liV2, null, this);

		makeTextView(TEST_RESULT[3] + " : " + testScore, WORD_SIZE, WORD_COLOR2, NO_ID, liV2, null, this).setGravity(
				Gravity.CENTER);

		makeTextView(" ", WORD_SIZE2, WORD_COLOR2, NO_ID, liV2, null, this);
		RelativeLayout r = makeRelativeLayout(BACK_COLOR4, liV2, null, this);
		Button button = makeButton(MainActivity.EX_BUTTON_NAME[3], CLOSE_BUTTON, NO_TAG, r, param2, this);
		button.setWidth(BIG_BUTTON_SIZE[0]);
		button.setHeight(BIG_BUTTON_SIZE[1]);
		button.setTextSize(WORD_SIZE2);

	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {

		if (hasFocus) {

			/* もろコピー、 */
			LinearLayout button = (LinearLayout) findViewById(10);
			//			RotateAnimation a2 = new RotateAnimation(-360.0f, 360.0f, button.getWidth() / 2, button.getHeight() / 2);
			//			a2.setDuration(3000);
			//			a2.setRepeatCount(Animation.INFINITE);
			//			a2.setRepeatMode(a2.REVERSE);
			//
			//			a2.setStartOffset(10);

			AnimatorSet a2 = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.scale_animation);
			a2.setTarget(button);
			a2.start();

//			button.startAnimation(a2);

		}
	}

	// ボタンクリック時の処理
	public void onClick(View view) {
		int id = view.getId();
		Intent intent = new Intent();

		/* 遷移先の指定 */
		switch (id) {

		case CLOSE_BUTTON:

			try {
				if (testScore >= 60) {

					String flag = "1";
					if (learningIndex == 0) {
						updateDB("randl", flag, DB_TABLE, db);
					} else if (learningIndex == 1) {
						updateDB("bandv", flag, DB_TABLE, db);
					} else if (learningIndex == 2) {
						updateDB("arander", flag, DB_TABLE, db);
					}
				}
			} catch (Exception e) {
				Log.d("d", "updateDBError");
			}

			intent.setClassName("com.example.ecctest", "com.example.ecctest.MainActivity");
			startActivity(intent);
			LearningTestResultActivity.this.finish();

		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			new AlertDialog.Builder(this).setTitle("確認")
					.setMessage(MainActivity.SUB_TITLE[PAGE_ID] + "データを保存せずに終了してもよろしいですか？")
					.setPositiveButton("はい", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent();
							intent.setClassName("com.example.ecctest", "com.example.ecctest.MainActivity");
							startActivity(intent);
							LearningTestResultActivity.this.finish();

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
