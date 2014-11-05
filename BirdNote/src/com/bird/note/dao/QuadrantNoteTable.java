package com.bird.note.dao;

/**
 * 象限笔记表
 * @author wangxianpeng
 *
 */
public class QuadrantNoteTable {
	public static int fatherID;
	public static int quadrant;
	
	public static int getFatherID() {
		return fatherID;
	}

	public static void setFatherID(int fatherID) {
		QuadrantNoteTable.fatherID = fatherID;
	}

	public static int getQuadrant() {
		return quadrant;
	}

	public static void setQuadrant(int quadrant) {
		QuadrantNoteTable.quadrant = quadrant;
	}

	public QuadrantNoteTable(int fatherID,int quadrant){
		this.fatherID=fatherID;
		this.quadrant=quadrant;
	}
	public static final String TABLE_NAME=" child_"+fatherID;
	public static final String  QUADRANT=" _id";
	public static final String TEXTLINES=" textlines";
	public static final String CONTENT=" content";
	public static final String THUMBNAIL=" thumbnail";
	
    public static final String SQL_CREATE="create table "+TABLE_NAME
    		+"("
    		+QUADRANT+" integer primary key autoincrement,"
    		+TEXTLINES+" text,"
    		+CONTENT+" blob,"
    		+THUMBNAIL+" blob)";	
}
