package com.example.ecctest;

import static com.example.ecctest.MethodSet.*;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class AchievementActivity extends BaseActivity implements OnClickListener {

	/* 保守パラメータ */
	private final int PAGE_ID = 2;

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

		//タイトル作成
		RelativeLayout re1 = makeRelativeLayout(BACK_COLOR1, liV1, null, this);
		makeTextView(MainActivity.SUB_TITLE[PAGE_ID], WORD_SIZE, WORD_COLOR2, NO_ID, re1, param1, this);
		makeButton(EX_BUTTON_NAME[1], BACK_BUTTON, NO_TAG, re1, param5, this);

		//マージンを設定
		LinearLayout liV2 = makeLinearLayout(BACK_COLOR1, LinearLayout.VERTICAL, liV1, this);
		LayoutParams lp = liV2.getLayoutParams();
		MarginLayoutParams mlp = (MarginLayoutParams) lp;
		mlp.setMargins(20, 20, 20, 20);
		liV2.setLayoutParams(mlp);

		makeTextView(" ", WORD_SIZE2, WORD_COLOR2, NO_ID, liV2, null, this);

		makeTextView("事前テスト得点 : " + initialscore, WORD_SIZE2, WORD_COLOR2, NO_ID, liV2, null, this).setGravity(
				Gravity.CENTER);
		for (int i = 0; i < leanigIndex.length; i++) {
			String successFlag = "まだ";
			switch (i) {
			case 0:
				if (randl.equals("1")) {
					successFlag = "合格";
				}
				break;
			case 1:
				if (bandv.equals("1")) {
					successFlag = "合格";
				}
				break;
			case 2:
				if (arander.equals("1")) {
					successFlag = "合格";
				}
				break;
			}

			makeTextView(leanigIndex[i] + " : " + successFlag, WORD_SIZE2, WORD_COLOR2, NO_ID, liV2, null, this)
					.setGravity(Gravity.CENTER);
		}
		makeTextView("最高得点 : " + highscore, WORD_SIZE2, WORD_COLOR2, NO_ID, liV2, null, this)
				.setGravity(Gravity.CENTER);

		makeTextView(" ", WORD_SIZE2, WORD_COLOR2, NO_ID, liV2, null, this);

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
			AchievementActivity.this.finish();
			break;

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
			AchievementActivity.this.finish();
		}
		return false;
	}
}
