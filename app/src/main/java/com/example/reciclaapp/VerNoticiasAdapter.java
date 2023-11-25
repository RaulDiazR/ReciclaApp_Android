package com.example.reciclaapp;

// VerNoticiasAdapter.java
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class VerNoticiasAdapter extends RecyclerView.Adapter<VerNoticiasAdapter.NewsViewHolder> {

    private final List<VerNoticiasItem> newsList;

    public VerNoticiasAdapter(Context context, List<VerNoticiasItem> newsList) {
        // Add this field for image loading
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ver_noticias_item, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        VerNoticiasItem VerNoticiasItem = newsList.get(position);

        // Set the data to views
        Picasso.get().load(VerNoticiasItem.getImageResource()).placeholder(R.drawable.icon_loading).error(R.drawable.icon_user_gray).into(holder.imageView); // Use Glide for image loading
        holder.titleTextView.setText(VerNoticiasItem.getTitle());
        holder.contentTextView.setText(VerNoticiasItem.getContent());
        holder.authorTextView.setText(VerNoticiasItem.getAuthor());
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView, contentTextView, authorTextView;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageNoticia); // Update with your ImageView ID
            titleTextView = itemView.findViewById(R.id.tituloNoticia);
            contentTextView = itemView.findViewById(R.id.cuerpoNoticia);
            authorTextView = itemView.findViewById(R.id.autorNoticia);
        }
    }
}

