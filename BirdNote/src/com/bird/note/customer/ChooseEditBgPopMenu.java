package com.bird.note.customer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.bird.note.R;
import com.bird.note.model.BirdPopMenuItem;
import com.bird.note.model.DBUG;
import com.bird.note.utils.BitmapUtil;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.view.MotionEvent;
/**
 * 编辑笔记界面的菜单
 * @author wangxianpeng
 *
 */
public class ChooseEditBgPopMenu extends PopupWindow {

	LayoutInflater inflater;
	View rootView;
    Context mContext;
	LinearLayout mItemsLayout;
    public List<BirdPopMenuItem> menuItems;
    GridView mBgGridView;
	public ChooseEditBgPopMenu(Context context) {
		         mContext = context;
                 inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                 rootView=inflater.inflate(R.layout.choos_editbg_pop_menu, null);
                 mItemsLayout = (LinearLayout) rootView.findViewById(R.id.id_popmenu_items);
                 mBgGridView = (GridView) rootView.findViewById(R.id.id_edit_choos_bg);
                 
                 this.setContentView(rootView);
                 this.setWidth(LayoutParams.MATCH_PARENT);
                 this.setHeight(LayoutParams.MATCH_PARENT);
                 this.setAnimationStyle(R.style.popmenuanim);

                 this.setOutsideTouchable(false);
                 this.setBackgroundDrawable(new BitmapDrawable());

                 LinearLayout linearLayout =(LinearLayout) rootView.findViewById(R.id.id_popmenu_root);
                 linearLayout.setOnClickListener(DismissListener );
        		
        		rootView.setFocusableInTouchMode(true);
        		 ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();  
        	      for(int i=0;i<BitmapUtil.EDIT_BGS.length;i++)  
        	      {  
        	        HashMap<String, Object> map = new HashMap<String, Object>();  
        	        map.put("ItemImage", BitmapUtil.EDIT_BGS[i]);//添加图像资源的ID  
        	        map.put("ItemText", "NO."+String.valueOf(i));//按序号做ItemText  
        	        lstImageItem.add(map);  
        	      }  
                 SimpleAdapter simpleAdapter = new SimpleAdapter(mContext, lstImageItem, R.layout.edit_bg_item, new String[]{"ItemImage"},new int[]{R.id.id_edit_bg_img});
        		mBgGridView.setAdapter(simpleAdapter);

	}
	
	
	public OnClickListener DismissListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
		    dismiss();	
		}
	};
}
