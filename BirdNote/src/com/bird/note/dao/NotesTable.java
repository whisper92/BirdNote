package com.bird.note.dao;

/**
 * Tbale show_notes
 * @author wangxianpeng
 * @since 19/12/14
 */
public class NotesTable {
	public static final String TABLE_NAME="show_notes";
	public static final String _ID="_id";
	public static final String LEVEL="level";
	public static final String TITLE="title";
	public static final String TEXTCONTENT="textcontents";
	public static final String QUA0="qua0";
	public static final String QUA1="qua1";
	public static final String QUA2="qua2";
	public static final String QUA3="qua3";
	public static final String BG_ID="background";
	public static final String STAR="star";
	public static final String CREATE_TIME="create_time";
	public static final String UPDATE_TIME="update_time";

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
    		+BG_ID+" integer,"
    		+STAR+" integer,"
    		+CREATE_TIME+" text,"
    		+UPDATE_TIME+" text)";

  public static final String TEXTCONTENT_HEADER="textcontents";
  public static final String TEXTCONTENT_OBJECT_QUA="qua";
  public static final String TEXTCONTENT_OBJECT_QUACONTENT="quacontent";
}
