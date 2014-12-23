package com.bird.note.utils;

import android.app.Application;
import android.content.Context;
import android.graphics.Paint;
import android.util.Log;

import com.bird.note.R;
import com.bird.note.model.BirdMessage;
import com.bird.note.model.BirdNote;

/**
 * @author wangxianpeng
 * @since 19/12/14
 *
 */
public class NoteApplication extends Application {

	/*
	 * 当前笔记的编辑状态：新建或更新
	 */
	private int CurrentNoteEidtType = BirdMessage.NOTE_EDIT_TYPE_CREATE;

	public int getCurrentNoteEidtType() {
		return CurrentNoteEidtType;
	}

	public void setCurrentNoteEidtType(int eidtType) {
		CurrentNoteEidtType = eidtType;
	}

	/*
	 * 正在更新的笔记的id
	 */
	private int EditNoteId = -1;

	public int getEditNoteId() {
		return EditNoteId;
	}

	public void setEditNoteId(int editNoteId) {
		EditNoteId = editNoteId;
	}

	/*
	 * 当前编辑的笔记对象
	 */
	private BirdNote EditNote = null;

	public BirdNote getEditNote() {
		return EditNote;
	}

	public void setEditNote(BirdNote editNote) {
		EditNote = editNote;
	}

	/*
	 * 更新笔记时，修改过的象限
	 */

	public int[] EditedQuadrants = new int[] { 0, 0, 0, 0 };

	public int[] getEditedQuadrants() {
		return EditedQuadrants;
	}

	public void setEditedQuadrants(int[] editedQuadrants) {
		EditedQuadrants = editedQuadrants;
	}

	/*
	 * 背景资源
	 */
	public int editBackground = BitmapUtil.EDIT_BGS[0];

	public int getEditBackground() {
		boolean flag = false;
		for (int i = 0; i < BitmapUtil.EDIT_BGS.length; i++) {
			if (editBackground == BitmapUtil.EDIT_BGS[i]) {
				flag = true;
			}
		}
		if (flag) {
			return editBackground;
		} else {
			return BitmapUtil.EDIT_BGS[0];
		}

	}

	public void setEditBackground(int editBackground) {
		this.editBackground = editBackground;
	}

	public int screenWidth = 0;
	public int screenHeight = 0;

	public int mCurrentEditMode = R.id.id_edit_title_pen;

	public int getCurrentEditMode() {
		return mCurrentEditMode;
	}

	public void setCurrentEditMode(int currentEditMode) {
		this.mCurrentEditMode = currentEditMode;
	}

	private static Context sContext;

	public static Context getContext() {
		return sContext;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		sContext = getApplicationContext();
	}

	public boolean edited = false;

	public boolean isEdited() {
		return edited;
	}

	public void setEdited(boolean edited) {
		this.edited = edited;
	}

	/*public boolean[][] undoredo = new boolean[][] { { false, false },
			{ false, false }, { false, false }, { false, false } };

	public boolean[][] getUndoredo() {
		return undoredo;
	}

	public void setUndoredo(boolean[][] undoredo) {
		this.undoredo = undoredo;
	}

	public void initUndoRedo() {
		undoredo = new boolean[][] { { false, false }, { false, false }, { false, false }, { false, false } };
	}*/

	public int notescount = 0;

	public int getNotescount() {
		return notescount;
	}

	public void setNotescount(int notescount) {
		this.notescount = notescount;
	}
	
	public Paint currentPaint = null;

	public Paint getCurrentPaint() {
		return currentPaint;
	}

	public void setCurrentPaint(Paint currentPaint) {
		this.currentPaint = currentPaint;
	}
	
	public boolean cleanMode = false;

	public boolean isCleanMode() {
		return cleanMode;
	}

	public void setCleanMode(boolean cleanMode) {
		this.cleanMode = cleanMode;
	}
}
