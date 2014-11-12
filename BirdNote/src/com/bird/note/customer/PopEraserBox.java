package com.bird.note.customer;

import com.bird.note.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

/**
 * 选择橡皮擦粗细的弹出框
 * @author wangxianpeng
 *
 */
public class PopEraserBox extends PopupWindow {

	LayoutInflater inflater;
	View rootView;
	public PopEraserBox(Context context) {
                 inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                 rootView=inflater.inflate(R.layout.edit_note_choose_earser, null);
                 this.setContentView(rootView);
                 this.setWidth(LayoutParams.WRAP_CONTENT);
                 this.setHeight(LayoutParams.WRAP_CONTENT);
	}
}
