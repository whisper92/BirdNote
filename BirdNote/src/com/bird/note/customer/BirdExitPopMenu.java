package com.bird.note.customer;

import java.util.ArrayList;
import java.util.List;

import com.bird.note.R;
import com.bird.note.model.BirdPopMenuItem;
import com.bird.note.model.DBUG;
import com.bird.note.utils.CommonUtils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
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
public class BirdExitPopMenu extends PopupWindow {

	LayoutInflater inflater;
	View rootView;
    Context mContext;
	LinearLayout mItemsLayout;
    public List<BirdPopMenuItem> menuItems;
    Button mCancelButton;
    Button mConfirmButton;
	public BirdExitPopMenu(Context context,OnClickListener listener) {
		         mContext = context;
                 inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                 rootView=inflater.inflate(R.layout.bird_exit_pop_menu, null);
                 mItemsLayout = (LinearLayout) rootView.findViewById(R.id.id_popmenu_items);
                 
                 this.setContentView(rootView);
                 this.setWidth(LayoutParams.MATCH_PARENT);
                 this.setHeight(LayoutParams.MATCH_PARENT);
                 this.setAnimationStyle(R.style.popmenuanim);

                 this.setOutsideTouchable(false);
                 this.setBackgroundDrawable(new BitmapDrawable());

                 LinearLayout linearLayout =(LinearLayout) rootView.findViewById(R.id.id_popmenu_root);
                 linearLayout.setOnClickListener(DismissListener );
        		
        		rootView.setFocusableInTouchMode(true);
        		
        		mCancelButton = (Button)rootView.findViewById(R.id.id_exit_cancel);
        		mConfirmButton = (Button) rootView.findViewById(R.id.id_exit_confirm);
        		
        		mCancelButton.setOnClickListener(listener);
        		mConfirmButton.setOnClickListener(listener);

	}

	public OnClickListener DismissListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
		    dismiss();	
		}
	};
}
