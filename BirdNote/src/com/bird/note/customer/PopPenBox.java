package com.bird.note.customer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bird.note.R;
import com.bird.note.model.DBUG;
import com.bird.note.model.DrawPaint;
import com.bird.note.model.SavedPaint;

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
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

/**
 * 选择画笔颜色和粗细的弹出框
 * 
 * @author wangxianpeng
 * 
 */
public class PopPenBox extends PopupWindow implements OnClickListener{

	LayoutInflater inflater;
	View rootView;
	float MAX = 100;
	int selectProcess = 0;
    private int mSelectPaintColor = SavedPaint.DEFAULT_PAINT_COLOR;
    private float mSelectPaintWidth = SavedPaint.DEFAULT_PAINT_WIDTH;
	public ColorCircle mColorCircle;
    public ColorLine mColorLine;

	public Map<Integer, Integer> mColorsMap=new HashMap<Integer, Integer>();
	
	private Paint mChoosePaint;
	private SavedPaint mSavedPaint;
    public void initColor(){
    	for (int i = 0; i < SavedPaint.mColorImages.length; i++) {
    		mColorsMap.put(SavedPaint.mColorImages[i], SavedPaint.mColors[i]);
		}
    }

	public PopPenBox(Context context) {
		initColor();
		mChoosePaint=DrawPaint.getInstance();
		mSavedPaint = new SavedPaint(context);
		mSelectPaintColor = mSavedPaint.getSavedPaintColor();
		mSelectPaintWidth = mSavedPaint.getSavedPaintWidth();
		
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		rootView = inflater.inflate(R.layout.edit_note_choose_pen, null);
		this.setContentView(rootView);
		this.setWidth(LayoutParams.WRAP_CONTENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setOutsideTouchable(true);
		this.setBackgroundDrawable(new BitmapDrawable());
		
		SeekBar penSize = (SeekBar) rootView.findViewById(R.id.id_choose_pen_seekbar);
		penSize.setProgress((int) (MAX*mSelectPaintWidth/SavedPaint.DEFAULT_PAINT_MAX_WIDTH));
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
				onPaintChangedListener.changePaint(getChoosePaint());
			}
		});
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
				onPaintChangedListener.changePaint(getChoosePaint());
			} else {
				((ImageView)rootView.findViewById(SavedPaint.mColorImages[i])).setBackgroundDrawable(null);
			}
		}
	}
}
