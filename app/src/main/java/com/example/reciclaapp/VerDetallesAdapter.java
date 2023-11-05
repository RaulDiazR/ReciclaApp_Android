package com.example.reciclaapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VerDetallesAdapter extends RecyclerView.Adapter<VerDetallesAdapter.ViewHolder> {
    private List<VerDetallesItem> items;

    public VerDetallesAdapter(List<VerDetallesItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ver_detalles_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VerDetallesItem item = items.get(position);

        holder.iconoMaterial.setImageResource(item.getIconoMaterial());
        holder.nombreMaterial.setText(item.getNombreMaterial());
        holder.unidadMaterial.setText(item.getUnidadMaterial());
        holder.cantidadMaterial.setText(item.getCantidadMaterial());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iconoMaterial;
        TextView nombreMaterial;
        TextView unidadMaterial;
        TextView cantidadMaterial;
        Button verFotoBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            iconoMaterial = itemView.findViewById(R.id.iconoMaterial);
            nombreMaterial = itemView.findViewById(R.id.nombreMaterial);
            unidadMaterial = itemView.findViewById(R.id.unidadMaterial);
            cantidadMaterial = itemView.findViewById(R.id.cantidadMaterial);
            verFotoBtn =  itemView.findViewById(R.id.verFotoBtn);
        }
    }
}

