package com.bird.note.dao;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bird.note.model.BirdNote;
import com.bird.note.model.QuadrantContent;
import com.bird.note.utils.CommonUtils;
import com.bird.note.utils.JsonUtil;

public class DbHelper {
	private String TAG = "DbHelper";
	private Db mDb;
	private SQLiteDatabase dbWrite;
	private SQLiteDatabase dbRead;
	
	public DbHelper(Context context){
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
	 */
	public void insertNewNote(int level,String title,String text_content,byte[] qua0,byte[] qua1,byte[]qua2,byte[] qua3,int bg_id,int star,String creatTime,String updateTime){
		ContentValues values=new ContentValues();
		values.put(NotesTable.LEVEL, level);
		values.put(NotesTable.TITLE, title);
		values.put(NotesTable.TEXTCONTENT, text_content);
		values.put(NotesTable.QUA0, qua0);
		values.put(NotesTable.QUA1, qua1);
		values.put(NotesTable.QUA2, qua2);
		values.put(NotesTable.QUA3, qua3);
		values.put(NotesTable.BG_ID, bg_id);
		values.put(NotesTable.STAR, star);
		values.put(NotesTable.CREATE_TIME, creatTime);
		values.put(NotesTable.UPDATE_TIME, updateTime);
		dbWrite.insert(NotesTable.TABLE_NAME, null, values);
		dbWrite.close();
	}
	
	public void insertNewNote(ContentValues values){
		dbWrite.insert(NotesTable.TABLE_NAME, null, values);
	}
	
	public void insertNewNote(BirdNote birdNote){
		birdNote.create_time = CommonUtils.getCurrentTime();
		birdNote.update_time = CommonUtils.getCurrentTime();
		insertNewNote(birdNote.level, birdNote.title, birdNote.textcontents, birdNote.qua0, birdNote.qua1, birdNote.qua2, birdNote.qua3, birdNote.background,birdNote.star,birdNote.create_time,birdNote.update_time);
	}
	
	
	public List<BirdNote> getBirdNoteListFromCursor(Cursor cursor){
		List<BirdNote> birdNotesList=new ArrayList<BirdNote>();
		while (cursor.moveToNext()) {
			BirdNote birdNote=new BirdNote();
			birdNote._id=cursor.getInt(cursor.getColumnIndex(NotesTable._ID));
			birdNote.level=cursor.getInt(cursor.getColumnIndex(NotesTable.LEVEL));
			birdNote.title=cursor.getString(cursor.getColumnIndex(NotesTable.TITLE));
			birdNote.background=cursor.getInt(cursor.getColumnIndex(NotesTable.BG_ID));
			birdNote.star=cursor.getInt(cursor.getColumnIndex(NotesTable.STAR));
			birdNote.update_time=cursor.getString(cursor.getColumnIndex(NotesTable.UPDATE_TIME));
			birdNotesList.add(birdNote);	
		}
		return birdNotesList;
	}
	/**
	 * 查询所有笔记的id，level,title，用于首页展示。
	 * @return
	 */
	public List<BirdNote> queryShowNotes(){
		List<BirdNote> birdNotesList=new ArrayList<BirdNote>();
		Cursor cursor=dbRead.query(NotesTable.TABLE_NAME, new String[]{NotesTable._ID,NotesTable.LEVEL,NotesTable.TITLE,NotesTable.BG_ID,NotesTable.STAR,NotesTable.UPDATE_TIME}, null, null, null, null, "_id desc");
		birdNotesList = getBirdNoteListFromCursor(cursor);
		cursor.close();
		return birdNotesList;
	}
	
	/**
	 * 查询所有收藏的笔记的id，level,title
	 * @return
	 */
	public List<BirdNote> queryStaredShowNotes(){
		List<BirdNote> birdNotesList=new ArrayList<BirdNote>();
		Cursor cursor=dbRead.query(NotesTable.TABLE_NAME, new String[]{NotesTable._ID,NotesTable.LEVEL,NotesTable.TITLE,NotesTable.BG_ID,NotesTable.STAR,NotesTable.UPDATE_TIME}, NotesTable.STAR+"=?", new String[]{"1"}, null, null, "_id desc");
		birdNotesList = getBirdNoteListFromCursor(cursor);
		cursor.close();
		return birdNotesList;
	}
	
	/**
	 * 通过关键字查询笔记
	 * @return
	 */
	public List<BirdNote> searchNotesByTag(String tag){
		List<BirdNote> birdNotesList=new ArrayList<BirdNote>();
		Cursor cursor=dbRead.rawQuery("select * from show_notes where title like '%"+tag+"%' or textcontents like '%"+tag+"%';", null);
		birdNotesList = getBirdNoteListFromCursor(cursor);
		cursor.close();
		return birdNotesList;
	}
	
	
	/**
	 * 按创建时间排序。
	 * @return
	 */
	public List<BirdNote> sortShowNotesByCreateTime(){
		List<BirdNote> birdNotesList=new ArrayList<BirdNote>();
		Cursor cursor=dbRead.query(NotesTable.TABLE_NAME, new String[]{NotesTable._ID,NotesTable.LEVEL,NotesTable.TITLE,NotesTable.BG_ID,NotesTable.STAR,NotesTable.UPDATE_TIME}, null, null, null, null, NotesTable.CREATE_TIME+" desc");
		birdNotesList = getBirdNoteListFromCursor(cursor);
		cursor.close();
		return birdNotesList;
	}
	
	/**
	 * 按最后更新时间排序。
	 * @return
	 */
	public List<BirdNote> sortShowNotesByUpdateTime(){
		List<BirdNote> birdNotesList=new ArrayList<BirdNote>();
		Cursor cursor=dbRead.query(NotesTable.TABLE_NAME, new String[]{NotesTable._ID,NotesTable.LEVEL,NotesTable.TITLE,NotesTable.BG_ID,NotesTable.STAR,NotesTable.UPDATE_TIME}, null, null, null, null, NotesTable.UPDATE_TIME+" desc");
		birdNotesList = getBirdNoteListFromCursor(cursor);
		cursor.close();
		return birdNotesList;
	}
	
	/**
	 * 按等级排序。
	 * @return
	 */
	public List<BirdNote> sortShowNotesByLevel(){
		List<BirdNote> birdNotesList=new ArrayList<BirdNote>();
		Cursor cursor=dbRead.query(NotesTable.TABLE_NAME, new String[]{NotesTable._ID,NotesTable.LEVEL,NotesTable.TITLE,NotesTable.BG_ID,NotesTable.STAR,NotesTable.UPDATE_TIME}, null, null, null, null, NotesTable.LEVEL+" desc");
		birdNotesList = getBirdNoteListFromCursor(cursor);
		cursor.close();
		return birdNotesList;
	}
	
	public Cursor queryShowNoteCursor(){
		Cursor cursor=dbRead.query(NotesTable.TABLE_NAME, new String[]{NotesTable._ID,NotesTable.LEVEL,NotesTable.TITLE,NotesTable.BG_ID,NotesTable.STAR,NotesTable.UPDATE_TIME}, null, null, null, null, null);
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
		values.put(NotesTable.BG_ID, birdNote.background);
		values.put(NotesTable.UPDATE_TIME, CommonUtils.getCurrentTime());
		dbWrite.update(NotesTable.TABLE_NAME, values, "_id=?", new String[]{note_id});
		Log.e(TAG,"update note success..."+CommonUtils.getCurrentTime());
		dbWrite.close();
	}
	
	/**
	 * 根据id删除笔记
	 * @param note_id
	 */
	public void deleteNoteById(String note_id){
		dbWrite.delete(NotesTable.TABLE_NAME, "_id=?", new String[]{note_id});
		Log.e(TAG,"delete note success...");
	}
	
	public void deleteNoteByIds(String[] note_ids){
		for (int i = 0; i < note_ids.length; i++) {
			if (!note_ids[i].equals(String.valueOf(-1))) {
				Log.e(TAG,"delete notes..."+note_ids[i]);
				dbWrite.delete(NotesTable.TABLE_NAME, "_id=?", new String[]{note_ids[i]});
			}		
		}
		Log.e(TAG,"delete notes success...");
	}
	
	/**
	 * 添加至收藏
	 * @param note_id
	 */
	public int queryStarById(String note_id){
		Cursor cursor = dbRead.query(NotesTable.TABLE_NAME, new String[]{NotesTable.STAR}, "_id=?",  new String[]{note_id}, null, null, null);
		int star = 0;
		while (cursor.moveToNext()) {
				star = cursor.getInt(cursor.getColumnIndex(NotesTable.STAR));
		}
		cursor.close();
        return star;
	}
	
	/**
	 * 添加至收藏
	 * @param note_id
	 */
	public void toggleStarNoteById(String note_id){
		Cursor cursor = dbWrite.query(NotesTable.TABLE_NAME, new String[]{NotesTable.STAR}, "_id=?",  new String[]{note_id}, null, null, null);
		int star = 0;
		while (cursor.moveToNext()) {
				star = cursor.getInt(cursor.getColumnIndex(NotesTable.STAR));
		}
		cursor.close();
		if (star==1) {
			star = 0;
		} else if (star== 0){
			star =1;
		}
		ContentValues values=new ContentValues();
		values.put(NotesTable.STAR, star);
		values.put(NotesTable.UPDATE_TIME, CommonUtils.getCurrentTime());
		dbWrite.update(NotesTable.TABLE_NAME, values, "_id=?", new String[]{note_id});
		Log.e(TAG,"star note success..."+CommonUtils.getCurrentTime());
	}
	
	
	/**
	 * 更改等级
	 * @param note_id
	 */
	public void updateLevelById(String note_id,int level){
		ContentValues values=new ContentValues();
		values.put(NotesTable.LEVEL, level);
		values.put(NotesTable.UPDATE_TIME, CommonUtils.getCurrentTime());
		dbWrite.update(NotesTable.TABLE_NAME, values, "_id=?", new String[]{note_id});
		Log.e(TAG,"updateLevelById success..."+CommonUtils.getCurrentTime());
	}
	
	/**
	 * 更改标题
	 * @param note_id
	 */
	public void updateTitleById(String note_id,String title){
		ContentValues values=new ContentValues();
		values.put(NotesTable.TITLE, title);
		values.put(NotesTable.UPDATE_TIME, CommonUtils.getCurrentTime());
		dbWrite.update(NotesTable.TABLE_NAME, values, "_id=?", new String[]{note_id});
		Log.e(TAG,"updateTitleById success..."+CommonUtils.getCurrentTime());
	}
	
	/**
	 * 更改背景
	 * @return
	 */
	public void updateBackgroundById(String note_id,int bgid){
		ContentValues values=new ContentValues();
		values.put(NotesTable.BG_ID, bgid);
		values.put(NotesTable.UPDATE_TIME, CommonUtils.getCurrentTime());
		dbWrite.update(NotesTable.TABLE_NAME, values, "_id=?", new String[]{note_id});
		Log.e(TAG,"updateBackgroundById success..."+CommonUtils.getCurrentTime());
	}
	
	public int getNoteCount(){
		int count = 0;
		Cursor cursor =dbRead.rawQuery("select count(*) from "+NotesTable.TABLE_NAME, null);
		while (cursor.moveToNext()) {
			count++;			
		}
		cursor.close();
		return count;
	}

	/*复制记录*/
	/*insert into show_notes(level,title,textcontents,qua0,qua1,qua2,qua3,thumbnail,background,star,create_time,update_time) select level,title,textcontents,qua0,qua1,qua2,qua3,thumbnail,background,star,create_time,update_time from show_notes where _id=*/
	
	
	
}
