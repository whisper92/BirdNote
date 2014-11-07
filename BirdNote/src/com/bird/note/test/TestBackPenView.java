package com.bird.note.test;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.bird.note.utils.BitmapUtil;
import com.bird.note.utils.CommonUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory.Options;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class TestBackPenView extends View
{

	Paint mPaint = null;		// used to draw line
	Paint mBitmapPaint = null; 	// used to draw bitmap
	Path mPath = null;			// save the point
	Bitmap mBitmap = null;		// used as choosing picture,用于从图库选择的背景图片
	Bitmap mBottomBitmap = null;// used as bottom background，用于背景
	Canvas mCanvas = null;		// what's it used for
	float posX,posY;			// used as touched position
	private final float TOUCH_TOLERANCE = 4;
	
	private DrawPath mDrawPath = null;
	private List<DrawPath> mSavePath 	= null;
	private List<DrawPath> mDeletePath	= null;
	
	private int mImageWidth = 480;
	private int mImageHeight = 800;
	private int mBottomBitmapDrawHeight = 0;
	
	private Context mContext;
	public TestBackPenView(Context context,AttributeSet attr,int defStyle)
	{
		super(context,attr,defStyle);
		init(context);
	}
	public TestBackPenView(Context context,AttributeSet attr)
	{
		super(context,attr);
		init(context);
	}
	
	public TestBackPenView(Context context)
	{
		super(context);
		init(context);
	}
	
	private void init(Context context)
	{
		mContext=context;
		mPaint = new Paint();
	    mPaint.setAntiAlias(true);
	    mPaint.setDither(true);
	    mPaint.setColor(0xFFCCCCCC);
	    mPaint.setStyle(Paint.Style.STROKE);
	    mPaint.setStrokeJoin(Paint.Join.ROUND);
	    mPaint.setStrokeCap(Paint.Cap.ROUND);
	    mPaint.setStrokeWidth(12);
	    
	    mBitmapPaint = new Paint(Paint.DITHER_FLAG);
	    
	    mSavePath = new ArrayList<DrawPath>();
	    mDeletePath = new ArrayList<DrawPath>();
	}
	
	
	
	private class DrawPath
	{
		Path path;
		Paint paint;
	}
	
	@Override
	protected void onSizeChanged(int w,int h,int oldw,int oldh)
	{
		super.onSizeChanged(w, h, oldw, oldh);
        mBottomBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBottomBitmap);
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		
		canvas.drawColor(0xFF000000);
		int nCanvasWidth 	= canvas.getWidth();
		int nCanvasHeight 	= canvas.getHeight();
		int nBitmapWidth 	= mBottomBitmap.getWidth();
		int nBitmapHeight 	= mBottomBitmap.getHeight();
		mBottomBitmapDrawHeight = (nCanvasHeight - nBitmapHeight)/2;
		canvas.drawBitmap(mBottomBitmap,0,mBottomBitmapDrawHeight,mBitmapPaint);
		if(mPath != null)
		{
			canvas.drawPath(mPath, mPaint);	
		}
		
		
		//canvas.drawRect(10,10,100,100,mPaint);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		float x = event.getX();
		float y = event.getY();
		
		switch(event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				mPath = new Path();
				mDrawPath = new DrawPath();
				mPath.moveTo(x, y);
				mDrawPath.paint = new Paint(mPaint);
				mDrawPath.path	= mPath;
				posX = x;
				posY = y;
				postInvalidate();
				
				break;
			case MotionEvent.ACTION_MOVE:
				float dx = Math.abs(x - posX);
				float dy = Math.abs(y - posY);
				if(dx >= TOUCH_TOLERANCE || dy > TOUCH_TOLERANCE)
				{
					mPath.quadTo(posX, posY, (x + posX)/2, (y + posY)/2);
					posX = x;
					posY = y;
				}
				postInvalidate();
				break;
			case MotionEvent.ACTION_UP:
				mPath.lineTo(posX, posY);
				mPath.offset(0, -mBottomBitmapDrawHeight);
				// avoid the previous line is cleared when press again
				mCanvas.drawPath(mPath,mPaint); 
				mSavePath.add(mDrawPath);
				mPath = null;
				postInvalidate();
				break;
		}
		return true;
	}
	
	public boolean setBitmap(String imagePath)
	{
		Log.e("wxp","setBitmap()");
		Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float nxScale = -1;
		float nyScale = -1;
		if( width!=0 && height!=0)
		{
			nxScale = (float)width/mImageWidth;	
			nyScale = (float)height/mImageHeight;
			if (nxScale>=1 && nyScale >=1 || nxScale<1 && nyScale<1)
			{
				if(nxScale > nyScale)
				{
					width = (int)(width/nxScale);
					height = (int)(height/nxScale);
				}
				else
				{
					width = (int)(width/nyScale);
					height = (int)(height/nyScale);
				}
				
			}
			if (nxScale >=1 && nyScale <1)
			{
				width = mImageWidth;
			}
			if(nxScale <=1 && nyScale >=1)
			{
				height = mImageHeight;
			}
			mBitmap = Bitmap.createScaledBitmap(bitmap,width,height,true);
			mBottomBitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);		
			mSavePath.clear();
			mDeletePath.clear();
			mCanvas.setBitmap(mBottomBitmap);
			mCanvas.drawBitmap(mBitmap,0,0,mBitmapPaint);
			postInvalidate();
			
			return true;
		}
		else 
			return false;
		
	}
	
	public void setBitmapColor(int color)
	{
		mBottomBitmap.eraseColor(color);
		mSavePath.clear();
		mDeletePath.clear();
		postInvalidate();
	}
	
	public void setPaint(Paint paint)
	{
		mPaint = paint;
		postInvalidate();
	}
	
	public void savePicture() {
		String filePath = CommonUtils.getSavePath();
		BitmapUtil.writeFile(BitmapUtil.bitmapToBytes(mBottomBitmap), filePath
				+ "/hello.png");
	}
	
	public void clearImage()
	{
		mSavePath.clear();
		mDeletePath.clear();
		
		if(mBitmap != null)
		{
			int width = mBitmap.getWidth();
			int height = mBitmap.getHeight();
			mBottomBitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
			mCanvas.setBitmap(mBottomBitmap);
			mCanvas.drawBitmap(mBitmap, 0,0, mBitmapPaint);
		}
		else
		{
			int width = mCanvas.getWidth();
			int height = mCanvas.getHeight();
			mBottomBitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
			mCanvas.setBitmap(mBottomBitmap);
			
		}
		postInvalidate();
		
	}
	
	public void undo()
	{
		int nSize = mSavePath.size();
		if (nSize >= 1)
		{
			mDeletePath.add(0, mSavePath.get(nSize-1) );
			mSavePath.remove(nSize -1);
		}
		else
			return;
				
		if(mBitmap != null)
		{
			int width = mBitmap.getWidth();
			int height = mBitmap.getHeight();
			mBottomBitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
			mCanvas.setBitmap(mBottomBitmap);
			mCanvas.drawBitmap(mBitmap, 0,0, mBitmapPaint);
		}
		else
		{
			int width = mCanvas.getWidth();
			int height = mCanvas.getHeight();
			mBottomBitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
			mCanvas.setBitmap(mBottomBitmap);
		}
		
		Iterator<DrawPath> iter = mSavePath.iterator();
		DrawPath temp;
		while(iter.hasNext())
		{
			temp = iter.next();
			mCanvas.drawPath(temp.path, temp.paint);
		}
		postInvalidate();
		
	}
	
	public void redo()
	{
		int nSize = mDeletePath.size();
		if (nSize >= 1)
		{
			mSavePath.add( mDeletePath.get(0) );
			mDeletePath.remove(0);
		}
		else
			return;
		
		if(mBitmap != null)
		{
			int width = mBitmap.getWidth();
			int height = mBitmap.getHeight();
			mBottomBitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
			mCanvas.setBitmap(mBottomBitmap);
			mCanvas.drawBitmap(mBitmap, 0,0, mBitmapPaint);
		}
		else
		{
			int width = mCanvas.getWidth();
			int height = mCanvas.getHeight();
			mBottomBitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
			mCanvas.setBitmap(mBottomBitmap);
		}
		Iterator<DrawPath> iter = mSavePath.iterator();
		DrawPath temp;
		while(iter.hasNext())
		{
			temp = iter.next();
			mCanvas.drawPath(temp.path, temp.paint);
		}
		postInvalidate();
	}
}
