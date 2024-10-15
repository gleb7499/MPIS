package com.example.laba_5.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laba_5.Model.Notes;
import com.example.laba_5.Model.NotesManager;
import com.example.laba_5.R;

import java.util.List;

public class RecyclerViewNotesAdapter extends RecyclerView.Adapter<RecyclerViewNotesAdapter.NotesViewHolder> {

    private final Context context;

    public RecyclerViewNotesAdapter(Context context, List<Notes> notes) {
        this.context = context;
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
        holder.textViewForNote.setText(NotesManager.getNote(position + 1));
    }

    @Override
    public int getItemCount() {
        return NotesManager.getNotesSize();
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
