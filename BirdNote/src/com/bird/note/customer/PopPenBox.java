package com.bird.note.customer;

import com.bird.note.R;
import com.bird.note.model.DBUG;

import android.content.Context;
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
 * @author wangxianpeng
 *
 */
public class PopPenBox extends PopupWindow {

	LayoutInflater inflater;
	View rootView;
	public PopPenBox(Context context,OnClickListener po) {
                 inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                 rootView=inflater.inflate(R.layout.edit_note_choose_pen, null);
                 this.setContentView(rootView);
                 this.setWidth(LayoutParams.WRAP_CONTENT);
                 this.setHeight(LayoutParams.WRAP_CONTENT);


               //  ImageView img=(ImageView)rootView.findViewById(R.id.id_pop_pen);
                // img.setOnClickListener(po);
                 //SeekBar penSize=(SeekBar)rootView.findViewById(R.id.id_pop_pen_size);
                /* penSize.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
					
					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						//Toast.makeText(context, seekBar.getMax(), duration).show();
						DBUG.e("这么长"+seekBar.getMax()+"选了这么多"+seekBar.getProgress());
						
					}
					
					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onProgressChanged(SeekBar seekBar, int progress,
							boolean fromUser) {
						// TODO Auto-generated method stub
						
					}
				});*/
	}
}
