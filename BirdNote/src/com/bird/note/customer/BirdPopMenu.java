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
public class BirdPopMenu extends PopupWindow {

	LayoutInflater inflater;
	View rootView;
    Context mContext;
	LinearLayout mItemsLayout;
    List<BirdPopMenuItem> menuItems;
    
	public BirdPopMenu(Context context) {
		         mContext = context;
                 inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                 rootView=inflater.inflate(R.layout.bird_pop_menu, null);
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

	}
	
	public void addCancelItem(){
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,    
				LinearLayout.LayoutParams.WRAP_CONTENT    
				);
		layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        layoutParams.bottomMargin = CommonUtils.dpToPx(mContext,5);
        int padding = CommonUtils.dpToPx(mContext,10);
		Button button = new Button(mContext);
		button.setTextColor(Color.WHITE);
		button.setText(mContext.getString(R.string.show_menu_cancel));
		button.setBackgroundResource(R.drawable.menu_cancle_bg);
		button.setLayoutParams(layoutParams);
		button.setPadding(padding, padding, padding, padding);
		button.setOnClickListener(DismissListener);
		mItemsLayout.addView(button);
	}
	
	public void setItemAdapter(List<BirdPopMenuItem> menuItems,OnClickListener listener){
		this.menuItems = menuItems;

		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,    
				LinearLayout.LayoutParams.WRAP_CONTENT    
				);
		layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        layoutParams.bottomMargin = CommonUtils.dpToPx(mContext,5);
        int padding = CommonUtils.dpToPx(mContext,10);
		for (int i = 0; i < menuItems.size(); i++) {
			Button button = new Button(mContext);
			button.setText(menuItems.get(i).menuText);
			button.setBackgroundResource(menuItems.get(i).menuBackground);
			button.setId(i);
			button.setTextColor(Color.WHITE);
			button.setLayoutParams(layoutParams);
			button.setPadding(padding, padding, padding, padding);
			button.setOnClickListener(listener);
			mItemsLayout.addView(button);
		}
	}


	
	public OnClickListener DismissListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
		    dismiss();	
		}
	};
}
