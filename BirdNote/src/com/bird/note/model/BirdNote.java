package com.bird.note.model;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * 一个笔记的主体类
 * 
 * @author wangxianpeng
 * 
 */
public class BirdNote implements Parcelable {

	/*
	 * 唯一的id
	 */
	public int _id;
	/*
	 * 等级：1,2,3,4
	 */
	public int level;
	/*
	 * 标题
	 */
	public String title;
	/*
	 * 笔记中包含的四个象限的笔记
	 */
	public List<ChildNote> childNotes = new ArrayList<ChildNote>();

	/*
	 * 绘制的内容 public Bitmap drawContent;
	 */
	public byte[] byteArrayNoteContent;
	/*
	 * 用于显示在首页的缩略图 public Bitmap thumbnail;
	 */
	public byte[] byteArrayNoteThumbnail;

	public String createTime;
	public String lastEditTime;

	@Override
	public int describeContents() {

		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeInt(_id);
		dest.writeInt(level);
		dest.writeString(title);
		dest.writeTypedList(childNotes);
		dest.writeByteArray(byteArrayNoteContent);
		dest.writeByteArray(byteArrayNoteThumbnail);
	}

	public static final Parcelable.Creator<BirdNote> CREATOR = new Parcelable.Creator<BirdNote>() {

		@Override
		public BirdNote createFromParcel(Parcel source) {
			BirdNote birdNote = new BirdNote();
			birdNote._id = source.readInt();
			birdNote.level = source.readInt();
			birdNote.title = source.readString();
			source.readTypedList(birdNote.childNotes, ChildNote.CREATOR);
			birdNote.byteArrayNoteContent = source.createByteArray();
			birdNote.byteArrayNoteThumbnail = source.createByteArray();
			return birdNote;
		}

		@Override
		public BirdNote[] newArray(int size) {

			return new BirdNote[size];
		}
	};

}
