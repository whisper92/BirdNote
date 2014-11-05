package com.bird.note.dao;

/**
 * 父笔记表
 * @author wangxianpeng
 *
 */
public class NotesTable {
	public static final String TABLE_NAME=" show_notes";
	public static final String _ID=" _id";
	public static final String LEVEL="level";
	public static final String TITLE=" title";
	public static final String TEXTLINES=" textlines";
	public static final String CONTENT=" content";
	public static final String THUMBNAIL=" thumbnail";
	
    public static final String SQL_CREATE="create table "+TABLE_NAME
    		+"("
    		+_ID+" integer primary key autoincrement,"
    		+LEVEL+" integer default 0,"
    		+TITLE+" text,"
    		+TEXTLINES+" text,"
    		+CONTENT+" blob,"
    		+THUMBNAIL+" blob)";	
}
