package com.bird.note.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;
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
import android.widget.Toast;

import com.bird.note.R;
import com.bird.note.customer.LevelFlag;
import com.bird.note.customer.QuadrantThumbnail;
import com.bird.note.customer.QuadrantThumbnail.OnQuadrantChangeListener;
import com.bird.note.dao.DbHelper;
import com.bird.note.model.BirdNote;
import com.bird.note.utils.BitmapUtil;
import com.bird.note.utils.JsonUtil;

public class EditNoteActivity extends FragmentActivity implements
		OnClickListener {

	public LevelFlag mLevelFlag;
	private QuadrantThumbnail quadrantThumbnail;

	/*
	 * 当前所处象限
	 */
	private int mCurrentQuadrant = 0;
	private EditQuadrantFragment mEditQuaFragment;
	/*
	 * 当前所处模式
	 */
	public int mCurrentMode = 0;
	

	private List<EditQuadrantFragment> mEditQuaFragmentsList = new ArrayList<EditQuadrantFragment>();
	private FragmentManager fragmentManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_note_main);
		mCurrentMode = getIntent().getIntExtra("type", R.id.id_edit_title_pen);
		mEditQuaFragment = EditQuadrantFragment.newInstance(mCurrentQuadrant, mCurrentMode);
		mEditQuaFragmentsList.add(0, mEditQuaFragment);
		mEditQuaFragmentsList.add(1, null);
		mEditQuaFragmentsList.add(2, null);
		mEditQuaFragmentsList.add(3, null);
		
		fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.id_edit_main_editfragment, mEditQuaFragment);
		transaction.commit();

		initView(mCurrentMode);
	}

	public void initView(int type) {
		mLevelFlag=(LevelFlag)findViewById(R.id.id_edit_level_flag);
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
		if (mEditQuaFragmentsList.get(qua) == null) {
			mEditQuaFragment = EditQuadrantFragment.newInstance(qua, R.id.id_edit_title_pen);
			mEditQuaFragmentsList.remove(qua);
			mEditQuaFragmentsList.add(qua, mEditQuaFragment);
			transaction.add(R.id.id_edit_main_editfragment, mEditQuaFragment);
		} else {
			mEditQuaFragment = mEditQuaFragmentsList.get(qua);
		}

		for (int i = 0; i < mEditQuaFragmentsList.size(); i++) {
			if (i == qua) {
				transaction.show(mEditQuaFragment);
			} else {
				if (mEditQuaFragmentsList.get(i)!=null) {
					transaction.hide(mEditQuaFragmentsList.get(i));
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
			Toast.makeText(EditNoteActivity.this, "确定返回", 500).show();
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
	
	/**
	 * 将笔记保存到数据库中
	 */
	public void insertNewNote(BirdNote birdNote){
		DbHelper dbHelper=new DbHelper(this);
		dbHelper.insertNewNote(birdNote.level, birdNote.title, birdNote.textContents, birdNote.byteArrayQua0, birdNote.byteArrayQua1, birdNote.byteArrayQua2, birdNote.byteArrayQua3, birdNote.byteArrayThumbnail);
	}
	
	/**
	 * 生成新的笔记对象
	 */
	public BirdNote createNewNote(){
		BirdNote birdNote=new BirdNote();
		int level=mLevelFlag.mCurrentLevel;
		String title="hello world";
		birdNote.level=level;
		birdNote.title=title;
		
		String[] text_array=new String[4];
		byte[] qua=null;
		for (int i = 0; i < mEditQuaFragmentsList.size(); i++) {
			if (mEditQuaFragmentsList.get(i)!=null) {
				//如果某个象限已经被实例化，则获取他的内容
				text_array[i]=mEditQuaFragmentsList.get(i).getTextContent();
                qua=mEditQuaFragmentsList.get(i).getQuadrantDrawContentBytes();
			} else {
				//如果某个象限未被实例化，则将他的内容设置为null
				text_array[i]=null;
				qua=null;
			}
			
			  switch (i) {
				case 0:
					birdNote.byteArrayQua0=qua;
					break;
				case 1:
					birdNote.byteArrayQua1=qua;
					break;
				case 2:
					birdNote.byteArrayQua2=qua;
					break;
				case 3:
					birdNote.byteArrayQua3=qua;
					break;
				default:
					break;
				}
			
		}
		String text_content=JsonUtil.createJsonFromStrings(text_array);
		birdNote.textContents=text_content;
		birdNote.byteArrayThumbnail=createThumbnailByQuadrant();
	    return birdNote;
	}

	/**
	 * 根据第一象限的内容生成预览图
	 * @return
	 */
	public byte[] createThumbnailByQuadrant(){
		Bitmap bitmap=mEditQuaFragmentsList.get(0).getQuadrantDrawContentBitmap();
		Bitmap thumbBitmap=Bitmap.createBitmap(bitmap, 0, 0,(int) getResources().getDimension(R.dimen.dimen_create_thumbnail_width), (int)getResources().getDimension(R.dimen.dimen_create_thumbnail_height));
		return BitmapUtil.decodeBitmapToBytes(thumbBitmap);
	}

}
