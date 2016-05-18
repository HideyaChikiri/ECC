package com.example.ecctest;

import static com.example.ecctest.MethodSet.*;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class LearningIndexActivity extends BaseActivity implements OnClickListener {

	/* 保守パラメータ */
	private final int PAGE_ID = 1; //testActivityと同じ

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

		//スクロールバー
		ScrollView sc1 = makeScrollView(BACK_COLOR4, liV1, this);
		LinearLayout liV2 = makeLinearLayout(BACK_COLOR4, LinearLayout.VERTICAL, sc1, this);

		for (int i = 0; i < leaningIndexLength; i++) {
			RelativeLayout r = makeRelativeLayout(BACK_COLOR4, liV2, null, this);
			Button button = makeButton(leanigIndex[i], i, NO_TAG, r, param2, this);
			button.setWidth(BIG_BUTTON_SIZE[0]);
			button.setHeight(BIG_BUTTON_SIZE[1]);
			button.setTextSize(WORD_SIZE2);

			//合格マーク
			ImageView successImage = new ImageView(this);
			successImage.setImageResource(R.drawable.success);
			//とりあえずアクオスフォンではこれ使えない
			if(!Build.MODEL.equals("SH-02E")) {
				successImage.setTranslationZ(5); //BUTTONより前に表示
			}
			RelativeLayout.LayoutParams param4 = new RelativeLayout.LayoutParams(SUCCESS_SIZE, SUCCESS_SIZE);
			param4.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			param4.addRule(RelativeLayout.CENTER_VERTICAL);

			switch(i) {
			case 0:
				if(randl.equals("0")) {
					successImage.setVisibility(View.INVISIBLE);
				}else if(randl.equals("1")) {
					successImage.setVisibility(View.VISIBLE);
				}else {
					Log.d("d", "dbError");
				}
				break;
			case 1:
				if(bandv.equals("0")) {
					successImage.setVisibility(View.INVISIBLE);
				}else if(bandv.equals("1")) {
					successImage.setVisibility(View.VISIBLE);
				}else {
					Log.d("d", "dbError");
				}
				break;
			case 2:
				if(arander.equals("0")) {
					successImage.setVisibility(View.INVISIBLE);
				}else if(arander.equals("1")) {
					successImage.setVisibility(View.VISIBLE);
				}else {
					Log.d("d", "dbError");
				}
				break;
			}


			r.addView(successImage, param4);
		}

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
			LearningIndexActivity.this.finish();
			break;

		case 0:
		case 1:
		case 2:
			intent.setClassName("com.example.ecctest", "com.example.ecctest.LearningActivity");
			intent.putExtra("learningIndex", id);
			startActivity(intent);
			LearningIndexActivity.this.finish();
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
			LearningIndexActivity.this.finish();
		}
		return false;
	}
}
