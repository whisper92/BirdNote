package com.bird.note.customer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bird.note.R;
import com.bird.note.model.BirdPopMenuItem;
import com.bird.note.utils.BitmapUtil;
import com.bird.note.utils.NoteApplication;

/**
 * 编辑笔记界面的菜单
 * 
 * @author wangxianpeng
 * 
 */
public class ChooseEditBgPopMenu extends PopupWindow implements
		OnItemClickListener {

	private LayoutInflater inflater;
	private View rootView;
	private Context mContext;
	public List<BirdPopMenuItem> menuItems;

	private Gallery gallery;
	private NoteApplication mNoteApplication = null;
	private String[] mBgArray = null;
	private ArrayList<HashMap<String, Object>> lstImageItem;
	ImageAdapter  imageAdapter;
	public ChooseEditBgPopMenu(Context context) {
		mContext = context;
		mNoteApplication = (NoteApplication) mContext.getApplicationContext();
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		rootView = inflater.inflate(R.layout.choos_editbg_pop_menu, null);

		this.setContentView(rootView);
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.MATCH_PARENT); 
		this.setAnimationStyle(R.style.popmenuanim);

		this.setOutsideTouchable(false);
		this.setBackgroundDrawable(new BitmapDrawable());

		LinearLayout linearLayout = (LinearLayout) rootView
				.findViewById(R.id.id_popmenu_root);
		linearLayout.setOnClickListener(DismissListener);

		rootView.setFocusableInTouchMode(true);
		mBgArray = mContext.getResources().getStringArray(R.array.bg_array);
		 lstImageItem = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < BitmapUtil.EDIT_BGS_PRE.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ItemImage", BitmapUtil.EDIT_BGS_PRE[i]);
			map.put("ItemRealBg", BitmapUtil.EDIT_BGS[i]);
			map.put("ItemText", mBgArray[i]);
			lstImageItem.add(map);
		}
		
		gallery = (Gallery) rootView.findViewById(R.id.gallery1);
		imageAdapter = new ImageAdapter(mContext);
		gallery.setAdapter(imageAdapter);// 设置图片适配器
		// 设置监听器

		gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mChangeBackgroundListener.changeBackground(BitmapUtil.EDIT_BGS[position]);
				if (mNoteApplication!=null) {
					mNoteApplication.setEdited(true);
					for (int i = 0; i < BitmapUtil.EDIT_BGS.length; i++) {
						mNoteApplication.setEditBackground(BitmapUtil.EDIT_BGS[position]);
					}
					imageAdapter.notifyDataSetChanged();
				}
			}
		});
		gallery.setSelection(1);
		

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		mChangeBackgroundListener.changeBackground(BitmapUtil.EDIT_BGS[arg2]);
	}

	OnChangeBackgroundListener mChangeBackgroundListener;

	public interface OnChangeBackgroundListener {
		public void changeBackground(int bgRsr);
	}

	public void setOnChangeBackgroundListener(
			OnChangeBackgroundListener listener) {
		this.mChangeBackgroundListener = listener;
	}

	public OnClickListener DismissListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			dismiss();
		}
	};

	
	class ImageAdapter extends BaseAdapter {
		private Context context;

		public ImageAdapter(Context c) {
			context = c;
		}

		@Override
		public int getCount() {
			return BitmapUtil.EDIT_BGS.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {		
			ViewHolder holder;  
            if(convertView == null){  
                holder = new ViewHolder();  
                convertView = View.inflate(context, R.layout.gallery_item, null);  
                holder.imageView = (ImageView) convertView.findViewById(R.id.id_img);  
                holder.textView = (TextView) convertView.findViewById(R.id.id_text);  
                holder.selectedImv = (ImageView) convertView.findViewById(R.id.id_img_select);
                convertView.setTag(holder);  
            }else {  
                holder = (ViewHolder) convertView.getTag();  
            }  
            holder.imageView.setBackgroundResource( BitmapUtil.EDIT_BGS_PRE[position]);  
            holder.textView.setText( mBgArray[position]);  
            int bg = mNoteApplication.getEditBackground();
            if (bg == BitmapUtil.EDIT_BGS[position]) {
            	holder.selectedImv.setVisibility(View.VISIBLE);
			}
            //holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            //holder.imageView.setLayoutParams(new RelativeLayout.LayoutParams(150,248));
			return convertView;
		}
	}

	final class ViewHolder {
		public ImageView imageView;
		public TextView textView;
		public ImageView selectedImv;
	}
	
}
