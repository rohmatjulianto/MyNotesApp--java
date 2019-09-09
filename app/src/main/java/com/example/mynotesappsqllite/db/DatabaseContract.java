package com.example.mynotesappsqllite.db;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;


public class DatabaseContract {
    public static String TABLE_NOTE = "note";
    public static final String AUTHORITY = "com.example.mynotesappsqllite";
    private static final String SCHEME = "content";

    public static final class NoteColumns implements BaseColumns{
        public static String TITLE = "title";
        public static String DESC = "desc";
        public static String DATE = "date";

        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NOTE)
                .build();
    }

    public static String getColumnString(Cursor cursor, String columnName){
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getColumnInt(Cursor cursor, String columnName){
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    public static long getColumnLong(Cursor cursor, String columnName){
        return cursor.getLong(cursor.getColumnIndex(columnName));
    }
}
