package com.bird.note.dao;

import org.json.JSONException;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

import com.bird.note.R;
import com.bird.note.utils.BitmapUtil;
import com.bird.note.utils.CommonUtils;
import com.bird.note.utils.JsonUtil;

/**
 * @author wangxianpeng
 * @since 19/12/14
 * 
 */
public class Db extends SQLiteOpenHelper {

	private Context mContext = null;
	public Db(Context context) {
		super(context, BirdDatabaseConstant.DATABASE_NAME, null,
				BirdDatabaseConstant.DATABASE_VERSION);
		mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(NotesTable.SQL_CREATE);
		try {
			inertDefaultNote(db);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.e("wxp", "db-oncreate ...");

	}

	public void inertDefaultNote(SQLiteDatabase db) throws JSONException {
		ContentValues values = new ContentValues();
		String[] textStrings = new String[4];
		textStrings[0] = mContext.getString(R.string.intro_content);
		values.put(NotesTable.LEVEL, 0);
		values.put(NotesTable.TITLE, mContext.getString(R.string.intro));
		values.put(NotesTable.BG_ID, 0);
		values.put(NotesTable.QUA0, BitmapUtil.decodeBitmapToBytes(Bitmap.createBitmap((int)mContext.getResources().getDimension(R.dimen.dimen_edit_canvas_width), (int)mContext. getResources().getDimension(R.dimen.dimen_edit_canvas_height), Bitmap.Config.ARGB_8888)));
		values.put(NotesTable.TEXTCONTENT,JsonUtil.createJsonFromStrings(textStrings));
		values.put(NotesTable.CREATE_TIME, CommonUtils.getCurrentTime());
		values.put(NotesTable.UPDATE_TIME, CommonUtils.getCurrentTime());
		db.insert(NotesTable.TABLE_NAME, null, values);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
