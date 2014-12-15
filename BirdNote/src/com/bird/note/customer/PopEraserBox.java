package com.bird.note.customer;

import com.bird.note.R;
import com.bird.note.model.CleanPaint;
import com.bird.note.model.SavedPaint;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

/**
 * 选择橡皮擦粗细的弹出框
 * 
 * @author wangxianpeng
 * 
 */
public class PopEraserBox extends PopupWindow {

	private LayoutInflater inflater;
	private View rootView;
	private SeekBar mSeekBar;
	private TextView mCleanAll;
	private float MAX = 100;
	private int selectProcess = 0;
	private CleanCircle mCleanCircle;
	private float mSelectPaintWidth = SavedPaint.DEFAULT_PAINT_WIDTH;
	private SavedPaint mSavedPaint;
	private Paint mChoosePaint;

	public PopEraserBox(Context context, OnClickListener cleanAllListener) {
		mChoosePaint = CleanPaint.getInstance();
		mSavedPaint = new SavedPaint(context);
		mSelectPaintWidth = mSavedPaint.getSavedCleanPaintWidth();
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		rootView = inflater.inflate(R.layout.box_choose_earser, null);
		mSeekBar = (SeekBar) rootView.findViewById(R.id.id_choose_eraser_seekbar);
		mSeekBar.setProgress((int) (MAX * mSelectPaintWidth / SavedPaint.DEFAULT_PAINT_MAX_WIDTH));
		mSeekBar.setProgressDrawable(context.getResources().getDrawable(R.drawable.seekbar_drawable14));
		mCleanAll = (TextView) rootView.findViewById(R.id.id_choose_eraser_clean_all);
		mCleanAll.setOnClickListener(cleanAllListener);
		mCleanCircle = (CleanCircle) rootView.findViewById(R.id.id_choose_eraser_circle);
		this.setContentView(rootView);
		this.setWidth(LayoutParams.WRAP_CONTENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		this.update();
		this.setBackgroundDrawable(new BitmapDrawable());

		mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				selectProcess = seekBar.getProgress();
				mSavedPaint.saveCleanPaintWidth(SavedPaint.DEFAULT_PAINT_MAX_WIDTH* selectProcess / MAX);
				onPaintChangedListener.changePaint(getChoosePaint());
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
				mSelectPaintWidth = progress / MAX* SavedPaint.DEFAULT_PAINT_MAX_WIDTH;
				mCleanCircle.setCleanPaintWidth(mSelectPaintWidth);
			}
		});
	}

	OnEraserChangedListener onPaintChangedListener;

	public void setOnPaintChangedListener(OnEraserChangedListener listener) {
		onPaintChangedListener = listener;
	}

	public interface OnEraserChangedListener {
		public void changePaint(Paint paint);
	}

	public Paint getChoosePaint() {
		mChoosePaint.setStrokeWidth(mSelectPaintWidth);
		return mChoosePaint;
	}

}
