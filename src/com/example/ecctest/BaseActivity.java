package com.example.ecctest;

import static com.example.ecctest.MethodSet.*;
import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.LinearLayout;

public class BaseActivity extends Activity {
	static protected MediaPlayer mp;

	//文章
	static final String APPLICATION_NAME = "ECC";
	static final String[] SUB_TITLE = { "テスト", "学習", "学習状況",/* "TEST" */};
	static final String EX_BUTTON_NAME[] = { "事前テスト", "戻る", "設定",
			"セーブして終了", "変更する", "登録する", "SKIP", "教材情報" };
	//	static final String[] SUB_TITLE = { "TEST", "LEARNING", "ACHIEVEMENT" };
	//	static final String[] LETTERS_IN_TEST = { "QUESTION" };
	static final String[] LETTERS_IN_TEST = { "問題" };
	//	static final String[] TEST_RESULT = { "RESULT", "CORRECT", "INCORRECT", "SCORE" };
	static final String[] TEST_RESULT = { "結果", "正解", "不正解", "得点" };
	//色(背景)
	static final int BACK_COLOR1 = Color.argb(200, 0, 0, 0);
	static final int BACK_COLOR2 = Color.rgb(255, 230, 180);
	static final int BACK_COLOR3 = Color.argb(100, 0, 0, 0);
	static final int BACK_COLOR4 = Color.argb(0, 0, 0, 0); //透明
	static final int BACK_COLOR5 = Color.rgb(0, 0, 0); //背景
	static final int MONSTER_BACKCOLOR = Color.argb(100, 0, 0, 0);

	//色（文字）
	static final int WORD_COLOR1 = Color.BLACK; //文字色（黒）
	static final int WORD_COLOR2 = Color.WHITE; //文字色（白）
	static final int WORD_COLOR3 = Color.RED; //文字色（赤）
	static final int WORD_COLOR4 = Color.rgb(132, 112, 255); //文字色（赤）

	//画像（背景）
	static final int BACK_GROUND_IMAGE = R.drawable.background8;

	//サイズ
	static float WORD_SIZE;
	static float WORD_SIZE2;
	static float WORD_SIZE3;

	static int BIG_BUTTON_SIZE[] = new int[2];
	static int SPEAKER_BUTTON_SIZE[] = new int[2];
	static int BACK_BUTTON_SIZE[] = new int[2];
	static int NEXT_BUTTON_SIZE[] = new int[2];
	static int MONSTER_VIEW[] = new int[4];
	static int SUCCESS_SIZE;
	static int MONSTER_SIZE1;
	static int SETTING_SIZE;

	//	//フォント
	static Typeface tf;

	/* アプリ共有不変定数定義 */
	public static final int MP = LinearLayout.LayoutParams.MATCH_PARENT; //画面最大
	public static final int WC = LinearLayout.LayoutParams.WRAP_CONTENT; //自動調節
	//ID(今後やる場合ENUM使え)
	public static final int INFO_BUTTON = -13;
	public static final int SKIP_BUTTON = -12;
	public static final int RECORD_BUTTON = -11;
	public static final int NAME_RECORD = 30; //なぜか-10でえらー
	public static final int RESET_BUTTON = -9;
	public static final int MODIFY_BUTTON = -7;
	public static final int SETTING_BUTTON = -6;
	public static final int LEARNING_TEST_BUTTON = -5;
	public static final int CLOSE_BUTTON = -4;
	public static final int SPEAKER_BUTTON = -3;
	public static final int BACK_BUTTON = -2;
	public static final int NO_ID = -1;
	public static final int ABOVE_BUTTON = 0;
	public static final int BELOW_BUTTON = 1;
	public static final int NEXT_BUTTON = 2;
	public static final int ANSWER_VIEW = 3;
	public static final int QUESTION_INDEX = 4;

	//タグ
	public static final String NO_TAG = "-1";

	/* MUSIC設定 */

	public static final int MAIN_MUSIC = R.raw.bgm4;
	public static final int SUB_MUSIC = R.raw.bgm5;
	public static final int QUIETE_MUSIC = R.raw.mute;

	/* 問題情報 */
	static final String testWord[][] = new String[15][2];
	static final int testLength = testWord.length * 2 / 3;

	/* 学習情報 */
	static final String leanigIndex[] = { "RとL", "BとV", "ARとER" };
	static final int leaningIndexLength = leanigIndex.length;
	//	private int testAnswer[];

	static final String LEARNING_SENTENCE[][] = {
			{
					"1.口を「う」の形にします。\n"
							+ "2.下を図のように来るように巻いてください。\n"
							+ "3.「R」を発音する。（口の中が振動している感触があれば正しい発音をしています）\n"
							+ "4.「RA」と息を吐き出す感じで発音します。",
					"1.口を軽く「あ」の口にします。\n"
							+ "2.舌の先を図のように上の歯の裏に押し当てます。\n"
							+ "3.舌の動きは「LA」の発音と同時に喉の方に持ってきます。\n"
							+ "\n※ Lの発音は「ら」の発音に似ているため、「ら」を意識してみましょう。" },
			{
					"1.口の形はフーセンガムを膨らませようとする時の、口をすぼめた形をしてみてください。\n"
							+ "2.「B」と発音したとき、一緒に強く息を出します。\n"
							+ "※ Bの発音が正しくされているのを確かめるには、手を口の前にかざすとわかります。手に強めの空気が押し当てられていれば、正しい発音をしています。",
					"1.上の歯で下唇の内側を押さえます。\n"
							+ "2.「V」と発音すると下唇が振動するはずです。\n"
							+ "\n※ Vの発音を確かめるには手を口の前にかざし、Bの発音より弱く空気が伝わってくれば、正しい発音をしています。" },
			{
					"1.口を縦に大きく開ける。\n" + "2.「A」の発音は短く。\n"
							+ "3.その後に「R」と発音する。\n"
							+ "その時の下の動きは「R」の発音と同様。",
					"1.口を「う」の形にします。\n" + "2.舌の先を図のように来るように巻いてください。\n"
							+ "3.「R」を発音します\n" + "\n※ Rの発音と全く同じ発音。" } };

	static final String LEARNIG_MP3[][][] = {
			{ { "R", "rick", "read", "rock", "road", "right" },
					{ "L", "lick", "lead", "lock", "load", "light" } },
			{ { "B", "berry", "ban", "bat", "bet", "boat" },
					{ "V", "very", "van", "vat", "vet", "vote" } },
			{
					{ "AR", "altar", "dear", "cellar", "hard", "hart" },
					{ "ER", "alter", "deer", "seller", "heard",
							"heart" } } };

	static final int MAX_LEN = 10; //「R」「L」などのMP3数

	/* バイブレーション */
	static final int VIB_TIME = 100;

	/* モンスターせりふ */
	static final String DEFAULT_NAME = "どせいさん";

	/* Handler用 */
	static int CURRENT_TESTSCORE = 60; //直近総合テストスコア
	static int TEXT_SPEED = 150; //出力間隔（ミリ秒）

	/* DBデータ */
	SQLiteDatabase db; //データベースオブジェクト
	static final String DB_NAME = "test.db";//DB名
	static final String DB_TABLE = "userinfo"; //テーブル名
	static final String DB_TABLE2 = "nameinfo"; //テーブル名
	static final int DB_VERSION = 1; //バージョン
	String testtimes = "-1";
	String highscore = "-1";
	String initialscore = "-1";
	String randl = "-1";
	String bandv = "-1";
	String arander = "-1";
	String animation = "-1";
	String name = "error";
	String bgm = "-1";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 画面上のタイトルを表示しない
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		/* データベースオブジェクトの取得 */
		DBHelper dbHelper = new DBHelper(this);
		db = dbHelper.getWritableDatabase();
		try {
			testtimes = readDB("testtimes", DB_TABLE, db)[1];
			highscore = readDB("highscore", DB_TABLE, db)[1];
			initialscore = readDB("initialscore", DB_TABLE, db)[1];
			randl = readDB("randl", DB_TABLE, db)[1];
			bandv = readDB("bandv", DB_TABLE, db)[1];
			arander = readDB("arander", DB_TABLE, db)[1];
			animation = readDB("animation", DB_TABLE, db)[1];
			name = readDB("name", DB_TABLE2, db)[1];
			bgm = readDB("bgm", DB_TABLE, db)[1];

		} catch (Exception e) {
			Log.d("d", "dbError");
		}

		if (mp == null) {
			mp = MediaPlayer.create(this, R.raw.mute);
			mp.setLooping(true);
		}

		tf = Typeface.createFromAsset(getAssets(),
				"fonts/TanukiMagic.ttf");

	}

	@Override
	protected void onResume() {
		super.onResume();
		//BGMの再生
		mp.start();
	}

	@Override
	protected void onPause() {
		super.onPause();
		//BGMの停止
		mp.stop();
	}

	protected void mpChange(int intSong) {

		if (bgm.equals("0")) {
			mp = MediaPlayer.create(this, R.raw.mute);
		} else {
			mp = MediaPlayer.create(this, intSong);
		}
		mp.setLooping(true);
	}

	protected void mpStart() {
		if (!mp.isPlaying()) {
			mp.start();
		}
	}

	protected void mpStop() {
		if (mp.isPlaying()) {
			mp.stop();
		}
	}

}