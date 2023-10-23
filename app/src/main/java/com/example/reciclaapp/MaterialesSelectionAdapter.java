package com.example.reciclaapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MaterialesSelectionAdapter extends RecyclerView.Adapter<MaterialesSelectionAdapter.MaterialViewHolder> {
    private final Context context;
    private final List<MaterialesSelectionItem> data; // You'll need to create a class for MaterialItem

    public MaterialesSelectionClickListener clickListener;

    public void setClickListener(MaterialesSelectionClickListener myListener){
        this.clickListener = myListener;
    }
    public MaterialesSelectionAdapter(Context context, List<MaterialesSelectionItem> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public MaterialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.materiales_selection_item, parent, false);
        return new MaterialViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MaterialViewHolder holder, int position) {
        MaterialesSelectionItem item = data.get(position);

        // Bind data to your view elements here
        holder.imageView.setImageResource(item.getImageResource());
        holder.textView.setText(item.getText());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MaterialViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView textView;

        MaterialViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image); // ImageView ID from your item layout
            textView = itemView.findViewById(R.id.text); // TextView ID from your item layout

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.onClick(v, getBindingAdapterPosition());
            }
        }
    }
}

