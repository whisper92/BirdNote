package com.bird.note.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.bird.note.R;
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

	/*
	 * 当前所处象限
	 */
	private int mCurrentQuadrant = 0;
	/*
	 * 当前所处模式
	 */
	public int mCurrentMode = 0;
	private EditFragment mEditFragment;

	private List<EditFragment> mEditFragmentsList = new ArrayList<EditFragment>();
	FragmentManager fragmentManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_note_main);
		fragmentManager = getSupportFragmentManager();

		mCurrentMode = getIntent().getIntExtra("type", R.id.id_edit_title_pen);

		mEditFragment = EditFragment
				.newInstance(mCurrentQuadrant, mCurrentMode);

		mEditFragmentsList.add(0, mEditFragment);
		mEditFragmentsList.add(1, null);
		mEditFragmentsList.add(2, null);
		mEditFragmentsList.add(3, null);
		
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.id_edit_main_editfragment, mEditFragment);
		transaction.commit();

		initView(mCurrentMode);
	}

	public void initView(int type) {
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
						Log.e("wxp", "切换至---》" + qua);
						changeToQuadrantAt(qua);
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

		changeOtherIconState(mCurrentMode);

	}

	/**
	 * 切换到某个象限
	 * 
	 * @param qua
	 * @return
	 */
	public void changeToQuadrantAt(int qua) {

		FragmentTransaction transaction = fragmentManager.beginTransaction();
		if (mEditFragmentsList.get(qua) == null) {
			mEditFragment = EditFragment.newInstance(qua, R.id.id_edit_title_pen);
			mEditFragmentsList.remove(qua);
			mEditFragmentsList.add(qua, mEditFragment);
			transaction.add(R.id.id_edit_main_editfragment, mEditFragment);
		} else {
			mEditFragment = mEditFragmentsList.get(qua);
		}

		for (int i = 0; i < mEditFragmentsList.size(); i++) {
			if (i == qua) {
				Log.e("wxp","show这个---》"+qua);
				transaction.show(mEditFragment);
			} else {
				if (mEditFragmentsList.get(i)!=null) {
					Log.e("wxp","hide这个---》"+i);
					transaction.hide(mEditFragmentsList.get(i));
				}		
			}
		}
		Log.e("wxp","mEditFragmentsList size"+mEditFragmentsList.size());
		transaction.commit();
	}

	/**
	 * 保存和回复撤销和重做图标的状态
	 */
	public void changeStateOfUndoRedo(boolean undoState, boolean redoState) {
		menu_Undo.setEnabled(undoState);
		menu_Redo.setEnabled(redoState);
	}

	/**
	 * 进入某一种模式的时候，要改变其他模式对应的图标的状态
	 */
	public void changeOtherIconState(int clickID) {
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
			mEditFragment.getmPenView().undo();
			break;
		case R.id.id_edit_title_next:
			mEditFragment.getmPenView().redo();
			break;
		case R.id.id_edit_title_more:
			break;
		case R.id.id_edit_title_save:
			mEditFragment.getmPenView().savePicture();
			break;
		case R.id.id_edit_title_pen:
			mEditFragment.changeCurrentMode(v.getId());
			break;
		case R.id.id_edit_title_text:
			mEditFragment.changeCurrentMode(v.getId());
			break;
		case R.id.id_edit_title_clean:
			mEditFragment.changeCurrentMode(v.getId());
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
