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
}
