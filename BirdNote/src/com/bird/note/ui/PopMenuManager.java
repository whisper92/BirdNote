package com.bird.note.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.EditText;

import com.bird.note.R;
import com.bird.note.utils.PreferenceUtil;

/**
 * @author wangxianpeng
 * @since 19/12/14
 *
 */
public class PopMenuManager {

	public static AlertDialog createSortChooseAlertDialog( Context context, int titleSrc, android.content.DialogInterface.OnClickListener listener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		AlertDialog dialog = builder.setTitle(
		        context.getString(titleSrc)).setSingleChoiceItems(context.getResources()
				.getStringArray(R.array.sortby_array), PreferenceUtil.getSortBy(), listener)
				.setPositiveButton(context.getString(R.string.show_menu_confirm), listener).show();
		return dialog;
	}

	public static AlertDialog createDeleteAlertDialog( Context context, int titleSrc, android.content.DialogInterface.OnClickListener listener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		AlertDialog dialog = builder.setTitle(context.getString(titleSrc))
				.setPositiveButton(context.getString(R.string.show_menu_confirm), listener)
				.setNegativeButton(context.getString(R.string.show_menu_cancel), listener)
				.show();
		return dialog;
	}

	public static AlertDialog.Builder createSaveNewNoteAlertDialog( Context context, int titleSrc, EditText editText, android.content.DialogInterface.OnClickListener listener) {
    AlertDialog.Builder builder = new AlertDialog.Builder(context)
				.setTitle(context.getString(titleSrc))
				.setIcon(android.R.drawable.ic_dialog_info)
				.setPositiveButton(context.getString(R.string.show_menu_confirm), listener)
				.setNegativeButton(context.getString(R.string.show_menu_cancel), listener);
		return builder;
	}

	public static AlertDialog.Builder createChooseMarkAlertDialog( Context context, int titleSrc, android.content.DialogInterface.OnClickListener listener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context)
				.setTitle(context.getString(titleSrc))
				.setIcon(android.R.drawable.ic_dialog_info)
				.setPositiveButton(context.getString(R.string.show_menu_confirm), listener)
				.setNegativeButton(context.getString(R.string.show_menu_cancel), listener);
		return builder;
	}

	public static AlertDialog createItemOperationDialog(Context context, int titleSrc, android.content.DialogInterface.OnClickListener listener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		AlertDialog dialog = builder
				.setTitle(context.getString(titleSrc))
				.setItems(context.getResources().getStringArray(R.array.itemmenu_array), listener).show();
		return dialog;
	}

	public static AlertDialog createExitAlertDialog(Context context, int titleSrc, android.content.DialogInterface.OnClickListener listener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		AlertDialog dialog = builder
				.setTitle(context.getString(titleSrc))
				.setPositiveButton(context.getString(R.string.exit_with_save), listener)
				.setNegativeButton(context.getString(R.string.exit_without_save), listener)
				.show();
		return dialog;
	}

}
