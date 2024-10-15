package com.example.laba_5.Model;

import java.util.ArrayList;
import java.util.List;

public class NotesManager {
    private static List<Notes> notes;

    public static void initializeNotes() {
        NotesManager.notes = new ArrayList<>();
    }

    public static void addNote(Notes note) {
        notes.add(note);
    }

    public static void delNote(int number) {
        notes.remove(number - 1);
    }

    public static void updateNote(int number, String content) {
        notes.get(number - 1).setContent(content);
    }

    public static void clearNotes() {
        if (!notes.isEmpty()) {
            notes.clear();
        }
    }

    public static int getNotesSize() {
        return notes.size();
    }

    public static Notes getNote(int number) {
        return notes.get(number - 1);
    }

    public static List<Notes> getNotes() {
        return notes;
    }

    public static void addNotes(List<Notes> allNotes) {
        clearNotes();
        notes.addAll(allNotes);
    }

    public static boolean isEmpty() {
        return notes.isEmpty();
    }
}
