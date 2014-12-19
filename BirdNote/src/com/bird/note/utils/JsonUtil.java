package com.bird.note.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bird.note.dao.NotesTable;

/**
 * @author wangxianpeng
 * @since 19/12/14
 *
 */
public class JsonUtil {

	/**
	 * 将各个象限的文字内容转化成json
	 *
	 * @param text_array
	 * @return
	 * @throws JSONException
	 */
	public static String createJsonFromStrings(String[] text_array) throws JSONException {
		String jsonString = "";
		jsonString += "{\"" + NotesTable.TEXTCONTENT_HEADER + "\":[";
		for (int i = 0; i < text_array.length; i++) {
			jsonString += "{";
			jsonString += "\"" + NotesTable.TEXTCONTENT_OBJECT_QUA + "\":" + i + ",";
			if (text_array[i] != null) {
				jsonString += "\"" + NotesTable.TEXTCONTENT_OBJECT_QUACONTENT	+ "\":\"" + text_array[i] + "\"";
			} else {
				jsonString += "\"" + NotesTable.TEXTCONTENT_OBJECT_QUACONTENT + "\":null";
			}

			jsonString += "}";
			if (i != text_array.length - 1) {
				jsonString += ",";
			}

		}
		jsonString += "]}";

		return jsonString;
	}

	/**
	 * 将json转换为字符串数组
	 */
	public static String[] parseJsonToStrings(String origJson) throws JSONException {
		String[] textContents = new String[4];
		JSONObject rootObject = new JSONObject(origJson);
		JSONArray textContentsArray = rootObject.getJSONArray(NotesTable.TEXTCONTENT_HEADER);
		for (int i = 0; i < textContentsArray.length(); i++) {
			JSONObject quaTextJsonObject = (JSONObject) textContentsArray.opt(i);
			textContents[i] = quaTextJsonObject.getString(NotesTable.TEXTCONTENT_OBJECT_QUACONTENT);// error
		}
		return textContents;
	}
}
