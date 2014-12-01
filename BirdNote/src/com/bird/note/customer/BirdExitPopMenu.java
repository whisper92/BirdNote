package com.bird.note.customer;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.bird.note.R;
import com.bird.note.model.BirdPopMenuItem;

/**
 * 编辑笔记界面的菜单
 * 
 * @author wangxianpeng
 * 
 */
public class BirdExitPopMenu extends PopupWindow {

	private LayoutInflater inflater;
	private View rootView;
	public List<BirdPopMenuItem> menuItems;
	private Button mCancelButton;
	private Button mConfirmButton;

	public BirdExitPopMenu(Context context, OnClickListener listener) {
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		rootView = inflater.inflate(R.layout.bird_exit_pop_menu, null);

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

		mCancelButton = (Button) rootView.findViewById(R.id.id_exit_cancel);
		mConfirmButton = (Button) rootView.findViewById(R.id.id_exit_confirm);

		mCancelButton.setOnClickListener(listener);
		mConfirmButton.setOnClickListener(listener);

	}

	public OnClickListener DismissListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			dismiss();
		}
	};
}
