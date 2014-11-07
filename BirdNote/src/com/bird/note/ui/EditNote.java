package com.bird.note.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.Toast;

import com.bird.note.R;
import com.bird.note.test.TestBackPenView;


public class EditNote extends Activity implements OnClickListener{
	private FullScreenEditText mEditText;
	private ImageView edit_Pen;
	private ImageView edit_Text;
	private ImageView edit_Clean;
	private ImageView menu_Undo;
	private ImageView menu_Redo;
	private ImageView menu_More;
	private ImageView menu_Save;

	private FrameLayout mWrapFrameLayout;
	private PenView mPenView;
	private int mType = 0;
	private boolean mFirstComeIn = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_note_main);
		mType = getIntent().getIntExtra("type", R.id.id_edit_title_pen);
		init(mType);
	}

	public void init(int type) {
		mWrapFrameLayout = (FrameLayout) findViewById(R.id.id_edit_main_fl_bg);
		mEditText = (FullScreenEditText) findViewById(R.id.id_edit_main_et);
		edit_Pen = (ImageView) findViewById(R.id.id_edit_title_pen);
		edit_Text = (ImageView) findViewById(R.id.id_edit_title_text);
		edit_Clean = (ImageView) findViewById(R.id.id_edit_title_clean);
		menu_Undo = (ImageView) findViewById(R.id.id_edit_title_pre);
		menu_Redo = (ImageView) findViewById(R.id.id_edit_title_next);
		menu_More = (ImageView) findViewById(R.id.id_edit_title_more);
		menu_Save = (ImageView) findViewById(R.id.id_edit_title_save);

		edit_Pen.setOnClickListener(this);
		edit_Text.setOnClickListener(this);
		edit_Clean.setOnClickListener(this);
		menu_Undo.setOnClickListener(this);
		menu_Redo.setOnClickListener(this);
		menu_More.setOnClickListener(this);
		menu_Save.setOnClickListener(this);

		mPenView = new PenView(this);
		mPenView.setLayoutParams(new FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		changeCurrentMode(type);

	}

	/**
	 * 设置当前的编辑模式
	 * 
	 * @param ClickID
	 */
	public void changeCurrentMode(int ClickID) {
		if (ClickID == R.id.id_edit_title_pen) {
			if (mFirstComeIn) {
				mWrapFrameLayout.addView(mPenView);
				mFirstComeIn = false;
			}
			edit_Pen.setSelected(true);
			edit_Text.setSelected(false);
			edit_Clean.setSelected(false);

			mPenView.bringToFront();
			mEditText.setCursorVisible(false);
			mPenView.initDrawPaint();
		}
		if (ClickID == R.id.id_edit_title_text) {
			edit_Text.setSelected(true);
			edit_Pen.setSelected(false);
			edit_Clean.setSelected(false);

			mEditText.bringToFront();
			mEditText.setCursorVisible(true);
		}
		if (ClickID == R.id.id_edit_title_clean) {
			if (mFirstComeIn) {
				mWrapFrameLayout.addView(mPenView);
				mFirstComeIn = false;
			}
			edit_Clean.setSelected(true);
			edit_Text.setSelected(false);
			edit_Pen.setSelected(false);

			mPenView.bringToFront();
			mEditText.setCursorVisible(false);

			mPenView.setCleanPaint();
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_edit_title_pre:
			mPenView.undo();
			break;
		case R.id.id_edit_title_next:
			mPenView.redo();
			break;
		case R.id.id_edit_title_more:
			Log.e("wxp", "text_lenth" + mEditText.getText().length());
			break;
		case R.id.id_edit_title_save:
			mPenView.savePicture();
			break;
		case R.id.id_edit_title_pen:
			changeCurrentMode(v.getId());
			break;
		case R.id.id_edit_title_text:
			changeCurrentMode(v.getId());
			break;
		case R.id.id_edit_title_clean:
			changeCurrentMode(v.getId());
			break;
		default:
			break;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Toast.makeText(EditNote.this, "确定返回", 500).show();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.id_edit_menu_save:
			
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
