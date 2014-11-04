package com.bird.utils;
/**
 * 数据库中的一些常量
 * @author wangxianpeng
 *
 */
public class BirdDatabaseConstant {

	/*
	 * 数据库名称
	 */
	public static final String DATABASE_NAME = "bird_notes";
	/*
	 * 数据库版本号
	 */
	public static final int DATABASE_VERSION = 1;
	
	/*
	 * 所有父笔记对应的表
	 */
	public static final String TABLE_NOTES= "notes";
	public static final String TABLE_NOTES_ID = "_id";
	public static final String TABLE_NOTES_LEVEL = "level";
	public static final String TABLE_NOTES_TITLE = "title";
	public static final String TABLE_NOTES_CHILDNOTES_COUNT= "childcouont";//一个BirdNote包含的子Note的个数
	public static final String TABLE_NOTES_CONTENT = "content";
	public static final String TABLE_NOTES_THUMBNAIL = "thumbnail";
	/*
	 * 创建父笔记的表
	 */
	public static final String SQL_CREATE_SHOW_NOTES = "create table notes(" +
			"_id integer primary key autoincrement," +
			"level integer default 0," +
			"title text default \"note\"," +
			"textlines integer default 0," +
			"content blob," +
			"thumbnail blob)";
	
	/*
	 * 所有子笔记对应的表
	 */
	public static final String TABLE_CHILD_NOTES= "childnotes";
	public static final String TABLE_CHILD_NOTES_ID = "_id";
	public static final String TABLE_CHILD_NOTES_BELONGTO_ID = "noteid";
	public static final String TABLE_CHILD_NOTES_QUADRANT = "quadrant";
	public static final String TABLE_CHILD_NOTES_TEXTLINES = "textlines";
	public static final String TABLE_CHILD_NOTES_CONTENT = "childcontent";
	public static final String TABLE_CHILD_NOTES_THUMBNAIL = "childthumbnail";

	
}
