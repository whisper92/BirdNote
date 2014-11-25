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
	public static BirdPopMenu createMenu (Context context,int itemStringsResource,int bgsResource,android.view.View.OnClickListener sortByListener){
		String[]  itemStrings= context.getResources().getStringArray(itemStringsResource);
		List<BirdPopMenuItem> mSortMenuItems = new ArrayList<BirdPopMenuItem>();
		int[] bgs=null;
		if (bgsResource == -1) {
			bgs = new int[itemStrings.length];
			for (int i = 0; i < itemStrings.length; i++) {
				bgs[i]= R.drawable.pop_menu_item;
			}
		} else {
			bgs = context.getResources().getIntArray(bgsResource);
		}
		BirdPopMenu mPopMenuSort = new BirdPopMenu(context);
		for (int i = 0; i < itemStrings.length; i++) {
			BirdPopMenuItem birdMenuItem = new BirdPopMenuItem();
			birdMenuItem.menuText = itemStrings[i];
			birdMenuItem.menuBackground = bgs[i];
			mSortMenuItems.add(birdMenuItem);
		}
		mPopMenuSort.setItemAdapter(mSortMenuItems, sortByListener);
		return mPopMenuSort;
	}
	
	
	/**
	 *  创建排序类型菜单
	 * @param context
	 * @param sortByListener
	 * @return
	 */
	public static BirdPopMenu createSortMenu (Context context,android.view.View.OnClickListener sortByListener){
		BirdPopMenu mPopMenuSort =  createMenu(context,R.array.sortby_array,-1,sortByListener);
		return mPopMenuSort;
	}
	
	/**
	 * 创建首页菜单
	 * @param context
	 * @param showMenuListener
	 * @return
	 */
	public static BirdPopMenu createShowMenu(Context context,android.view.View.OnClickListener showMenuListener){
		BirdPopMenu mShowPopMenu =  createMenu(context,R.array.showmenu_array,-1,showMenuListener);
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
		BirdPopMenu mShowPopMenu =  createMenu(context,R.array.itemmenu_array,-1,showMenuListener);
		mShowPopMenu.addCancelItem();
		return mShowPopMenu;
	}
	
	/**
	 * 创建编辑界面菜单
	 * @param context
	 * @param showMenuListener
	 * @return
	 */
	public static BirdPopMenu createEditNewNoteMenu(Context context,android.view.View.OnClickListener showMenuListener){
		BirdPopMenu mShowPopMenu =  createMenu(context,R.array.editnewnote_array,-1,showMenuListener);
		mShowPopMenu.addCancelItem();
		return mShowPopMenu;
	}

	public static BirdPopMenu createEditUpdateNoteMenu(Context context,android.view.View.OnClickListener showMenuListener){
		BirdPopMenu mShowPopMenu =  createMenu(context,R.array.editupdatenote_array,-1,showMenuListener);
		mShowPopMenu.addCancelItem();
		return mShowPopMenu;
	}
	
	
	/**
	 * 创建标签颜色菜单
	 */
	public static BirdPopMenu createChooseMarkColorMenu(Context context,android.view.View.OnClickListener showMenuListener){
			BirdPopMenu mMkColorPopMenu =  createMenu(context,R.array.editupdatenote_array,R.array.pop_menu_markcolor,showMenuListener);
			
		 return mMkColorPopMenu;
	}
	
	
	
	
	
	
}
