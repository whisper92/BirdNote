package com.bird.note.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bird.note.R;
import com.bird.note.dao.Db;
import com.bird.note.model.BirdNote;
import com.bird.note.model.ShowNoteAdapter;
import com.bird.note.model.TextLine;
import com.bird.note.ui.EditNoteActivity;
import com.bird.note.utils.BitmapUtil;

import android.app.*;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.*;
import android.util.Log;
import android.view.*;
import android.widget.*;

/**
 * 测试序列化 测试结果：OK
 * 
 * @author root
 * 
 */
public class TestGridViewActivity extends Activity implements
		View.OnClickListener {
	ImageView addPen;
	ImageView addText;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_notes);
		Db db = new Db(this);
		SQLiteDatabase dbRead = db.getReadableDatabase();

		ArrayList<BirdNote> listData = new ArrayList<BirdNote>();
		for (int i = 0; i < 40; i++) {

			BirdNote birdNote = new BirdNote();
			birdNote._id = i;
			birdNote.level = i;
			birdNote.title = i + "--->";
			if (i % 4 == 1) {
				birdNote.byteArrayThumbnail = BitmapUtil
						.decodeBitmapToBytes(BitmapUtil
								.decodeDrawableToBitmap(getResources().getDrawable(
										R.drawable.ic_launcher)));
			} else {
				birdNote.byteArrayThumbnail = BitmapUtil
						.decodeBitmapToBytes(BitmapUtil
								.decodeDrawableToBitmap(getResources().getDrawable(
										R.drawable.show_title_add_text)));

			}
			listData.add(birdNote);
		}

		GridView gridView = (GridView) findViewById(R.id.id_show_gv);
		ShowNoteAdapter noteAdapter = new ShowNoteAdapter(this, listData,
				gridView);
		gridView.setAdapter(noteAdapter);
		noteAdapter.notifyDataSetChanged();

		Log.e("wxp", "data length--->" + listData.size());
		addPen = (ImageView) findViewById(R.id.id_show_title_new_pen);
		addPen.setOnClickListener(this);
		addText = (ImageView) findViewById(R.id.id_show_title_new_text);
		addText.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		intent.setClass(TestGridViewActivity.this, EditNoteActivity.class);
		if (v.getId() == R.id.id_show_title_new_pen) {
			intent.putExtra("type", R.id.id_edit_title_pen);
		}
		if (v.getId() == R.id.id_show_title_new_text) {
			intent.putExtra("type", R.id.id_edit_title_text);
		}
		startActivity(intent);
	}

}
