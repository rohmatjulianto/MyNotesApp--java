package com.example.mynotesappsqllite.entity;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.mynotesappsqllite.db.DatabaseContract;

import static android.provider.BaseColumns._ID;
import static com.example.mynotesappsqllite.db.DatabaseContract.getColumnInt;
import static com.example.mynotesappsqllite.db.DatabaseContract.getColumnString;

public class note implements Parcelable {
    private int id;
    private String title, desc, date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.desc);
        dest.writeString(this.date);
    }

    public note() {
    }



    protected note(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.desc = in.readString();
        this.date = in.readString();
    }

    public note(int id, String title, String desc, String date) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.date = date;
    }

    public note(Cursor cursor){
        this.id = getColumnInt(cursor , _ID);
        this.title = getColumnString(cursor, DatabaseContract.NoteColumns.TITLE);
        this.desc = getColumnString(cursor, DatabaseContract.NoteColumns.DESC);
        this.date = getColumnString(cursor, DatabaseContract.NoteColumns.DATE);
    }

    public static final Parcelable.Creator<note> CREATOR = new Parcelable.Creator<note>() {
        @Override
        public note createFromParcel(Parcel source) {
            return new note(source);
        }

        @Override
        public note[] newArray(int size) {
            return new note[size];
        }
    };
}
