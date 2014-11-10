package com.bird.note.dao;

/**
 * 笔记表
 * @author wangxianpeng
 *
 */
public class NotesTable {
	public static final String TABLE_NAME=" show_notes";
	public static final String _ID=" _id";
	public static final String LEVEL="level";
	public static final String TITLE="title";
	public static final String TEXTCONTENT="textcontents";
	public static final String QUA0="qua0";
	public static final String QUA1="qua1";
	public static final String QUA2="qua2";
	public static final String QUA3="qua3";
	public static final String THUMBNAIL="thumbnail";
	
    public static final String SQL_CREATE="create table "+TABLE_NAME
    		+"("
    		+_ID+" integer primary key autoincrement,"
    		+LEVEL+" integer,"
    		+TITLE+" text,"
    		+TEXTCONTENT+" text,"
    		+QUA0+" blob,"
    		+QUA1+" blob,"
    		+QUA2+" blob,"
    		+QUA3+" blob,"
    		+THUMBNAIL+" blob)";	
}
