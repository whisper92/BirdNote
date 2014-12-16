package com.bird.note.model;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class ShowNoteSimpleAdapter extends BaseAdapter implements OnItemClickListener,OnItemLongClickListener,OnMultiChoiceClickListener{
	private List<BirdNote> mListData;
	private GridView mGridView;
	private Context mContext;
	private LayoutInflater mInflater;
	private BirdInputTitleDialog mBirdInputTitleDialog;
	private ActionMode mActionMode = null;
	OnConfirmDeleteListener mOnConfirmDeleteListener = null;


	public void selectAll() {
		for (int i = 0; i < mListData.size(); i++) {
			mGridView.setItemChecked(i, true);
		}
		notifyDataSetChanged();
	}
	
	public void diselectAll() {
		for (int i = 0; i < mListData.size(); i++) {
			mGridView.setItemChecked(i, false);
		}
		notifyDataSetChanged();
	}


	public ShowNoteSimpleAdapter(Activity context, List<BirdNote> listData,
			GridView gridView) {
		super();

		this.mContext = context;
		this.mListData = listData;
		this.mGridView = gridView;
		mGridView.setOnItemClickListener(this);
		mGridView.setOnItemLongClickListener(this);
		mGridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
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
		operatePosition= position;
		rootView = view;
        //Start the CAB using the ActionMode.Callback defined above  
        mActionMode = ((Activity) mContext).startActionMode(mCallback);  
        mGridView.setItemChecked(position, true);
		return true;
	}

	private ActionMode.Callback mCallback = new ActionMode.Callback() {

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			diselectAll();
			return false;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {

			mActionMode = null;
			diselectAll();
		}

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			MenuInflater inflater = mode.getMenuInflater();
			inflater.inflate(R.menu.show_menu_actionmode, menu);
			return true;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			if (item.getItemId() == R.id.id_show_menu_multi_delete_confirm) {

				if (mOnConfirmDeleteListener!=null) {
					mOnConfirmDeleteListener.confirmDelete(getSelectNoteIds());
				} else {

				}
				mode.finish();
			}
			if (item.getItemId() == R.id.id_show_menu_multi_delete_selectall) {
				selectAll();
			}
			if (item.getItemId() == R.id.id_show_menu_multi_delete_diselectall) {
				diselectAll();
			} 
			return false;
		}
	};
	

	public interface OnConfirmDeleteListener{
		public void confirmDelete(String[] noteids);
	}
	
	public void setOnConfirmDeleteListener(OnConfirmDeleteListener listener){
		this.mOnConfirmDeleteListener = listener;
	}
	
	public String[] getSelectNoteIds(){
		String[] noteids = new String[mListData.size()];
		for (int i = 0; i < noteids.length; i++) {
			if (mGridView.isItemChecked(i)) {
				noteids[i] = mListData.get(i)._id+"";
			} else {
				noteids[i] = String.valueOf(-1);
			}		
		}
		return noteids;
	}
	
/*	AlertDialog.Builder builder = null;
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
	*/
	
	
/*	public String mNewTitleString = "";
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
	*/


	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3) {	
		boolean flag = mGridView.isItemChecked(position)?false:true;	
		if (mActionMode != null) {
			if (flag == true) {
				mGridView.setItemChecked(position, false);
			} else {
				mGridView.setItemChecked(position, true);
			}
			notifyDataSetChanged();
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
            convertView=mInflater.inflate(R.layout.show_notes_item, parent,false);
            holder.thumbnail=(ImageView)convertView.findViewById(R.id.id_note_item_cover_iv);
            holder.title=(TextView)convertView.findViewById(R.id.id_note_item_title_tv);
            holder.updatedate=(TextView)convertView.findViewById(R.id.id_note_item_date_tv);
            convertView.setTag(holder);

		}else {
			holder=(NoteHolder)convertView.getTag();
		}

			holder.thumbnail.setBackgroundResource(BitmapUtil.getCoverBgByLevel(birdNote.level));
	        holder.title.setText(birdNote.title);
	        holder.updatedate.setText(birdNote.update_time);
	      
	        
        if (mActionMode!=null) {
        	if (mGridView.isItemChecked(position)) {
        		holder.title.setBackgroundColor(Color.BLACK);
			} else {
			    holder.title.setBackgroundResource(getMarkByLevel(birdNote.level));
			}
         }

		return convertView;
	}
	
	class NoteHolder{
        ImageView thumbnail;
        TextView title;
        TextView updatedate;
}
	
	@Override
	public int getItemViewType(int position) {	
		return getItem(position).level;
	}

	@Override
	public void onClick(DialogInterface dialog, int which, boolean isChecked) {
		// TODO Auto-generated method stub
		
	}

	
}
