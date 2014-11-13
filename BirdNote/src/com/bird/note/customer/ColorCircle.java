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

public class ColorCircle extends View{

	Paint mPaint;
	float width=SavedPaint.DEFAULT_PAINT_WIDTH;
	int color=SavedPaint.DEFAULT_PAINT_COLOR;
	public ColorCircle(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
        init(context);
	}

	public ColorCircle(Context context, AttributeSet attrs) {
		this(context, attrs, 0);

	}

	public ColorCircle(Context context) {
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
		mPaint.setColor(sp.getInt(SavedPaint.SP_PAINT_COLOR, SavedPaint.DEFAULT_PAINT_COLOR));
		mPaint.setStrokeWidth(sp.getFloat(SavedPaint.SP_PAINT_WIDTH, SavedPaint.DEFAULT_PAINT_WIDTH));
	}
	
	public void setPaintWidth(float width){
		this.width=width;
		invalidate();
	}
	
	public void setPaintColor(int color){
		this.color=color;
		mPaint.setColor(color);
		invalidate();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
       canvas.drawCircle(61, 37, width, mPaint);
	}
	

}
