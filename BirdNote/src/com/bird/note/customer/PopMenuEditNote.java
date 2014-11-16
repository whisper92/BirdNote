package com.bird.note.customer;

import com.bird.note.R;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

/**
 * 编辑笔记界面的菜单
 * @author wangxianpeng
 *
 */
public class PopMenuEditNote extends PopupWindow {

	LayoutInflater inflater;
	View rootView;
	Button mCancleBtn;
	Button mDeleteBtn;
	Button mStarBtn;
	Button mChangeBgBtn;
	Button mSaveAsBtn;
	public PopMenuEditNote(Context context,OnClickListener listener) {
            
                 inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                 rootView=inflater.inflate(R.layout.edit_note_popmenu, null);
                 this.setContentView(rootView);
                 this.setWidth(LayoutParams.MATCH_PARENT);
                 this.setHeight(LayoutParams.WRAP_CONTENT);
                 this.setAnimationStyle(R.style.popmenuanim);
                 
         		this.setFocusable(true); 
        		this.setOutsideTouchable(true); 
        		this.update(); 
        		this.setBackgroundDrawable(new BitmapDrawable()); 
        		
                 mCancleBtn=(Button) rootView.findViewById(R.id.id_popmenu_cancle);
                 mDeleteBtn =(Button) rootView.findViewById(R.id.id_popmenu_delete);
                 mStarBtn =(Button) rootView.findViewById(R.id.id_popmenu_star);
                 mChangeBgBtn =(Button) rootView.findViewById(R.id.id_popmenu_change_bg);
                 mSaveAsBtn =(Button) rootView.findViewById(R.id.id_popmenu_saveas);
                 
                 mCancleBtn.setOnClickListener(new OnClickListener() {				
					@Override
					public void onClick(View v) {
						dismiss();				
					}
				});
                 
                mDeleteBtn.setOnClickListener(listener);
                mStarBtn.setOnClickListener(listener);
                mChangeBgBtn.setOnClickListener(listener);
                mSaveAsBtn.setOnClickListener(listener);
                 

	}
}
