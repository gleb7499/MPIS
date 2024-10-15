package com.example.laba_5.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;

import com.example.laba_5.Notes;

import java.util.ArrayList;
import java.util.List;

public class Database {

    private final SQLiteDatabase db;
    private final DatabaseHelper dbHelper;

    public Database(Context context) {
        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public boolean addNote(@NonNull Notes note) {
        if (existsNote(note.getContent())) {
            return false;
        }
        ContentValues values = new ContentValues();
        values.put("content", note.getContent());
        db.insertOrThrow("Notes", null, values);
        return true;
    }

    public List<Notes> getAllNotes() {
        Cursor cursor = db.query("Notes", null, null, null, null, null, null);
        List<Notes> notes = new ArrayList<>();
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String content = cursor.getString(cursor.getColumnIndex("content"));
            notes.add(new Notes(content));
        }
        cursor.close();
        return notes;
    }

    private boolean existsNote(String content) {
        boolean flag;
        Cursor cursor = db.query("Notes", null, "content = ?", new String[] { content }, null, null, null);
        flag = cursor.moveToFirst();
        cursor.close();
        return flag;
    }

    public void close() {
        db.close();
        dbHelper.close();
    }
}

class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Notes.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Notes (id INTEGER PRIMARY KEY AUTOINCREMENT, content TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Notes");
        onCreate(db);
    }
}
