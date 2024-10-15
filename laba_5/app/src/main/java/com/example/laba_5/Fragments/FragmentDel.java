package com.example.laba_5.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.laba_5.Database.Database;
import com.example.laba_5.Database.DatabaseManager;
import com.example.laba_5.Model.Notes;
import com.example.laba_5.R;

public class FragmentDel extends Fragment {

    private Database database;
    private EditText editText;

    public FragmentDel() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_del, container, false);
        database = DatabaseManager.getDatabase();
        editText = view.findViewById(R.id.editText);
        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click();
            }
        });
        return view;
    }

    private void click() {
        int content = editText.getText().toString().isEmpty() ? 0 : Integer.parseInt(editText.getText().toString());
        if (content > 0 && database.delNote(content)) {
            Toast.makeText(getContext(), "Note deleted!", Toast.LENGTH_SHORT).show();
            editText.setText("");
        } else {
            Toast.makeText(getContext(), "There are no notes under this number", Toast.LENGTH_SHORT).show();
        }
    }
}