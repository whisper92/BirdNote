package com.bird.note.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.bird.note.dao.DbHelper;
import com.bird.note.ui.EditNoteActivity;
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
		level=level%4;
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

	@Override
	public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position,long arg3) {
		//view.scrollTo(0, view.getHeight());
		/*ObjectAnimator oaAnimator=ObjectAnimator.ofFloat(view, "translationY", CommonUtils.dpToPx(mContext, mContext.getResources().getDimension(R.dimen.dimen_show_note_item_menu_height)));
		ObjectAnimator oaAnimator=ObjectAnimator.ofFloat(view, "translationY",view.getHeight());
		oaAnimator.setDuration(500);
		oaAnimator.start();*/
		return true;
	}

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
				view.setBackgroundColor(Color.BLACK);
			} else {
				mDeleteIds[position] = String.valueOf(-1);
				view.setBackgroundDrawable(null);
			}

		}else {
			Intent intent=new Intent();
			intent.setClass(mContext, EditNoteActivity.class);
			intent.putExtra(BirdMessage.START_TYPE_KEY, BirdMessage.START_TYPE_UPDATE_VALUE);
			intent.putExtra(BirdMessage.START_MODE_KEY, BirdMessage.START_MODE_DRAW_KEY);
			intent.putExtra(BirdMessage.INITENT_PARCEL_NOTE, mListData.get(position));
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
			BitmapUtil.writeBytesToFile(birdNote.thumbnail, CommonUtils.getSavePath()+"/hello.png");
	        holder.title.setText(birdNote.title);
	        holder.title.setBackgroundResource(getMarkByLevel(birdNote.level));
	        
	        if (mDeleteState) {
			   if (!mDeleteIds[position].equals(String.valueOf(-1))) {
				    DBUG.e("删除"+mDeleteIds[position]);
					convertView.setBackgroundColor(Color.BLACK);			
		    	}
		    } else if ((mDeleteIds!=null)){
		    	if(mDeleteIds[position].equals(String.valueOf(-1))){
		    		convertView.setBackgroundDrawable(null);
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
