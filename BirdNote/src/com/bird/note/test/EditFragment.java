package com.bird.note.test;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

import com.bird.note.R;
import com.bird.note.customer.FullScreenEditText;
import com.bird.note.customer.PenView;
import com.bird.note.customer.PenView.OnPathListChangeListener;

public class EditFragment extends Fragment implements OnClickListener {
	private FullScreenEditText mEditText;

	private FrameLayout mWrapFrameLayout;
	private PenView mPenView;
	private boolean mUndoState;
	private boolean mRedoState;
	private boolean mFirstComeIn = true;

	private int mType;

	public static EditFragment newInstance(int qua, int mode) {
		EditFragment editFragment = new EditFragment();
		Bundle b = new Bundle();
		b.putInt("quadrant", qua);
		b.putInt("mode", mode);
		editFragment.setArguments(b);
		return editFragment;

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.edit_note_fragment, container,
				false);
		init(view);

		Bundle b = this.getArguments();
		if (b != null) {
			mType = b.getInt("mode");
			changeCurrentMode(mType);
		}	
		return view;
	}

	public void init(View view) {
		mWrapFrameLayout = (FrameLayout) view
				.findViewById(R.id.id_edit_main_fl_bg);
		mEditText = (FullScreenEditText) view
				.findViewById(R.id.id_edit_main_et);
		mPenView = new PenView(getActivity());
		mPenView.setOnPathListChangeListenr(new OnPathListChangeListener() {

			@Override
			public void changeState(int undocount, int redocount) {

				mUndoState = undocount > 0 ? true : false;
				mRedoState = redocount > 0 ? true : false;
				((EditNoteFragmentActivity)getActivity()).changeStateOfUndoRedo(mUndoState, mRedoState);
			}
		});
		mPenView.setLayoutParams(new FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

	}

	/**
	 * 设置当前的编辑模式
	 * 
	 * @param clickID
	 */
	public void changeCurrentMode(int clickID) {

		if (clickID == R.id.id_edit_title_pen) {
			if (mFirstComeIn) {
				mWrapFrameLayout.addView(mPenView);
				mFirstComeIn = false;
			}
			mPenView.bringToFront();
			mEditText.setCursorVisible(false);
			mPenView.initDrawPaint();
			((EditNoteFragmentActivity)getActivity()).changeStateOfUndoRedo(mUndoState, mRedoState);

		}
		if (clickID == R.id.id_edit_title_text) {
			mEditText.bringToFront();
			mEditText.setCursorVisible(true);

		}
		if (clickID == R.id.id_edit_title_clean) {
			if (mFirstComeIn) {
				mWrapFrameLayout.addView(mPenView);
				mFirstComeIn = false;
			}
			mPenView.bringToFront();
			mEditText.setCursorVisible(false);
			mPenView.setCleanPaint();
			((EditNoteFragmentActivity)getActivity()).changeStateOfUndoRedo(mUndoState, mRedoState);
		}

	}

	@Override
	public void onClick(View v) {

	}

	public PenView getmPenView() {
		return mPenView;
	}

	public void setmPenView(PenView mPenView) {
		this.mPenView = mPenView;
	}

}
