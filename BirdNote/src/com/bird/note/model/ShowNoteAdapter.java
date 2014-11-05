package com.bird.note.model;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import com.bird.note.R;
import com.bird.note.utils.BitmapUtil;
import com.bird.note.utils.CommonUtils;

import android.R.integer;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

public class ShowNoteAdapter extends BaseAdapter implements OnItemClickListener,OnItemLongClickListener{
	private List<BirdNote> mListData;
	private GridView mGridView;
	private Context mContext;
	private LayoutInflater mInflater;
	
	private Scroller scroller;
	public ShowNoteAdapter(Activity context, ArrayList<BirdNote> listData,
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
	
		Toast.makeText(mContext, mListData.get(position).title, 500).show();
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
		Log.e("wxp","getView start");
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
		
		/*
		 * 应该在实例化类的时候就判断bitmap是否为空，而不是在这里判断
		 */
		holder.thumbnail.setImageBitmap(BitmapUtil.bytesToBitmap(birdNote.byteArrayNoteThumbnail));
        holder.title.setText(birdNote.title);
        holder.title.setBackgroundResource(getMarkByLevel(birdNote.level));
    	Log.e("wxp","getView end");

		return convertView;
	}
	
	@Override
	public int getItemViewType(int position) {	
		return getItem(position).level;
	}
	
}
