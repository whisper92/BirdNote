package com.bird.dao;

import android.database.sqlite.SQLiteDatabase;

public class DbHelper {

	public void createChildNoteTable(SQLiteDatabase db, int fatherId) {
		/*
		 * 创建子笔记的表,表名暂时设为父笔记的id，后续应改为 时间_父笔记id 这样的格式
		 */
		String SQL_CREATE_CHILD_NOTES = "create table "+fatherId+"("
				+ "_id integer primary key autoincrement," +
				" fatherId integer default -1,"
				+ "quadrant integer default 0,"
				+ "textlines integer default 0,"
				+ "childcontent blog,"
				+ "childthumbnail blob)";
		db.execSQL(SQL_CREATE_CHILD_NOTES);
	}

	public void createChildNoteTextlinesTable(SQLiteDatabase db, int fatherId,int childId,int quadrant){
		/*
		 * 创建子笔记的包含的中文本行对应的表,表名暂时设为 父id子id 的格式（保证textlines表的唯一性），后续应改为 时间_父id子id 这样的格式
		 */
		String tablename=fatherId+"_"+childId;
		String SQL_CREATE_CHILD_NOTES = "create table "+tablename+"("
				+ "_id integer primary key autoincrement,fatherId integer default "+fatherId+",childId integer default "+childId+"," + "quadrant integer default "+quadrant+",integer x default 0,integer y default 0,textcontent text)";
		db.execSQL(SQL_CREATE_CHILD_NOTES);
	}
}
