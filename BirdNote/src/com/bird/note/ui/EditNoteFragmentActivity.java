package com.bird.note.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.bird.note.R;
import com.bird.note.customer.QuadrantThumbnail;
import com.bird.note.customer.QuadrantThumbnail.OnQuadrantChangeListener;

public class EditNoteFragmentActivity extends FragmentActivity implements
		OnClickListener {


	private QuadrantThumbnail quadrantThumbnail;

	/*
	 * 当前所处象限
	 */
	private int mCurrentQuadrant = 0;
	private EditFragment mEditFragment;
	/*
	 * 当前所处模式
	 */
	public int mCurrentMode = 0;
	

	private List<EditFragment> mEditFragmentsList = new ArrayList<EditFragment>();
	private FragmentManager fragmentManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_note_main);
		mCurrentMode = getIntent().getIntExtra("type", R.id.id_edit_title_pen);
		mEditFragment = EditFragment.newInstance(mCurrentQuadrant, mCurrentMode);
		mEditFragmentsList.add(0, mEditFragment);
		mEditFragmentsList.add(1, null);
		mEditFragmentsList.add(2, null);
		mEditFragmentsList.add(3, null);
		
		fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.id_edit_main_editfragment, mEditFragment);
		transaction.commit();

		initView(mCurrentMode);
	}

	public void initView(int type) {

		quadrantThumbnail = (QuadrantThumbnail) findViewById(R.id.id_edit_quathumb);
		quadrantThumbnail.setQuadrantChangeListener(new OnQuadrantChangeListener() {
					@Override
					public void changeQua(int qua) {
						mCurrentQuadrant=qua;
						changeToQuadrantAt(qua);			
					}
				});
	}

	/**
	 * 切换到某个象限:最初知会实例化第一个象限，只有使用到其他象限时才会实例化该象限的fagment
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
				transaction.show(mEditFragment);
			} else {
				if (mEditFragmentsList.get(i)!=null) {
					transaction.hide(mEditFragmentsList.get(i));
				}		
			}
		}
		transaction.commit();
	}

	

	@Override
	public void onClick(View v) {
		
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
