package com.example.laba_4.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.laba_4.R;
import com.example.laba_4.model.Photo;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {

    private final Context context;
    private final List<Photo> photos;

    public PhotoAdapter(Context context, List<Photo> photos) {
        this.context = context;
        this.photos = photos;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View photoItems = LayoutInflater.from(context).inflate(R.layout.photo_item, parent, false);
        return new PhotoViewHolder(photoItems);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Glide.with(context).load(photos.get(position).getUser()).into(holder.imageViewUser);
        holder.textViewUser.setText(photos.get(position).getUser());
        Glide.with(context).load(photos.get(position).getLargeImageURL()).into(holder.imageViewLarge);
    }

    public static final class PhotoViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageViewUser;
        public TextView textViewUser;
        public ImageView imageViewLarge;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewUser = itemView.findViewById(R.id.imageViewUser);
            textViewUser = itemView.findViewById(R.id.textViewUser);
            imageViewLarge = itemView.findViewById(R.id.imageViewLarge);
        }
    }
}
