package com.example.ecctest;

import static com.example.ecctest.MethodSet.*;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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

public class TestResultActivity extends BaseActivity implements OnClickListener {

	/* 保守パラメータ */
	private final int PAGE_ID = 0; //testActivityと同じ

	/* テスト情報取得 */
	private final String testWord[][] = MainActivity.testWord;
	private final int testLength = MainActivity.testLength;
	//	private final int testAnswer[] = MainActivity.testAnswer;
	private int testResult;
	private int testScore;

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		/* BGM */
		mpChange(SUB_MUSIC);

		/* testActivityからのデータ受け渡し */
		Intent intent = getIntent();
		testResult = intent.getIntExtra("testResult", 99);

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
		makeTextView(MainActivity.SUB_TITLE[PAGE_ID], WORD_SIZE, WORD_COLOR2, NO_ID, re1, param1, this);

		//マージンを設定
		LinearLayout liV2 = makeLinearLayout(BACK_COLOR3, LinearLayout.VERTICAL, liV1, this);
		LayoutParams lp = liV2.getLayoutParams();
		MarginLayoutParams mlp = (MarginLayoutParams) lp;
		mlp.setMargins(20, 20, 20, 20);
		liV2.setLayoutParams(mlp);

		makeTextView(TEST_RESULT[0], WORD_SIZE, WORD_COLOR2, NO_ID, liV2, null, this).setGravity(Gravity.CENTER);
		makeTextView(" ", WORD_SIZE2, WORD_COLOR2, NO_ID, liV2, null, this);
		makeTextView(TEST_RESULT[1] + " : " + testResult + "／" + testLength, WORD_SIZE2, WORD_COLOR2, NO_ID, liV2,
				null, this).setGravity(Gravity.CENTER);
		makeTextView(TEST_RESULT[2] + " : " + (testLength - testResult) + "／" + testLength, WORD_SIZE2, WORD_COLOR2,
				NO_ID, liV2, null, this).setGravity(Gravity.CENTER);

		makeTextView(" ", WORD_SIZE2, WORD_COLOR2, NO_ID, liV2, null, this);

		testScore = (int) (((double) testResult / testLength) * 100);
		CURRENT_TESTSCORE = testScore;
		makeTextView(TEST_RESULT[3] + " : " + testScore, WORD_SIZE, WORD_COLOR2, NO_ID, liV2, null, this).setGravity(
				Gravity.CENTER);

		makeTextView(" ", WORD_SIZE2, WORD_COLOR2, NO_ID, liV2, null, this);
		RelativeLayout r = makeRelativeLayout(BACK_COLOR4, liV2, null, this);
		Button button = makeButton(MainActivity.EX_BUTTON_NAME[3], CLOSE_BUTTON, NO_TAG, r, param2, this);
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

		case CLOSE_BUTTON:

			int testtimes_i = Integer.valueOf(testtimes);
			int highscore_i = Integer.valueOf(highscore);

			try {
				if (testtimes_i == 0) {
					updateDB("initialscore", String.valueOf(testScore), DB_TABLE, db);
				} else {
					if (highscore_i < testScore) {
						updateDB("highscore", String.valueOf(testScore), DB_TABLE, db);
					}
				}
				updateDB("testtimes", String.valueOf(++testtimes_i), DB_TABLE, db);
			} catch (Exception e) {
				Log.d("d", "updateDBError");
			}

			if (!randl.equals("0") && !bandv.equals("0") && !arander.equals("0")) {
				intent.setClassName("com.example.ecctest", "com.example.ecctest.AfterTestActivity");
			} else {
				intent.setClassName("com.example.ecctest", "com.example.ecctest.MainActivity");
			}

			startActivity(intent);
			TestResultActivity.this.finish();

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
							TestResultActivity.this.finish();

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
