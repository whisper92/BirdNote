package com.bird.note.dao;

import com.bird.note.dao.BirdDatabaseConstant;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Db extends SQLiteOpenHelper{

	public Db(Context context){
		super(context, BirdDatabaseConstant.DATABASE_NAME, null, BirdDatabaseConstant.DATABASE_VERSION);
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(NotesTable.SQL_CREATE);
		Log.d("wxp","create table success...");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
