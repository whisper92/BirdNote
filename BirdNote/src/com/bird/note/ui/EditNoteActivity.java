package com.bird.note.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bird.note.R;
import com.bird.note.customer.BirdWaitDialog;
import com.bird.note.customer.LevelFlag;
import com.bird.note.customer.QuadrantThumbnail;
import com.bird.note.customer.QuadrantThumbnail.OnQuadrantChangeListener;
import com.bird.note.dao.DbHelper;
import com.bird.note.model.BirdMessage;
import com.bird.note.model.BirdNote;
import com.bird.note.model.DBUG;
import com.bird.note.model.QuadrantContent;
import com.bird.note.utils.BitmapUtil;
import com.bird.note.utils.CommonUtils;
import com.bird.note.utils.JsonUtil;
import com.bird.note.utils.NoteApplication;

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
	
	public BirdNote mBirdNote=null;

	private DbHelper dbHelper;
	private List<EditQuadrantFragment> mEditQuaFragmentsList = new ArrayList<EditQuadrantFragment>();
	private List<QuadrantContent> mQuaList;
	private FragmentManager fragmentManager;
	public int mNoteEditType=BirdMessage.NOTE_EDIT_TYPE_CREATE;
	private NoteApplication mNoteApplication=null;
	private int[] mEditedQuadrant;
	
	public String mTitleString="";
	private BirdWaitDialog mWaitDialog = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_note_main);
		mWaitDialog  =new BirdWaitDialog(this, R.style.birdalertdialog);
		mNoteApplication=(NoteApplication)getApplication();
		mNoteEditType=mNoteApplication.getCurrentNoteEidtType();
		mEditedQuadrant=mNoteApplication.getEditedQuadrants();
		
		dbHelper=new DbHelper(this);
		Intent intent=getIntent();
		mCurrentType=intent.getIntExtra(BirdMessage.START_TYPE_KEY, BirdMessage.START_TYPE_CREATE_VALUE);
		
		if (mCurrentType==BirdMessage.START_TYPE_UPDATE_VALUE) {
			//若更新笔记，获得传过来Note(不完整)
			mBirdNote=intent.getParcelableExtra(BirdMessage.INITENT_PARCEL_NOTE);
			mTitleString = mBirdNote.title;
			//查询获取完整的Note
			mBirdNote=dbHelper.queryNoteById(mBirdNote, mBirdNote._id+"");
			mNoteApplication.setEditBackground(mBirdNote.background);
			DBUG.e("背景ID"+mBirdNote.background);
		} else {
			//若创建笔记
		}
		
		mCurrentMode = intent.getIntExtra(BirdMessage.START_MODE_KEY, R.id.id_edit_title_pen);
			
		try {
			initActivityView(mCurrentType);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void initActivityView(int type) throws JSONException{

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
			if (!mEditQuaFragment.isAdded()) {
				transaction.add(R.id.id_edit_main_editfragment, mEditQuaFragment);
			}					
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

	public void initUpdateView(int type) throws JSONException{
		mLevelFlag.setCurrentLeve(mBirdNote.level);
		mEditQuaFragmentsList.add(0, null);
		mEditQuaFragmentsList.add(1, null);
		mEditQuaFragmentsList.add(2, null);
		mEditQuaFragmentsList.add(3, null);
		
		mQuaList=dbHelper.generateQuadrantFromNote(mBirdNote);
		Iterator< QuadrantContent> iterator=mQuaList.iterator();
		QuadrantContent quadrantContent;
		while (iterator.hasNext()) {
			quadrantContent = (QuadrantContent) iterator.next();
			if (quadrantContent != null) {
				EditQuadrantFragment editQuadrantFragment=EditQuadrantFragment.newInstance(mCurrentMode,quadrantContent);
				mEditQuaFragmentsList.remove(quadrantContent.quadrant);
				mEditQuaFragmentsList.add(quadrantContent.quadrant, editQuadrantFragment);
				
			}
			
		}
		
		/*
		 * 先默认0，后期要改成可以进入任意象限
		 */
		mEditQuaFragment = mEditQuaFragmentsList.get(0);
		mEditQuaFragmentsList.remove(0);
		mEditQuaFragmentsList.add(0, mEditQuaFragment);
		mEditedQuadrant[0]=1;
		mNoteApplication.setEditedQuadrants(mEditedQuadrant);
		
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



	@Override
	public void onClick(View v) {	
	
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_MENU && event.getRepeatCount() == 0) {
			mEditQuaFragment.togglePopMenu();
			return true;
		}else if (keyCode == KeyEvent.KEYCODE_BACK){
			if (mEditQuaFragment.mPopMenu.isShowing()) {
				mEditQuaFragment.closePopMenu();
				return true;
			}
			
		}
		return super.onKeyDown(keyCode, event);
	}

	
	public void deleteNote(){
		if (mBirdNote!=null) {
			dbHelper.deleteNoteById(mBirdNote._id+"");
		}
		editHandler.sendEmptyMessage(BirdMessage.DELETE_OVER);
	}
	/**
	 * 生成新的笔记对象
	 */
	public BirdNote generateNewNote(){
		BirdNote birdNote=new BirdNote();
		int[] edited=mNoteApplication.getEditedQuadrants();
		int level=mLevelFlag.mCurrentLevel;
		String title=mEditQuaFragment.mTitleString;
		birdNote.level=level;
		if (mCurrentType == BirdMessage.START_TYPE_CREATE_VALUE) {
			birdNote.title=title;		
		} else {
			birdNote.title = mTitleString;
		}
		
		String[] text_array=new String[4];
		byte[] qua=null;
		for (int i = 0; i < mEditQuaFragmentsList.size(); i++) {
			if (mEditQuaFragmentsList.get(i)!=null) {
						    
                   if (edited[i]==1) {
                	   //若编辑过，则保存新内容
                    	  		text_array[i]=mEditQuaFragmentsList.get(i).getTextContent();
        		                qua=mEditQuaFragmentsList.get(i).getQuadrantDrawContentBytes();			
					} else {
						//若未编辑过，则保存原始内容
								text_array[i]=mQuaList.get(i).textcontent;
								qua=mQuaList.get(i).quadrantdraw;	
					}
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
		DBUG.e("保存背景ID"+mNoteApplication.getEditBackground());
		birdNote.background=mNoteApplication.getEditBackground();
		birdNote.star = 0;
	    return birdNote;
	}

	/**
	 * 根据第一象限的内容生成预览图
	 * @return
	 */
	public byte[] createThumbnailByQuadrant(){
		Bitmap bitmap=mEditQuaFragmentsList.get(0).getAllBitmap();
		BitmapUtil.writeBytesToFile(BitmapUtil.decodeBitmapToBytes(bitmap), "thumbnail");
		return BitmapUtil.generateThumbnailBytes(this, bitmap);
	}

	public Handler editHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			Intent intent=new Intent();
			switch (msg.what) {
			case BirdMessage.SAVE_AS_OVER:			
				if (mWaitDialog!=null&&mWaitDialog.isShowing()) {
					mWaitDialog.dismiss();
					Toast.makeText(EditNoteActivity.this, getString(R.string.save_as_toast_start)+msg.obj, Toast.LENGTH_LONG).show();
				}
				break;
			case BirdMessage.SAVE_OVER:			
				intent.setClass(EditNoteActivity.this, ShowNotesActivity.class);
				startActivity(intent);
				finish();
				break;
			case BirdMessage.DELETE_OVER:		
				intent.setClass(EditNoteActivity.this, ShowNotesActivity.class);
				startActivity(intent);
				finish();
				break;				
			case BirdMessage.SAVE_RUNNABLE_START:
				mWaitDialog.setWaitContent(getString(R.string.saveing_note));
				mWaitDialog.show();
				break;
			case BirdMessage.DELETE_RUNNABLE_START:
				mWaitDialog.setWaitContent(getString(R.string.deleteing_note));
				mWaitDialog.show();
				break;

			default:
				break;
			}
		};
	};
}
