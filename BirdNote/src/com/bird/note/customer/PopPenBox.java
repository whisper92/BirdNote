package com.bird.note.customer;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.bird.note.R;
import com.bird.note.model.DrawPaint;
import com.bird.note.model.SavedPaint;

/**
 * 选择画笔颜色和粗细的弹出框
 * 
 * @author wangxianpeng
 * 
 */
public class PopPenBox extends PopupWindow implements OnClickListener{

	private LayoutInflater inflater;
	private View rootView;
	private float MAX = 100;
	private int selectProcess = 0;
    private int mSelectPaintColor = SavedPaint.DEFAULT_PAINT_COLOR;
    private float mSelectPaintWidth = SavedPaint.DEFAULT_PAINT_WIDTH;
	public ColorCircle mColorCircle;
    public ColorLine mColorLine;
    private Context mContext; 
	public Map<Integer, Integer> mColorsMap=new HashMap<Integer, Integer>();
	public Map<Integer, Integer> mProgressDrawables=new HashMap<Integer, Integer>();
	private Paint mChoosePaint;
	private SavedPaint mSavedPaint;
	private SeekBar penSize;
    public void initColor(){
    	for (int i = 0; i < SavedPaint.mColorImages.length; i++) {
    		mColorsMap.put(SavedPaint.mColorImages[i], SavedPaint.mColors[i]);
		}
    	
		for (int i = 0; i < SavedPaint.mColors.length; i++) {
			mProgressDrawables.put(SavedPaint.mColors[i], SavedPaint.mProgressDrawable[i]);
		}
		
    }

	public PopPenBox(Context context) {
		initColor();
		mContext = context;
		mChoosePaint=DrawPaint.getInstance();
		mSavedPaint = new SavedPaint(context);
		mSelectPaintColor = mSavedPaint.getSavedPaintColor();
		mSelectPaintWidth = mSavedPaint.getSavedPaintWidth();
		
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		rootView = inflater.inflate(R.layout.pen_box_choose_pen, null);
		this.setContentView(rootView);
		this.setWidth(LayoutParams.WRAP_CONTENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);

		this.setFocusable(true); 
		this.setOutsideTouchable(true); 
		this.update(); 
		this.setBackgroundDrawable(new BitmapDrawable()); 
		
		
		 penSize = (SeekBar) rootView.findViewById(R.id.id_choose_pen_seekbar);
		penSize.setProgress((int) (MAX*mSelectPaintWidth/SavedPaint.DEFAULT_PAINT_MAX_WIDTH));
		penSize.setProgressDrawable(context.getResources().getDrawable(getProgressDrawableByColorId(mSelectPaintColor)));
		
		mColorCircle = (ColorCircle) rootView.findViewById(R.id.id_choose_pen_circle);
		mColorLine=(ColorLine) rootView.findViewById(R.id.id_choose_pen_line);
		for (int i = 0; i < SavedPaint.mColorImages.length; i++) {
			ImageView imageView=(ImageView)rootView.findViewById(SavedPaint.mColorImages[i]);
			imageView.setOnClickListener(this);
			if (mColorsMap.get(imageView.getId()) == mSelectPaintColor) {
				imageView.setBackgroundResource(R.drawable.tool_color_sel);
			}
		}
		
		penSize.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				selectProcess = seekBar.getProgress();
				mSavedPaint.savePaintWidth(SavedPaint.DEFAULT_PAINT_MAX_WIDTH*selectProcess/MAX);
				onPaintChangedListener.changePaint(getChoosePaint());
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				mSelectPaintWidth = progress / MAX * SavedPaint.DEFAULT_PAINT_MAX_WIDTH;
				mColorCircle.setPaintWidth(mSelectPaintWidth);
				mColorLine.setPaintWidth(mSelectPaintWidth);			
			}
		});
	}
	

	public int getProgressDrawableByColorId(int color){
		return mProgressDrawables.get(color);
	}
	
	OnPaintChangedListener onPaintChangedListener;
	public void setOnPaintChangedListener(OnPaintChangedListener listener){
		onPaintChangedListener=listener;
	}
	public interface OnPaintChangedListener{
		public void changePaint(Paint paint);
	}
	public Paint getChoosePaint(){
		mChoosePaint.setStrokeWidth(mSelectPaintWidth);
		mChoosePaint.setColor(mSelectPaintColor);
		return mChoosePaint;
	}
	
	@Override
	public void onClick(View v) {
		for (int i = 0; i <SavedPaint. mColorImages.length; i++) {
			if (SavedPaint.mColorImages[i] == v.getId()) {
				mSelectPaintColor=mColorsMap.get(v.getId());
				mColorCircle.setPaintColor(mSelectPaintColor);
				mColorLine.setPaintColor(mSelectPaintColor);
				((ImageView)v).setBackgroundResource(R.drawable.tool_color_sel);
				penSize.setProgressDrawable(mContext.getResources().getDrawable(getProgressDrawableByColorId(mSelectPaintColor)));
				onPaintChangedListener.changePaint(getChoosePaint());
			} else {
				((ImageView)rootView.findViewById(SavedPaint.mColorImages[i])).setBackgroundDrawable(null);
			}
		}
		//dismiss();
	}
}
