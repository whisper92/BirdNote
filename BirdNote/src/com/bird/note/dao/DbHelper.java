package com.bird.note.dao;

import android.database.sqlite.SQLiteDatabase;
import android.content.Context;

import com.bird.note.utils.*;

import android.content.*;

public class DbHelper {
	private Context mContext;
	private Db mDb;
	private SQLiteDatabase dbWrite;
	private SQLiteDatabase dbRead;
	
	public DbHelper(Context context){
		this.mContext=context;
		mDb=new Db(context);
		dbRead=mDb.getReadableDatabase();
		dbWrite=mDb.getWritableDatabase();
	}
	
	public void insertNewNote(ContentValues values){
		

	}

}
