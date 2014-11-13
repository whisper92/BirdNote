package com.bird.note.customer;

import com.bird.note.model.DBUG;
import com.bird.note.model.SavedPaint;

import android.R.integer;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CleanCircle extends View{

	Paint mPaint;
	float width=SavedPaint.DEFAULT_CLEAN_PAINT_WIDTH;
	public CleanCircle(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
        init(context);
	}

	public CleanCircle(Context context, AttributeSet attrs) {
		this(context, attrs, 0);

	}

	public CleanCircle(Context context) {
		this(context, null);

	}
	public void init(Context context){
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setStyle(Paint.Style.FILL);	
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		SharedPreferences sp= context.getSharedPreferences(SavedPaint.SP_PAINT_KEY, Context.MODE_PRIVATE);
		width =  sp.getFloat(SavedPaint.SP_PAINT_CLEAN_WIDTH, SavedPaint.DEFAULT_CLEAN_PAINT_WIDTH);
		mPaint.setStrokeWidth(width);
		mPaint.setColor(Color.WHITE);
	}
	
	public void setCleanPaintWidth(float width){
		this.width=width;
		invalidate();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
       canvas.drawCircle(61, 37, width/2, mPaint);
	}
	

}
