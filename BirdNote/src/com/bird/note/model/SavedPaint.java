package com.bird.note.model;

import android.graphics.Color;
import android.graphics.Paint;

public class SavedPaint {
	Paint paint=new Paint();
	int color = Color.GRAY;
	int width = 5;

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
		paint.setColor(color);
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
		paint.setStrokeWidth(width);
	}
	
	public SavedPaint(int color,int width) {
		paint.setColor(color);
		paint.setStrokeWidth(width);
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);
	}
	public Paint getSavePaint(){
		return paint;
	}

}
