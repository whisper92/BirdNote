package com.bird.note.model;

import android.util.Log;

import com.bird.note.R;

public class BirdMessage {
	/*
	 * 要启动的类型：创建或更新
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
	/*
	 * intent传递序列化对象
	 */
	public static final String INITENT_PARCEL_NOTE="parcel_note";
	
	/*
	 * 要启动的模式：绘图或文字编辑
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
	 * 新建笔记，保存完成之后，返回首页
	 */
    public static final int SAVE_OVER=201;
    

}
