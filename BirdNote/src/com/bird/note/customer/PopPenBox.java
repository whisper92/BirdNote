package com.bird.note.customer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bird.note.R;
import com.bird.note.model.DBUG;
import com.bird.note.model.DrawPaint;

import android.content.Context;
import android.graphics.Paint;
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
    private Integer mSelectPaintColor = 0xff000000;
    private float mSelectPaintWidth = 5;
	public ColorCircle mColorCircle;
    public ColorLine mColorLine;
    public static int[] mColorImages=new int[]{
    		R.id.id_color_00,R.id.id_color_01,R.id.id_color_02,R.id.id_color_03,R.id.id_color_04,R.id.id_color_05,R.id.id_color_06,R.id.id_color_07,
    		R.id.id_color_08,R.id.id_color_09,R.id.id_color_10,R.id.id_color_11,R.id.id_color_12,R.id.id_color_13,R.id.id_color_14,R.id.id_color_15};
    public static int[] mColors=new int[]{
    		0xff16cc79,0xff01932e,0xff04672e,0xffff82c9,0xffff365b,0xffff0000,0xffff6000,0xffa5a5a5,
    		0xff727272,0xff363636,0xff38a8fe,0xff3467fe,0xff005aff,0xfffcff0c,0xffffffff,0xff000000
    };
	public Map<Integer, Integer> mColorsMap=new HashMap<Integer, Integer>();
	private Paint choosePaint;
	
    public void initColor(){
    	for (int i = 0; i < mColorImages.length; i++) {
    		mColorsMap.put(mColorImages[i], mColors[i]);
		}
    }

	public PopPenBox(Context context) {
		initColor();
		choosePaint=DrawPaint.getInstance();
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		rootView = inflater.inflate(R.layout.edit_note_choose_pen, null);
		this.setContentView(rootView);
		this.setWidth(LayoutParams.WRAP_CONTENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		
		SeekBar penSize = (SeekBar) rootView.findViewById(R.id.id_choose_pen_seekbar);
		penSize.setProgress(25);
		mColorCircle = (ColorCircle) rootView.findViewById(R.id.id_choose_pen_circle);
		mColorLine=(ColorLine) rootView.findViewById(R.id.id_choose_pen_line);
		for (int i = 0; i < mColorImages.length; i++) {
			ImageView imageView=(ImageView)rootView.findViewById(mColorImages[i]);
			imageView.setOnClickListener(this);
		}
		
		penSize.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				selectProcess = seekBar.getProgress();
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				mSelectPaintWidth = progress / MAX * 20;
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
		choosePaint.setStrokeWidth(mSelectPaintWidth);
		choosePaint.setColor(mSelectPaintColor);
		return choosePaint;
	}
	
	@Override
	public void onClick(View v) {
		for (int i = 0; i < mColorImages.length; i++) {
			if (mColorImages[i] == v.getId()) {
				mSelectPaintColor=mColorsMap.get(v.getId());
				mColorCircle.setPaintColor(mSelectPaintColor);
				mColorLine.setPaintColor(mSelectPaintColor);
				((ImageView)v).setBackgroundResource(R.drawable.tool_color_sel);
				onPaintChangedListener.changePaint(getChoosePaint());
			} else {
				((ImageView)rootView.findViewById(mColorImages[i])).setBackgroundDrawable(null);
			}
		}
	}
}
