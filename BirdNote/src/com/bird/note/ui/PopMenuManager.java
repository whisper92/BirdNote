package com.bird.note.ui;

import java.util.ArrayList;
import java.util.List;

import com.bird.note.R;
import com.bird.note.customer.BirdPopMenu;
import com.bird.note.model.BirdPopMenuItem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.EditText;

public class PopMenuManager {

	/**
	 *  创建排序类型菜单
	 * @param context
	 * @param sortByListener
	 * @return
	 */
	public static BirdPopMenu createSortMenu (Context context,android.view.View.OnClickListener sortByListener){
		List<BirdPopMenuItem> mSortMenuItems = new ArrayList<BirdPopMenuItem>();
		BirdPopMenu mPopMenuSort = new BirdPopMenu(context);
		String[] mSortItemsStrings = context.getResources().getStringArray(R.array.sortby_array);
		
		for (int i = 0; i < mSortItemsStrings.length; i++) {
			BirdPopMenuItem birdMenuItem = new BirdPopMenuItem();
			birdMenuItem.menuText = mSortItemsStrings[i];
			birdMenuItem.menuBackground = R.drawable.pop_menu_item;
			mSortMenuItems.add(birdMenuItem);
		}
		mPopMenuSort.setItemAdapter(mSortMenuItems, sortByListener);
		return mPopMenuSort;
	}
	
	/**
	 * 创建首页菜单
	 * @param context
	 * @param showMenuListener
	 * @return
	 */
	public static BirdPopMenu createShowMenu(Context context,android.view.View.OnClickListener showMenuListener){
		List<BirdPopMenuItem> mBirdMenuItems;
		BirdPopMenu mShowPopMenu;
		mBirdMenuItems = new ArrayList<BirdPopMenuItem>();
		mShowPopMenu = new BirdPopMenu(context);
		String[] mShowMenuItemsStrings = context.getResources().getStringArray(R.array.showmenu_array);
		for (int i = 0; i < mShowMenuItemsStrings.length; i++) {
			BirdPopMenuItem birdMenuItem = new BirdPopMenuItem();
			birdMenuItem.menuText = mShowMenuItemsStrings[i];
			birdMenuItem.menuBackground = R.drawable.pop_menu_item;
			mBirdMenuItems.add(birdMenuItem);
		}
		mShowPopMenu.setItemAdapter(mBirdMenuItems, showMenuListener);
		mShowPopMenu.addCancelItem();
		return mShowPopMenu;
	}
	
	/**
	 * 长按某一项弹出菜单
	 * @param context
	 * @param showMenuListener
	 * @return
	 */
	public static BirdPopMenu createItemOperateMenu(Context context,android.view.View.OnClickListener showMenuListener){
		List<BirdPopMenuItem> mBirdMenuItems;
		BirdPopMenu mShowPopMenu;
		mBirdMenuItems = new ArrayList<BirdPopMenuItem>();
		mShowPopMenu = new BirdPopMenu(context);
		String[] mShowMenuItemsStrings = context.getResources().getStringArray(R.array.itemmenu_array);
		for (int i = 0; i < mShowMenuItemsStrings.length; i++) {
			BirdPopMenuItem birdMenuItem = new BirdPopMenuItem();
			birdMenuItem.menuText = mShowMenuItemsStrings[i];
			birdMenuItem.menuBackground = R.drawable.pop_menu_item;
			mBirdMenuItems.add(birdMenuItem);
		}
		mShowPopMenu.setItemAdapter(mBirdMenuItems, showMenuListener);
		mShowPopMenu.addCancelItem();
		return mShowPopMenu;
	}
	
	/**
	 * 创建标签颜色菜单
	 */
	public static BirdPopMenu createChooseMarkColorMenu(Context context,android.view.View.OnClickListener showMenuListener){

		    List<BirdPopMenuItem> mBirdMenuItems;
			BirdPopMenu mMkColorPopMenu;
			int[] markColors = new int[]{R.drawable.mark_bg_blue,R.drawable.mark_bg_green,R.drawable.mark_bg_yellow,R.drawable.mark_bg_red};
			mBirdMenuItems = new ArrayList<BirdPopMenuItem>();
			mMkColorPopMenu = new BirdPopMenu(context);
			for (int i = 0; i < markColors.length; i++) {
				BirdPopMenuItem birdMenuItem = new BirdPopMenuItem();
				birdMenuItem.menuBackground = markColors[i];
				mBirdMenuItems.add(birdMenuItem);
			}
			mMkColorPopMenu.setItemAdapter(mBirdMenuItems, showMenuListener);	
		 return mMkColorPopMenu;
	}
	
	
	
	
	
	
}
