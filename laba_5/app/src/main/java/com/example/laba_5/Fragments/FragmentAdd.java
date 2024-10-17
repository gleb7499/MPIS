package com.example.laba_5.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.laba_5.Database.Database;
import com.example.laba_5.Database.DatabaseManager;
import com.example.laba_5.MainActivity;
import com.example.laba_5.Model.Notes;
import com.example.laba_5.Model.NotesManager;
import com.example.laba_5.R;

public class FragmentAdd extends Fragment {

    Database database;
    EditText editText;

    public FragmentAdd() {
        // Required empty public constructor
    }

    private void setMargins(View view) {
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            mlp.leftMargin = insets.left;
            mlp.bottomMargin = insets.bottom;
            mlp.rightMargin = insets.right;
            mlp.topMargin = insets.top;
            v.setLayoutParams(mlp);
            return WindowInsetsCompat.CONSUMED;
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        database = DatabaseManager.getDatabase();
        editText = view.findViewById(R.id.editTextNumNote);

        Button button = view.findViewById(R.id.button);

        setMargins(button);

        button.setOnClickListener(new View.OnClickListener() {
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