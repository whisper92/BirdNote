package com.bird.note.model;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bird.note.R;
import com.bird.note.ui.EditNoteActivity;
import com.bird.note.utils.BitmapUtil;
import com.bird.note.utils.NoteApplication;

public class ReadStaredNoteAdapter extends BaseAdapter implements OnItemClickListener{
	private List<BirdNote> mListData;
	private GridView mGridView;
	private Context mContext;
	private LayoutInflater mInflater;

	public ReadStaredNoteAdapter(Activity context, List<BirdNote> listData,
			GridView gridView) {
		super();

		this.mContext = context;
		this.mListData = listData;
		this.mGridView = gridView;
		mGridView.setOnItemClickListener(this);
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


	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3) {

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
			/*后期缩略图的背景要切一个小一点的图片*/
	        holder.thumbnail.setBackgroundResource(getPreBgByBg(birdNote.background));
	        holder.title.setText(birdNote.title);
	        holder.title.setBackgroundResource(getMarkByLevel(birdNote.level));
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
