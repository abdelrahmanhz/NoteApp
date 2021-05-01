package com.example.notes;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class MyDatabase extends SQLiteAssetHelper {

    private static final String DB_NAME = "notes.db";
    private static final int DB_VERSION = 1;

    public static final String NOTE_TABLE_NAME = "note";
    public static final String NOTE_ID_COL = "id";
    public static final String NOTE_TITLE_COL = "title";
    public static final String NOTE_CONTENT_COL = "content";

    public MyDatabase(Context context){

        super(context, DB_NAME, null, DB_VERSION);
    }
}
