package com.bird.note.customer;

import com.bird.note.R;
import com.bird.note.model.DBUG;
import com.bird.note.utils.CommonUtils;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.view.MotionEvent;
/**
 * 编辑笔记界面的菜单
 * @author wangxianpeng
 *
 */
public class PopSortBy extends PopupWindow {

	private LayoutInflater inflater;
	private View rootView;
	private Button mBlue;
	private Button mGreen;
	private Button mYellow;
	private Button mRed;


	public PopSortBy(Context context,OnClickListener listener) {

                 inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                 rootView=inflater.inflate(R.layout.show_note_pop_mark_color, null);
                 this.setContentView(rootView);
                 this.setWidth(LayoutParams.MATCH_PARENT);
                 this.setHeight(LayoutParams.MATCH_PARENT);
                 this.setAnimationStyle(R.style.popmenuanim);

                 this.setOutsideTouchable(false);
                 this.setBackgroundDrawable(new BitmapDrawable());

                 LinearLayout linearLayout =(LinearLayout) rootView.findViewById(R.id.id_popmenu_root);
                 linearLayout.setOnClickListener(DismissListener );
        		
        		rootView.setFocusableInTouchMode(true);
        		
        		mBlue=(Button) rootView.findViewById(R.id.id_pop_mark_bg_blue);
        		mGreen =(Button) rootView.findViewById(R.id.id_pop_mark_bg_green);
        		mYellow =(Button) rootView.findViewById(R.id.id_pop_mark_bg_yellow);
        		mRed =(Button) rootView.findViewById(R.id.id_pop_mark_bg_red);
               
                 mBlue.setOnClickListener(listener);               
                 mGreen.setOnClickListener(listener);
                 mYellow.setOnClickListener(listener);
                 mRed.setOnClickListener(listener);

	}
	public OnClickListener DismissListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
		    dismiss();	
		}
	};
}
