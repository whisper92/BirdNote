package com.bird.note.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.FrameLayout.LayoutParams;

import com.bird.note.R;
import com.bird.note.customer.FullScreenEditText;
import com.bird.note.customer.LevelFlag;
import com.bird.note.customer.PenView;
import com.bird.note.customer.PenView.OnPathListChangeListener;
import com.bird.note.dao.DbHelper;
import com.bird.note.model.BirdMessage;
import com.bird.note.model.BirdNote;
import com.bird.note.model.DBUG;
import com.bird.note.model.QuadrantContent;
import com.bird.note.test.MainActivity;
import com.bird.note.utils.BitmapUtil;
import com.bird.note.utils.NoteApplication;

public class EditQuadrantFragment extends Fragment implements OnClickListener {

	private FullScreenEditText mEditText;

	/*
	 * 包含编辑区域以及象限切换菜单的布局
	 */
	private FrameLayout mWrapFrameLayout;
	/*
	 * 当前所处的模式：绘画，文字，清除
	 */
	public int mCurrentMode = R.id.id_edit_title_pen;
	private QuadrantContent quadrantContent=null;
	private int mNoteId=0;
    private int mCurrentType=BirdMessage.START_TYPE_CREATE_VALUE;
	public int mCurrentQuadrant;
	private PenView mPenView;
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
	
    private NoteApplication mNoteApplication;
	
	/*
	 * 创建笔记时实例化的方式
	 */
	public static EditQuadrantFragment newInstance(int qua, int mode) {
		EditQuadrantFragment editFragment = new EditQuadrantFragment();
		Bundle b = new Bundle();
		b.putInt(BirdMessage.START_TYPE_KEY, BirdMessage.START_TYPE_CREATE_VALUE);
		b.putInt("type", BirdMessage.START_TYPE_CREATE_VALUE);
		b.putInt("quadrant", qua);
		b.putInt("mode", mode);
		editFragment.setArguments(b);	
		return editFragment;
	}

	/*
	 * 更新笔记时实例化的方式
	 */
	public static EditQuadrantFragment newInstance(int mode,QuadrantContent quadrantContent) {
		EditQuadrantFragment editFragment = new EditQuadrantFragment();
		Bundle b = new Bundle();
		b.putInt(BirdMessage.START_TYPE_KEY, BirdMessage.START_TYPE_UPDATE_VALUE);
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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.edit_note_fragment, container,
				false);
		mNoteApplication=(NoteApplication)getActivity().getApplication();
		mNoteId = mNoteApplication.getEditNoteId();
		initEditFragmentView(view);

		Bundle b = this.getArguments();
		if (b != null) {
			mCurrentType=b.getInt(BirdMessage.START_TYPE_KEY);
			if (mCurrentType==BirdMessage.START_TYPE_UPDATE_VALUE) {
				quadrantContent=b.getParcelable("quadrantContent");			
				DBUG.e(mNoteId+"TTTTTTTTTTTTMMMMMMMMMMMMMMMMDDDDDDDDDDDDDDDDD");
			} else {
			
			}
			
			mCurrentMode = b.getInt("mode");
			mCurrentType = b.getInt("type");
			mCurrentQuadrant=b.getInt("quadrant");
           initView(mCurrentType);
			changeCurrentMode(mCurrentMode);
			changeOtherIconState(mCurrentMode);
		}
		return view;
	}
	
	public void initView(int type){
		DBUG.e("initView"+mCurrentQuadrant);
		if (type==BirdMessage.START_TYPE_CREATE_VALUE) {
			initCreateView(type);
		}
		if (type==BirdMessage.START_TYPE_UPDATE_VALUE) {
			initUpdateView(type,quadrantContent);
		}
		mPenView.setOnPathListChangeListenr(new OnPathListChangeListener() {
			@Override
			public void changeState(int undocount, int redocount) {
				mUndoState = undocount > 0 ? true : false;
				mRedoState = redocount > 0 ? true : false;
               changeStateOfUndoRedo(mUndoState, mRedoState);
			}
		});
		mPenView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}
	
	public void initCreateView(int type){
		mPenView = new PenView(getActivity());
		
	}
	
	/**
	 * 将已经存在的内容绘制到penView上去
	 * @param type
	 * @param mBirdNote
	 */
	public void initUpdateView(int type,QuadrantContent quadrantContent){
		DBUG.e("initUpdateView"+type);
		mPenView = new PenView(getActivity());
		mPenView.setExistBitmap(BitmapUtil.decodeBytesToBitmap(quadrantContent.quadrantdraw));
		mPenView.invalidateExistBitmap();
	}

	public void initEditFragmentView(View view) {
		DBUG.e("initEditFragmentView");
		mWrapFrameLayout = (FrameLayout) view.findViewById(R.id.id_edit_main_fl_warpper);
		mEditText = (FullScreenEditText) view.findViewById(R.id.id_edit_main_et);
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

	}

	/**
	 * 设置当前的编辑模式
	 * 
	 * @param clickID
	 */
	public void changeCurrentMode(int clickID) {
		mCurrentMode = clickID;
		if (clickID == R.id.id_edit_title_pen) {
			if (mFirstComeIn) {
				mWrapFrameLayout.addView(mPenView);
				mFirstComeIn = false;
			}
			mPenView.bringToFront();
			mEditText.setCursorVisible(false);
			mPenView.initDrawPaint();
			changeStateOfUndoRedo(mUndoState, mRedoState);
		}
		if (clickID == R.id.id_edit_title_text) {
			mEditText.bringToFront();
			mEditText.setCursorVisible(true);
		}
		if (clickID == R.id.id_edit_title_clean) {
			if (mFirstComeIn) {
				mWrapFrameLayout.addView(mPenView);
				mFirstComeIn = false;
			}
			mPenView.bringToFront();
			mEditText.setCursorVisible(false);
			mPenView.setCleanPaint();
			changeStateOfUndoRedo(mUndoState, mRedoState);
		}

	}

	@Override
	public void onClick(View v) {
		changeOtherIconState(v.getId());
		switch (v.getId()) {
		case R.id.id_edit_title_pre:
			mPenView.undo();
			break;
		case R.id.id_edit_title_next:
			mPenView.redo();
			break;
		case R.id.id_edit_title_more:
			break;
		case R.id.id_edit_title_save:
			//mPenView.savePicture(mCurrentQuadrant);
			if ((((EditNoteActivity)getActivity()).mNoteEditType)==BirdMessage.NOTE_EDIT_TYPE_UPDATE) {
				DBUG.e("保存修改过的笔记");
				saveUpdateNote();
			} else {
				DBUG.e("保存新建的笔记");
				saveNewNote();
			}
			
			break;
		case R.id.id_edit_title_pen:
			changeCurrentMode(v.getId());
			break;
		case R.id.id_edit_title_text:
			changeCurrentMode(v.getId());
			break;
		case R.id.id_edit_title_clean:
			changeCurrentMode(v.getId());
			break;
		default:
			break;
		}
	}
	
	public void saveNewNote(){
		  new Handler().post(new Runnable() {
			@Override
			public void run() {			
				new DbHelper(getActivity()).insertNewNote(((EditNoteActivity)getActivity()).generateNewNote());
				((EditNoteActivity)getActivity()).editHandler.sendEmptyMessage(BirdMessage.SAVE_OVER);
			}
		});	  
	}

	public void saveUpdateNote(){
		  new Handler().post(new Runnable() {
			@Override
			public void run() {			
				DBUG.e("update id"+mNoteId);
				new DbHelper(getActivity()).updateNoteById(((EditNoteActivity)getActivity()).generateNewNote(),mNoteId+"");
				((EditNoteActivity)getActivity()).editHandler.sendEmptyMessage(BirdMessage.SAVE_OVER);
			}
		});	  
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
	
	/**
	 * 返回当前象限的文本内容
	 * @return
	 */
	public String getTextContent(){
		if (mEditText!=null) {
			return mEditText.getText().toString();
		} else {
			return null;
		}
		
	}
	
	/**
	 * 返回当前象限绘制的内容
	 * @return
	 */
	public byte[] getQuadrantDrawContentBytes(){
		return BitmapUtil.decodeBitmapToBytes(mPenView.mDrawBitmap);
	}
	public Bitmap getQuadrantDrawContentBitmap(){
		return mPenView.mDrawBitmap;
	}

}
