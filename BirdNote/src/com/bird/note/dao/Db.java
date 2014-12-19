package com.bird.note.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * @author wangxianpeng
 * @since 19/12/14
 * 
 */
public class Db extends SQLiteOpenHelper {

	public Db(Context context) {
		super(context, BirdDatabaseConstant.DATABASE_NAME, null,
				BirdDatabaseConstant.DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(NotesTable.SQL_CREATE);
		Log.e("wxp", "db-oncreate ...");
/*		ContentValues values = new ContentValues();
		getWritableDatabase().insert(NotesTable.TABLE_NAME, null, values);*/
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
