package com.bird.note.customer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bird.note.R;
import com.bird.note.utils.BitmapUtil;

/**
 * The view used to change the cover of the note
 *
 * @author wangxianpeng
 * @since 19/12/14
 */
public class ChangeCover extends LinearLayout {

	private String TAG = "ChangeQua";

	private LayoutInflater mInflater = null;
	private Button mPreButton = null;
	private Button mNextButton = null;
	private TextView mIndex = null;
	private GridView mCoverGridView = null;
	private int cover = 0;
	private ImageAdapter imageAdapter = null;

	int mSelectedCover = BitmapUtil.EDIT_COVER_PRE[0];

	public ChangeCover(Context context) {
		super(context);
		init(context);
	}

	public ChangeCover(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);

	}

	private void init(Context context) {
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = mInflater.inflate(R.layout.edit_note_choose_cover, this);
		imageAdapter = new ImageAdapter(context);
		mCoverGridView = (GridView) view.findViewById(R.id.id_edit_note_choose_cover);

		mCoverGridView.setAdapter(imageAdapter);
		mCoverGridView.setItemChecked(1, true);
		mCoverGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				mSelectedCover = BitmapUtil.EDIT_COVER_PRE[position];
				imageAdapter.notifyDataSetChanged();
			}
		});

	}

	/**
	 * Set the cover at position 'pos' checked
	 *
	 * @param pos
	 *            the position to set
	 */
	public void setCoverChecked(int pos) {
		mSelectedCover = pos;
		imageAdapter.notifyDataSetChanged();
	}

	/**
	 * Return the potition of selected cover
	 */
	public int getSelectCover() {
		for (int i = 0; i < imageAdapter.getCount(); i++) {
			if (mSelectedCover == BitmapUtil.EDIT_COVER_PRE[i]) {
				return i;
			}
		}
		return 0;
	}

	OnChangeQuaListener mOnChangeQuaListener = null;

	public int getCurrentQua() {
		return cover;
	}

	public interface OnChangeQuaListener {
		public void changeQua(int cover);
	}

	/**
	 * Register a callback to be invoked when the cover changed
	 *
	 * @param listener
	 *            the callback will run
	 */
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
				convertView = View.inflate(context,R.layout.edit_note_choos_cover_item, null);
				holder.imageView = (ImageView) convertView.findViewById(R.id.id_img);
				holder.selectedImv = (ImageView) convertView.findViewById(R.id.id_img_select);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.imageView.setImageResource(BitmapUtil.EDIT_COVER_PRE[position]);
			holder.selectedImv.setVisibility(mSelectedCover == BitmapUtil.EDIT_COVER_PRE[position] ? View.VISIBLE : View.GONE);

			return convertView;
		}
	}

	final class ViewHolder {
		public ImageView imageView;
		public ImageView selectedImv;
	}

}
