package com.bird.note.ui;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;

import com.bird.note.R;
import com.bird.note.customer.BirdPopMenu;
import com.bird.note.customer.ChooseEditBgPopMenu;
import com.bird.note.model.BirdPopMenuItem;
import com.bird.note.utils.PreferenceUtil;

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
	
	public static BirdPopMenu createEditUpdateNoteRmStarMenu(Context context,android.view.View.OnClickListener showMenuListener){
		BirdPopMenu mShowPopMenu =  createMenu(context,R.array.editupdatenote_rmstar_array,-1,showMenuListener);
		mShowPopMenu.addCancelItem();
		return mShowPopMenu;
	}
	
	/**
	 * 创建标签颜色菜单
	 */
	public static BirdPopMenu createChooseMarkColorMenu(Context context,android.view.View.OnClickListener showMenuListener){

		    int[]  itembgs= new int[]{R.drawable.mark_bg_blue,R.drawable.mark_bg_green,R.drawable.mark_bg_yellow,R.drawable.mark_bg_red};
			List<BirdPopMenuItem> mSortMenuItems = new ArrayList<BirdPopMenuItem>();
			BirdPopMenu mMkColorPopMenu = new BirdPopMenu(context);
			for (int i = 0; i < itembgs.length; i++) {
				BirdPopMenuItem birdMenuItem = new BirdPopMenuItem();
				birdMenuItem.menuBackground = itembgs[i];
				mSortMenuItems.add(birdMenuItem);
			}
			mMkColorPopMenu.setItemAdapter(mSortMenuItems, showMenuListener);
	
		 return mMkColorPopMenu;
	}
	
	/**
	 * 创建标签颜色菜单
	 */
	public static ChooseEditBgPopMenu createChooseEditBgMenu(Context context){
			ChooseEditBgPopMenu mMkColorPopMenu = new ChooseEditBgPopMenu(context);
		 return mMkColorPopMenu;
	}
	
	
	public static AlertDialog createMarkChooseAlertDialog(Context context,int titleSrc,OnClickListener listener){
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		AlertDialog dialog = builder.create();
		View view = inflater.inflate(R.layout.show_notes_menu_markcolor,null);
		dialog.setView(view);
		dialog.setTitle(context.getString(titleSrc));
		view.setOnClickListener(listener);
		return dialog;
	}
	
	public static AlertDialog createSortChooseAlertDialog(Context context,int titleSrc,android.content.DialogInterface.OnClickListener listener){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		AlertDialog dialog = builder.setTitle(context.getString(titleSrc)).setSingleChoiceItems(
				context.getResources().getStringArray(R.array.sortby_array), PreferenceUtil.getSortBy(), listener).setNegativeButton(
						context.getString(R.string.show_menu_confirm), listener).show();
		return dialog;
	}
	
	public static AlertDialog createDeleteAlertDialog(Context context,int titleSrc,android.content.DialogInterface.OnClickListener listener){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		AlertDialog dialog = builder.setTitle(context.getString(titleSrc)).setPositiveButton(context.getString(R.string.show_menu_confirm), listener).setNegativeButton(
				context.getString(R.string.show_menu_cancel), listener).show();
		return dialog;
	}
	
	public static AlertDialog.Builder createSaveNewNoteAlertDialog(Context context,int titleSrc,EditText editText ,android.content.DialogInterface.OnClickListener listener){
		AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle(context.getString(titleSrc)).setIcon(android.R.drawable.ic_dialog_info).setPositiveButton(context.getString(R.string.show_menu_confirm), listener).setNegativeButton(context.getString(R.string.show_menu_cancel), listener);
		return builder;
	}
	
	
	public static AlertDialog createItemOperationDialog(Context context,int titleSrc,android.content.DialogInterface.OnClickListener listener){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		AlertDialog dialog = builder.setTitle(context.getString(titleSrc)).setItems(
				context.getResources().getStringArray(R.array.itemmenu_array),  listener).show();
		return dialog;
	}
	
	public static AlertDialog createExitAlertDialog(Context context,int titleSrc,android.content.DialogInterface.OnClickListener listener){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		AlertDialog dialog = builder.setTitle(context.getString(titleSrc)).setPositiveButton(context.getString(R.string.exit_with_save), listener).setNegativeButton(
				context.getString(R.string.exit_without_save), listener).show();
		return dialog;
	}
	
}
