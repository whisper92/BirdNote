package com.bird.note.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.bird.note.R;
import com.bird.note.customer.BirdInputTitleDialog;
import com.bird.note.customer.BirdWaitDialog;
import com.bird.note.customer.ChangeCover;
import com.bird.note.customer.ChangeQua;
import com.bird.note.customer.ChangeQua.OnChangeQuaListener;
import com.bird.note.dao.DbHelper;
import com.bird.note.model.BirdMessage;
import com.bird.note.model.BirdNote;
import com.bird.note.model.QuadrantContent;
import com.bird.note.utils.BitmapUtil;
import com.bird.note.utils.CommonUtils;
import com.bird.note.utils.JsonUtil;
import com.bird.note.utils.NoteApplication;
/*[BIRD][BIRD_ALI_IOS8_SYSTEMUI][bug-106178][随笔删除提示框显示问题] huangzhangbin 20150107 begin*/
import android.view.LayoutInflater;
import android.widget.LinearLayout;
/*[BIRD][BIRD_ALI_IOS8_SYSTEMUI][bug-106178][随笔删除提示框显示问题] huangzhangbin 20150107 end*/

/**
 * @author wangxianpeng
 * @since 19/12/14
 *
 */
public class EditNoteActivity extends FragmentActivity {
	private String TAG = "EditNoteActivity";
	/*
	 * 当前所处模式：绘图或文字
	 */
	public int mCurrentMode = 0;
	/*
	 * 当前的类型：创建或更新
	 */
	public int mCurrentType = BirdMessage.START_TYPE_CREATE_VALUE;
	private EditQuadrantFragment mEditQuaFragment;
	/*
	 * 当前所处象限
	 */
	private int mCurrentQuadrant = 0;
	public BirdNote mBirdNote = null;
	private DbHelper dbHelper;
	private List<EditQuadrantFragment> mEditQuaFragmentsList = new ArrayList<EditQuadrantFragment>();
	private List<QuadrantContent> mQuaList;
	private FragmentManager fragmentManager;
	public int mNoteEditType = BirdMessage.NOTE_EDIT_TYPE_CREATE;
	private NoteApplication mNoteApplication = null;
	private int[] mEditedQuadrant;
	public String mTitleString = "";
	private BirdWaitDialog mWaitDialog = null;
	//private ActionBar mActionBar = null;

	public EditText mEditText;
    /*[BIRD][BIRD_ALI_IOS8_SYSTEMUI][bug-106178][随笔删除提示框显示问题] huangzhangbin 20150107 begin*/
    private AlertDialog.Builder saveNewNoteBuilder = null;
    private EditText mInputTitleEditText = null;
    /*[BIRD][BIRD_ALI_IOS8_SYSTEMUI][bug-106178][随笔删除提示框显示问题] huangzhangbin 20150107 end*/
	private int mPenHasSelected = 0;
	private int mEraserHasSelected = 0;

	private ChangeQua mChangeQua = null;
	private AlertDialog.Builder builder = null;
	public int getmPenHasSelected() {
		return mPenHasSelected;
	}

	public void setmPenHasSelected(int mPenHasSelected) {
		this.mPenHasSelected = mPenHasSelected;
	}

	public int getmEraserHasSelected() {
		return mEraserHasSelected;
	}

	public void setmEraserHasSelected(int mEraserHasSelected) {
		this.mEraserHasSelected = mEraserHasSelected;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.edit_note_main);
		//mActionBar = getActionBar();
		mChangeQua = (ChangeQua) findViewById(R.id.id_edit_change_qua);
		mChangeQua.setOnChangeQuaListener(mOnChangeQuaListener);
		mWaitDialog = new BirdWaitDialog(this,android.R.style.Theme_Holo_Light_Dialog);
		mNoteApplication = (NoteApplication) getApplication();
		mNoteApplication.setEdited(false);
		mNoteEditType = mNoteApplication.getCurrentNoteEidtType();
		mEditedQuadrant = mNoteApplication.getEditedQuadrants();
		dbHelper = new DbHelper(this);
		Intent intent = getIntent();
		mCurrentType = intent.getIntExtra(BirdMessage.START_TYPE_KEY,BirdMessage.START_TYPE_CREATE_VALUE);

		if (mCurrentType == BirdMessage.START_TYPE_UPDATE_VALUE) {
			/* 若更新笔记，获得传过来Note(不完整) */
			mBirdNote = intent.getParcelableExtra(BirdMessage.INITENT_PARCEL_NOTE);
			mTitleString = mBirdNote.title;
			/* 查询获取完整的Note */
			mBirdNote = dbHelper.queryNoteById(mBirdNote, mBirdNote._id + "");
		} else {

		}

		mCurrentMode = intent.getIntExtra(BirdMessage.START_MODE_KEY,R.id.id_edit_title_pen);
		mNoteApplication.setCurrentEditMode(mCurrentMode);
		try {
			initActivityView(mCurrentType);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		builder = PopMenuManager.createChooseMarkAlertDialog( EditNoteActivity.this, R.string.edit_menu_change_cover, mChooseCoverListener);
	}

	OnChangeQuaListener mOnChangeQuaListener = new OnChangeQuaListener() {

		@Override
		public void changeQua(int qua) {
			mCurrentQuadrant = qua;
			changeToQuadrantAt(qua);
		}
	};


	View mCoverView = null;
	ChangeCover mChangeCover = null;


	android.content.DialogInterface.OnClickListener mChooseCoverListener = new android.content.DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			if (mChangeCover != null) {
				if (which == -1) {
					dbHelper.updateLevelById(mBirdNote._id + "", mChangeCover.getSelectCover());
				}
			}

		}

	};
	

	@Override
	public void openOptionsMenu() {
		super.openOptionsMenu();
	}

	public void initActivityView(int type) throws JSONException {
		if (type == BirdMessage.START_TYPE_CREATE_VALUE) {
			initCreateView(type);
		}
		if (type == BirdMessage.START_TYPE_UPDATE_VALUE) {
			initUpdateView(type);
		}
	}

	/**
	 * 切换到某个象限:最初知会实例化第一个象限，只有使用到其他象限时才会实例化该象限的fagment
	 */
	public void changeToQuadrantAt(int qua) {
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		if (mEditQuaFragmentsList.get(qua) == null) {
			mEditQuaFragment = EditQuadrantFragment.newInstance(qua, mNoteApplication.getCurrentEditMode());
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
				if (mEditQuaFragmentsList.get(i) != null) {
					transaction.hide(mEditQuaFragmentsList.get(i));
				}
			}
		}
		transaction.commit();
		
		editHandler.post(new Runnable() {
			@Override
			public void run() {	
				mEditQuaFragment.changeCurrentMode(mNoteApplication.getCurrentEditMode());
				mEditQuaFragment.changeOtherIconState(mNoteApplication.getCurrentEditMode());
			}
		});

	}

	public void initUpdateView(int type) throws JSONException {
		mEditQuaFragmentsList.add(0, null);
		mEditQuaFragmentsList.add(1, null);
		mEditQuaFragmentsList.add(2, null);
		mEditQuaFragmentsList.add(3, null);

		mQuaList = dbHelper.generateQuadrantFromNote(mBirdNote);
		Iterator<QuadrantContent> iterator = mQuaList.iterator();
		QuadrantContent quadrantContent;
		while (iterator.hasNext()) {
			quadrantContent = (QuadrantContent) iterator.next();
			if (quadrantContent != null) {
				EditQuadrantFragment editQuadrantFragment = EditQuadrantFragment.newInstance(mNoteApplication.getCurrentEditMode(), quadrantContent);
				mEditQuaFragmentsList.remove(quadrantContent.quadrant);
				mEditQuaFragmentsList.add(quadrantContent.quadrant, editQuadrantFragment);
			}

		}

		mEditQuaFragment = mEditQuaFragmentsList.get(0);
		mEditQuaFragmentsList.remove(0);
		mEditQuaFragmentsList.add(0, mEditQuaFragment);
		mEditedQuadrant[0] = 1;
		mNoteApplication.setEditedQuadrants(mEditedQuadrant);

		fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.id_edit_main_editfragment, mEditQuaFragment);
		transaction.commit();
	}

	public void initCreateView(int type) {
		mEditQuaFragment = EditQuadrantFragment.newInstance(mCurrentQuadrant, mNoteApplication.getCurrentEditMode());
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mNoteApplication != null) {
				if (mNoteApplication.isEdited()) {
					PopMenuManager.createExitAlertDialog(this,	R.string.exit_ensure, exitListener);
				}
			}
			if (mEditQuaFragment.chooseEditBgPopMenu != null  && mEditQuaFragment.chooseEditBgPopMenu.isShowing()) {
				mEditQuaFragment.chooseEditBgPopMenu.dismiss();
				return true;
			}
		}
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			if (mEditQuaFragment.chooseEditBgPopMenu != null  && mEditQuaFragment.chooseEditBgPopMenu.isShowing()) {
				mEditQuaFragment.chooseEditBgPopMenu.dismiss();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	android.content.DialogInterface.OnClickListener exitListener = new android.content.DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			if (which == -1) {
				mEditQuaFragment.saveNote();
			}
			if (which == -2) {
				finish();
			}
		}
	};

	@Override
	protected void onPause() {
		super.onPause();
		mEditQuaFragment.hideInputMethod();
	}

	private Runnable deleteRunnable = new Runnable() {
		@Override
		public void run() {
			if (mBirdNote != null) {
				dbHelper.deleteNoteById(mBirdNote._id + "");
			}
			editHandler.sendEmptyMessage(BirdMessage.DELETE_OVER);

		}
	};

	/**
	 * 生成新的笔记对象
	 */
	public BirdNote generateNewNote() {
		BirdNote birdNote = new BirdNote();
		int[] edited = mNoteApplication.getEditedQuadrants();
		String title = mEditQuaFragment.mTitleString;
		if (mCurrentType == BirdMessage.START_TYPE_CREATE_VALUE) {
			birdNote.title = title;
		} else {
			birdNote.title = mTitleString;
		}

		String[] text_array = new String[4];
		byte[] qua = null;
		for (int i = 0; i < mEditQuaFragmentsList.size(); i++) {
			if (mEditQuaFragmentsList.get(i) != null) {
				if (edited[i] == 1) {
					/* 若编辑过，则保存新内容 */
					text_array[i] = mEditQuaFragmentsList.get(i).getTextContent();
					qua = mEditQuaFragmentsList.get(i).getQuadrantDrawContentBytes();
				} else {
					/* 若未编辑过，则保存原始内容 */
					text_array[i] = mQuaList.get(i).textcontent;
					qua = mQuaList.get(i).quadrantdraw;
				}
			} else {
				/* 如果某个象限未被实例化，则将他的内容设置为null */
				text_array[i] = null;
				qua = null;
			}

			switch (i) {
			case 0:
				birdNote.qua0 = qua;
				break;
				
			case 1:
				birdNote.qua1 = qua;
				break;
				
			case 2:
				birdNote.qua2 = qua;
				break;
				
			case 3:
				birdNote.qua3 = qua;
				break;
				
			default:
				break;
			}

		}
		String text_content = "";
		try {
			text_content = JsonUtil.createJsonFromStrings(text_array);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		birdNote.textcontents = text_content;
		birdNote.background = mNoteApplication.getEditBackground();
		birdNote.star = 0;
		return birdNote;
	}

	public final Handler editHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case BirdMessage.SAVE_AS_OVER:
				if (mWaitDialog != null && mWaitDialog.isShowing()) {
					mWaitDialog.dismiss();
					Toast.makeText(EditNoteActivity.this,getString( R.string.save_as_toast_start) + msg.obj, Toast.LENGTH_LONG).show();
				}
				if (mEditQuaFragment.mEditText != null) {
					mEditQuaFragment.mEditText.setCursorVisible(true);
				}
				break;

			case BirdMessage.SAVE_OVER:
				recycleBitmap();
				finish();
				break;

			case BirdMessage.DELETE_OVER:
				recycleBitmap();
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

	public void recycleBitmap() {
		for (int i = 0; i < mEditQuaFragmentsList.size(); i++) {
			if (mEditQuaFragmentsList.get(i) != null) {
				if (mEditQuaFragmentsList.get(i).mPenView != null) {
					if (mEditQuaFragmentsList.get(i).mPenView.mExistBitmap != null && !mEditQuaFragmentsList.get(i).mPenView.mExistBitmap.isRecycled()) {
						mEditQuaFragmentsList.get(i).mPenView.mExistBitmap.recycle();
					}
					if (mEditQuaFragmentsList.get(i).mPenView.mDrawBitmap != null && mEditQuaFragmentsList.get(i).mPenView.mDrawBitmap.isRecycled()) {
						mEditQuaFragmentsList.get(i).mPenView.mDrawBitmap.recycle();
					}

				}
			}
		}
		System.gc();
	}

	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		if (mCurrentType == BirdMessage.START_TYPE_UPDATE_VALUE) {
			int star = dbHelper.queryStarById(mBirdNote._id + "");
			if (star == 0) {
				getMenuInflater().inflate(R.menu.edit_menu_tostar, menu);
			} else {
				getMenuInflater().inflate(R.menu.edit_menu_cancelstar, menu);
			}
		} else {
			getMenuInflater().inflate(R.menu.create_newnote_menu, menu);
		}

		return true;
	};

	public int getCoverByLevel(int level) {
		return BitmapUtil.EDIT_COVER_PRE[level];
	}

	BirdInputTitleDialog mRenameDialog = null;

	public void renameNote() {
        /*[BIRD][BIRD_ALI_IOS8_SYSTEMUI][bug-106178][随笔删除提示框显示问题] huangzhangbin 20150107 begin*/
        //mInputTitleEditText = new EditText(this);
        LayoutInflater inflater =getLayoutInflater();
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.bird_edit_text_layout, null);
        mInputTitleEditText = (EditText) view.findViewById(R.id.editText);
		saveNewNoteBuilder = PopMenuManager.createSaveNewNoteAlertDialog(this, R.string.input_title_dialog_title, view, undapteTitleDialogListener);
        /*[BIRD][BIRD_ALI_IOS8_SYSTEMUI][bug-106178][随笔删除提示框显示问题] huangzhangbin 20150107 end*/
		mRenameDialog = new BirdInputTitleDialog(this, android.R.style.Theme_Holo_Light_Dialog);
		mRenameDialog.setTitle(R.string.alert_input_newname);
		mRenameDialog.setOnConfirmClickListener(mUpdateTitleClickListener);
        /*[BIRD][BIRD_ALI_IOS8_SYSTEMUI][bug-106178][随笔删除提示框显示问题] huangzhangbin 20150107 begin*/
		//mRenameDialog.show();
        saveNewNoteBuilder.create().show();
		if (mCurrentType == BirdMessage.START_TYPE_UPDATE_VALUE) {
            mInputTitleEditText.setText(mBirdNote.title);
			//mRenameDialog.setInputContent(mBirdNote.title);
		} else {
            mInputTitleEditText.setText(CommonUtils.getDefaultTitle(this));
			//mRenameDialog.setInputContent(CommonUtils.getDefaultTitle(this));
		}
        /*[BIRD][BIRD_ALI_IOS8_SYSTEMUI][bug-106178][随笔删除提示框显示问题] huangzhangbin 20150107 end*/

	}

	OnClickListener mUpdateTitleClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			editHandler.post(updateTitleRunnable);
		}
	};

    /*[BIRD][BIRD_ALI_IOS8_SYSTEMUI][bug-106178][随笔删除提示框显示问题] huangzhangbin 20150107 begin*/
    android.content.DialogInterface.OnClickListener undapteTitleDialogListener = new android.content.DialogInterface.OnClickListener(){
        public void onClick(DialogInterface dialog, int which){
            if(which == -1) {
             editHandler.post(updateTitleRunnable);
            }
        }
    };
    /*[BIRD][BIRD_ALI_IOS8_SYSTEMUI][bug-106178][随笔删除提示框显示问题] huangzhangbin 20150107 end*/
	public Runnable updateTitleRunnable = new Runnable() {
		@Override
		public void run() {
            /*[BIRD][BIRD_ALI_IOS8_SYSTEMUI][bug-106178][随笔删除提示框显示问题] huangzhangbin 20150107 begin*/
			//dbHelper.updateTitleById(mBirdNote._id + "", mRenameDialog.getContent());
			dbHelper.updateTitleById(mBirdNote._id + "", mInputTitleEditText.getText().toString());
            /*[BIRD][BIRD_ALI_IOS8_SYSTEMUI][bug-106178][随笔删除提示框显示问题] huangzhangbin 20150107 end*/
			editHandler.sendEmptyMessage(BirdMessage.UPDATETITLE_RUNNABLE_OVER);
			if (mRenameDialog != null) {
				mRenameDialog.dismiss();
			}
		}
	};

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.id_edit_menu_rename:
			renameNote();
			break;

		case R.id.id_edit_menu_change_cover:
			mChangeCover = new ChangeCover(this);
			mChangeCover.setCoverChecked(getCoverByLevel(mBirdNote.level));
			builder.setView(mChangeCover);
			builder.create().show();
			break;

		case R.id.id_edit_menu_change_bg:
			mEditQuaFragment.showChangeBg();
			break;

		case R.id.id_edit_menu_saveas:
			mEditQuaFragment.showSaveAs();
			break;

		case R.id.id_edit_menu_star:
			dbHelper.toggleStarNoteById(mBirdNote._id + "");
			invalidateOptionsMenu();
			break;

		case R.id.id_edit_menu_delete:
			PopMenuManager.createDeleteAlertDialog(EditNoteActivity.this, R.string.alert_delete_content, deleteListener);
			break;

		case R.id.id_edit_menu_removefavor:
			dbHelper.toggleStarNoteById(mBirdNote._id + "");
			invalidateOptionsMenu();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	android.content.DialogInterface.OnClickListener deleteListener = new android.content.DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case -1:
				editHandler.sendEmptyMessage(BirdMessage.DELETE_RUNNABLE_START);
				editHandler.post(deleteRunnable);
				break;

			case -2:
				break;

			default:
				break;
			}
		}
	};

	protected void onDestroy() {
		super.onDestroy();
	};
}
