package com.bird.model;

import android.R.integer;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.EditText;

/**
 * 笔记中每一个文本行对应的类
 * @author wangxianpeng
 *
 */
public class TextLine  implements Parcelable {

	/*
	 * 文本的横坐标
	 */
	public int x;
	/*
	 * 文本的纵坐标
	 */
	public int y;
	
	/*
	 * 文本的内容
	 */
	public String textContent;

	public TextLine(int x,int y) {
		this.x=x;
		this.y=y;
	}
	
	public TextLine(int x,int y,String content) {
		this.x=x;
		this.y=y;
		this.textContent=content;
	}
	
	public TextLine(Parcel source){
		x=source.readInt();
		y=source.readInt();
		textContent=source.readString();
	}

	@Override
	public int describeContents() {	
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(x);
		dest.writeInt(y);
		dest.writeString(textContent);
	}

	 //实例化静态内部对象CREATOR实现接口Parcelable.Creator  
    public static final Parcelable.Creator<TextLine> CREATOR = new Creator<TextLine>() {  
          
        @Override  
        public TextLine[] newArray(int size) {  
            return new TextLine[size];  
        }  
          
        //将Parcel对象反序列化为TextLine  
        @Override  
        public TextLine createFromParcel(Parcel source) {  
            return new TextLine(source);  
        }  
    };  
    
}
