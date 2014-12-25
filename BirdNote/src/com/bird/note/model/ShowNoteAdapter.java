package com.bird.note.model;

import java.util.List;

import android.R.integer;
import android.annotation.SuppressLint;
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
import com.bird.note.utils.CommonUtils;
import com.bird.note.utils.NoteApplication;

/**
 * @author wangxianpeng
 * @since 19/12/14
 *
 */
public class ShowNoteAdapter extends BaseAdapter implements
		OnItemClickListener, OnItemLongClickListener,
		OnMultiChoiceClickListener {
	private List<BirdNote> mListData;
	private GridView mGridView;
	private Context mContext;
	private LayoutInflater mInflater;
	private BirdInputTitleDialog mBirdInputTitleDialog;
	public ActionMode mActionMode = null;
	/*
	 * type 0:ActionMode模式为删除 type1:ActionMode模式为取消收藏
	 */
	private int mType = 0;
	private OnConfirmActionListener mOnConfirmDeleteListener = null;

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

	public ShowNoteAdapter(Activity context, int type, List<BirdNote> listData,GridView gridView) {
		super();

		mType = type;
		this.mContext = context;
		this.mListData = listData;
		this.mGridView = gridView;
		mGridView.setOnItemClickListener(this);
		mGridView.setOnItemLongClickListener(this);
		mGridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
		this.mInflater = context.getLayoutInflater();

	}

	int singleNoteId = -1;

	public int getSingleNoteId() {
		return singleNoteId;
	}

	public void setSingleNoteId(int singleNoteId) {
		this.singleNoteId = singleNoteId;
	}

	private int mChoosePosition = 0;
	private View rootView;
	private int operatePosition = 0;

	@Override
	public boolean onItemLongClick(AdapterView<?> adapterView, View view,int position, long arg3) {
		mChoosePosition = position;
		operatePosition = position;
		rootView = view;
		// Start the CAB using the ActionMode.Callback defined above
		mActionMode = ((Activity) mContext).startActionMode(mCallback);

		mGridView.setItemChecked(position, true);
		return true;
	}

	private String[] todeletids = null;
	public ActionMode.Callback mCallback = new ActionMode.Callback() {

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
			if (mType == 0) {
				inflater.inflate(R.menu.show_menu_actionmode, menu);
			} else {
				inflater.inflate(R.menu.star_menu_actionmode, menu);
			}

			return true;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			if (item.getItemId() == R.id.id_show_menu_multi_delete_confirm || item.getItemId() == R.id.id_star_menu_multi_rm_confirm) {

				if (mOnConfirmDeleteListener != null) {
					todeletids = getSelectNoteIds();
					PopMenuManager.createDeleteAlertDialog(mContext,(item.getItemId() == R.id.id_show_menu_multi_delete_confirm) ?R.string.alert_delete_content:R.string.edit_menu_removefavor, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {

							if (which == -1) {
								if (todeletids != null) {
									mOnConfirmDeleteListener.confirmDo(todeletids, 0);
								}
								
							}
							
						}
					});
					
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

	/*
	 * type = 0:delete notes type = 1:start notes;
	 */
	public interface OnConfirmActionListener {
		public void confirmDo(String[] noteids, int type);
	}

	public void setOnConfirmDeleteListener(OnConfirmActionListener listener) {
		this.mOnConfirmDeleteListener = listener;
	}

	public String[] getSelectNoteIds() {
		String[] noteids = new String[mListData.size()];
		for (int i = 0; i < noteids.length; i++) {
			if (mGridView.isItemChecked(i)) {
				noteids[i] = mListData.get(i)._id + "";
				
			} else {
				noteids[i] = String.valueOf(-1);
			}
		}
		return noteids;
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view,
			int position, long arg3) {
		boolean flag = mGridView.isItemChecked(position) ? false : true;
		if (mActionMode != null) {
			if (flag == true) {
				mGridView.setItemChecked(position, false);
			} else {
				mGridView.setItemChecked(position, true);
			}
			notifyDataSetChanged();
		} else {
			Intent intent = new Intent();
			intent.setClass(mContext, EditNoteActivity.class);
			intent.putExtra(BirdMessage.START_TYPE_KEY, BirdMessage.START_TYPE_UPDATE_VALUE);
			intent.putExtra(BirdMessage.START_MODE_KEY, BirdMessage.START_MODE_DRAW_KEY);
			intent.putExtra(BirdMessage.INITENT_PARCEL_NOTE, mListData.get(position));
			intent.putExtra(BirdMessage.START_TYPE_UPDATE_TITLE_KEY, mListData.get(position).title);
			intent.putExtra(BirdMessage.STAR, mListData.get(position).star);
			NoteApplication noteApplication = (NoteApplication) mContext.getApplicationContext();
			noteApplication.setEditBackground(mListData.get(position).background);
			noteApplication.setCurrentNoteEidtType(BirdMessage.NOTE_EDIT_TYPE_UPDATE);
			noteApplication.setEditNoteId(mListData.get(position)._id);
			noteApplication.setEditedQuadrants(new int[] { 0, 0, 0, 0 });
			mContext.startActivity(intent);
		}
	}

	@Override
	public BirdNote getItem(int position) {
		BirdNote birdNote = mListData.get(position);
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

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		NoteHolder holder = null;
		BirdNote birdNote = (BirdNote) getItem(position);
		if (convertView == null) {
			holder = new NoteHolder();
			convertView = mInflater.inflate(R.layout.show_notes_item, parent,false);
			holder.cover = (ImageView) convertView.findViewById(R.id.id_note_item_cover_iv);
			holder.fav = (ImageView) convertView.findViewById(R.id.id_note_item_cover_fav);
			holder.title = (TextView) convertView.findViewById(R.id.id_note_item_title_tv);
			holder.updatedate = (TextView) convertView.findViewById(R.id.id_note_item_date_tv);
			convertView.setTag(holder);

		} else {
			holder = (NoteHolder) convertView.getTag();
		}

		holder.cover.setImageResource(BitmapUtil.getCoverBgByLevel(birdNote.level));
		holder.fav.setVisibility(birdNote.star == 0 ? (View.GONE): (View.VISIBLE));
		holder.title.setText(CommonUtils.spliteTitle(birdNote.title));
		holder.updatedate.setText(CommonUtils.formatUpdateTime(birdNote.update_time));

		if (mActionMode != null) {
			if (mGridView.isItemChecked(position)) {
				holder.cover.setBackgroundResource(R.drawable.th01_cover_sel);
			} else {
				holder.cover.setBackground(null);
			}
		} else {
			holder.cover.setBackground(null);
		}

		return convertView;
	}

	class NoteHolder {
		ImageView cover;
		TextView title;
		TextView updatedate;
		ImageView fav;
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
