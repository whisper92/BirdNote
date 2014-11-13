package com.bird.note.model;

import com.bird.note.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * 保存画笔配置
 */
public class SavedPaint {
    public static int[] mColorImages=new int[]{
		R.id.id_color_00,R.id.id_color_01,R.id.id_color_02,R.id.id_color_03,R.id.id_color_04,R.id.id_color_05,R.id.id_color_06,R.id.id_color_07,
		R.id.id_color_08,R.id.id_color_09,R.id.id_color_10,R.id.id_color_11,R.id.id_color_12,R.id.id_color_13,R.id.id_color_14,R.id.id_color_15};
    public static int[] mColors=new int[]{
		0xff16cc79,0xff01932e,0xff04672e,0xffff82c9,0xffff365b,0xffff0000,0xffff6000,0xffa5a5a5,
		0xff727272,0xff363636,0xff38a8fe,0xff3467fe,0xff005aff,0xfffcff0c,0xffffffff,0xff000000
		};

    public static final String SP_PAINT_KEY="saved_paint";
    public static final String SP_PAINT_COLOR="save_paint_color";
    public static final String SP_PAINT_WIDTH="saved_paint_width";
    public static final String SP_PAINT_CLEAN_WIDTH="saved_clean_pain_width";
    
	Paint paint=new Paint();
	public static final int DEFAULT_PAINT_COLOR = 0xff000000;
	public static final float DEFAULT_PAINT_WIDTH = 5f;
	public static final float DEFAULT_CLEAN_PAINT_WIDTH = 5f;
	public static final float DEFAULT_PAINT_MAX_WIDTH=40f;
	
	public Context mContext;
	SharedPreferences mSavedPaintSP;
	public SavedPaint(Context context){
		mContext=context;
		mSavedPaintSP=mContext.getSharedPreferences(SavedPaint.SP_PAINT_KEY, Context.MODE_PRIVATE);
		paint.setColor(mSavedPaintSP.getInt(SP_PAINT_COLOR, DEFAULT_PAINT_COLOR));
		paint.setStrokeWidth(mSavedPaintSP.getFloat(SP_PAINT_WIDTH, DEFAULT_PAINT_WIDTH));
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);
	}
	
	public int getSavedPaintColor(){
		return mSavedPaintSP.getInt(SP_PAINT_COLOR, DEFAULT_PAINT_COLOR);
	}
	
	public float getSavedPaintWidth(){
		return mSavedPaintSP.getFloat(SP_PAINT_WIDTH, DEFAULT_PAINT_WIDTH);
	}
	
	public float getSavedCleanPaintWidth(){
		return mSavedPaintSP.getFloat(SP_PAINT_CLEAN_WIDTH, DEFAULT_CLEAN_PAINT_WIDTH);
	}
	public void savePaintColor(int color){
		Editor editor=mSavedPaintSP.edit();
		editor.putInt(SP_PAINT_COLOR, color);
		editor.commit();
	}
	
	public void savePaintWidth(float width){
		Editor editor=mSavedPaintSP.edit();
		editor.putFloat(SP_PAINT_WIDTH, width);
		editor.commit();
	}
	
	public void saveCleanPaintWidth(float width){
		Editor editor=mSavedPaintSP.edit();
		editor.putFloat(SP_PAINT_CLEAN_WIDTH, width);
		editor.commit();
	}
	public Paint getSavePaint(){
		int sColor=mSavedPaintSP.getInt(SavedPaint.SP_PAINT_COLOR, DEFAULT_PAINT_COLOR);
		float sWidth=mSavedPaintSP.getFloat(SavedPaint.SP_PAINT_WIDTH, DEFAULT_PAINT_WIDTH);
		paint.setColor(sColor);
		paint.setStrokeWidth(sWidth);
		return paint;
	}

}
