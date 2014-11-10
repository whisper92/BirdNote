package com.bird.note.model;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.bird.note.R;
import com.bird.note.ui.EditNoteActivity;
import com.bird.note.utils.BitmapUtil;
import com.bird.note.utils.CommonUtils;

public class ShowNoteAdapter extends BaseAdapter implements OnItemClickListener,OnItemLongClickListener{
	private List<BirdNote> mListData;
	private GridView mGridView;
	private Context mContext;
	private LayoutInflater mInflater;
	
	private Scroller scroller;
	public ShowNoteAdapter(Activity context, List<BirdNote> listData,
			GridView gridView) {
		super();

		this.mContext = context;
		this.mListData = listData;
		this.mGridView = gridView;
		mGridView.setOnItemClickListener(this);
		mGridView.setOnItemLongClickListener(this);
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

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3) {
	
		Toast.makeText(mContext, mListData.get(position)._id+"", 500).show();
		Intent intent=new Intent();
		intent.setClass(mContext, EditNoteActivity.class);
		intent.putExtra(BirdMessage.START_TYPE_UPDATE, BirdMessage.START_TYPE_UPDATE_VALUE);
		intent.putExtra(BirdMessage.START_MODE_DRAW, BirdMessage.START_MODE_DRAW_KEY);
		intent.putExtra(BirdMessage.START_NOTE_ID_KEY, mListData.get(position)._id);
		
		mContext.startActivity(intent);
	}

	@Override
	public BirdNote getItem(int position) {
		BirdNote birdNote=mListData.get(position);
		return birdNote;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
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
    	
		return convertView;
	}
	
	@Override
	public int getItemViewType(int position) {	
		return getItem(position).level;
	}
	
}
