package com.bird.note.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.Display;
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
import android.widget.RelativeLayout;

import com.bird.note.R;
import com.bird.note.customer.BirdAlertDialog;
import com.bird.note.customer.BirdInputTitleDialog;
import com.bird.note.customer.BirdPopMenu;
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
import com.bird.note.model.DBUG;
import com.bird.note.model.QuadrantContent;
import com.bird.note.model.SavedPaint;
import com.bird.note.utils.BitmapUtil;
import com.bird.note.utils.CommonUtils;
import com.bird.note.utils.NoteApplication;

public class EditQuadrantFragment extends Fragment implements OnClickListener {
    private NoteApplication mNoteApplication;
	private FrameLayout mEditMainLayout;
	/*
	 * 包含编辑区域以及象限切换菜单的布局
	 */
	private FrameLayout mWrapFrameLayout;
	private EditText mEditText;
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
	/*
	 * 当前所处的模式：绘画，文字，清除
	 */
	public int mCurrentMode = R.id.id_edit_title_pen;
	private QuadrantContent quadrantContent=null;

    private int mCurrentType=BirdMessage.START_TYPE_CREATE_VALUE;
	public int mCurrentQuadrant;
    private int[] mEditedQuadrants;
    
    private RelativeLayout mHeaderLayout;
    private PopPenBox mPopPenBox;
    private PopEraserBox mPopEraserBox;
    public BirdPopMenu mPopMenu;
    private boolean mPenBoxOpened=false;
    private boolean mEraserBoxOpened=false;
    private boolean mPopMenuOpened=false;
    private int mPenHasSelected=0;
    private int mEraserHasSelected=0;

    private float mSelectPaintWidth;
    private int mSelectPaintColor;
    
    private SavedPaint mSavedPaint;
    public String mTitleString="";
    private BirdAlertDialog mBirdAlertDialog;
    private BirdInputTitleDialog mBirdInputTitleDialog;
    private Handler mainHandler=null;
    
	private int mNoteId=0;
    private int mStar = 0;
    private BirdNote mBirdNote;
    private DbHelper mDbHelper;
	private ChooseEditBgPopMenu chooseEditBgPopMenu;
	
	/**
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

	/**
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
		mDbHelper = new DbHelper(getActivity());
		mNoteApplication=(NoteApplication)getActivity().getApplication();
		mNoteId = mNoteApplication.getEditNoteId();
		mEditedQuadrants=mNoteApplication.getEditedQuadrants();
		
		initEditFragmentView(view);

		Bundle b = this.getArguments();
		if (b != null) {
			mCurrentType=b.getInt(BirdMessage.START_TYPE_KEY);
			if (mCurrentType==BirdMessage.START_TYPE_UPDATE_VALUE) {
				quadrantContent=b.getParcelable("quadrantContent");			
			} else {
			
			}	
			
		mCurrentMode = b.getInt("mode");
		mCurrentType = b.getInt("type");
		mCurrentQuadrant=b.getInt("quadrant");
		mEditedQuadrants[mCurrentQuadrant]=1;
		mNoteApplication.setEditedQuadrants(mEditedQuadrants);
        initView(mCurrentType);
		changeCurrentMode(mCurrentMode);
		changeOtherIconState(mCurrentMode);
		}
		
		mainHandler = ((EditNoteActivity)getActivity()).editHandler;
		mBirdAlertDialog=new BirdAlertDialog(getActivity(),R.style.birdalertdialog);

		return view;
	}
	
	public void initView(int type){
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
		mPopMenu=PopMenuManager.createEditNewNoteMenu(getActivity(),popMenuListener);
	}
	
	/**
	 * 将已经存在的内容绘制到penView上去
	 * @param type
	 * @param mBirdNote
	 */
	public void initUpdateView(int type,QuadrantContent quadrantContent){
		mPenView = new PenView(getActivity());
		mPenView.setExistBitmap(BitmapUtil.decodeBytesToBitmap(quadrantContent.quadrantdraw));
		mPenView.invalidateExistBitmap();	
		mEditText.setText(quadrantContent.textcontent);
		mPopMenu=PopMenuManager.createEditUpdateNoteMenu(getActivity(),popMenuListener);
		if (((EditNoteActivity)getActivity()).mBirdNote!=null) {
			mBirdNote = ((EditNoteActivity)getActivity()).mBirdNote;
		}
	
	}

	public void initEditFragmentView(View view) {
		mEditMainLayout=(FrameLayout)view.findViewById(R.id.id_edit_main_fl);
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
		
		mHeaderLayout=(RelativeLayout)view.findViewById(R.id.id_edit_title_header_rl);
	
		mSavedPaint = new SavedPaint(getActivity());
		
		chooseEditBgPopMenu= PopMenuManager.createChooseEditBgMenu(getActivity());
		chooseEditBgPopMenu.setOnChangeBackgroundListener(new OnChangeBackgroundListener() {

			@Override
			public void changeBackground(int bgRsr) {
				mWrapFrameLayout.setBackgroundResource(bgRsr);
				mNoteApplication.setEditBackground(bgRsr);
				if (chooseEditBgPopMenu!=null&&chooseEditBgPopMenu.isShowing()) {
					chooseEditBgPopMenu.dismiss();
				}
				
			}
		});
		
	}
	
	public boolean closePopMenu(){
		if (mPopMenu!=null &&mPopMenu.isShowing()) {
			mPopMenu.dismiss();
			return true;		
		} else {
			return false;
		}
		
	}
	public void togglePopMenu(){
		
		if  (!mPopMenu.isShowing()) {
			if (mCurrentType == BirdMessage.START_TYPE_UPDATE_VALUE) {
				int star = mDbHelper.queryStarById(mBirdNote._id+"");
				if (star == 0) {
					//如果未收藏，则显示收藏
					mPopMenu=PopMenuManager.createEditUpdateNoteMenu(getActivity(),popMenuListener);
				}else {
					//若已收藏，则显示取消收藏
					mPopMenu=PopMenuManager.createEditUpdateNoteRmStarMenu(getActivity(),popMenuListener);
				}
			}
			
			mPopMenu.showAtLocation(mWrapFrameLayout, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
		} else {
			mPopMenu.dismiss();
		}
	}
	

	public OnClickListener popMenuListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			closePopMenu();
			if (v.getId() == 0) {   
				//更改背景

				chooseEditBgPopMenu.showAtLocation(mWrapFrameLayout, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);

			}
           if (v.getId() == 1) {  
        	   //另存为
        	   mBirdInputTitleDialog=new BirdInputTitleDialog(getActivity(), R.style.birdalertdialog);
       		   mBirdInputTitleDialog.setOnConfirmClickListener(ConfirmSaveAsPngListener);
       		   mBirdInputTitleDialog.setTitleString(getString(R.string.save_as_title));
       		   mBirdInputTitleDialog.show();
       		   if (mCurrentType == BirdMessage.START_TYPE_UPDATE_VALUE) {
       			   mBirdInputTitleDialog.setInputContent(((EditNoteActivity)getActivity()).mBirdNote.title+"_qua"+mCurrentQuadrant);	
			   }else {
				   mBirdInputTitleDialog.setInputContent(CommonUtils.getDefaultTitle()+"_qua"+mCurrentQuadrant);	
			   }
       		   		
			}
			if (v.getId() == 2) {    
				//添加至收藏
				//toggleStarNoteById
				//((EditNoteActivity)getActivity()).mBirdNote.
				mDbHelper.toggleStarNoteById(mBirdNote._id+"");
			}
			if (v.getId() == 3) {           			
				mBirdAlertDialog.setAlertContent(getString(R.string.alert_delete_content));
				mBirdAlertDialog.setOnConfirmListener(ConfirmDeleteNoteListener);
				mBirdAlertDialog.show();				
/*				mPopMenu.showAsDropDown(mWrapFrameLayout);*/				
			}
			mPopMenu.dismiss();	

			
		}
	};
	
	public Bitmap getTextBitmap(){
		mEditText.setDrawingCacheEnabled(true);
        // 去掉状态栏
        Bitmap bmp = Bitmap.createBitmap(mEditText.getDrawingCache(), 0,
        		0, mEditText.getWidth(), mEditText.getHeight());
        // 销毁缓存信息
        mEditText.destroyDrawingCache();
        return bmp;
	}
	

	public OnClickListener ConfirmSaveAsPngListener = new OnClickListener() {	
		@Override
		public void onClick(View v) {
			 mBirdInputTitleDialog.dismiss();
			 mainHandler.sendEmptyMessage(BirdMessage.SAVE_RUNNABLE_START);
            new SaveAsThread().start();                
		}
	};
	
	public String mSavePath="";
	public class SaveAsThread extends Thread {
		
		@Override
		public void run() {
			Bitmap bitmap= getAllBitmap();
			mSavePath = CommonUtils.getSavePath()+"/"+mBirdInputTitleDialog.getContent()+".png";
            BitmapUtil.writeBytesToFile(BitmapUtil.decodeBitmapToBytes(bitmap),  "/"+mBirdInputTitleDialog.getContent());	
            mainHandler.obtainMessage(BirdMessage.SAVE_AS_OVER, mSavePath).sendToTarget();
		}
	};
	
	public String getSavePath(){
		return mSavePath;
	}
	public Bitmap getAllBitmap(){
		Resources resources = getActivity().getResources();;
        return  BitmapUtil.mergeBitmap(getActivity(),BitmapUtil.decodeDrawableToBitmap(getActivity().getResources().getDrawable(mNoteApplication.getEditBackground())),mPenView.mDrawBitmap, getTextBitmap());
	}
	
	/**
	 * 设置当前的编辑模式
	 * 
	 * @param clickID
	 */
	public void changeCurrentMode(int clickID) {
		mCurrentMode = clickID;
		if (clickID == R.id.id_edit_title_pen) {
			hideInputMethod();
			if (mFirstComeIn) {
				mWrapFrameLayout.addView(mPenView);
				mFirstComeIn = false;
			}
			mPenView.bringToFront();
			mEditText.setCursorVisible(false);
			mPenView.initDrawPaint();
			changeStateOfUndoRedo(mUndoState, mRedoState);
			mPenHasSelected+=1;
			mEraserHasSelected=0;
		}
		if (clickID == R.id.id_edit_title_text) {
			mEditText.bringToFront();
			mEditText.setCursorVisible(true);
			mPenHasSelected=0;
			mEraserHasSelected=0;
		}
		if (clickID == R.id.id_edit_title_clean) {
			hideInputMethod();
			if (mFirstComeIn) {
				mWrapFrameLayout.addView(mPenView);
				mFirstComeIn = false;
			}
			mPenView.bringToFront();
			mEditText.setCursorVisible(false);
			mPenView.setCleanPaint();
			changeStateOfUndoRedo(mUndoState, mRedoState);
			mPenHasSelected=0;
			mEraserHasSelected+=1;
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
			togglePopMenu();
			break;
		case R.id.id_edit_title_save:
			//mPenView.savePicture(mCurrentQuadrant);
			closePopMenu();
			if ((((EditNoteActivity)getActivity()).mNoteEditType)==BirdMessage.NOTE_EDIT_TYPE_UPDATE) {
				saveUpdateNote();
			} else {
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
		togglePenBox(mCurrentMode);
		toggleEraserBox(mCurrentMode);
	}
	

	public void createPenBox(){
		mPopPenBox=new PopPenBox(getActivity());
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
	
	public void createEraserBox(){
       mPopEraserBox=new PopEraserBox(getActivity(),new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mPopEraserBox.isShowing()) {
					mPopEraserBox.dismiss();
				}
				mBirdAlertDialog.setAlertContent(getString(R.string.alert_clear_all));
				mBirdAlertDialog.setOnConfirmListener(ConfirmClearAllListener);
				mBirdAlertDialog.show();
               		
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
	/**
	 * 开关笔刷设置框
	 * @param mode
	 */
	public void togglePenBox(int mode){
		createPenBox();
		if (mode==BirdMessage.START_MODE_DRAW_KEY) {
			if (!mPenBoxOpened||(!mPopPenBox.isShowing())) {
				if (mPenHasSelected>1) {
					mPenHasSelected=1;
					mPopPenBox.showAsDropDown(edit_Pen);
					mPenBoxOpened=true;
				}
			} else {
				mPopPenBox.dismiss();
				mPenBoxOpened=false;			
			}
		}
		 else {
				mPopPenBox.dismiss();
				mPenBoxOpened=false;
			}
	}
	
	/**
	 * 开关橡皮擦设置框
	 * @param mode
	 */
	public void toggleEraserBox(int mode){
		 createEraserBox();
		if (mode==BirdMessage.START_MODE_CLEAN_KEY) {
			if (!mEraserBoxOpened||(!mPopEraserBox.isShowing())) {
				if (mEraserHasSelected>1) {				
					mPopEraserBox.showAsDropDown(edit_Clean);
					mEraserBoxOpened=true;
					mEraserHasSelected=1;
				}
			} else {
				mPopEraserBox.dismiss();
				mEraserBoxOpened=false;			
			}
		}
		 else {
			 mPopEraserBox.dismiss();
				mEraserBoxOpened=false;			
			}
	}
	
	
	public void saveNewNote(){
		mBirdInputTitleDialog=new BirdInputTitleDialog(getActivity(), R.style.birdalertdialog);
		mBirdInputTitleDialog.setTitleString(getString(R.string.input_title_dialog_title));
		mBirdInputTitleDialog.setOnConfirmClickListener(ConfirmSaveNewNoteClickListener);		
		mBirdInputTitleDialog.show();		
		mBirdInputTitleDialog.setInputContent(CommonUtils.getDefaultTitle());
	}

	public void saveUpdateNote(){	 
		mainHandler.sendEmptyMessage(BirdMessage.SAVE_RUNNABLE_START);
		new SaveUpdateNoteThread().start();
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
			mEraserHasSelected=0;
			break;
		case R.id.id_edit_title_text:
			edit_Text.setSelected(true);
			edit_Pen.setSelected(false);
			edit_Clean.setSelected(false);
			menu_Undo.setEnabled(false);
			menu_Redo.setEnabled(false);
			mPenHasSelected=0;
			mEraserHasSelected=0;
			break;
		case R.id.id_edit_title_clean:
			edit_Clean.setSelected(true);
			edit_Text.setSelected(false);
			edit_Pen.setSelected(false);
			menu_Undo.setClickable(true);
			menu_Redo.setClickable(true);
			mPenHasSelected=0;
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
	
	public QuadrantContent generateEditQuadrantContent(){
		QuadrantContent quadrantContent=new QuadrantContent();
		quadrantContent.quadrant=mCurrentQuadrant;
		quadrantContent.quadrantdraw=getQuadrantDrawContentBytes();
		quadrantContent.textcontent=getTextContent();
		return quadrantContent;
	}


	/**
	 * 监听确认保存新笔记
	 */
	public OnClickListener ConfirmSaveNewNoteClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.id_alertdiaolg_confirm) {
				 mTitleString=mBirdInputTitleDialog.getContent();
				 mBirdInputTitleDialog.dismiss();
				 mainHandler.sendEmptyMessage(BirdMessage.SAVE_RUNNABLE_START);		
				 new SaveNewNoteThread().start();
			}				
		}
	};
	
   private  class SaveNewNoteThread extends Thread{
	   @Override
	public void run() {
			new DbHelper(getActivity()).insertNewNote(((EditNoteActivity)getActivity()).generateNewNote());					
			mainHandler.sendEmptyMessage(BirdMessage.SAVE_OVER);	
	}	   
   }
	
   private  class SaveUpdateNoteThread extends Thread{
	   @Override
	public void run() {
			NoteApplication noteApplication=(NoteApplication)getActivity().getApplication();
			new DbHelper(getActivity()).updateNoteById(((EditNoteActivity)getActivity()).generateNewNote(),noteApplication.getEditNoteId()+"");
			mainHandler.sendEmptyMessage(BirdMessage.SAVE_OVER);	
	}	   
   }
   
	/**
	 * 监听确认删除笔记
	 */
	public OnClickListener ConfirmDeleteNoteListener=new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.id_alertdiaolg_confirm) {
				mBirdAlertDialog.dismiss();
				mainHandler.sendEmptyMessage(BirdMessage.DELETE_RUNNABLE_START);
				mainHandler.postDelayed(DeleteNoteRunnable,300);	 
			}		
		}
	};
	
	public Runnable DeleteNoteRunnable =new Runnable() {
		@Override
		public void run() {
			((EditNoteActivity)getActivity()).deleteNote();	
		}
	};
	
	public OnClickListener ConfirmClearAllListener =new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			mPenView.clearAll();
			mBirdAlertDialog.dismiss();
		}
	};
	
	
	public void hideInputMethod(){
		View view = getActivity().getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
	}
}
