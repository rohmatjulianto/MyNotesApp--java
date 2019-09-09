package com.example.mynotesappsqllite.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

import com.example.mynotesappsqllite.MainActivity;
import com.example.mynotesappsqllite.db.DatabaseContract;
import com.example.mynotesappsqllite.db.NoteHelper;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import static com.example.mynotesappsqllite.db.DatabaseContract.AUTHORITY;
import static com.example.mynotesappsqllite.db.DatabaseContract.NoteColumns.CONTENT_URI;
import static com.example.mynotesappsqllite.db.DatabaseContract.TABLE_NOTE;

public class NoteProvider extends ContentProvider {

    private static final int NOTE = 1;
    private static final int NOTE_ID = 2;
    private static final UriMatcher sUrimatch = new UriMatcher(UriMatcher.NO_MATCH);
    private NoteHelper noteHelper;

    static {
        sUrimatch.addURI(AUTHORITY, TABLE_NOTE, NOTE);
        sUrimatch.addURI(AUTHORITY, TABLE_NOTE + "/#", NOTE_ID);
    }

    @Override
    public boolean onCreate() {
        noteHelper = NoteHelper.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        noteHelper.open();
        Cursor cursor;
        switch (sUrimatch.match(uri)){
            case NOTE:
                cursor = noteHelper.queryProvider();
                break;
            case NOTE_ID:
                cursor = noteHelper.queryByIdProvider(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        noteHelper.open();
        long added;
        switch (sUrimatch.match(uri)){
            case NOTE:
                added = noteHelper.insertProvider(contentValues);
                break;
            default:
                added = 0;
                break;
        }

        getContext().getContentResolver().notifyChange(CONTENT_URI, new MainActivity.DataObserver(new Handler(), getContext()));
        return Uri.parse(CONTENT_URI + "/" + added);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        noteHelper.open();
        int deleted;
        switch (sUrimatch.match(uri)){
            case NOTE_ID:
                deleted = noteHelper.deleteProvider(uri.getLastPathSegment());
                break;
            default:
                deleted = 0;
                break;
        }
        getContext().getContentResolver().notifyChange(CONTENT_URI, new MainActivity.DataObserver(new Handler(), getContext()));
        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        noteHelper.open();
        int updated;
        switch (sUrimatch.match(uri)){
            case NOTE_ID:
                updated = noteHelper.updateProvider(uri.getLastPathSegment(), contentValues);
                break;
            default:
                updated = 0;
                break;
        }

        getContext().getContentResolver().notifyChange(CONTENT_URI, new MainActivity.DataObserver(new Handler(), getContext()));
        return updated;

    }
}
