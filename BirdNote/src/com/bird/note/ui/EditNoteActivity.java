package com.bird.note.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;

import android.R.integer;
import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bird.note.R;
import com.bird.note.customer.BirdWaitDialog;
import com.bird.note.customer.ChangeQua;
import com.bird.note.customer.ChangeQua.OnChangeQuaListener;
import com.bird.note.customer.LevelFlag;
import com.bird.note.customer.PenView;
import com.bird.note.customer.PopEraserBox;
import com.bird.note.customer.PopPenBox;
import com.bird.note.customer.LevelFlag.OnLevelChangeListener;
import com.bird.note.customer.PenView.OnPathListChangeListener;
import com.bird.note.customer.PopEraserBox.OnEraserChangedListener;
import com.bird.note.customer.PopPenBox.OnPaintChangedListener;
import com.bird.note.dao.DbHelper;
import com.bird.note.model.BirdMessage;
import com.bird.note.model.BirdNote;
import com.bird.note.model.QuadrantContent;
import com.bird.note.model.SavedPaint;
import com.bird.note.utils.BitmapUtil;
import com.bird.note.utils.JsonUtil;
import com.bird.note.utils.NoteApplication;

public class EditNoteActivity extends FragmentActivity implements OnClickListener {
   private String TAG ="EditNoteActivity";
	public LevelFlag mLevelFlag;
	/*
	 * 当前所处模式：绘图或文字
	 */
	public int mCurrMode = 0;
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
    private ActionBar mActionBar = null;
    
    
	private FrameLayout mWrapFrameLayout;
	public EditText mEditText;

	/*
	 * 撤销和重做图标的状态
	 */
	private boolean mUndoState;
	private boolean mRedoState;
	private boolean mFirstComeIn = true;
	private ImageView edit_Pen;
	private ImageView edit_Text;
	private ImageView edit_Clean;
	private ImageView menu_Undo;
	private ImageView menu_Redo;
	private ImageView menu_More;
	private ImageView menu_Save;


	private int mPenHasSelected = 0;
	private int mEraserHasSelected = 0;
	
	private PopPenBox mPopPenBox;
	private PopEraserBox mPopEraserBox;
	private boolean mPenBoxOpened = false;
	private boolean mEraserBoxOpened = false;
	private SavedPaint mSavedPaint;
	private ChangeQua mChangeQua = null;
	
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
		mActionBar = getActionBar();
		mChangeQua = (ChangeQua) findViewById(R.id.id_edit_change_qua);
		mChangeQua.setOnChangeQuaListener(mOnChangeQuaListener);
		mWaitDialog = new BirdWaitDialog(this, android.R.style.Theme_Holo_Light_Dialog);
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

		mCurrMode = intent.getIntExtra(BirdMessage.START_MODE_KEY,R.id.id_edit_title_pen);
		mNoteApplication.setCurrentEditMode(mCurrMode);
		try {
			initActivityView(mCurrentType);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		initActionBar();
	}

	OnChangeQuaListener mOnChangeQuaListener = new OnChangeQuaListener() {
		
		@Override
		public void changeQua(int qua) {
			mCurrentQuadrant = qua;
			changeToQuadrantAt(qua);		
		}
	};
	
	public void initActionBar(){
		  View headView = getLayoutInflater().inflate(R.layout.edit_note_header, null);
			mActionBar.setCustomView(headView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);  
			mActionBar.setDisplayShowCustomEnabled(true); 
			
			edit_Pen = (ImageView) headView.findViewById(R.id.id_edit_title_pen);
			edit_Text = (ImageView) headView.findViewById(R.id.id_edit_title_text);
			edit_Clean = (ImageView) headView.findViewById(R.id.id_edit_title_clean);
			menu_Undo = (ImageView) headView.findViewById(R.id.id_edit_title_pre);
			menu_Redo = (ImageView) headView.findViewById(R.id.id_edit_title_next);
			menu_More = (ImageView) headView.findViewById(R.id.id_edit_title_more);
			menu_Save = (ImageView) headView.findViewById(R.id.id_edit_title_save);

			edit_Pen.setOnClickListener(this);
			edit_Text.setOnClickListener(this);
			edit_Clean.setOnClickListener(this);
			menu_Undo.setOnClickListener(this);
			menu_Redo.setOnClickListener(this);
			menu_More.setOnClickListener(this);
			menu_Save.setOnClickListener(this);

			menu_Undo.setEnabled(false);
			menu_Redo.setEnabled(false);
			
			mSavedPaint = new SavedPaint(this);
			
			changeOtherIconState(mCurrMode);
			createEraserBox();
			createPenBox();
	}
	
	public OnPathListChangeListener mOnPathListChangeListener = new OnPathListChangeListener() {
		@Override
		public void changeState(int undocount, int redocount) {
			mUndoState = undocount > 0 ? true : false;
			mRedoState = redocount > 0 ? true : false;
			changeStateOfUndoRedo(mUndoState, mRedoState);
			
		}
	};
	
	/**
	 * 保存和回复撤销和重做图标的状态
	 */
	public void changeStateOfUndoRedo(boolean undoState, boolean redoState) {
		menu_Undo.setEnabled(undoState);
		menu_Redo.setEnabled(redoState);
		
		mNoteApplication.undoredo[mCurrentQuadrant][0] = undoState;
		mNoteApplication.undoredo[mCurrentQuadrant][1] = redoState;
	}

	
	/**
	 * 进入某一种模式的时候，要改变其他模式对应的图标的状态
	 */
	public void changeOtherIconState(int clickID) {
		Log.e(TAG,"changeOtherIconState----->");
		mCurrMode = clickID;
		
		switch (clickID) {
		case R.id.id_edit_title_pen:
			edit_Pen.setSelected(true);
			edit_Text.setSelected(false);
			edit_Clean.setSelected(false);
			menu_Undo.setClickable(true);
			menu_Redo.setClickable(true);
			mPenHasSelected +=1;
			mEraserHasSelected = 0;
			break;
			
		case R.id.id_edit_title_text:
			edit_Text.setSelected(true);
			edit_Pen.setSelected(false);
			edit_Clean.setSelected(false);
			menu_Undo.setEnabled(false);
			menu_Redo.setEnabled(false);
			mPenHasSelected = 0;
			mEraserHasSelected = 0;
			break;
			
		case R.id.id_edit_title_clean:
			edit_Clean.setSelected(true);
			edit_Text.setSelected(false);
			edit_Pen.setSelected(false);
			menu_Undo.setClickable(true);
			menu_Redo.setClickable(true);
			mPenHasSelected = 0;
			mEraserHasSelected +=1;
			break;

		default:
			break;
		}
	}
	
	@Override
	public void onClick(View v) {
		mOnClickTitleMenuListener.clickMenu(v.getId());
		mCurrMode = v.getId();
		changeOtherIconState(mCurrMode);
         switch (v.getId()) {
		case R.id.id_edit_title_pen:
			mNoteApplication.setCurrentEditMode(mCurrMode);
			togglePenBox(mCurrMode);		
			mPenHasSelected+=1;			
			mEraserHasSelected = 0;				
			break;
			
		case R.id.id_edit_title_text:
			mNoteApplication.setCurrentEditMode(mCurrMode);
			mPenHasSelected = 0;
			mEraserHasSelected = 0;
			break;
			
		case R.id.id_edit_title_clean:
			mNoteApplication.setCurrentEditMode(mCurrMode);
			toggleEraserBox(mCurrMode);
			mPenHasSelected = 0;
			mEraserHasSelected += 1;		
			break;
			
		case R.id.id_edit_title_pre:
			mEditQuaFragment.mPenView.undo();
			break;
			
		case R.id.id_edit_title_next:
			mEditQuaFragment.mPenView.redo();
			break;
			
		case R.id.id_edit_title_more:
			openOptionsMenu();
			break;
			
		case R.id.id_edit_title_save:
			mEditQuaFragment.saveNote();
			break;
			
		default:
			break;
		}
	}

	/**
	 * 开关笔刷设置框
	 * 
	 * @param mode
	 */
	public void togglePenBox(int mode) {
		createPenBox();
		if (mode == BirdMessage.START_MODE_DRAW_KEY) {
			if (!mPenBoxOpened || (!mPopPenBox.isShowing())) {
				if (mPenHasSelected > 1) {
					mPenHasSelected = 1;
					mPopPenBox.showAsDropDown(edit_Pen);
					mPenBoxOpened = true;
				}
			} else {
				mPopPenBox.dismiss();
				mPenBoxOpened = false;
			}
		} else {
			mPopPenBox.dismiss();
			mPenBoxOpened = false;
		}
	}

	/**
	 * 开关橡皮擦设置框
	 * 
	 * @param mode
	 */
	public void toggleEraserBox(int mode) {
		createEraserBox();
		if (mode == BirdMessage.START_MODE_CLEAN_KEY) {
			if (!mEraserBoxOpened || (!mPopEraserBox.isShowing())) {
				if (mEraserHasSelected > 1) {
					mPopEraserBox.showAsDropDown(edit_Clean);
					mEraserBoxOpened = true;
					mEraserHasSelected = 1;
				}
			} else {
				mPopEraserBox.dismiss();
				mEraserBoxOpened = false;
			}
		} else {
			mPopEraserBox.dismiss();
			mEraserBoxOpened = false;
		}
	}
	
	public void createPenBox() {
		mPopPenBox = new PopPenBox(this);
		mPopPenBox.setOnPaintChangedListener(new OnPaintChangedListener() {
			@Override
			public void changePaint(Paint paint) {
				mEditQuaFragment.mPenView.setDrawPaintColor(paint.getColor());
				mEditQuaFragment.mPenView.setDrawPaintWidth(paint.getStrokeWidth());
				mSavedPaint.savePaintColor(paint.getColor());
				mSavedPaint.savePaintWidth(paint.getStrokeWidth());
			}
		});
	}

	public void createEraserBox() {
		mPopEraserBox = new PopEraserBox(this, new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mPopEraserBox.isShowing()) {
					mPopEraserBox.dismiss();
				}
				PopMenuManager.createDeleteAlertDialog(EditNoteActivity.this, R.string.alert_clear_all, ConfirmClearAllListener);
			}
		});
		mPopEraserBox.setOnPaintChangedListener(new OnEraserChangedListener() {
			@Override
			public void changePaint(Paint paint) {
				mEditQuaFragment.mPenView.setCleanPaintWidth(paint.getStrokeWidth());
				mSavedPaint.saveCleanPaintWidth(paint.getStrokeWidth());
			}
		});
	}

	public android.content.DialogInterface.OnClickListener ConfirmClearAllListener = new android.content.DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			if (which == -1) {
				mEditQuaFragment.mPenView.clearAll();
			    mNoteApplication.setEdited(true);
			}
			
		}
	};

	
	public void closeBox(){
        if (mPopEraserBox!=null && mPopEraserBox.isShowing()) {
        	mPopEraserBox.dismiss();
		}
        if (mPopPenBox!=null && mPopPenBox.isShowing()) {
        	mPopPenBox.dismiss();
		}
	}

	
	OnClickTitleMenuListener  mOnClickTitleMenuListener = null;
	public interface OnClickTitleMenuListener{
		public void clickMenu(int menuid);
	}
	
	public void setOnClickTitleMenuListener(OnClickTitleMenuListener listener){
		this.mOnClickTitleMenuListener = listener; 
	}
	
	@Override
	public void openOptionsMenu() {
		super.openOptionsMenu();
	}

	public void initActivityView(int type) throws JSONException {

/*		mLevelFlag = (LevelFlag) findViewById(R.id.id_edit_level_flag);
		mLevelFlag.setOnLevelChangeListener(new OnLevelChangeListener() {			
			@Override
			public void changeLevel(int level) {
				if (mBirdNote != null) {
					dbHelper.updateLevelById(mBirdNote._id+"", level);
				}
				
			}
		});*/
		
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
			mEditQuaFragment = EditQuadrantFragment.newInstance(qua,mNoteApplication.getCurrentEditMode());
			mEditQuaFragmentsList.remove(qua);
			mEditQuaFragmentsList.add(qua, mEditQuaFragment);
			transaction.add(R.id.id_edit_main_editfragment, mEditQuaFragment);
		} else {
			mEditQuaFragment = mEditQuaFragmentsList.get(qua);
			if (!mEditQuaFragment.isAdded()) {
				transaction.add(R.id.id_edit_main_editfragment,mEditQuaFragment);

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
				mEditQuaFragment.changeCurrentMode(mNoteApplication
						.getCurrentEditMode());
				changeStateOfUndoRedo(mNoteApplication.undoredo[mCurrentQuadrant][0], mNoteApplication.undoredo[mCurrentQuadrant][1]);
			}
		});

	}

	public void initUpdateView(int type) throws JSONException {
		//mLevelFlag.setCurrentLeve(mBirdNote.level);
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
				EditQuadrantFragment editQuadrantFragment = EditQuadrantFragment.newInstance(mCurrMode, quadrantContent);
				mEditQuaFragmentsList.remove(quadrantContent.quadrant);
				mEditQuaFragmentsList.add(quadrantContent.quadrant,
						editQuadrantFragment);
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
		mEditQuaFragment = EditQuadrantFragment.newInstance(mCurrentQuadrant,mCurrMode);
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
       if (keyCode ==KeyEvent.KEYCODE_BACK) {
    	   if (mNoteApplication!=null) {
				if (mNoteApplication.isEdited()) {
					PopMenuManager.createExitAlertDialog(this, R.string.exit_ensure, exitListener);
				}
			}
    	   if (mEditQuaFragment.chooseEditBgPopMenu!=null && mEditQuaFragment.chooseEditBgPopMenu.isShowing()) {
			mEditQuaFragment.chooseEditBgPopMenu.dismiss();
			return true;
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
		//int level = mLevelFlag.mCurrentLevel;
		String title = mEditQuaFragment.mTitleString;
		//birdNote.level = level;
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
					text_array[i] = mEditQuaFragmentsList.get(i)
							.getTextContent();
					qua = mEditQuaFragmentsList.get(i)
							.getQuadrantDrawContentBytes();
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
					Toast.makeText(EditNoteActivity.this,getString(R.string.save_as_toast_start) + msg.obj,Toast.LENGTH_LONG).show();
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
	
	public void recycleBitmap(){
		for (int i = 0; i < mEditQuaFragmentsList.size(); i++) {
			if (mEditQuaFragmentsList.get(i)!=null) {
				if (mEditQuaFragmentsList.get(i).mPenView!=null) {
					if (mEditQuaFragmentsList.get(i).mPenView.mExistBitmap!=null&&!mEditQuaFragmentsList.get(i).mPenView.mExistBitmap.isRecycled()) {
						mEditQuaFragmentsList.get(i).mPenView.mExistBitmap.recycle();
					}
					if (mEditQuaFragmentsList.get(i).mPenView.mDrawBitmap!=null&&mEditQuaFragmentsList.get(i).mPenView.mDrawBitmap.isRecycled()) {
						mEditQuaFragmentsList.get(i).mPenView.mDrawBitmap.recycle();
					}
/*					if (mEditQuaFragmentsList.get(i).mPenView.wholeBitmap!=null&&mEditQuaFragmentsList.get(i).mPenView.wholeBitmap.isRecycled()) {
						mEditQuaFragmentsList.get(i).mPenView.wholeBitmap.recycle();
					}*/
				}
			}
		}
		Log.e(TAG, "recycle--existbitmap;drawbitmap");
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
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.id_edit_menu_change_cover:
			//mEditQuaFragment.showChangeBg();
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
