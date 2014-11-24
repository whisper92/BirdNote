package com.bird.note.customer;

import com.bird.note.R;
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

	private LayoutInflater inflater;
	private View rootView;
	private Button mBlue;
	private Button mGreen;
	private Button mYellow;
	private Button mRed;
    private Context mContext;
	private LinearLayout mItemsLayout;

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
	
	public void setItemAdapter(String[] items,OnClickListener listener){
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,    
				LinearLayout.LayoutParams.WRAP_CONTENT    
				);
		layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        layoutParams.bottomMargin = CommonUtils.dpToPx(mContext,5);
        int padding = CommonUtils.dpToPx(mContext,10);
		for (int i = 0; i < items.length; i++) {
			Button button = new Button(mContext);
			button.setText(items[i]);
			button.setId(i);
			button.setTextColor(Color.WHITE);
			button.setBackgroundResource(R.drawable.pop_menu_item);
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
