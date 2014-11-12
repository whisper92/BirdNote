package com.bird.note.customer;

import com.bird.note.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

/**
 * 编辑笔记界面的菜单
 * @author wangxianpeng
 *
 */
public class PopMenuEditNote extends PopupWindow {

	LayoutInflater inflater;
	View rootView;
	public PopMenuEditNote(Context context) {
                 inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                 rootView=inflater.inflate(R.layout.edit_note_popmenu, null);
                 this.setContentView(rootView);
                 //设置PopupWindow弹出窗体的宽
                 this.setWidth(LayoutParams.FILL_PARENT);
                 //设置PopupWindow弹出窗体的高
                 this.setHeight(LayoutParams.WRAP_CONTENT);
                 //设置PopupWindow弹出窗体可点击
                 this.setFocusable(true);
	}
}
