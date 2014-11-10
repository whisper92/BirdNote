package com.bird.note.dao;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.content.Context;

import com.bird.note.model.BirdNote;
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
	
	/**
	 * 插入新的笔记
	 * @param level : 笔记等级
	 * @param title : 笔记标题
	 * @param text_content : 笔记文本内容
	 * @param qua0 : 0象限绘制内容
	 * @param qua1 : 1象限绘制内容
	 * @param qua2 : 2象限绘制内容
	 * @param qua3 : 3象限绘制内容
	 * @param thumbnail : 缩略图
	 */
	public void insertNewNote(int level,String title,String text_content,byte[] qua0,byte[] qua1,byte[]qua2,byte[] qua3,byte[] thumbnail,int bg_id){
		ContentValues values=new ContentValues();
		values.put(NotesTable.LEVEL, level);
		values.put(NotesTable.TITLE, title);
		values.put(NotesTable.TEXTCONTENT, text_content);
		values.put(NotesTable.QUA0, qua0);
		values.put(NotesTable.QUA1, qua1);
		values.put(NotesTable.QUA2, qua2);
		values.put(NotesTable.QUA3, qua3);
		values.put(NotesTable.THUMBNAIL, thumbnail);
		values.put(NotesTable.BG_ID, bg_id);
		dbWrite.insert(NotesTable.TABLE_NAME, null, values);
		dbWrite.close();
		Log.e("wxp","插入成功");
	}
	
	public void insertNewNote(ContentValues values){
		dbWrite.insert(NotesTable.TABLE_NAME, null, values);
	}
	
	public void insertNewNote(Context context,BirdNote birdNote){
		DbHelper dbHelper=new DbHelper(context);
		dbHelper.insertNewNote(birdNote.level, birdNote.title, birdNote.textcontents, birdNote.qua0, birdNote.qua1, birdNote.qua2, birdNote.qua3, birdNote.thumbnail,birdNote.background);
	}
	
	public List<BirdNote> queryShowNotes(){
		List<BirdNote> birdNotesList=new ArrayList<BirdNote>();
		Cursor cursor=dbRead.query(NotesTable.TABLE_NAME, new String[]{NotesTable.LEVEL,NotesTable.TITLE,NotesTable.THUMBNAIL,NotesTable.BG_ID}, null, null, null, null, "_id desc");
		while (cursor.moveToNext()) {
			BirdNote birdNote=new BirdNote();
			birdNote.level=cursor.getInt(cursor.getColumnIndex(NotesTable.LEVEL));
			birdNote.title=cursor.getString(cursor.getColumnIndex(NotesTable.TITLE));
			//byte[] blob = cursor.getBlob(cursor.getColumnIndex(NotesTable.THUMBNAIL));
			//Bitmap bmp = BitmapFactory.decodeByteArray(blob, 0, blob.length);  
			birdNote.thumbnail=cursor.getBlob(cursor.getColumnIndex(NotesTable.THUMBNAIL));
			birdNote.background=cursor.getInt(cursor.getColumnIndex(NotesTable.BG_ID));
			birdNotesList.add(birdNote);	
		}
		cursor.close();
		return birdNotesList;
	}
	
	public Cursor queryShowNoteCursor(){
		Cursor cursor=dbRead.query(NotesTable.TABLE_NAME, new String[]{NotesTable.LEVEL,NotesTable.TITLE,NotesTable.THUMBNAIL,NotesTable.BG_ID}, null, null, null, null, null);
        return cursor;
	}

}
