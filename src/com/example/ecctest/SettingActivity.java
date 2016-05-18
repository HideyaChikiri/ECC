package com.example.ecctest;

import static com.example.ecctest.MethodSet.*;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

/* なぜかグローバル変数渡しでは値を変更しても反映されない
 * よって、キモイけどBaseAcrivityを継承してみる */
public class SettingActivity extends BaseActivity implements OnClickListener {


	/*  設定パラメータ */
	/* p_name変更しらp_valueも追加するの忘れるな！！ */
	String p_name[] = { "テスト回数", "最高得点", "事前テスト", leanigIndex[0], leanigIndex[1], leanigIndex[2], "アニメ", "名前" ,"BGM"};
	String d_name[] = { "testtimes", "highscore", "initialscore", "randl", "bandv", "arander", "animation", "name" ,"bgm"};
	private int p_num = p_name.length;
	Object p_value[] = new Object[p_num];

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		/* BGM */
		mpChange(SUB_MUSIC);

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

		//BACKボタン用
		RelativeLayout.LayoutParams param5 = new RelativeLayout.LayoutParams(BACK_BUTTON_SIZE[0], BACK_BUTTON_SIZE[1]);// ボタン類
		param5.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

		/* 画面レイアウト */
		// 親レイアウト生成

		LinearLayout liV1 = makeLinearLayout(BACK_COLOR4, LinearLayout.VERTICAL, null, this);
		Drawable drawable = getResources().getDrawable(BACK_GROUND_IMAGE);
		liV1.setBackground(drawable);
		setContentView(liV1);

		/* データベースオブジェクトの取得 */
		DBHelper dbHelper = new DBHelper(this);
		db = dbHelper.getWritableDatabase();
		try {

			for (int i = 0; i < p_num; i++) {
				if (i == 7) {
					p_value[i] = readDB(d_name[i], MainActivity.DB_TABLE2, db)[1];
				} else {
					p_value[i] = readDB(d_name[i], DB_TABLE, db)[1];
				}
			}

		} catch (Exception e) {
			Log.d("d", "dbError");
		}

		//タイトル作成
		RelativeLayout re1 = makeRelativeLayout(BACK_COLOR1, liV1, null, this);
		makeTextView(MainActivity.EX_BUTTON_NAME[2], WORD_SIZE, WORD_COLOR2, NO_ID, re1, param1, this);
		makeButton(EX_BUTTON_NAME[1], BACK_BUTTON, NO_TAG, re1, param5, this);

		//スクロールバー
		ScrollView sc1 = makeScrollView(BACK_COLOR4, liV1, this);
		LinearLayout liV1_2 = makeLinearLayout(BACK_COLOR3,
				LinearLayout.VERTICAL, sc1, this);

		//マージンを設定
		LinearLayout liV2 = makeLinearLayout(BACK_COLOR1, LinearLayout.VERTICAL, liV1_2, this);
		LayoutParams lp = liV2.getLayoutParams();
		MarginLayoutParams mlp = (MarginLayoutParams) lp;
		mlp.setMargins(20, 20, 20, 20);
		liV2.setLayoutParams(mlp);

		/* テキストとエディットテキストのパラム */
		RelativeLayout.LayoutParams param_et = new RelativeLayout.LayoutParams(WC, WC);
		param_et.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

		/* 画面サイズ取得→機種対応 */
		Display display = getWindowManager().getDefaultDisplay();
		Point p = new Point();
		display.getSize(p);
		int textsize = (int) (p.x / 28.8);

		/* 設定パラメーター */

		for (int i = 0; i < p_num; i++) {
			RelativeLayout re = makeRelativeLayout(BACK_COLOR4, liV2, null, this);
			makeTextView(p_name[i], WORD_SIZE2, WORD_COLOR2, NO_ID, re, null, this);
			EditText et = makeEditText(String.valueOf(p_value[i]), textsize, i, NO_TAG, re, param_et, this);
//			et.setWidth(p.x / 5);
			et.setBackgroundResource(R.drawable.layout_shape2); //XMLでフレーム定義
		}

		//小ボタン用
		RelativeLayout.LayoutParams param_mini = new RelativeLayout.LayoutParams(WC, WC);
		param_mini.addRule(RelativeLayout.CENTER_HORIZONTAL);
		makeButton("全てリセット", RESET_BUTTON, NO_TAG, makeRelativeLayout(BACK_COLOR4, liV2, null, this), param_mini, this);

		RelativeLayout r = makeRelativeLayout(BACK_COLOR4, liV2, null, this);
		Button button = makeButton(MainActivity.EX_BUTTON_NAME[4], MODIFY_BUTTON, NO_TAG, r, param2, this);
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
			intent.setClassName("com.example.ecctest", "com.example.ecctest.MainActivity");
			startActivity(intent);
			SettingActivity.this.finish();
			break;

		case CLOSE_BUTTON:
			intent.setClassName("com.example.ecctest", "com.example.ecctest.MainActivity");
			startActivity(intent);
			SettingActivity.this.finish();
			break;

		case RESET_BUTTON:
			for (int i = 0; i < p_num; i++) {
				EditText et = (EditText) findViewById(i);

				//valueが文字列のやつ
				if (i == 7) {
					et.setText(DEFAULT_NAME);
				} else {
					et.setText("0");
				}
			}
			break;

		case MODIFY_BUTTON:

			try {

				for (int i = 0; i < p_num; i++) {
					if (i == 7) {
						updateDB(d_name[i], ((EditText) findViewById(i)).getText().toString(), MainActivity.DB_TABLE2,
								db);
					} else {
						updateDB(d_name[i], ((EditText) findViewById(i)).getText().toString(), DB_TABLE, db);
					}
				}
			} catch (Exception e) {
				Log.d("d", e.toString());
			}

			new AlertDialog.Builder(this).setTitle("確認").setMessage("二度と元のデータに戻すことができなくなります。\n本当に変更してもよろしいですか？")
					.setPositiveButton("はい", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent();
							intent.setClassName("com.example.ecctest", "com.example.ecctest.MainActivity");
							startActivity(intent);
							SettingActivity.this.finish();
						}
					}).setNegativeButton("いいえ", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					}).show();

		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.setClassName("com.example.ecctest", "com.example.ecctest.MainActivity");
			startActivity(intent);
			SettingActivity.this.finish();
		}
		return false;
	}
}
