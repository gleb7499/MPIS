package com.example.laba_5.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laba_5.Notes;
import com.example.laba_5.R;

import java.util.List;

public class RecyclerViewNotesAdapter extends RecyclerView.Adapter<RecyclerViewNotesAdapter.NotesViewHolder> {

    private final Context context;
    private final List<Notes> notes;

    public RecyclerViewNotesAdapter(Context context, List<Notes> notes) {
        this.context = context;
        this.notes = notes;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_view_notes, parent, false);
        return new NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        holder.textViewNumberNote.setText(String.valueOf(position + 1));
        holder.textViewForNote.setText(notes.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public static final class NotesViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewNumberNote;
        private final TextView textViewForNote;

        public NotesViewHolder(View view) {
            super(view);
            textViewNumberNote = view.findViewById(R.id.textViewNumberNote);
            textViewForNote = view.findViewById(R.id.textViewForNote);
        }
    }
}
