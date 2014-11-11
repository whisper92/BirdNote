package com.bird.note.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 每个象限的内容
 * 
 * @author root
 * 
 */
public class QuadrantContent implements Parcelable {

	/*
	 * 象限序号
	 */
	public int quadrant;
	/*
	 * 象限绘制内容
	 */
	public byte[] quadrantdraw;
	/*
	 * 象限文字内容
	 */
	public String textcontent;

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(quadrant);
		dest.writeByteArray(quadrantdraw);
		dest.writeString(textcontent);
	}

	public static final Parcelable.Creator<QuadrantContent> CREATOR = new Parcelable.Creator<QuadrantContent>() {

		@Override
		public QuadrantContent createFromParcel(Parcel source) {
			QuadrantContent quadrantContent = new QuadrantContent();
                         quadrantContent.quadrant=source.readInt();
                         quadrantContent.quadrantdraw=source.createByteArray();
                         quadrantContent.textcontent=source.readString();
			return quadrantContent;
		}

		@Override
		public QuadrantContent[] newArray(int size) {

			return new QuadrantContent[size];
		}
	};
}
