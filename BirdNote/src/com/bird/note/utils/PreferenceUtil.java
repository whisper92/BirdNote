package com.bird.note.utils;

import com.bird.note.model.BirdMessage;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * @author wangxianpeng
 * @since 19/12/14
 *
 */
public class PreferenceUtil {

	public static void setSortBy(int value) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(NoteApplication.getContext());
		sharedPreferences.edit().putInt(BirdMessage.SORT_BY, value).commit();
	}

	public static int getSortBy() {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(NoteApplication.getContext());
		return sharedPreferences.getInt(BirdMessage.SORT_BY, BirdMessage.SORT_BY_DEFAULT);
	}

}
