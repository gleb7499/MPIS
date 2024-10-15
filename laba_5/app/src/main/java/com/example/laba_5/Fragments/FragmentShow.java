package com.example.laba_5.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laba_5.Adapters.RecyclerViewNotesAdapter;
import com.example.laba_5.Database.Database;
import com.example.laba_5.Database.DatabaseManager;
import com.example.laba_5.Model.Notes;
import com.example.laba_5.Model.NotesManager;
import com.example.laba_5.R;

import java.util.List;

public class FragmentShow extends Fragment {

    private Database database;
    private RecyclerViewNotesAdapter recyclerViewNotesAdapter;
    private RecyclerView recyclerView;
    private TextView textViewNothing;

    public FragmentShow() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        database = DatabaseManager.getDatabase();
        textViewNothing = view.findViewById(R.id.textViewNothing);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        addNote();
    }

    private void addNote() {
        NotesManager.addNotes(database.getAllNotes());
        recyclerViewNotesAdapter = new RecyclerViewNotesAdapter(getContext(), NotesManager.getNotes());
        recyclerView.setAdapter(recyclerViewNotesAdapter);
        textViewNothing.setVisibility(NotesManager.isEmpty() ? View.VISIBLE : View.INVISIBLE);
        recyclerView.setVisibility(NotesManager.isEmpty() ? View.INVISIBLE : View.VISIBLE);
    }
}