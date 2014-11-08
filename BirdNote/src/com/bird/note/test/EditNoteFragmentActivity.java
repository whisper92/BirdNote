package com.bird.note.test;

import android.R.integer;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.Toast;

import com.bird.note.R;
import com.bird.note.customer.FullScreenEditText;
import com.bird.note.customer.PenView;
import com.bird.note.customer.PenView.OnPathListChangeListener;
import com.bird.note.customer.QuadrantThumbnail;
import com.bird.note.customer.QuadrantThumbnail.OnQuadrantChangeListener;

public class EditNoteFragmentActivity extends FragmentActivity implements
		OnClickListener {
	private ImageView edit_Pen;
	private ImageView edit_Text;
	private ImageView edit_Clean;
	private ImageView menu_Undo;
	private ImageView menu_Redo;
	private ImageView menu_More;
	private ImageView menu_Save;

	private QuadrantThumbnail quadrantThumbnail;

	public int mType = 0;
	private boolean mFirstComeIn = true;
	EditFragment editFragment;

	private int mCurQua = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_note_main);
		mType = getIntent().getIntExtra("type", R.id.id_edit_title_pen);
		Log.e("wxp", "Activity.TYPE--->" + mType);
		editFragment = EditFragment.newInstance(mCurQua, mType);

		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.id_edit_main_editfragment, editFragment);
		transaction.commit();
		init(mType);
	}

	public void init(int type) {
		edit_Pen = (ImageView) findViewById(R.id.id_edit_title_pen);
		edit_Text = (ImageView) findViewById(R.id.id_edit_title_text);
		edit_Clean = (ImageView) findViewById(R.id.id_edit_title_clean);
		menu_Undo = (ImageView) findViewById(R.id.id_edit_title_pre);
		menu_Redo = (ImageView) findViewById(R.id.id_edit_title_next);
		menu_More = (ImageView) findViewById(R.id.id_edit_title_more);
		menu_Save = (ImageView) findViewById(R.id.id_edit_title_save);
		quadrantThumbnail = (QuadrantThumbnail) findViewById(R.id.id_edit_quathumb);
		quadrantThumbnail
				.setQuadrantChangeListener(new OnQuadrantChangeListener() {

					@Override
					public void changeQua(int qua) {
						// TODO Auto-generated method stub

					}
				});

		edit_Pen.setOnClickListener(this);
		edit_Text.setOnClickListener(this);
		edit_Clean.setOnClickListener(this);
		menu_Undo.setOnClickListener(this);
		menu_Redo.setOnClickListener(this);
		menu_More.setOnClickListener(this);
		menu_Save.setOnClickListener(this);

		menu_Undo.setEnabled(false);
		menu_Redo.setEnabled(false);

		changeOtherIconState(mType);

	}

	/**
	 * 保存和回复撤销和重做图标的状态
	 */
	public void changeStateOfUndoRedo(boolean undoState, boolean redoState) {
		menu_Undo.setEnabled(undoState);
		menu_Redo.setEnabled(redoState);
	}

	public void changeOtherIconState(int clickID) {
		Log.e("wxp","clickID"+clickID);
		switch (clickID) {
		case R.id.id_edit_title_pen:
			edit_Pen.setSelected(true);
			edit_Text.setSelected(false);
			edit_Clean.setSelected(false);
			menu_Undo.setClickable(true);
			menu_Redo.setClickable(true);
			break;
		case R.id.id_edit_title_text:
			edit_Text.setSelected(true);
			edit_Pen.setSelected(false);
			edit_Clean.setSelected(false);
			menu_Undo.setEnabled(false);
			menu_Redo.setEnabled(false);
			break;
		case R.id.id_edit_title_clean:
			edit_Clean.setSelected(true);
			edit_Text.setSelected(false);
			edit_Pen.setSelected(false);
			menu_Undo.setClickable(true);
			menu_Redo.setClickable(true);
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		changeOtherIconState(v.getId());
		switch (v.getId()) {
		case R.id.id_edit_title_pre:
			editFragment.getmPenView().undo();
			break;
		case R.id.id_edit_title_next:
			editFragment.getmPenView().redo();
			break;
		case R.id.id_edit_title_more:
			break;
		case R.id.id_edit_title_save:
			editFragment.getmPenView().savePicture();
			break;
		case R.id.id_edit_title_pen:
			editFragment.changeCurrentMode(v.getId());
			break;
		case R.id.id_edit_title_text:
			editFragment.changeCurrentMode(v.getId());
			break;
		case R.id.id_edit_title_clean:
			editFragment.changeCurrentMode(v.getId());
			break;
		default:
			break;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Toast.makeText(EditNoteFragmentActivity.this, "确定返回", 500).show();
			return false;
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
