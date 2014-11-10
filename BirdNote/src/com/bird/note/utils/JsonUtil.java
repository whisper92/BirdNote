package com.bird.note.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JsonUtil {

	/**
	 * 将各个象限的文字内容转化成json
	 * @param text_array
	 * @return
	 * @throws JSONException 
	 */
	public static String createJsonFromStrings(String[] text_array) throws JSONException{
		String jsonString="";
		jsonString+="{\"textcontents\":[";
		for (int i = 0; i < text_array.length; i++) {
			jsonString+="{";
			jsonString+="\"qua\":"+i+",\"quacontent\":\""+text_array[i]+"\"";
			jsonString+="}";
			if (i!=text_array.length-1) {
				jsonString+=",";
			}
			
		}
		jsonString+="]}";
		
/*		
		String[] hello=parseJsonToStrings(jsonString);
		for (int i = 0; i < hello.length; i++) {
			Log.e("wxp",i+hello[i]);
		}*/
		return jsonString;
	}
	
	/**
	 * 将json转换为字符串数组
	 */
	public static String[] parseJsonToStrings(String origJson) throws JSONException{
		List<String> textList=new ArrayList<String>();
		String[] textContents=new String[4];
		JSONObject rootObject=new JSONObject(origJson);
		JSONArray textContentsArray=rootObject.getJSONArray("textcontents");
		for (int i = 0; i < textContentsArray.length(); i++) {
			JSONObject quaTextJsonObject=(JSONObject) textContentsArray.opt(i);
			textContents[i]=quaTextJsonObject.getString("quacontent");
			textList.add(i, quaTextJsonObject.getString("quacontent"));
		}
		return textContents;
	}
}
