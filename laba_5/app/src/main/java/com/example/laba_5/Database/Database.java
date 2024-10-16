package com.example.laba_5.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

import com.example.laba_5.Model.Notes;

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

    public boolean delNote(int noteNumber) {
        int id = getIdByNoteNumber(noteNumber);
        if (id != -1) {
            int result = db.delete("Notes", "id = ?", new String[] { String.valueOf(id) });
            return result > 0;
        } else {
            return false;
        }
    }

    public boolean updateNote(int noteNumber, String note) {
        if (existsNote(note)) {
            return false;
        }
        int id = getIdByNoteNumber(noteNumber);
        if (id != -1) {
            ContentValues values = new ContentValues();
            values.put("content", note);
            int result = db.update("Notes", values, "id = ?", new String[] { String.valueOf(id) });
            return result > 0;
        } else {
            return false;
        }
    }

    @SuppressLint("Range")
    private int getIdByNoteNumber(int noteNumber) {
        Cursor cursor = db.query("Notes", new String[] { "id" }, null, null, null, null, null);
        int id = -1;
        while (cursor.moveToNext()) {
            id = cursor.getInt(cursor.getColumnIndex("id"));
            if (cursor.getPosition() == noteNumber - 1) {
                break;
            }
        }
        cursor.close();
        return id;
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
    public void onCreate(@NonNull SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Notes (id INTEGER PRIMARY KEY AUTOINCREMENT, content TEXT)");
    }

    @Override
    public void onUpgrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Notes");
        onCreate(db);
    }
}
