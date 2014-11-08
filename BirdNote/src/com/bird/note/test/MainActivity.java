package com.bird.note.test;

import java.util.ArrayList;
import java.util.List;

import com.bird.note.R;
import com.bird.note.dao.Db;
import com.bird.note.model.BirdNote;
import com.bird.note.model.ChildNote;
import com.bird.note.model.TextLine;
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
       
        Intent intent=new Intent(this,TestParcelActivity.class);
        Bundle bundle=new Bundle();
        
        TextLine textLine=new TextLine(0, 0, "hello");     
        Bitmap b1=Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Bitmap b2=Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        
        ChildNote childNote=new ChildNote();     
        List<TextLine> textLines=new ArrayList<TextLine>();
        textLines.add(textLine);      
        childNote.noteID=1;
        childNote.quadrant=1;
        childNote.textLines=textLines;
        childNote.byteArrayChildDrawContent=BitmapUtil.decodeBitmapToBytes(b1);
        childNote.byteArrayChildThumbnail=BitmapUtil.decodeBitmapToBytes(b2);
        
   
        BirdNote birdNote=new BirdNote();
        List<ChildNote> childNotes=new ArrayList<ChildNote>();
        childNotes.add(childNote);
        birdNote._id=1;
        birdNote.level=1;
        birdNote.title="title";
        birdNote.childNotes=childNotes;
        birdNote.byteArrayNoteContent=BitmapUtil.decodeBitmapToBytes(b1);
        birdNote.byteArrayNoteThumbnail=BitmapUtil.decodeBitmapToBytes(b2);
        
        bundle.putParcelable("note", childNote);
        bundle.putParcelable("birdnote", birdNote);
        intent.putExtras(bundle);
        //intent.putExtra("note", birdNote);
        
        //startActivity(intent);
        Db db=new Db(this);
        SQLiteDatabase dbRead=db.getReadableDatabase();
    }
}
