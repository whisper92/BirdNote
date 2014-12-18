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
import com.bird.note.ui.EditNoteActivity.OnClickTitleMenuListener;
import com.bird.note.utils.BitmapUtil;
import com.bird.note.utils.CommonUtils;
import com.bird.note.utils.NoteApplication;

public class EditQuadrantFragment extends Fragment {
	private static String TAG = "EditQuadrantFragment";
	private EditText mInputTitleEditText;
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
	private DbHelper mDbHelper ;
	
	private EditNoteActivity mEditNoteActivity = null;

	/**
	 * 创建笔记时实例化的方式
	 */
	public static EditQuadrantFragment newInstance(int qua, int mode) {
		EditQuadrantFragment editFragment = new EditQuadrantFragment();
		Bundle b = new Bundle();
		b.putInt(BirdMessage.START_TYPE_KEY,BirdMessage.START_TYPE_CREATE_VALUE);
		b.putInt("type", BirdMessage.START_TYPE_CREATE_VALUE);
		b.putInt("quadrant", qua);
		b.putInt("mode", mode);
		editFragment.setArguments(b);
		return editFragment;
	}

	/**
	 * 更新笔记时实例化的方式
	 */
	public static EditQuadrantFragment newInstance(int mode,QuadrantContent quadrantContent) {
		EditQuadrantFragment editFragment = new EditQuadrantFragment();
		Bundle b = new Bundle();
		b.putInt(BirdMessage.START_TYPE_KEY,BirdMessage.START_TYPE_UPDATE_VALUE);
		b.putInt("type", BirdMessage.START_TYPE_UPDATE_VALUE);
		b.putInt("quadrant", quadrantContent.quadrant);
		b.putInt("mode", mode);
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		mEditNoteActivity = ((EditNoteActivity) getActivity());
		mainHandler =mEditNoteActivity.editHandler;
		mDbHelper = new DbHelper(getActivity());
		chooseEditBgPopMenu = new ChooseEditBgPopMenu(getActivity());
		saveNewNoteBuilder = PopMenuManager.createSaveNewNoteAlertDialog(getActivity(), R.string.input_title_dialog_title, mInputTitleEditText, null);
		View view = inflater.inflate(R.layout.edit_note_fragment, container,false);
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

			mCurrentMode = b.getInt("mode");
			mCurrentType = b.getInt("type");
			mCurrentQuadrant = b.getInt("quadrant");
			mEditedQuadrants[mCurrentQuadrant] = 1;
			mNoteApplication.setEditedQuadrants(mEditedQuadrants);
			initView(mCurrentType);
			changeCurrentMode(mCurrentMode);
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
		mPenView.setOnPathListChangeListenr(((EditNoteActivity)getActivity()).mOnPathListChangeListener);
		mPenView.setLayoutParams(new FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
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
		if (quarBitmap!=null&&!quarBitmap.isRecycled()) {
			quarBitmap.recycle();
			Log.e(TAG,"recycled-quarBitmap");
		}
		System.gc();
	}
	
	public void initEditFragmentView(View view) {
		((EditNoteActivity)getActivity()).setOnClickTitleMenuListener(mOnClickTitleMenuListener);
		mWrapFrameLayout = (FrameLayout) view.findViewById(R.id.id_edit_main_fl_warpper);
		mWrapFrameLayout.setBackgroundResource(mNoteApplication.getEditBackground());
		mEditText = (EditText) view.findViewById(R.id.id_edit_main_et);
		chooseEditBgPopMenu
				.setOnChangeBackgroundListener(new OnChangeBackgroundListener() {

					@Override
					public void changeBackground(int bgRsr) {
						mWrapFrameLayout.setBackgroundResource(bgRsr);
						mNoteApplication.setEditBackground(bgRsr);
						if (chooseEditBgPopMenu != null
								&& chooseEditBgPopMenu.isShowing()) {
							chooseEditBgPopMenu.dismiss();
						}

					}
				});
	}


	public void showChangeBg(){
		/* 更改背景 */
		chooseEditBgPopMenu.showAtLocation(mWrapFrameLayout,
				Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
	}
	
	public void showSaveAs(){
		/* 另存为 */
		mBirdInputTitleDialog = new BirdInputTitleDialog(getActivity(),android.R.style.Theme_Holo_Light_Dialog);
		mBirdInputTitleDialog.setTitle(R.string.save_as_title);
		mBirdInputTitleDialog.setOnConfirmClickListener(ConfirmSaveAsPngListener);
		mBirdInputTitleDialog.show();
		if (mCurrentType == BirdMessage.START_TYPE_UPDATE_VALUE) {
			mBirdInputTitleDialog.setInputContent(((EditNoteActivity) getActivity()).mBirdNote.title+ "_qua" + mCurrentQuadrant);
		} else {
			mBirdInputTitleDialog.setInputContent(CommonUtils.getDefaultTitle() + "_qua" + mCurrentQuadrant);
		}
	}
	
	public Bitmap getTextBitmap() {
		mEditText.setDrawingCacheEnabled(true);
		Bitmap bmp = Bitmap.createBitmap(mEditText.getDrawingCache(), 0, 0,mEditText.getWidth(), mEditText.getHeight());
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
			mSavePath = CommonUtils.getSavePath() + "/"+ mBirdInputTitleDialog.getContent() + ".png";
			BitmapUtil.writeBytesToFile(BitmapUtil.decodeBitmapToBytes(allbitmap),"/" + mBirdInputTitleDialog.getContent());
			mainHandler.obtainMessage(BirdMessage.SAVE_AS_OVER, mSavePath).sendToTarget();
			
			if (bgBitmap!=null&&!bgBitmap.isRecycled()) {
				bgBitmap.recycle();
			}
			if (allbitmap!=null&&!allbitmap.isRecycled()) {
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
		/*已回收*/
		bgBitmap = BitmapUtil.decodeDrawableToBitmap(getActivity().getResources().getDrawable(mNoteApplication.getEditBackground()));
		return BitmapUtil.mergeBitmap(getActivity(), bgBitmap,mPenView.mDrawBitmap, getTextBitmap());
	}
	

	public OnClickTitleMenuListener mOnClickTitleMenuListener = new OnClickTitleMenuListener() {
		
		@Override
		public void clickMenu(int menuid) {
			changeCurrentMode(menuid);			
		}
	};
	
	/**
	 * 设置当前的编辑模式
	 * 
	 * @param clickID
	 */
	public void changeCurrentMode(int clickID) {
		
		mCurrentMode = clickID;
		
		if (clickID == R.id.id_edit_title_pen) {
			Log.e(TAG, "changeCurrentMode----->id_edit_title_pen");
			mNoteApplication.setCurrentEditMode(mCurrentMode);
			hideInputMethod();
			if (mFirstComeIn) {
				mWrapFrameLayout.addView(mPenView);
				mFirstComeIn = false;
			}
			mPenView.bringToFront();
			mEditText.setCursorVisible(false);
			mPenView.initDrawPaint();
		} else if (clickID == R.id.id_edit_title_text) {
			Log.e(TAG, "changeCurrentMode----->id_edit_title_text");
			mNoteApplication.setCurrentEditMode(mCurrentMode);
			mEditText.bringToFront();
			mEditText.setCursorVisible(true);
			mEditText.setFocusable(true);
			mEditText.setFocusableInTouchMode(true);
			mEditText.requestFocus();
			showInputMethod();
		} else if (clickID == R.id.id_edit_title_clean) {
			Log.e(TAG, "changeCurrentMode----->id_edit_title_clean");
			mNoteApplication.setCurrentEditMode(mCurrentMode);
			hideInputMethod();
			if (mFirstComeIn) {
				mWrapFrameLayout.addView(mPenView);
				mFirstComeIn = false;
			}
			mPenView.bringToFront();
			mEditText.setCursorVisible(false);
			mPenView.setCleanPaint();
		} else {
			Log.e(TAG, "changeCurrentMode----->else");
		}

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
	   mBirdInputTitleDialog = new BirdInputTitleDialog(getActivity(),android.R.style.Theme_Holo_Light_Dialog);
	    mBirdInputTitleDialog.setTitle(R.string.input_title_dialog_title);
        mBirdInputTitleDialog.setOnConfirmClickListener(ConfirmSaveNewNoteClickListener);
		mBirdInputTitleDialog.show();
		mBirdInputTitleDialog.setInputContent(CommonUtils.getDefaultTitle());
	}

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
	/*需回收*/
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
			mDbHelper.updateNoteById(((EditNoteActivity) getActivity()).generateNewNote(),noteApplication.getEditNoteId() + "");
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
	
	
	
	
}
