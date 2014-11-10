package com.bird.note.test;

import java.util.ArrayList;
import java.util.List;

import com.bird.note.R;
import com.bird.note.dao.Db;
import com.bird.note.model.BirdNote;
import com.bird.note.test.TestParcelActivity;
import com.bird.note.utils.BitmapUtil;

import android.app.*;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.*;
import android.view.*;
import android.widget.*;

public class MainActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_notes);
       
    }
}
