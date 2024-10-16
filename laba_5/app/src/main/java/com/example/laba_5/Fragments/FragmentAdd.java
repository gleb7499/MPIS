package com.example.laba_5.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.laba_5.Database.Database;
import com.example.laba_5.Database.DatabaseManager;
import com.example.laba_5.Model.Notes;
import com.example.laba_5.Model.NotesManager;
import com.example.laba_5.R;

public class FragmentAdd extends Fragment {

    Database database;
    EditText editText;

    public FragmentAdd() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        database = DatabaseManager.getDatabase();
        editText = view.findViewById(R.id.editTextNumNote);

        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNote(v);
            }
        });

        return view;
    }

    public void addNote(View view) {
        String text = editText.getText().toString();
        if (!text.isEmpty()) {
            Notes note = new Notes(text) ;
            if (database.addNote(note)) {
                Toast.makeText(getContext(), "Note added!", Toast.LENGTH_SHORT).show();
                NotesManager.addNote(note);
                editText.setText("");
            } else {
                Toast.makeText(getContext(), "Note already exists", Toast.LENGTH_SHORT).show();
            }
        }
    }
}