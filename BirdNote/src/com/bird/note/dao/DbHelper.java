package com.bird.note.dao;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
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
		dbWrite.insert(NotesTable.TABLE_NAME, null, values);
	}
	
	public void insertNewNote(int level,String title,String text_content,byte[] qua0,byte[] qua1,byte[]qua2,byte[] qua3,byte[] thumbnail){
		ContentValues values=new ContentValues();
		values.put(NotesTable.LEVEL, level);
		values.put(NotesTable.TITLE, title);
		values.put(NotesTable.TEXTCONTENT, text_content);
		values.put(NotesTable.QUA0, qua0);
		values.put(NotesTable.QUA1, qua1);
		values.put(NotesTable.QUA2, qua2);
		values.put(NotesTable.QUA3, qua3);
		values.put(NotesTable.THUMBNAIL, thumbnail);
		dbWrite.insert(NotesTable.TABLE_NAME, null, values);
		dbWrite.close();
		Log.e("wxp","插入成功");
	}

}
