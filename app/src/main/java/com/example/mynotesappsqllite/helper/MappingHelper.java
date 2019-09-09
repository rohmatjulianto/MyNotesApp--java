package com.example.mynotesappsqllite.helper;

import android.database.Cursor;

import com.example.mynotesappsqllite.entity.note;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.example.mynotesappsqllite.db.DatabaseContract.NoteColumns.DATE;
import static com.example.mynotesappsqllite.db.DatabaseContract.NoteColumns.DESC;
import static com.example.mynotesappsqllite.db.DatabaseContract.NoteColumns.TITLE;

public class MappingHelper {

    public static ArrayList<note> mapCursorToArrayList(Cursor cursor){
        ArrayList<note> noteList = new ArrayList<>();

        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(_ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(TITLE));
            String desc = cursor.getString(cursor.getColumnIndexOrThrow(DESC));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(DATE));

            noteList.add(new note(id, title, desc,date));
        }
        return noteList;
    }
}
