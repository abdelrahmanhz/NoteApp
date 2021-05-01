package com.example.notes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseAccess {

    private SQLiteDatabase database;
    private SQLiteOpenHelper openHelper;
    private static DatabaseAccess instance;

    public DatabaseAccess(Context context) {

        this.openHelper = new MyDatabase(context);
    }

    public static DatabaseAccess getInstance(Context context){

        if(instance == null)
            instance = new DatabaseAccess(context);
        return instance;
    }

    public void open(){

        this.database = this.openHelper.getWritableDatabase();
    }

    public void close(){

        if(this.database != null)
            this.database.close();
    }

    public boolean insertNote(Note note){

        ContentValues contentValues = new ContentValues();
        contentValues.put(MyDatabase.NOTE_TITLE_COL, note.getTitle());
        contentValues.put(MyDatabase.NOTE_CONTENT_COL, note.getContent());

        long result = database.insert(MyDatabase.NOTE_TABLE_NAME, null, contentValues);

        return result != -1;
    }

    public boolean updateNote(Note note){

        ContentValues contentValues = new ContentValues();
        contentValues.put(MyDatabase.NOTE_TITLE_COL, note.getTitle());
        contentValues.put(MyDatabase.NOTE_CONTENT_COL, note.getContent());

        String args[] = {String.valueOf(note.getId())};
        int result = database.update(MyDatabase.NOTE_TABLE_NAME, contentValues,"id=?", args);

        return result > 0;
    }

    public boolean deleteNote(Note note){

        String args[] = {String.valueOf(note.getId())};
        int result = database.delete(MyDatabase.NOTE_TABLE_NAME,"id=?", args);

        return result > 0;
    }

    public ArrayList<Note> getAllNotes(){

        ArrayList<Note> notes = new ArrayList<>();


        Cursor cursor = database.rawQuery("SELECT * FROM " + MyDatabase.NOTE_TABLE_NAME, null);

        if(cursor != null && cursor.moveToFirst()){

            do{

                    int id = cursor.getInt(cursor.getColumnIndex(MyDatabase.NOTE_ID_COL));
                    String title = cursor.getString(cursor.getColumnIndex(MyDatabase.NOTE_TITLE_COL));
                    String content = cursor.getString(cursor.getColumnIndex(MyDatabase.NOTE_CONTENT_COL));
                    Note note = new Note(id, title, content);
                    notes.add(note);
            }
            while (cursor.moveToNext());
            cursor.close();


        }
        return notes;
    }

    public ArrayList<Note> searchNotes(String searchWord){

        ArrayList<Note> notes = new ArrayList<>();


        Cursor cursor = database.rawQuery("SELECT * FROM " + MyDatabase.NOTE_TABLE_NAME + " WHERE " +
                MyDatabase.NOTE_TITLE_COL + " OR " + MyDatabase.NOTE_CONTENT_COL + " LIKE ? ", new String[]{"%" + searchWord + "%"});

        if(cursor != null && cursor.moveToFirst()){

            do{

                int id = cursor.getInt(cursor.getColumnIndex(MyDatabase.NOTE_ID_COL));
                String title = cursor.getString(cursor.getColumnIndex(MyDatabase.NOTE_TITLE_COL));
                String content = cursor.getString(cursor.getColumnIndex(MyDatabase.NOTE_CONTENT_COL));
                Note note = new Note(id, title, content);
                notes.add(note);
            }
            while (cursor.moveToNext());
            cursor.close();
        }
        return notes;
    }

    public long getNotesCount(){

        return DatabaseUtils.queryNumEntries(database, MyDatabase.NOTE_TABLE_NAME);
    }


    public Note getNote(int noteId) {

        Cursor cursor = database.rawQuery("SELECT * FROM " + MyDatabase.NOTE_TABLE_NAME + " WHERE " +
                MyDatabase.NOTE_ID_COL + " =? ", new String[]{String.valueOf(noteId)});

        if(cursor != null && cursor.moveToFirst()){

            int id = cursor.getInt(cursor.getColumnIndex(MyDatabase.NOTE_ID_COL));
            String title = cursor.getString(cursor.getColumnIndex(MyDatabase.NOTE_TITLE_COL));
            String content = cursor.getString(cursor.getColumnIndex(MyDatabase.NOTE_CONTENT_COL));
            Note note = new Note(id, title, content);
            cursor.close();
            return note;
        }
        return null;
    }
}
