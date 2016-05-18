package com.example.ecctest;

import static com.example.ecctest.MethodSet.*;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class LomActivity extends BaseActivity implements
		OnClickListener {

	String index[] = { "教材名", "概要", "キーワード", "目標", "対象者", "対象者の条件",
			"学習時間", "使用料", "企画・政策", "評価の対象者・方法" };

	String value[] = {
			"ECC (English Circulation Curriculum)",
			"1.始めに、『事前テスト』にてリスニングテストを受ける\n"
					+ "2.『学習』にて教材を用い、発音とリスニングの学習をする\n"
					+ "3.『テスト』にてもう１度異なる内容のリスニングテストを受ける\n"
					+ "4.『事前テスト』『テスト』の成績から成長度合を集計・評価する",
			"スマートフォン(Android)", "英語の発音、リスニング能力向上を目指す",
			"一般的な英語教育を修了している大学生", "中学・高校等で最低限の英語教育を受けている", "最短で15分",
			"無料", "(グループ名) ECC",
			"『事前テスト』『テスト』の成績から被験者の成長度合を集計して 教材を評価する" };

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		/* BGM */
		mpChange(QUIETE_MUSIC);

		/* 相対レイアウトのパラメータ設定 */
		//タイトル用
		RelativeLayout.LayoutParams param1 = new RelativeLayout.LayoutParams(
				MP, WC);
		param1.addRule(RelativeLayout.CENTER_HORIZONTAL, 1);
		param1.addRule(RelativeLayout.CENTER_VERTICAL);

		//ボタン用
		RelativeLayout.LayoutParams param2 = new RelativeLayout.LayoutParams(
				WC, WC);
		param2.addRule(RelativeLayout.CENTER_HORIZONTAL);

		//コメント用
		RelativeLayout.LayoutParams param3 = new RelativeLayout.LayoutParams(
				MP, WC);
		param3.addRule(RelativeLayout.CENTER_IN_PARENT);

		//BACKボタン用
		RelativeLayout.LayoutParams param5 = new RelativeLayout.LayoutParams(
				BACK_BUTTON_SIZE[0], BACK_BUTTON_SIZE[1]);// ボタン類
		param5.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

		/* 画面レイアウト */
		// 親レイアウト生成
		LinearLayout liV1 = makeLinearLayout(BACK_COLOR4,
				LinearLayout.VERTICAL, null, this);
		Drawable drawable = getResources().getDrawable(
				BACK_GROUND_IMAGE);
		liV1.setBackground(drawable);
		setContentView(liV1);

		//タイトル作成
		RelativeLayout re1 = makeRelativeLayout(BACK_COLOR1, liV1,
				null, this);
		makeTextView(EX_BUTTON_NAME[7], WORD_SIZE, WORD_COLOR2,
				NO_ID, re1, param1, this);
		makeButton(EX_BUTTON_NAME[1], BACK_BUTTON, NO_TAG, re1,
				param5, this);

		//マージンを設定
		LinearLayout liV2 = makeLinearLayout(BACK_COLOR1,
				LinearLayout.VERTICAL, liV1, this);
		LayoutParams lp = liV2.getLayoutParams();
		MarginLayoutParams mlp = (MarginLayoutParams) lp;
		mlp.setMargins(20, 20, 20, 20);
		liV2.setLayoutParams(mlp);

		//スクロールバー
		ScrollView sc1 = makeScrollView(BACK_COLOR4, liV2, this);
		LinearLayout liV2_2 = makeLinearLayout(BACK_COLOR4,
				LinearLayout.VERTICAL, sc1, this);

		makeTextView(" ", WORD_SIZE2, WORD_COLOR2, NO_ID, liV2_2,
				null, this);

		for (int i = 0; i < index.length; i++) {
			makeTextView(index[i], WORD_SIZE, WORD_COLOR4, NO_ID,
					liV2_2, null, this);
			makeTextView(value[i], WORD_SIZE2, WORD_COLOR2, NO_ID,
					liV2_2, null, this);
			makeTextView(" ", WORD_SIZE2, WORD_COLOR2, NO_ID, liV2_2,
					null, this);
		}

		makeTextView(" ", WORD_SIZE2, WORD_COLOR2, NO_ID, liV2, null,
				this);

	}

	// ボタンクリック時の処理
	public void onClick(View view) {
		int id = view.getId();
		Intent intent = new Intent();

		/* 遷移先の指定 */
		switch (id) {

		case BACK_BUTTON:
			intent.setClassName("com.example.ecctest",
					"com.example.ecctest.MainActivity");
			startActivity(intent);
			LomActivity.this.finish();
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.setClassName("com.example.ecctest",
					"com.example.ecctest.MainActivity");
			startActivity(intent);
			LomActivity.this.finish();
		}
		return false;
	}
}
