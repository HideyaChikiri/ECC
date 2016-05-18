package com.example.ecctest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

//データベースヘルパーの定義（ローカルクラス）
public class DBHelper extends SQLiteOpenHelper {

	/* DBデータ */
	final static String DB_NAME = BaseActivity.DB_NAME;
	final static String DB_TABLE = BaseActivity.DB_TABLE;
	final static String DB_TABLE2 = BaseActivity.DB_TABLE2;
	final static int DB_VERSION = BaseActivity.DB_VERSION;

	//データベースヘルパーのコンストラクタ
	public DBHelper(Context context) {
		super(context, BaseActivity.DB_NAME, null, BaseActivity.DB_VERSION);
	}

	//データベースの生成
	/* アンインストールしないと呼ばれない */
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d("d", "DBのonCreate1");
		db.execSQL("create table if not exists " + DB_TABLE + "(id text primary key,num integer)");
		db.execSQL("Insert into " + DB_TABLE + " values('testtimes', 0)");
		db.execSQL("Insert into " + DB_TABLE + " values('highscore', 0)");
		db.execSQL("Insert into " + DB_TABLE + " values('initialscore', 0)");
		db.execSQL("Insert into " + DB_TABLE + " values('randl', 0)"); //1ならクリア表示
		db.execSQL("Insert into " + DB_TABLE + " values('bandv', 0)");
		db.execSQL("Insert into " + DB_TABLE + " values('arander', 0)");
		db.execSQL("Insert into " + DB_TABLE + " values('animation', 0)");
		db.execSQL("Insert into " + DB_TABLE + " values('bgm', 0)");

		db.execSQL("create table if not exists " + DB_TABLE2 + "(id text primary key,name text)");
		db.execSQL("Insert into " + DB_TABLE2 + " values('name', '"+ BaseActivity.DEFAULT_NAME +"')");
		Log.d("d", "DBのonCreate3");
	}

	//データベースのアップグレード
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop talbe if exists " + DB_TABLE);
		onCreate(db);
	}
}