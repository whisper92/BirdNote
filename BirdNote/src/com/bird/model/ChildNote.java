package com.bird.model;

import java.util.ArrayList;
import java.util.List;
import com.bird.utils.BitmapUtil;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;


/**
 * 每个象限对应的笔记
 * 
 * @author wangxianpeng
 * 
 */
public class ChildNote implements Parcelable {

	/*
	 * 子笔记的id
	 */
	public int childID;
	/*
	 * 所属父笔记的id
	 */
	public int noteID;

	/*
	 * 所属的象限：1，2，3，4
	 */
	public int quadrant;

	/*
	 * 每个笔记中包含的文本行
	 */
	public List<TextLine> textLines =new ArrayList<TextLine>();

	/*
	 * 每个象限的笔记的绘制内容	public Bitmap bitmapDrawContent;
	 */

	public byte[] byteArrayChildDrawContent;
	/*
	 * 每个象限的笔记的缩略图 	public Bitmap bitmapChildThumbnail;
	 */

	public byte[] byteArrayChildThumbnail;

	@Override
	public int describeContents() {

		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(childID);
		dest.writeInt(noteID);
		dest.writeInt(quadrant);
		dest.writeTypedList(textLines);
		dest.writeByteArray(byteArrayChildDrawContent);
		dest.writeByteArray(byteArrayChildThumbnail);

	}

	// 实例化静态内部对象CREATOR实现接口Parcelable.Creator
	public static final Parcelable.Creator<ChildNote> CREATOR = new Creator<ChildNote>() {

		@Override
		public ChildNote[] newArray(int size) {
			return new ChildNote[size];
		}

		// 将Parcel对象反序列化
		@Override
		public ChildNote createFromParcel(Parcel source) {
			ChildNote childNote=new ChildNote();
			childNote.childID=source.readInt();
			childNote.noteID=source.readInt();
			childNote.quadrant=source.readInt();
			source.readTypedList(childNote.textLines, TextLine.CREATOR);
			childNote.byteArrayChildDrawContent=source.createByteArray();
			childNote.byteArrayChildThumbnail=source.createByteArray();	
			return childNote;
		}
	};

}
