package com.bird.note.customer;

import com.bird.note.R;
import com.bird.note.customer.ChooseEditBgPopMenu.ViewHolder;
import com.bird.note.utils.BitmapUtil;

import android.R.integer;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

public class ChangeCover extends LinearLayout {

	private String TAG = "ChangeQua";

	LayoutInflater mInflater = null;
	Button mPreButton = null;
	Button mNextButton = null;
	TextView mIndex = null;
	GridView mCoverGridView = null;
	int qua = 0;
	ImageAdapter imageAdapter = null;

	int mSelectedCover = BitmapUtil.EDIT_COVER_PRE[0];
	public ChangeCover(Context context) {
		super(context);
		init(context);
	}

	public ChangeCover(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);

	}

	public void init(Context context) {
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = mInflater.inflate(R.layout.edit_note_choose_cover, this);
		imageAdapter = new ImageAdapter(context);
		mCoverGridView = (GridView) view
				.findViewById(R.id.id_edit_note_choose_cover);
		mCoverGridView.setAdapter(imageAdapter);
		mCoverGridView.setItemChecked(1, true);
		
		mCoverGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mSelectedCover = BitmapUtil.EDIT_COVER_PRE[position];
			    imageAdapter.notifyDataSetChanged();
			}
		});
		

	}

	public void setCoverChecked(int pos){	
		mSelectedCover = pos;
		imageAdapter.notifyDataSetChanged();
	}
	
	public int getSelectCover(){
		for (int i = 0; i < imageAdapter.getCount(); i++) {
			if (mSelectedCover == BitmapUtil.EDIT_COVER_PRE[i]) {
				return i;
			}
		}
		return 0;
	}
	
	
	OnChangeQuaListener mOnChangeQuaListener = null;

	public int getCurrentQua() {
		return qua;
	}

	public interface OnChangeQuaListener {
		public void changeQua(int qua);
	}

	public void setOnChangeQuaListener(OnChangeQuaListener listener) {
		this.mOnChangeQuaListener = listener;
	}

	class ImageAdapter extends BaseAdapter {
		private Context context;

		public ImageAdapter(Context c) {
			context = c;
		}

		@Override
		public int getCount() {
			return BitmapUtil.EDIT_COVER_PRE.length;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(context,
						R.layout.edit_note_choos_cover_item, null);
				holder.imageView = (ImageView) convertView
						.findViewById(R.id.id_img);
				holder.selectedImv = (ImageView) convertView
						.findViewById(R.id.id_img_select);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.imageView
					.setImageResource(BitmapUtil.EDIT_COVER_PRE[position]);
			holder.selectedImv.setVisibility(mSelectedCover ==  BitmapUtil.EDIT_COVER_PRE[position]? View.VISIBLE : View.GONE);

			return convertView;
		}
	}

	final class ViewHolder {
		public ImageView imageView;
		public ImageView selectedImv;
	}

}
