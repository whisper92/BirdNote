package com.bird.note.model;

import java.util.ArrayList;
import java.util.List;

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
	
	public String textContents;

	/*
	 * 绘制的内容 public Bitmap drawContent;
	 */
	public byte[] byteArrayQua0=null;
	public byte[] byteArrayQua1=null;
	public byte[] byteArrayQua2=null;
	public byte[] byteArrayQua3=null;
	/*
	 * 用于显示在首页的缩略图 public Bitmap thumbnail;
	 */
	public byte[] byteArrayThumbnail=null;

	/*
	 * public String createTime; public String lastEditTime;
	 */

	public BirdNote(){
		
	}
	
	public BirdNote(int level, String title, String textContents, byte[] byteArrayQua0,
			byte[] byteArrayQua1, byte[] byteArrayQua2, byte[] byteArrayQua3,
			byte[] byteArrayThumbnail) {
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeInt(_id);
		dest.writeInt(level);
		dest.writeString(title);
		dest.writeString(textContents);
		dest.writeByteArray(byteArrayQua0);
		dest.writeByteArray(byteArrayQua1);
		dest.writeByteArray(byteArrayQua2);
		dest.writeByteArray(byteArrayQua3);
		dest.writeByteArray(byteArrayThumbnail);
		
	}

	public static final Parcelable.Creator<BirdNote> CREATOR = new Parcelable.Creator<BirdNote>() {

		@Override
		public BirdNote createFromParcel(Parcel source) {
			BirdNote birdNote = new BirdNote();
			birdNote._id = source.readInt();
			birdNote.level = source.readInt();
			birdNote.title = source.readString();
			birdNote.textContents = source.readString();
			birdNote.byteArrayQua0 = source.createByteArray();
			birdNote.byteArrayQua1 = source.createByteArray();
			birdNote.byteArrayQua2 = source.createByteArray();
			birdNote.byteArrayQua3 = source.createByteArray();
			birdNote.byteArrayThumbnail = source.createByteArray();
			
			return birdNote;
		}

		@Override
		public BirdNote[] newArray(int size) {

			return new BirdNote[size];
		}
	};

}
