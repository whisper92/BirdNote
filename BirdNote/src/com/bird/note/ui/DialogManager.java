package com.bird.note.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.EditText;

public class DialogManager {
	public static void createTitleInputDialog(Context context,EditText editText){
		AlertDialog.Builder alertDialog=new AlertDialog.Builder(context);
		alertDialog.setTitle("输入标题").setView(editText).setPositiveButton("确认", new OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				
			}
		}).setNegativeButton("取消", new OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {			
				
			}
		}).create().show();
	}
}
