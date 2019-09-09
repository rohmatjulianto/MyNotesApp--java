package com.example.mynotesappsqllite.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mynotesappsqllite.entity.note;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.example.mynotesappsqllite.db.DatabaseContract.NoteColumns.DATE;
import static com.example.mynotesappsqllite.db.DatabaseContract.NoteColumns.DESC;
import static com.example.mynotesappsqllite.db.DatabaseContract.NoteColumns.TITLE;
import static com.example.mynotesappsqllite.db.DatabaseContract.TABLE_NOTE;

public class NoteHelper {
    private static final String DATABASE_TABLE = TABLE_NOTE;
    private static DbHelper dbHelper;
    private static NoteHelper INSTANCE;

    private static SQLiteDatabase database;

    public NoteHelper(Context  context) {
        dbHelper = new DbHelper(context);
    }

    public static NoteHelper getInstance(Context context){
        if (INSTANCE == null){
            synchronized (SQLiteOpenHelper.class){
                if (INSTANCE == null){
                    INSTANCE = new NoteHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException{
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();

        if (database.isOpen())
            database.close();
    }

    public ArrayList<note> getAllNotes(){
        ArrayList<note> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE, null,
                null,null, null,null, _ID + " ASC", null);
        cursor.moveToFirst();
        note Note ;
        if (cursor.getCount() > 0){
            do {
                Note = new note();
                Note.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                Note.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                Note.setDesc(cursor.getString(cursor.getColumnIndexOrThrow(DESC)));
                Note.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DATE)));

                arrayList.add(Note);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long insertNote(note Note){
        ContentValues args = new ContentValues();
        args.put(TITLE, Note.getTitle());
        args.put(DESC, Note.getDesc());
        args.put(DATE, Note.getDate());

        return database.insert(DATABASE_TABLE, null, args);
    }

    public int updateNote(note Note){
        ContentValues args = new ContentValues();
        args.put(TITLE, Note.getTitle());
        args.put(DESC, Note.getDesc());
        args.put(DATE, Note.getDate());

        return database.update(DATABASE_TABLE, args, _ID + "= '" + Note.getId() + "'", null);
    }

    public int deleteNote(int id){
        return database.delete(TABLE_NOTE, _ID + "= '" + id + "'", null);
    }

    public Cursor queryByIdProvider(String id){
        return database.query(DATABASE_TABLE, null
        ,_ID + " = ?"
        , new String[]{id}
        ,null
        ,null
        ,null
        ,null);

    }

    public Cursor queryProvider(){
        return database.query(
                DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                _ID + " ASC"
        );
    }

    public long insertProvider(ContentValues contentValues){
        return database.insert(DATABASE_TABLE, null, contentValues);
    }

    public int updateProvider(String id , ContentValues contentValues){
        return database.update(DATABASE_TABLE, contentValues, _ID + " = ?", new String[]{id});
    }

    public int deleteProvider(String id){
        return database.delete(DATABASE_TABLE, _ID + " = ?", new String[]{id});
    }
}
