package com.bird.note.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;

import com.bird.note.R;
import com.bird.note.customer.BirdInputTitleDialog;
import com.bird.note.customer.ChooseEditBgPopMenu;
import com.bird.note.customer.ChooseEditBgPopMenu.OnChangeBackgroundListener;
import com.bird.note.customer.PenView;
import com.bird.note.customer.PenView.OnPathListChangeListener;
import com.bird.note.customer.PopEraserBox;
import com.bird.note.customer.PopEraserBox.OnEraserChangedListener;
import com.bird.note.customer.PopPenBox;
import com.bird.note.customer.PopPenBox.OnPaintChangedListener;
import com.bird.note.dao.DbHelper;
import com.bird.note.model.BirdMessage;
import com.bird.note.model.BirdNote;
import com.bird.note.model.QuadrantContent;
import com.bird.note.model.SavedPaint;

import com.bird.note.utils.BitmapUtil;
import com.bird.note.utils.CommonUtils;
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
public class EditQuadrantFragment extends Fragment implements OnClickListener{
	private static String TAG = "EditQuadrantFragment";
    //private EditText mInputTitleEditText;//[BIRD][BIRD_ALI_IOS8_SYSTEMUI][bug-106178][随笔删除提示框显示问题] huangzhangbin 20150107
	private NoteApplication mNoteApplication;
	/*
	 * 包含编辑区域以及象限切换菜单的布局
	 */
	private FrameLayout mWrapFrameLayout;
	public EditText mEditText;
	public PenView mPenView;
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
	/*
	 * 当前所处的模式：绘画，文字，清除
	 */
	public int mCurrentMode = R.id.id_edit_title_pen;
	private QuadrantContent quadrantContent = null;
	private int mCurrentType = BirdMessage.START_TYPE_CREATE_VALUE;
	public int mCurrentQuadrant;
	private int[] mEditedQuadrants;

	public String mTitleString = "";
	private BirdInputTitleDialog mBirdInputTitleDialog;
	private Handler mainHandler = null;
	private BirdNote mBirdNote;
	public ChooseEditBgPopMenu chooseEditBgPopMenu;
	private DbHelper mDbHelper;
	private SavedPaint mSavedPaint;
	private EditNoteActivity mEditNoteActivity = null;
	private PopPenBox mPopPenBox;
	private PopEraserBox mPopEraserBox;
	private int mPenHasSelected = 0;
	private int mEraserHasSelected = 0;
	
	private boolean mPenBoxOpened = false;
	private boolean mEraserBoxOpened = false;
	/*[BIRD][BIRD_ALI_IOS8_SYSTEMUI][bug-106178][随笔删除提示框显示问题] huangzhangbin 20150107 begin*/
    private EditText mInputNewNoteTitleEditText = null;
    private EditText mInputSaveAsTitleEditText = null;
    /*[BIRD][BIRD_ALI_IOS8_SYSTEMUI][bug-106178][随笔删除提示框显示问题] huangzhangbin 20150107 end*/
	/**
	 * 创建笔记时实例化的方式
	 */
	public static EditQuadrantFragment newInstance(int qua, int mode) {
		EditQuadrantFragment editFragment = new EditQuadrantFragment();
		Bundle b = new Bundle();
		b.putInt(BirdMessage.START_TYPE_KEY, BirdMessage.START_TYPE_CREATE_VALUE);
		b.putInt("type", BirdMessage.START_TYPE_CREATE_VALUE);
		b.putInt("quadrant", qua);
		editFragment.setArguments(b);
		return editFragment;
	}

	/**
	 * 更新笔记时实例化的方式
	 */
	public static EditQuadrantFragment newInstance(int mode, QuadrantContent quadrantContent) {
		EditQuadrantFragment editFragment = new EditQuadrantFragment();
		Bundle b = new Bundle();
		b.putInt(BirdMessage.START_TYPE_KEY, BirdMessage.START_TYPE_UPDATE_VALUE);
		b.putInt("type", BirdMessage.START_TYPE_UPDATE_VALUE);
		b.putInt("quadrant", quadrantContent.quadrant);
		b.putParcelable("quadrantContent", quadrantContent);
		editFragment.setArguments(b);
		return editFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	AlertDialog.Builder saveNewNoteBuilder = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mEditNoteActivity = ((EditNoteActivity) getActivity());
		mainHandler = mEditNoteActivity.editHandler;
		mDbHelper = new DbHelper(getActivity());
		chooseEditBgPopMenu = new ChooseEditBgPopMenu(getActivity());
        /*[BIRD][BIRD_ALI_IOS8_SYSTEMUI][bug-106178][随笔删除提示框显示问题] huangzhangbin 20150107 begin*/
        //saveNewNoteBuilder = PopMenuManager.createSaveNewNoteAlertDialog( getActivity(), R.string.input_title_dialog_title,
           // mInputTitleEditText, null);
        /*[BIRD][BIRD_ALI_IOS8_SYSTEMUI][bug-106178][随笔删除提示框显示问题] huangzhangbin 20150107 end*/
		View view = inflater.inflate(R.layout.edit_note_fragment, container, false);
		mNoteApplication = (NoteApplication) getActivity().getApplication();
		mEditedQuadrants = mNoteApplication.getEditedQuadrants();
		initEditFragmentView(view);
		Bundle b = this.getArguments();
		if (b != null) {
			mCurrentType = b.getInt(BirdMessage.START_TYPE_KEY);
			if (mCurrentType == BirdMessage.START_TYPE_UPDATE_VALUE) {
				quadrantContent = b.getParcelable("quadrantContent");
			} else {

			}

			mCurrentMode = mNoteApplication.getCurrentEditMode();
			mCurrentType = b.getInt("type");
			mCurrentQuadrant = b.getInt("quadrant");
			mEditedQuadrants[mCurrentQuadrant] = 1;
			mNoteApplication.setEditedQuadrants(mEditedQuadrants);
			initView(mCurrentType);
			changeCurrentMode(mCurrentMode);
			changeOtherIconState(mCurrentMode);
		}

		return view;
	}

	public void initView(int type) {
		if (type == BirdMessage.START_TYPE_CREATE_VALUE) {
			initCreateView(type);
		}
		if (type == BirdMessage.START_TYPE_UPDATE_VALUE) {
			initUpdateView(type, quadrantContent);
		}
		mPenView.setOnPathListChangeListenr(mOnPathListChangeListener);
		mPenView.setLayoutParams(new FrameLayout.LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
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
	}
	
	public void initCreateView(int type) {
		mPenView = new PenView(getActivity());
	}

	/**
	 * 将已经存在的内容绘制到penView上去
	 *
	 * @param type
	 * @param mBirdNote
	 */
	private Bitmap quarBitmap = null;

	public void initUpdateView(int type, QuadrantContent quadrantContent) {
		mPenView = new PenView(getActivity());
		quarBitmap = BitmapUtil.decodeBytesToBitmap(quadrantContent.quadrantdraw);
		mPenView.setExistBitmap(quarBitmap);

		mPenView.invalidateExistBitmap();
		mEditText.setText(quadrantContent.textcontent);

		if (((EditNoteActivity) getActivity()).mBirdNote != null) {
			mBirdNote = ((EditNoteActivity) getActivity()).mBirdNote;
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (quarBitmap != null && !quarBitmap.isRecycled()) {
			quarBitmap.recycle();
			Log.e(TAG, "recycled-quarBitmap");
		}
		System.gc();
	}

	public void initEditFragmentView(View view) {

		mWrapFrameLayout = (FrameLayout) view.findViewById(R.id.id_edit_main_fl_warpper);
		mWrapFrameLayout.setBackgroundResource(mNoteApplication.getEditBackground());
		mEditText = (EditText) view.findViewById(R.id.id_edit_main_et);
		
		edit_Pen = (ImageView) view.findViewById(R.id.id_edit_title_pen);
		edit_Text = (ImageView) view.findViewById(R.id.id_edit_title_text);
		edit_Clean = (ImageView) view.findViewById(R.id.id_edit_title_clean);
		menu_Undo = (ImageView) view.findViewById(R.id.id_edit_title_pre);
		menu_Redo = (ImageView) view.findViewById(R.id.id_edit_title_next);
		menu_More = (ImageView) view.findViewById(R.id.id_edit_title_more);
		menu_Save = (ImageView) view.findViewById(R.id.id_edit_title_save);

		edit_Pen.setOnClickListener(this);
		edit_Text.setOnClickListener(this);
		edit_Clean.setOnClickListener(this);
		menu_Undo.setOnClickListener(this);
		menu_Redo.setOnClickListener(this);
		menu_More.setOnClickListener(this);
		menu_Save.setOnClickListener(this);

		menu_Undo.setEnabled(false);
		menu_Redo.setEnabled(false);

		mSavedPaint = new SavedPaint(getActivity());
		
		chooseEditBgPopMenu.setOnChangeBackgroundListener(new OnChangeBackgroundListener() {

					@Override
					public void changeBackground(int bgRsr) {
						mWrapFrameLayout.setBackgroundResource(bgRsr);
						mNoteApplication.setEditBackground(bgRsr);
						if (chooseEditBgPopMenu != null && chooseEditBgPopMenu.isShowing()) {
							chooseEditBgPopMenu.dismiss();
						}

					}
				});
	}

	/* 更改背景 */
	public void showChangeBg() {

		chooseEditBgPopMenu.showAtLocation(mWrapFrameLayout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
	}

  /* 另存为 */
	public void showSaveAs() {
        /*[BIRD][BIRD_ALI_IOS8_SYSTEMUI][bug-106178][随笔删除提示框显示问题] huangzhangbin 20150107 begin*/
        //mInputSaveAsTitleEditText = new EditText(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.bird_edit_text_layout, null);
        mInputSaveAsTitleEditText = (EditText) view.findViewById(R.id.editText);
		saveNewNoteBuilder = PopMenuManager.createSaveNewNoteAlertDialog( getActivity(), R.string.input_title_dialog_title, view, saveAsDialogListener);
        /*[BIRD][BIRD_ALI_IOS8_SYSTEMUI][bug-106178][随笔删除提示框显示问题] huangzhangbin 20150107 end*/
		mBirdInputTitleDialog = new BirdInputTitleDialog(getActivity(),android.R.style.Theme_Holo_Light_Dialog);
		mBirdInputTitleDialog.setTitle(R.string.save_as_title);
		mBirdInputTitleDialog.setOnConfirmClickListener(ConfirmSaveAsPngListener);
        /*[BIRD][BIRD_ALI_IOS8_SYSTEMUI][bug-106178][随笔删除提示框显示问题] huangzhangbin 20150107 begin*/
		//mBirdInputTitleDialog.show();
        saveNewNoteBuilder.create().show();
		if (mCurrentType == BirdMessage.START_TYPE_UPDATE_VALUE) {
            mInputSaveAsTitleEditText.setText(((EditNoteActivity) getActivity()).mBirdNote.title + "_qua" + mCurrentQuadrant);
			//mBirdInputTitleDialog.setInputContent(((EditNoteActivity) getActivity()).mBirdNote.title + "_qua" + mCurrentQuadrant);
		} else {
            mInputSaveAsTitleEditText.setText(CommonUtils.getDefaultTitle(getActivity()) + "_qua" + mCurrentQuadrant);
			//mBirdInputTitleDialog.setInputContent(CommonUtils.getDefaultTitle(getActivity()) + "_qua" + mCurrentQuadrant);
		}
        /*[BIRD][BIRD_ALI_IOS8_SYSTEMUI][bug-106178][随笔删除提示框显示问题] huangzhangbin 20150107 end*/
	}
    /*[BIRD][BIRD_ALI_IOS8_SYSTEMUI][bug-106178][随笔删除提示框显示问题] huangzhangbin 20150107 begin*/
    android.content.DialogInterface.OnClickListener saveAsDialogListener = new android.content.DialogInterface.OnClickListener(){
        public void onClick(DialogInterface dialog, int which){
            if(which == -1) {
			mainHandler.sendEmptyMessage(BirdMessage.SAVE_RUNNABLE_START);
			if (mEditText != null) {
				mEditText.setCursorVisible(false);
			}
			new SaveAsThread().start();
            }
        }
    };
    /*[BIRD][BIRD_ALI_IOS8_SYSTEMUI][bug-106178][随笔删除提示框显示问题] huangzhangbin 20150107 end*/

	public Bitmap getTextBitmap() {
		mEditText.setDrawingCacheEnabled(true);
		Bitmap bmp = Bitmap.createBitmap(mEditText.getDrawingCache(), 0, 0, mEditText.getWidth(), mEditText.getHeight());
		mEditText.destroyDrawingCache();
		return bmp;
	}

	public OnClickListener ConfirmSaveAsPngListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			mBirdInputTitleDialog.dismiss();
			mainHandler.sendEmptyMessage(BirdMessage.SAVE_RUNNABLE_START);
			if (mEditText != null) {
				mEditText.setCursorVisible(false);
			}
			new SaveAsThread().start();
		}
	};

	public String mSavePath = "";
	Bitmap bgBitmap = null;

	public class SaveAsThread extends Thread {

		@Override
		public void run() {
			Bitmap allbitmap = getAllBitmap();
            /*[BIRD][BIRD_ALI_IOS8_SYSTEMUI][bug-106178][随笔删除提示框显示问题] huangzhangbin 20150107 begin*/
			/*mSavePath = CommonUtils.getSavePath() + "/" + mBirdInputTitleDialog.getContent() + ".png";
			BitmapUtil.writeBytesToFile( BitmapUtil.decodeBitmapToBytes(allbitmap), "/" + mBirdInputTitleDialog.getContent());*/
			mSavePath = CommonUtils.getSavePath() + "/" + mInputSaveAsTitleEditText.getText().toString() + ".png";
			BitmapUtil.writeBytesToFile( BitmapUtil.decodeBitmapToBytes(allbitmap), "/" + mInputSaveAsTitleEditText.getText().toString());
            /*[BIRD][BIRD_ALI_IOS8_SYSTEMUI][bug-106178][随笔删除提示框显示问题] huangzhangbin 20150107 end*/
			mainHandler.obtainMessage(BirdMessage.SAVE_AS_OVER, mSavePath).sendToTarget();

			if (bgBitmap != null && !bgBitmap.isRecycled()) {
				bgBitmap.recycle();
			}
			if (allbitmap != null && !allbitmap.isRecycled()) {
				allbitmap.recycle();
			}
			Log.e(TAG, "recycle--bgBitmap;allbitmap");
			System.gc();
		}
	};

	public String getSavePath() {
		return mSavePath;
	}

	public Bitmap getAllBitmap() {
		/* 已回收 */
		bgBitmap = BitmapUtil.decodeDrawableToBitmap(getActivity().getResources().getDrawable( mNoteApplication.getEditBackground()));
		return BitmapUtil.mergeBitmap(getActivity(), bgBitmap, mPenView.mDrawBitmap, getTextBitmap());
	}



	/**
	 * 设置当前的编辑模式
	 *
	 * @param clickID
	 */
	public void changeCurrentMode(int clickID) {
		mNoteApplication = (NoteApplication) getActivity().getApplication();

		if (clickID == R.id.id_edit_title_pen) {
			mCurrentMode = R.id.id_edit_title_pen;
			mNoteApplication.setCurrentEditMode(mCurrentMode);
			hideInputMethod();
			if (mFirstComeIn) {
				mWrapFrameLayout.addView(mPenView);
				mFirstComeIn = false;
			}
			changeStateOfUndoRedo(mUndoState, mRedoState);
			mWrapFrameLayout.bringChildToFront(mPenView);		
			mEditText.setCursorVisible(false);
			mPenView.initDrawPaint();

		} else if (clickID == R.id.id_edit_title_text) {
			mCurrentMode = R.id.id_edit_title_text;
			mWrapFrameLayout.bringChildToFront(mEditText);			
			mNoteApplication.setCurrentEditMode(mCurrentMode);	
			mEditText.setCursorVisible(true);
			mEditText.setFocusable(true);
			mEditText.setFocusableInTouchMode(true);
			mEditText.requestFocus();
			
			showInputMethod();
		} else if (clickID == R.id.id_edit_title_clean) {
			mCurrentMode = R.id.id_edit_title_clean;
			mNoteApplication.setCurrentEditMode(mCurrentMode);
			hideInputMethod();
			if (mFirstComeIn) {
				mWrapFrameLayout.addView(mPenView);
				mFirstComeIn = false;
			}
			changeStateOfUndoRedo(mUndoState, mRedoState);
			mWrapFrameLayout.bringChildToFront(mPenView);
			mEditText.setCursorVisible(false);
			mPenView.setCleanPaint();
			
		} else {
		}
		mWrapFrameLayout.requestLayout();
	}

	public void saveNote() {
		hideInputMethod();
		mNoteApplication.setEdited(false);
		if ((((EditNoteActivity) getActivity()).mNoteEditType) == BirdMessage.NOTE_EDIT_TYPE_UPDATE) {
			saveUpdateNote();
		} else {
			saveNewNote();
		}

	}

	public void saveNewNote() {
        /*[BIRD][BIRD_ALI_IOS8_SYSTEMUI][bug-106178][随笔删除提示框显示问题] huangzhangbin 20150107 begin*/
        //mInputNewNoteTitleEditText = new EditText(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.bird_edit_text_layout, null);
        mInputNewNoteTitleEditText = (EditText) view.findViewById(R.id.editText);
		saveNewNoteBuilder = PopMenuManager.createSaveNewNoteAlertDialog(getActivity(), R.string.input_title_dialog_title, view, saveNewNoteDialogListener);
        /*[BIRD][BIRD_ALI_IOS8_SYSTEMUI][bug-106178][随笔删除提示框显示问题] huangzhangbin 20150107 end*/
		mBirdInputTitleDialog = new BirdInputTitleDialog(getActivity(),
				android.R.style.Theme_Holo_Light_Dialog);
		mBirdInputTitleDialog.setTitle(R.string.input_title_dialog_title);
		mBirdInputTitleDialog.setOnConfirmClickListener(ConfirmSaveNewNoteClickListener);
        /*[BIRD][BIRD_ALI_IOS8_SYSTEMUI][bug-106178][随笔删除提示框显示问题] huangzhangbin 20150107 begin*/
		//mBirdInputTitleDialog.show();
        saveNewNoteBuilder.create().show();
        mInputNewNoteTitleEditText.setText(CommonUtils.getDefaultTitle(getActivity()));
		//mBirdInputTitleDialog.setInputContent(CommonUtils.getDefaultTitle(getActivity()));
        /*[BIRD][BIRD_ALI_IOS8_SYSTEMUI][bug-106178][随笔删除提示框显示问题] huangzhangbin 20150107 end*/
	}

    /*[BIRD][BIRD_ALI_IOS8_SYSTEMUI][bug-106178][随笔删除提示框显示问题] huangzhangbin 20150107 begin*/
    android.content.DialogInterface.OnClickListener saveNewNoteDialogListener = new android.content.DialogInterface.OnClickListener(){
        public void onClick(DialogInterface dialog, int which){
            if(which == -1) {
                mTitleString = mInputNewNoteTitleEditText.getText().toString();
				mainHandler.sendEmptyMessage(BirdMessage.SAVE_RUNNABLE_START);
				new SaveNewNoteThread().start();
            }
        }
    };
    /*[BIRD][BIRD_ALI_IOS8_SYSTEMUI][bug-106178][随笔删除提示框显示问题] huangzhangbin 20150107 end*/

	public void saveUpdateNote() {
		mainHandler.sendEmptyMessage(BirdMessage.SAVE_RUNNABLE_START);
		new SaveUpdateNoteThread().start();
	}

	/**
	 * 返回当前象限的文本内容
	 *
	 * @return
	 */
	public String getTextContent() {
		if (mEditText != null) {
			return mEditText.getText().toString();
		} else {
			return null;
		}

	}

	/**
	 * 返回当前象限绘制的内容
	 *
	 * @return
	 */
	/* 需回收 */
	public byte[] getQuadrantDrawContentBytes() {
		return BitmapUtil.decodeBitmapToBytes(mPenView.mDrawBitmap);
	}

	public Bitmap getQuadrantDrawContentBitmap() {
		return mPenView.mDrawBitmap;
	}

	public QuadrantContent generateEditQuadrantContent() {
		QuadrantContent quadrantContent = new QuadrantContent();
		quadrantContent.quadrant = mCurrentQuadrant;
		quadrantContent.quadrantdraw = getQuadrantDrawContentBytes();
		quadrantContent.textcontent = getTextContent();
		return quadrantContent;
	}

	/**
	 * 监听确认保存新笔记
	 */
	public OnClickListener ConfirmSaveNewNoteClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.id_alertdiaolg_confirm) {
				mTitleString = mBirdInputTitleDialog.getContent();
				mBirdInputTitleDialog.dismiss();
				mainHandler.sendEmptyMessage(BirdMessage.SAVE_RUNNABLE_START);
				new SaveNewNoteThread().start();
			}
		}
	};

	private class SaveNewNoteThread extends Thread {
		@Override
		public void run() {
			mDbHelper.insertNewNote(((EditNoteActivity) getActivity()).generateNewNote());
			mainHandler.sendEmptyMessage(BirdMessage.SAVE_OVER);
		}
	}

	private class SaveUpdateNoteThread extends Thread {
		@Override
		public void run() {
			NoteApplication noteApplication = (NoteApplication) getActivity().getApplication();
			mDbHelper.updateNoteById( ((EditNoteActivity) getActivity()).generateNewNote(), noteApplication.getEditNoteId() + "");
			mainHandler.sendEmptyMessage(BirdMessage.SAVE_OVER);
		}
	}

	public void showInputMethod() {
		if (mEditText != null) {
			InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
			imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
		}
	}

	public void hideInputMethod() {
		View view = getActivity().getWindow().peekDecorView();
		if (view != null) {
			InputMethodManager inputmanger = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
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
			mPenHasSelected += 1;
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
			mEraserHasSelected += 1;
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
		mPopPenBox = new PopPenBox(getActivity());
		mPopPenBox.setOnPaintChangedListener(new OnPaintChangedListener() {
			@Override
			public void changePaint(Paint paint) {
				mPenView.setDrawPaintColor(paint.getColor());
				mPenView.setDrawPaintWidth(paint.getStrokeWidth());
				mSavedPaint.savePaintColor(paint.getColor());
				mSavedPaint.savePaintWidth(paint.getStrokeWidth());
			}
		});
	}

	public void createEraserBox() {
		mPopEraserBox = new PopEraserBox(getActivity(), new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mPopEraserBox.isShowing()) {
					mPopEraserBox.dismiss();
				}
				PopMenuManager.createDeleteAlertDialog(getActivity(), R.string.alert_clear_all, ConfirmClearAllListener);
			}
		});
		mPopEraserBox.setOnPaintChangedListener(new OnEraserChangedListener() {
			@Override
			public void changePaint(Paint paint) {
				mPenView.setCleanPaintWidth(paint.getStrokeWidth());
				mSavedPaint.saveCleanPaintWidth(paint.getStrokeWidth());
			}
		});
	}

	public android.content.DialogInterface.OnClickListener ConfirmClearAllListener = new android.content.DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			if (which == -1) {
				mPenView.clearAll();
				mNoteApplication.setEdited(true);
			}

		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_edit_title_pen:
			mCurrentMode = R.id.id_edit_title_pen;
			mNoteApplication.setCurrentEditMode(mCurrentMode);
            changeCurrentMode(R.id.id_edit_title_pen);
			changeOtherIconState(mCurrentMode);
			togglePenBox(mCurrentMode);
			mPenHasSelected += 1;
			mEraserHasSelected = 0;
			break;

		case R.id.id_edit_title_text:
			mCurrentMode = R.id.id_edit_title_text;
			mNoteApplication.setCurrentEditMode(mCurrentMode);
			changeCurrentMode(R.id.id_edit_title_text);
			changeOtherIconState(mCurrentMode);
			mPenHasSelected = 0;
			mEraserHasSelected = 0;
			break;

		case R.id.id_edit_title_clean:
			mCurrentMode = R.id.id_edit_title_clean;
			mNoteApplication.setCurrentEditMode(mCurrentMode);
			changeCurrentMode(R.id.id_edit_title_clean);
			changeOtherIconState(mCurrentMode);
			toggleEraserBox(mCurrentMode);
			mPenHasSelected = 0;
			mEraserHasSelected += 1;
			break;

		case R.id.id_edit_title_pre:
			mPenView.undo();
			break;

		case R.id.id_edit_title_next:
			mPenView.redo();
			break;

		case R.id.id_edit_title_more:
			((EditNoteActivity)getActivity()).openOptionsMenu();
			break;

		case R.id.id_edit_title_save:
			saveNote();
			break;

		default:
			break;
		}
		
	}

}
