package com.bird.note.dao;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.content.Context;

import com.bird.note.model.BirdNote;
import com.bird.note.model.QuadrantContent;
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
	}
	
	public void insertNewNote(ContentValues values){
		dbWrite.insert(NotesTable.TABLE_NAME, null, values);
	}
	
	public void insertNewNote(BirdNote birdNote){
		insertNewNote(birdNote.level, birdNote.title, birdNote.textcontents, birdNote.qua0, birdNote.qua1, birdNote.qua2, birdNote.qua3, birdNote.thumbnail,birdNote.background);
	}
	
	/**
	 * 查询所有笔记的id，level,title,以及thumbnail，用于首页展示。
	 * @return
	 */
	public List<BirdNote> queryShowNotes(){
		List<BirdNote> birdNotesList=new ArrayList<BirdNote>();
		Cursor cursor=dbRead.query(NotesTable.TABLE_NAME, new String[]{NotesTable._ID,NotesTable.LEVEL,NotesTable.TITLE,NotesTable.THUMBNAIL,NotesTable.BG_ID}, null, null, null, null, "_id desc");
		while (cursor.moveToNext()) {
			BirdNote birdNote=new BirdNote();
			birdNote._id=cursor.getInt(cursor.getColumnIndex(NotesTable._ID));
			birdNote.level=cursor.getInt(cursor.getColumnIndex(NotesTable.LEVEL));
			birdNote.title=cursor.getString(cursor.getColumnIndex(NotesTable.TITLE));
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
	
	/**
	 * 这个方法由birdnote，以及id，从数据库中查询生成了一个完整的BirdNote.用于继续编辑笔记。
	 * @param birdNote
	 * @param note_id
	 * @return
	 */
	public BirdNote queryNoteById(BirdNote birdNote,String note_id){
		Cursor cursor=dbRead.query(NotesTable.TABLE_NAME, new String[]{NotesTable.TEXTCONTENT,NotesTable.QUA0,NotesTable.QUA1,NotesTable.QUA2,NotesTable.QUA3}, "_id=?", new String[]{note_id}, null, null, null);
		while (cursor.moveToNext()) {
			birdNote.textcontents=cursor.getString(cursor.getColumnIndex(NotesTable.TEXTCONTENT));
			birdNote.qua0=cursor.getBlob(cursor.getColumnIndex(NotesTable.QUA0));
			birdNote.qua1=cursor.getBlob(cursor.getColumnIndex(NotesTable.QUA1));
			birdNote.qua2=cursor.getBlob(cursor.getColumnIndex(NotesTable.QUA2));
			birdNote.qua3=cursor.getBlob(cursor.getColumnIndex(NotesTable.QUA3));
		}
        cursor.close();
		return birdNote;
	}
	
	/**
	 * 从一个笔记中生成四个象限的内容
	 * @param birdNote
	 * @return
	 * @throws JSONException 
	 */
	public List<QuadrantContent> generateQuadrantFromNote(BirdNote birdNote) throws JSONException{
		
		List<QuadrantContent> quadrantContentList=new ArrayList<QuadrantContent>();
		String[] textconrents=JsonUtil.parseJsonToStrings(birdNote.textcontents);
		for (int i = 0; i < 4; i++) {		
			switch (i) {
			case 0:
				if (birdNote.qua0!=null&&textconrents[0]!=null) {
					QuadrantContent quadrantContent=new QuadrantContent();
					quadrantContent.quadrant=0;
					quadrantContent.quadrantdraw=birdNote.qua0;
					quadrantContent.textcontent=textconrents[0];
					quadrantContentList.add(0, quadrantContent);
				} else {
					quadrantContentList.add(0, null);
				}
				break;
			case 1:
				if (birdNote.qua1!=null&&textconrents[1]!=null) {
					QuadrantContent quadrantContent=new QuadrantContent();
					quadrantContent.quadrant=1;
					quadrantContent.quadrantdraw=birdNote.qua1;
					quadrantContent.textcontent=textconrents[1];
					quadrantContentList.add(1, quadrantContent);
				}else {
					quadrantContentList.add(1, null);
				}
				break;
			case 2:
				if (birdNote.qua2!=null&&textconrents[2]!=null) {
					QuadrantContent quadrantContent=new QuadrantContent();
					quadrantContent.quadrant=2;
					quadrantContent.quadrantdraw=birdNote.qua2;
					quadrantContent.textcontent=textconrents[2];
					quadrantContentList.add(2, quadrantContent);
				}else {
					quadrantContentList.add(2, null);
				}
				break;
			case 3:
				if (birdNote.qua3!=null&&textconrents[3]!=null) {
					QuadrantContent quadrantContent=new QuadrantContent();
					quadrantContent.quadrant=3;
					quadrantContent.quadrantdraw=birdNote.qua3;
					quadrantContent.textcontent=textconrents[3];
					quadrantContentList.add(3, quadrantContent);
				}else {
					quadrantContentList.add(3, null);
				}
				break;
			default:
				break;
			}	
		}
		return quadrantContentList;
	}
	
	/**
	 * 根据id更新笔记
	 * @param birdNote
	 * @param note_id
	 */
	public void updateNoteById(BirdNote birdNote,String  note_id){
		ContentValues values=new ContentValues();
		values.put(NotesTable.LEVEL, birdNote.level);
		values.put(NotesTable.TITLE, birdNote.title);
		values.put(NotesTable.TEXTCONTENT, birdNote.textcontents);
		values.put(NotesTable.QUA0, birdNote.qua0);
		values.put(NotesTable.QUA1, birdNote.qua1);
		values.put(NotesTable.QUA2, birdNote.qua2);
		values.put(NotesTable.QUA3, birdNote.qua3);
		values.put(NotesTable.THUMBNAIL, birdNote.thumbnail);
		dbWrite.update(NotesTable.TABLE_NAME, values, "_id=?", new String[]{note_id});
		dbWrite.close();
	}

}
