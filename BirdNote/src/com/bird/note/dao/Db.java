package com.bird.note.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bird.note.model.DBUG;

public class Db extends SQLiteOpenHelper{

	public Db(Context context){
		super(context, BirdDatabaseConstant.DATABASE_NAME, null, BirdDatabaseConstant.DATABASE_VERSION);
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(NotesTable.SQL_CREATE);
		DBUG.e("create table success...");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
