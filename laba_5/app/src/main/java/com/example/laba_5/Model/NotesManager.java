package com.example.laba_5.Model;

import java.util.ArrayList;
import java.util.List;

public class NotesManager {
    private static List<Notes> notes;

    public static void setNotes() {
        NotesManager.notes = new ArrayList<>();
    }

    public static void addNote(Notes note) {
        notes.add(note);
    }



    public static List<Notes> getNotes() {
        return notes;
    }
}
