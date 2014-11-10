package com.bird.note.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import android.R.integer;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Handler;
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
import com.bird.note.model.BirdMessage;
import com.bird.note.model.BirdNote;
import com.bird.note.test.TestGridViewActivity;
import com.bird.note.utils.BitmapUtil;
import com.bird.note.utils.CommonUtils;
import com.bird.note.utils.JsonUtil;

public class EditNoteActivity extends FragmentActivity implements
		OnClickListener {

	public LevelFlag mLevelFlag;
	private QuadrantThumbnail quadrantThumbnail;


	/*
	 * 当前所处模式：绘图或文字
	 */
	public int mCurrentMode = 0;
	
	/*
	 * 当前的类型：创建或更新
	 */
	public int mCurrentType=BirdMessage.START_TYPE_CREATE_VALUE;
	
	private EditQuadrantFragment mEditQuaFragment;
	/*
	 * 当前所处象限
	 */
	private int mCurrentQuadrant = 0;
	
	private BirdNote mBirdNote=null;

	private DbHelper dbHelper;
	private List<EditQuadrantFragment> mEditQuaFragmentsList = new ArrayList<EditQuadrantFragment>();
	private FragmentManager fragmentManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_note_main);
		dbHelper=new DbHelper(this);
		Intent intent=getIntent();
		mCurrentType=intent.getIntExtra(BirdMessage.START_TYPE_KEY, BirdMessage.START_TYPE_CREATE_VALUE);
		
		if (mCurrentType==BirdMessage.START_TYPE_UPDATE_VALUE) {
			//若更新笔记，获得传过来的ID
			mBirdNote=intent.getParcelableExtra(BirdMessage.INITENT_PARCEL_NOTE);
			mBirdNote=dbHelper.queryNoteById(mBirdNote, mBirdNote._id+"");
		} else {
			//若创建笔记
		}
		
		mCurrentMode = intent.getIntExtra(BirdMessage.START_MODE_KEY, R.id.id_edit_title_pen);
			
		initView(mCurrentType);
	}

	public void initView(int type){
		
		mLevelFlag=(LevelFlag)findViewById(R.id.id_edit_level_flag);
		quadrantThumbnail = (QuadrantThumbnail) findViewById(R.id.id_edit_quathumb);
		quadrantThumbnail.setQuadrantChangeListener(new OnQuadrantChangeListener() {
					@Override
					public void changeQua(int qua) {
						mCurrentQuadrant=qua;
						changeToQuadrantAt(qua);			
					}
				});
		
		if (type==BirdMessage.START_TYPE_CREATE_VALUE) {
			initCreateView(type);
			
		}
        if (type==BirdMessage.START_TYPE_UPDATE_VALUE) {
        	initUpdateView(type);
		}
	}
	
	public void initUpdateView(int type){
		mLevelFlag.setCurrentLeve(mBirdNote.level);
		mEditQuaFragment = EditQuadrantFragment.newInstance(mCurrentQuadrant, mCurrentMode,mBirdNote);
		mEditQuaFragmentsList.add(0, mEditQuaFragment);
		mEditQuaFragmentsList.add(1, null);
		mEditQuaFragmentsList.add(2, null);
		mEditQuaFragmentsList.add(3, null);
		
		fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.id_edit_main_editfragment, mEditQuaFragment);
		transaction.commit();
	}
	public void initCreateView(int type) {
		mEditQuaFragment = EditQuadrantFragment.newInstance(mCurrentQuadrant, mCurrentMode);
		mEditQuaFragmentsList.add(0, mEditQuaFragment);
		mEditQuaFragmentsList.add(1, null);
		mEditQuaFragmentsList.add(2, null);
		mEditQuaFragmentsList.add(3, null);
		
		fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.id_edit_main_editfragment, mEditQuaFragment);
		transaction.commit();

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
/*		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Toast.makeText(EditNoteActivity.this, "确定返回", 500).show();
			return false;
		}*/
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
	 * 生成新的笔记对象
	 */
	public BirdNote generateNewNote(){
		BirdNote birdNote=new BirdNote();
		int level=mLevelFlag.mCurrentLevel;
		String title="hello world";
		birdNote.level=level;
		birdNote.title=title;
		
		String[] text_array=new String[4];
		byte[] qua=null;
		for (int i = 0; i < mEditQuaFragmentsList.size(); i++) {
			if (mEditQuaFragmentsList.get(i)!=null) {
				//如果某个象限已经被实例化，则获取他的内容,此处有坑，第一象限始终实例化，但是如果不输入文字，他的内容就全是空格，后期要判断一下。
				text_array[i]=mEditQuaFragmentsList.get(i).getTextContent();
                qua=mEditQuaFragmentsList.get(i).getQuadrantDrawContentBytes();
			} else {
				//如果某个象限未被实例化，则将他的内容设置为null
				text_array[i]=null;
				qua=null;
			}
			
			  switch (i) {
				case 0:
					birdNote.qua0=qua;
					break;
				case 1:
					birdNote.qua1=qua;
					break;
				case 2:
					birdNote.qua2=qua;
					break;
				case 3:
					birdNote.qua3=qua;
					break;
				default:
					break;
				}
			
		}
		String text_content="";
		try {
			text_content = JsonUtil.createJsonFromStrings(text_array);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		birdNote.textcontents=text_content;
		birdNote.thumbnail=createThumbnailByQuadrant();
		birdNote.background=R.drawable.note_bg_style00;
	    return birdNote;
	}

	/**
	 * 根据第一象限的内容生成预览图
	 * @return
	 */
	public byte[] createThumbnailByQuadrant(){
		Bitmap bitmap=mEditQuaFragmentsList.get(0).getQuadrantDrawContentBitmap();
		return BitmapUtil.generateThumbnailBytes(this, bitmap);
	}

	public Handler editHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case BirdMessage.SAVE_OVER:			
				Intent intent=new Intent();
				intent.setClass(EditNoteActivity.this, TestGridViewActivity.class);
				startActivity(intent);
				finish();
				break;

			default:
				break;
			}
		};
	};
}