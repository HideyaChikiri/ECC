package com.example.ecctest;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MethodSet extends BaseActivity{

	private static final int WC = LinearLayout.LayoutParams.WRAP_CONTENT;

	/* Layout関連メソッド */

	//ScrollView生成メソッド
	public static ScrollView makeScrollView(int color, ViewGroup parent, Context context) {
		ScrollView scrollView = new ScrollView(context);
		scrollView.setBackgroundColor(color);
		//親がないとき
		if (parent != null) {
			parent.addView(scrollView);
		}
		return scrollView;
	}

	//RelativeLayout生成メソッド
	//paramあり
	public static RelativeLayout makeRelativeLayout(int color, ViewGroup parent, RelativeLayout.LayoutParams param,
			Context context) {
		RelativeLayout relativeLayout = new RelativeLayout(context);
		relativeLayout.setBackgroundColor(color);
		//親がないとき
		if (parent != null) {
			if (param != null) {
				parent.addView(relativeLayout, param);
			} else {
				parent.addView(relativeLayout);
			}
		}
		return relativeLayout;
	}

	//LinearLayout生成メソッド
	public static LinearLayout makeLinearLayout(int color, int orientation, ViewGroup parent, Context context) {
		LinearLayout linearLayout = new LinearLayout(context);
		linearLayout.setBackgroundColor(color);
		linearLayout.setOrientation(orientation);
		//親がないとき
		if (parent != null) {
			parent.addView(linearLayout);
		}
		return linearLayout;
	}

	//ボタン生成メソッド
	//param有(うまくいかない)
	public static Button makeButton(String text, int id, String tag, ViewGroup parent,
			RelativeLayout.LayoutParams param, Context context) {
		Button button = new Button(context);
		button.setText(text);
		button.setBackgroundResource(R.drawable.abc_popup_background_mtrl_mult);
//		button.setBackgroundResource(R.drawable.button_brock);
		button.setId(id);
		button.setTag(tag);// ここではボタンが押されたかを判断（0：押されていない　1：押された）
		button.setTypeface(tf);
		button.setOnClickListener((View.OnClickListener) context);
		/* これを設定するとテーブルの行に追加されない */
		//		button.setLayoutParams(new LinearLayout.LayoutParams(WC, WC));
		//親がないとき
		if (parent != null) {
			if (param != null) {
				parent.addView(button, param);
			} else {
				parent.addView(button);
			}
		}
		return button;
	}

	/* Bottonでもイメージボタン作れる！！ */
	//イメージボタン生成メソッド
	public static ImageButton makeImageButton(int resource, int id, String tag, ViewGroup parent,
			RelativeLayout.LayoutParams param, Context context) {
		ImageButton imageButton = new ImageButton(context);
		imageButton.setImageResource(resource);
		imageButton.setId(id);
		imageButton.setTag(tag);
		imageButton.setScaleType(ScaleType.CENTER_INSIDE);
		imageButton.setBackgroundColor(Color.argb(100, 0, 0, 0));
		imageButton.setOnClickListener((View.OnClickListener) context);
		//親がないとき
		if (parent != null) {
			if (param != null) {
				parent.addView(imageButton, param);
			} else {
				parent.addView(imageButton);
			}
		}
		return imageButton;
	}

	// テキスト生成メソッド
	//param有
	public static TextView makeTextView(String text, float size, int color, int id, ViewGroup parent,
			RelativeLayout.LayoutParams param, Context context) {
		TextView t = new TextView(context);
		t.setText(text);
		t.setTextSize(size);
		t.setTextColor(color);
		t.setId(id);
		t.setTypeface(tf);
		/* これを設定するとテーブルの行に追加されない */
		//		t.setLayoutParams(new LinearLayout.LayoutParams(WC, WC));
		//親がないとき
		if (parent != null) {
			if (param != null) {
				parent.addView(t, param);
			} else {
				parent.addView(t);
			}
		}
		return t;
	}

	//チェックボックス生成メソッド
	//param有
	public static CheckBox makeCheckBox(int id, String check_flag, ViewGroup parent, RelativeLayout.LayoutParams param,
			Context context) {
		CheckBox checkBox = new CheckBox(context);
		checkBox.setId(id);
		checkBox.setHeight(100);
		checkBox.setScaleX(1.5f);
		checkBox.setScaleY(1.5f);
		if (check_flag.equals("0")) {
			checkBox.setChecked(false);
		} else {
			checkBox.setChecked(true);
		}
		checkBox.setOnClickListener((View.OnClickListener) context);
		//親がないとき
		if (parent != null) {
			if (param != null) {
				parent.addView(checkBox, param);
			} else {
				parent.addView(checkBox);
			}
		}

		return checkBox;
	}

	//エディットテキスト
	public static EditText makeEditText(String text, int size, int id, String tag, ViewGroup parent,
			RelativeLayout.LayoutParams param, Context context) {
		EditText et = new EditText(context);
		et.setText(text);
		et.setTextSize(size);
		et.setId(id);
		et.setTag(tag);

		et.setTextColor(Color.WHITE);
		et.setBackgroundColor(Color.argb(0, 0, 0, 0));
		et.setTypeface(tf);

		if (parent != null) {
			if (param != null) {
				parent.addView(et, param);
			} else {
				parent.addView(et);
			}
		}

		return et;
	}

	/* DB処理 */

	//データベースからの読み込み
	public static String[] readDB(String id, String DB_TABLE, SQLiteDatabase db) throws Exception {

		Cursor c = null;
		if(DB_TABLE.equals("userinfo")) {
			c = db.query(DB_TABLE, new String[] { "id", "num" }, "id='" + id + "'", null, null, null, null);
		}else if(DB_TABLE.equals("nameinfo")) {
			c = db.query(DB_TABLE, new String[] { "id", "name" }, "id='" + id + "'", null, null, null, null);
		}

		if (c.getCount() == 0)
			throw new Exception();
		c.moveToFirst();
		String str[] = new String[2];
		str[0] = c.getString(0);
		str[1] = c.getString(1);
		c.close();
		return str;
	}

	//データベースへの書き込み
	//	public void writeDB(String id, String DB_TABLE, SQLiteDatabase db) throws Exception {
	//		ContentValues values = new ContentValues();
	//		values.put("id", "0");
	//		values.put("info", info);
	//		int colNum = db.update(DB_TABLE, values, null, null);
	//		if (colNum == 0)
	//			db.insert(DB_TABLE, "", values);
	//	}

	//チェックボックスフラグ更新
	public static void updateDB(String id, String value, String DB_TABLE, SQLiteDatabase db) throws Exception {
		String sql = null;
		if(DB_TABLE.equals("userinfo")) {
			sql = "update " + DB_TABLE + " set num ='" + value + "' where id = '" + id + "';";
		}else if(DB_TABLE.equals("nameinfo")) {
			sql = "update " + DB_TABLE + " set name ='" + value + "' where id = '" + id + "';";
		}

		db.execSQL(sql);

	}

}
