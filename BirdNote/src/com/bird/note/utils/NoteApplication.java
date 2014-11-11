package com.bird.note.utils;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;

import com.bird.note.model.BirdMessage;
import com.bird.note.model.BirdNote;
import com.bird.note.model.QuadrantContent;

/**
 * 保存一些全局变量
 */
public class NoteApplication extends Application{

	/*
	 * 当前笔记的编辑状态：新建或更新
	 */
	private int CurrentNoteEidtType=BirdMessage.NOTE_EDIT_TYPE_CREATE;
	public int getCurrentNoteEidtType() {
		return CurrentNoteEidtType;
	}
	public void setCurrentNoteEidtType(int eidtType) {
		CurrentNoteEidtType = eidtType;
	}
	
	/*
	 * 正在更新的笔记的id
	 */
	private int EditNoteId=-1;
	public int getEditNoteId() {
		return EditNoteId;
	}
	public void setEditNoteId(int editNoteId) {
		EditNoteId = editNoteId;
	}

	/*
	 * 当前编辑的笔记对象
	 */
	private BirdNote EditNote=null;
	public BirdNote getEditNote() {
		return EditNote;
	}
	public void setEditNote(BirdNote editNote) {
		EditNote = editNote;
	}
	
	/*
	 * 更新笔记时，修改过的象限
	 */

	public int[] EditedQuadrants=new int[]{0,0,0,0};
	public int[] getEditedQuadrants() {
		return EditedQuadrants;
	}
	public void setEditedQuadrants(int[] editedQuadrants) {
		EditedQuadrants = editedQuadrants;
	}
	
	
	
	/*
	 * 保存已经更新过的象限
	 */
	public List<QuadrantContent> EditedQuadrantContents=new ArrayList<QuadrantContent>();
	public List<QuadrantContent> getEditedQuadrantContents() {
		return EditedQuadrantContents;
	}
	public void setEditedQuadrantContents(
			List<QuadrantContent> editedQuadrantContents) {
		EditedQuadrantContents = editedQuadrantContents;
	}
	
}
