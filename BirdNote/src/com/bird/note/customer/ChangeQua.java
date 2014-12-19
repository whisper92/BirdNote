package com.bird.note.customer;

import com.bird.note.R;

import android.R.integer;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

/**
 * The view used to change to other quadrant at the bottom of EditNoteActivity.
 *
 * @author wangxianpeng
 * @since 19/12/14
 */
public class ChangeQua extends LinearLayout implements OnClickListener,
		OnTouchListener {

	private String TAG = "ChangeQua";

	private LayoutInflater mInflater = null;
	private Button mPreButton = null;
	private Button mNextButton = null;
	private TextView mIndex = null;
	private int qua = 0;
	private Context mContext = null;
  private OnChangeQuaListener mOnChangeQuaListener = null;

	public ChangeQua(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = mInflater.inflate(R.layout.edit_note_change_qua, this);
		mPreButton = (Button) view.findViewById(R.id.id_change_qua_pre);
		mNextButton = (Button) view.findViewById(R.id.id_change_qua_next);
		mIndex = (TextView) view.findViewById(R.id.id_change_qua_index);

		mPreButton.setOnClickListener(this);
		mNextButton.setOnClickListener(this);
		mIndex.setOnTouchListener(this);

		mIndex.setText((qua + 1) + "/4");
		mPreButton.setEnabled(false);

	}

	private Toast mToast = null;

	private void showToast(int qua) {
		String toastString = String.format(mContext.getString(R.string.changequatoast), qua);
		if (mToast == null) {
			mToast = Toast.makeText(mContext, toastString, Toast.LENGTH_SHORT);
		} else {
			mToast.setText(toastString);
		}
		mToast.show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_change_qua_pre:
			qua = qua - 1;
			mIndex.setText((qua + 1) + "/4");
			showToast(qua + 1);
			mOnChangeQuaListener.changeQua(qua);
			mNextButton.setEnabled(true);
			if (qua - 1 < 0) {
				mPreButton.setEnabled(false);
			}
			break;
			
		case R.id.id_change_qua_next:
			qua = qua + 1;
			mIndex.setText((qua + 1) + "/4");
			showToast(qua + 1);
			mOnChangeQuaListener.changeQua(qua);
			mPreButton.setEnabled(true);
			if (qua + 1 > 3) {
				mNextButton.setEnabled(false);
			}
			break;

		case R.id.id_change_qua_index:
			break;
			
		default:
			break;
		}

	}

	/**
	 * Return current quadrant
	 *
	 * @return int
	 */
	public int getCurrentQua() {
		return qua;
	}

	public interface OnChangeQuaListener {
		public void changeQua(int qua);
	}

	/**
	 * Register a callback to invoke when the quadrant changed
	 *
	 * @param listener
	 *            the callback will run
	 */
	public void setOnChangeQuaListener(OnChangeQuaListener listener) {
		this.mOnChangeQuaListener = listener;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (v.getId() == R.id.id_change_qua_index) {
			return true;
		} else {
		}
		return false;
	}

}
