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
    public VerNoticiasItemClickListener clickListener;

    public VerNoticiasAdapter(List<VerNoticiasItem> newsList) {
        // Add this field for image loading
        this.newsList = newsList;
    }

    public void setClickListener(VerNoticiasItemClickListener myListener){
        this.clickListener = myListener;
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
        Picasso.get().load(VerNoticiasItem.getFotoUrl()).placeholder(R.drawable.icon_loading).error(R.drawable.socio1).into(holder.imageView);
        holder.titleTextView.setText(VerNoticiasItem.getTitle());
        holder.contentTextView.setText(VerNoticiasItem.getContent());
        holder.authorTextView.setText(VerNoticiasItem.getAuthor());


    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView titleTextView, contentTextView, authorTextView;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageNoticia); // Update with your ImageView ID
            titleTextView = itemView.findViewById(R.id.tituloNoticia);
            contentTextView = itemView.findViewById(R.id.cuerpoNoticia);
            authorTextView = itemView.findViewById(R.id.autorNoticia);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.onClick(view, getBindingAdapterPosition());
            }
        }
    }
}