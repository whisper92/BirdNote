package com.bird.note.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.R.integer;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.bird.note.R;
import com.bird.note.customer.BirdAlertDialog;
import com.bird.note.customer.BirdWaitDialog;
import com.bird.note.customer.PopChangeMarkColor;
import com.bird.note.customer.PopMenuEditNote;
import com.bird.note.customer.PopMenuShowNote;
import com.bird.note.customer.PopPenBox;
import com.bird.note.dao.DbHelper;
import com.bird.note.ui.EditNoteActivity;
import com.bird.note.ui.ShowNotesActivity;
import com.bird.note.utils.BitmapUtil;
import com.bird.note.utils.CommonUtils;
import com.bird.note.utils.NoteApplication;

public class ShowNoteAdapter extends BaseAdapter implements OnItemClickListener,OnItemLongClickListener,MultiChoiceModeListener{
	private List<BirdNote> mListData;
	private GridView mGridView;
	private Context mContext;
	private LayoutInflater mInflater;
	private Scroller scroller;
	
	private boolean mDeleteState=false;
	private DbHelper mDbHelper;
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
		//初始化设置所有位置为-1
		for (int i = 0; i < mListData.size(); i++) {
			mDeleteIds[i]=String.valueOf(-1);
		}
		mDbHelper = new DbHelper(mContext);
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
		//mGridView.setMultiChoiceModeListener(this);
		this.mInflater=context.getLayoutInflater();
		scroller=new Scroller(context);

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
	
	/*
	 * 根据id获得缩略图的背景
	 */
	public int getThumbnailBgById(int bg_id){
		
		int drawableID=R.drawable.note_bg_style00_thumbnail;
		switch (bg_id) {
		case 0:
			drawableID=R.drawable.note_bg_style00_thumbnail;
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

	public Runnable scrollRunnable = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
	};

	
	int singleNoteId=-1;
	public int getSingleNoteId() {
		return singleNoteId;
	}

	public void setSingleNoteId(int singleNoteId) {
		this.singleNoteId = singleNoteId;
	}
	

	
	PopMenuShowNote popMenu= null;
	BirdAlertDialog birdAlertDialog = null;
	PopChangeMarkColor popChangeMarkColor = null;
	@Override
	public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position,long arg3) {
		setSingleNoteId(getItem(position)._id);
		final View rootView = view;
		popMenu =new PopMenuShowNote(mContext,new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (v.getId() == R.id.id_popmenu_delete) {
					birdAlertDialog = new BirdAlertDialog(mContext, R.style.birdalertdialog);
					birdAlertDialog.setAlertContent(mContext.getString(R.string.alert_delete_content));
					popMenu.dismiss();					
					birdAlertDialog.setOnConfirmListener(deleteListener);
					birdAlertDialog.show();
				}
				
				if (v.getId() == R.id.id_popmenu_mark_color) {				
					popMenu.dismiss();		
					popChangeMarkColor = new PopChangeMarkColor(mContext, changeMarkColorListener);
					popChangeMarkColor.showAtLocation(rootView, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
				}
				
			}
		});
		popMenu.showAtLocation(view, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
		
		return true;
	}

	public int chooseLevel=-1;
	public OnClickListener changeMarkColorListener =new OnClickListener() {					
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.id_pop_mark_bg_blue:
				chooseLevel=0;
				break;
            case R.id.id_pop_mark_bg_green:
            	chooseLevel=1;
				break;
            case R.id.id_pop_mark_bg_yellow:
            	chooseLevel=2;
				break;
            case R.id.id_pop_mark_bg_red:
            	chooseLevel=3;
				break;
			default:
				break;
			}
			popChangeMarkColor.dismiss();
		   ((ShowNotesActivity)mContext).showHandler.sendEmptyMessage(BirdMessage.CHANGEMARKCOLOR_RUNNABLE_START);															
		}
	};
	
	public OnClickListener deleteListener =new OnClickListener() {					
		@Override
		public void onClick(View v) {	
			    birdAlertDialog.dismiss();
				((ShowNotesActivity)mContext).showHandler.sendEmptyMessage(BirdMessage.DELETE_SINGLE_NOTE_RUNNABLE_START);															
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
		BirdNote birdNote=getItem(position);
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
			NoteApplication noteApplication=(NoteApplication)mContext.getApplicationContext();
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
            convertView=mInflater.inflate(R.layout.note_item, parent,false);
            holder.thumbnail=(ImageView)convertView.findViewById(R.id.id_note_item_thumb_iv);
            holder.title=(TextView)convertView.findViewById(R.id.id_note_item_title_tv);
            convertView.setTag(holder);

		}else {
			holder=(NoteHolder)convertView.getTag();
		}

			holder.thumbnail.setImageBitmap(BitmapUtil.decodeBytesToBitmap(birdNote.thumbnail));
			//后期缩略图的背景要切一个小一点的图片
	        holder.thumbnail.setBackgroundResource(getThumbnailBgById(birdNote.background));
	        holder.title.setText(birdNote.title);
	        holder.title.setBackgroundResource(getMarkByLevel(birdNote.level));
	        
	        if (mDeleteState) {
			   if (!mDeleteIds[position].equals(String.valueOf(-1))) {
				    DBUG.e("删除"+mDeleteIds[position]);
				    holder.title.setBackgroundColor(Color.BLACK);			
		    	}
		    } else if ((mDeleteIds!=null)){
		    	if(mDeleteIds[position].equals(String.valueOf(-1))){

		    	} 
		    }

        

		return convertView;
	}
	
	@Override
	public int getItemViewType(int position) {	
		return getItem(position).level;
	}

	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onDestroyActionMode(ActionMode mode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onItemCheckedStateChanged(ActionMode mode, int position,
			long id, boolean checked) {
		// TODO Auto-generated method stub
		
	}
	
}
