package com.bird.note.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author wangxianpeng
 * @since 19/12/14
 *
 */
public class BirdNote implements Parcelable {

	/*
	 * 唯一的id
	 */
	public int _id;
	/*
	 * 等级：用于标示不同封面
	 */
	public int level;
	/*
	 * 标题
	 */
	public String title;
	
	public String textcontents;

	/*
	 * 绘制的内容 public Bitmap drawContent;
	 */
	public byte[] qua0=null;
	public byte[] qua1=null;
	public byte[] qua2=null;
	public byte[] qua3=null;

	/*
	 * 使用的背景图片id
	 */
	public int background=0;
	/*
	 * 收藏
	 */
	public int star=0;
	
	/*
	 * public String createTime; public String lastEditTime;
	 */
	public String create_time;
	public String update_time;
	

	public BirdNote(){
		
	}
	
	public BirdNote(int level, String title, String textContents, byte[] byteArrayQua0,
			byte[] byteArrayQua1, byte[] byteArrayQua2, byte[] byteArrayQua3,int background,int star) {
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
		dest.writeString(textcontents);
		dest.writeByteArray(qua0);
		dest.writeByteArray(qua1);
		dest.writeByteArray(qua2);
		dest.writeByteArray(qua3);
		dest.writeInt(background);
		dest.writeInt(star);
		dest.writeString(create_time);
		dest.writeString(update_time);
	}

	public static final Parcelable.Creator<BirdNote> CREATOR = new Parcelable.Creator<BirdNote>() {

		@Override
		public BirdNote createFromParcel(Parcel source) {
			BirdNote birdNote = new BirdNote();
			birdNote._id = source.readInt();
			birdNote.level = source.readInt();
			birdNote.title = source.readString();
			birdNote.textcontents = source.readString();
			birdNote.qua0 = source.createByteArray();
			birdNote.qua1 = source.createByteArray();
			birdNote.qua2 = source.createByteArray();
			birdNote.qua3 = source.createByteArray();
			birdNote.background = source.readInt();
			birdNote.star = source.readInt();
			birdNote.create_time = source.readString();
			birdNote.update_time = source.readString();
			return birdNote;
		}

		@Override
		public BirdNote[] newArray(int size) {
			return new BirdNote[size];
		}
	};

}
