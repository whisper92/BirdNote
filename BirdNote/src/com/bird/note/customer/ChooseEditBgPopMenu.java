package com.bird.note.customer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;

import com.bird.note.R;
import com.bird.note.model.BirdPopMenuItem;
import com.bird.note.utils.BitmapUtil;

/**
 * 编辑笔记界面的菜单
 * 
 * @author wangxianpeng
 * 
 */
public class ChooseEditBgPopMenu extends PopupWindow implements
		OnItemClickListener {

	private LayoutInflater inflater;
	private View rootView;
	private Context mContext;
	public List<BirdPopMenuItem> menuItems;
	private GridView mBgGridView;

	public ChooseEditBgPopMenu(Context context) {
		mContext = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		rootView = inflater.inflate(R.layout.choos_editbg_pop_menu, null);
		mBgGridView = (GridView) rootView.findViewById(R.id.id_edit_choos_bg);

		this.setContentView(rootView);
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.MATCH_PARENT);
		this.setAnimationStyle(R.style.popmenuanim);

		this.setOutsideTouchable(false);
		this.setBackgroundDrawable(new BitmapDrawable());

		LinearLayout linearLayout = (LinearLayout) rootView
				.findViewById(R.id.id_popmenu_root);
		linearLayout.setOnClickListener(DismissListener);

		rootView.setFocusableInTouchMode(true);
		ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < BitmapUtil.EDIT_BGS.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ItemImage", BitmapUtil.EDIT_BGS[i]);
			map.put("ItemText", "NO." + String.valueOf(i));
			lstImageItem.add(map);
		}
		SimpleAdapter simpleAdapter = new SimpleAdapter(mContext, lstImageItem,
				R.layout.edit_bg_item, new String[] { "ItemImage" },
				new int[] { R.id.id_edit_bg_img });
		mBgGridView.setAdapter(simpleAdapter);
		mBgGridView.setOnItemClickListener(this);

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		mChangeBackgroundListener.changeBackground(BitmapUtil.EDIT_BGS[arg2]);
	}

	OnChangeBackgroundListener mChangeBackgroundListener;

	public interface OnChangeBackgroundListener {
		public void changeBackground(int bgRsr);
	}

	public void setOnChangeBackgroundListener(
			OnChangeBackgroundListener listener) {
		this.mChangeBackgroundListener = listener;
	}

	public OnClickListener DismissListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			dismiss();
		}
	};

}
