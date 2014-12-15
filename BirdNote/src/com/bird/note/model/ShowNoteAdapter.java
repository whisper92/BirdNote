package com.bird.note.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bird.note.R;
import com.bird.note.customer.BirdInputTitleDialog;
import com.bird.note.ui.EditNoteActivity;
import com.bird.note.ui.PopMenuManager;
import com.bird.note.ui.ShowNotesActivity;
import com.bird.note.utils.BitmapUtil;
import com.bird.note.utils.NoteApplication;

public class ShowNoteAdapter extends BaseAdapter implements OnItemClickListener,OnItemLongClickListener{
	private List<BirdNote> mListData;
	private GridView mGridView;
	private Context mContext;
	private LayoutInflater mInflater;
	private BirdInputTitleDialog mBirdInputTitleDialog;
	private boolean mDeleteState=false;
	/*
	 * 用于保存要删除的笔记的id：如果要删除，则该位置的id为笔记id，否则为-1
	 */
	private String[] mDeleteIds = null;
	
	private Map<Integer, Boolean> mToDeleteNote =new HashMap<Integer, Boolean>();
	public boolean ismDeleteState() {
		return mDeleteState;
	}

	public void setDeleteState(boolean mDeleteState) {
		this.mDeleteState = mDeleteState;
		mDeleteIds = new String[mListData.size()];
		/*初始化设置所有位置为-1*/
		for (int i = 0; i < mListData.size(); i++) {
			mDeleteIds[i]=String.valueOf(-1);
		}
	}
	
	public boolean getDeleteState(){
		return mDeleteState;
	}

	public ShowNoteAdapter(Activity context, List<BirdNote> listData,
			GridView gridView) {
		super();

		this.mContext = context;
		this.mListData = listData;
		this.mGridView = gridView;
		mGridView.setOnItemClickListener(this);
		mGridView.setOnItemLongClickListener(this);
		this.mInflater=context.getLayoutInflater();

	}

	/*
	 * 根据笔记的等级判断他的mark的颜色
	 */
	public int getMarkByLevel(int level){
		int drawableID=0;
		switch (level) {
		case 0:
			drawableID=R.drawable.mark_bg_blue;
			break;
		case 1:
			drawableID=R.drawable.mark_bg_green;
			break;
		case 2:
			drawableID=R.drawable.mark_bg_yellow;
			break;
		case 3:
			drawableID=R.drawable.mark_bg_red;
			break;			
		default:
			break;
		}
		return drawableID;
	}
	
	class NoteHolder{
             ImageView thumbnail;
             TextView title;
	}
	
	int singleNoteId=-1;
	public int getSingleNoteId() {
		return singleNoteId;
	}

	public void setSingleNoteId(int singleNoteId) {
		this.singleNoteId = singleNoteId;
	}
	
	int mChoosePosition = 0;
	View rootView;
	int operatePosition= 0 ;
	@Override
	public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position,long arg3) {
		mChoosePosition = position;
		setSingleNoteId(getItem(position)._id);
		operatePosition= position;
		rootView = view;
		PopMenuManager.createItemOperationDialog(mContext, R.string.item_operation, itemOperateListener);
		return true;
	}

	AlertDialog.Builder builder = null;
	AlertDialog alertDialog = null;
	public android.content.DialogInterface.OnClickListener itemOperateListener = new android.content.DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			if (which ==0) {	
				PopMenuManager.createDeleteAlertDialog(mContext, R.string.alert_delete_content, deleteListener);
			}

			if (which == 1) {				
				mBirdInputTitleDialog = new BirdInputTitleDialog(mContext, android.R.style.Theme_Holo_Light_Dialog);
				mBirdInputTitleDialog.setOnConfirmClickListener(ConfirmUpdateTitleListener);
				mBirdInputTitleDialog.setTitle(R.string.input_title_dialog_title);
	       		mBirdInputTitleDialog.show();		
	       		mBirdInputTitleDialog.setInputContent(getItem(operatePosition).title);
			}
			
			if (which == 2) {				
				LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View view = inflater.inflate(R.layout.show_notes_choos_markcolor, null);
				builder = PopMenuManager.createChooseMarkAlertDialog(mContext, R.string.choose_mark_color);
				builder.setView(view);
				LinearLayout markLayout = (LinearLayout) view.findViewById(R.id.id_choose_mark_ll);
				for (int i = 0; i < markLayout.getChildCount(); i++) {
					Button button = (Button)markLayout.getChildAt(i);
					button.setId(i);
					button.setOnClickListener(changeMarkColorListener);
				}
				alertDialog = builder.create();				
				alertDialog.show();
			}
					
		}
	};
	
	public int chooseLevel=0;
	public OnClickListener changeMarkColorListener =new OnClickListener() {					
		@Override
		public void onClick(View v) {
		    chooseLevel=v.getId();
		    ((ShowNotesActivity)mContext).showHandler.obtainMessage(BirdMessage.CHANGEMARKCOLOR_RUNNABLE_START, mChoosePosition).sendToTarget();													
		    if (alertDialog!=null) {
		    	alertDialog.cancel();
			}
		}
	};
	
	
	public String mNewTitleString = "";
	public OnClickListener ConfirmUpdateTitleListener = new OnClickListener() {	
		@Override
		public void onClick(View v) {
			mNewTitleString = mBirdInputTitleDialog.getContent();
			((ShowNotesActivity)mContext).showHandler.obtainMessage(BirdMessage.UPDATETITLE_RUNNABLE_START, mChoosePosition).sendToTarget();
			mBirdInputTitleDialog.dismiss();
		}
	};
	

	public android.content.DialogInterface.OnClickListener deleteListener =new android.content.DialogInterface.OnClickListener() {					
		@Override
		public void onClick(DialogInterface dialog, int which) {
			if (which == -1) {
			    ((ShowNotesActivity)mContext).showHandler.obtainMessage(BirdMessage.DELETE_SINGLE_NOTE_RUNNABLE_START, mChoosePosition).sendToTarget();													
			}
			
		}
	};
	
	/**
	 * 全选
	 */
	public void selectAll(){
		if (mDeleteState) {
			for (int i = 0; i <getCount(); i++) {				
				mDeleteIds[i]=getItem(i)._id+"";
				notifyDataSetChanged();
			}
		}
		
	}
	/**
	 * 取消
	 */
	public void cancelDelete(){
		for (int i = 0; i < getCount(); i++) {
			mDeleteIds[i]=String.valueOf(-1);
			notifyDataSetChanged();
		}
	}
	
	/**
	 * 获取要删除的笔记id数组
	 * @return
	 */
	public String[] getSelectedNote(){
		return mDeleteIds;
	}
	
	public void confirm(){
		
	}
	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3) {
		if (mDeleteState) {
			if (mDeleteIds[position].equals(String.valueOf(-1))) {
				mDeleteIds[position] = mListData.get(position)._id+"";
				view.findViewById(R.id.id_note_item_title_tv).setBackgroundColor(Color.BLACK);
			} else {
				mDeleteIds[position] = String.valueOf(-1);
				view.findViewById(R.id.id_note_item_title_tv).setBackgroundResource(getMarkByLevel(mListData.get(position).level));
			}

		}else {
			Intent intent=new Intent();
			intent.setClass(mContext, EditNoteActivity.class);
			intent.putExtra(BirdMessage.START_TYPE_KEY, BirdMessage.START_TYPE_UPDATE_VALUE);
			intent.putExtra(BirdMessage.START_MODE_KEY, BirdMessage.START_MODE_DRAW_KEY);
			intent.putExtra(BirdMessage.INITENT_PARCEL_NOTE, mListData.get(position));
			intent.putExtra(BirdMessage.START_TYPE_UPDATE_TITLE_KEY, mListData.get(position).title);
			intent.putExtra(BirdMessage.STAR, mListData.get(position).star);
			NoteApplication noteApplication=(NoteApplication)mContext.getApplicationContext();
			noteApplication.setEditBackground(mListData.get(position).background);
			noteApplication.setCurrentNoteEidtType(BirdMessage.NOTE_EDIT_TYPE_UPDATE);
			noteApplication.setEditNoteId(mListData.get(position)._id);
			noteApplication.setEditedQuadrants(new int[]{0,0,0,0});
			mContext.startActivity(intent);
		}
	}

	@Override
	public BirdNote getItem(int position) {
		BirdNote birdNote=mListData.get(position);
		return birdNote;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}


	@Override
	public int getCount() {
		return mListData.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		NoteHolder holder=null;
		BirdNote birdNote=(BirdNote)getItem(position);
		
		if(convertView == null){
			holder =new NoteHolder();
            convertView=mInflater.inflate(R.layout.show_notes_gridview_item, parent,false);
            holder.thumbnail=(ImageView)convertView.findViewById(R.id.id_note_item_thumb_iv);
            holder.title=(TextView)convertView.findViewById(R.id.id_note_item_title_tv);
            convertView.setTag(holder);

		}else {
			holder=(NoteHolder)convertView.getTag();
		}

			holder.thumbnail.setImageBitmap(BitmapUtil.decodeBytesToBitmap(birdNote.thumbnail));
			/*后期缩略图的背景要切一个小一点的图片*/
	        holder.thumbnail.setBackgroundResource(getPreBgByBg(birdNote.background));
	        holder.title.setText(birdNote.title);
	        holder.title.setBackgroundResource(getMarkByLevel(birdNote.level));
	        
	        if (mDeleteState) {
			   if (!mDeleteIds[position].equals(String.valueOf(-1))) {
				    holder.title.setBackgroundColor(Color.BLACK);			
		    	}
		    } else if ((mDeleteIds!=null)){
		    	if(mDeleteIds[position].equals(String.valueOf(-1))){

		    	} 
		    }

        

		return convertView;
	}
	
	public int getPreBgByBg(int bg){
		int prebg = R.drawable.preview_style00;
		switch (bg) {
		case R.drawable.note_bg_style00:
			prebg = R.drawable.preview_style00;
			break;
		case R.drawable.note_bg_style01:
			prebg = R.drawable.preview_style01;
			break;
		case R.drawable.note_bg_style02:
			prebg = R.drawable.preview_style02;
			break;
		case R.drawable.note_bg_style03:
			prebg = R.drawable.preview_style03;
			break;
		case R.drawable.note_bg_style04:
			prebg = R.drawable.preview_style04;
			break;
			
		default:
			break;
		}
		return prebg;
	}
	@Override
	public int getItemViewType(int position) {	
		return getItem(position).level;
	}

	
}
