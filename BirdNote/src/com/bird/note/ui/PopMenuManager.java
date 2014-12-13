package com.bird.note.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.bird.note.R;
import com.bird.note.utils.PreferenceUtil;

public class PopMenuManager {

	
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
	
	public static AlertDialog.Builder createChooseMarkAlertDialog(Context context,int titleSrc){
		AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle(context.getString(titleSrc)).setIcon(android.R.drawable.ic_dialog_info);
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
