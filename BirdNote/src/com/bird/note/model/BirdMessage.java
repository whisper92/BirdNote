package com.bird.note.model;

import com.bird.note.R;

public class BirdMessage {
	/*
	 * 要启动的某个象限的编辑类型：创建或更新
	 */
	public static final String START_TYPE_KEY="start_type";
	/*
	 * 创建新笔记
	 */
	public static final int START_TYPE_CREATE_VALUE=101;
	/*
	 * 更新笔记
	 */
	public static final int START_TYPE_UPDATE_VALUE=102;
	public static final String START_TYPE_UPDATE_TITLE_KEY="note_title";
	/*
	 * intent传递序列化对象
	 */
	public static final String INITENT_PARCEL_NOTE="parcel_note";
	
	/*
	 * 要启动的某个象限的编辑模式：绘图或文字编辑
	 */
	public static final String START_MODE_KEY="start_mode";
	/*
	 * 绘图模式
	 */
	public static final int START_MODE_DRAW_KEY=R.id.id_edit_title_pen;
	/*
	 * 文字编辑模式
	 */
	public static final int START_MODE_TEXT_KEY=R.id.id_edit_title_text;
	/*
	 * 橡皮擦编辑模式
	 */
	public static final int START_MODE_CLEAN_KEY=R.id.id_edit_title_clean;
	
	/*
	 * 整个笔记的编辑模式：创建或更新
	 */
	public static final int NOTE_EDIT_TYPE_CREATE=103;
    public static final int NOTE_EDIT_TYPE_UPDATE=104;

    public static final int QUERY_RUNNABLE_START=200;
    public static final int QUERY_RUNNABLE_OVER=300;
    public static final int SAVE_RUNNABLE_START=201;
    public static final int DELETE_RUNNABLE_START=202;
    public static final int DELETE_SINGLE_NOTE_RUNNABLE_START=203;
    public static final int CHANGEMARKCOLOR_RUNNABLE_START=204;
    public static final int UPDATETITLE_RUNNABLE_START=205;
    public static final int SAVEAS_RUNNABLE_START=206;
    public static final int SORT_START=207;
	/*
	 * 新建笔记，保存完成之后，返回首页
	 */
    public static final int SAVE_OVER=301;
    public static final int DELETE_OVER=302;
    public static final int UPDATETITLE_RUNNABLE_OVER=305;
    public static final int SAVE_AS_OVER=306;
    public static final int SORT_OVER=307;
    

    /*
     * SortBy
     */
    public static final String SORT_BY="sortby";
    public static final int SORT_BY_DEFAULT=0;
    
    public static final String STAR="note_star";

}
