package com.example.laba_4.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laba_4.model.Photo;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {

    Context context;
    List<Photo> photos;

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {

    }

    public static final class PhotoViewHolder extends RecyclerView.ViewHolder {

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
