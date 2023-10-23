package com.example.reciclaapp;

import static android.app.PendingIntent.getActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MaterialesHeaderAdapter extends RecyclerView.Adapter<MaterialesHeaderAdapter.HeaderViewHolder> {
    private final Context context;
    private final MaterialesHeaderItem headerData;

    public MaterialesHeaderAdapter(Context context, MaterialesHeaderItem headerData) {
        this.context = context;
        this.headerData = headerData;
    }

    @NonNull
    @Override
    public HeaderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.materiales_header, parent, false);
        return new HeaderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HeaderViewHolder holder, int position) {
        // Bind data to your header view elements here
        holder.fechaOrdenTextView.setText(headerData.getFechaOrden());
        holder.tiempoOrdenTextView.setText(headerData.getTiempoOrden());
        holder.buttonCrear.setOnClickListener(v -> {
            Intent intent = new Intent(context, MaterialesSelectionActivity.class);
            context.startActivity(intent);
            ((Activity)(context)).finish();
        });
    }

    @Override
    public int getItemCount() {
        return 1; // There's only one header view
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView fechaOrdenTextView;
        TextView tiempoOrdenTextView;
        Button buttonCrear;

        HeaderViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageview);
            fechaOrdenTextView = itemView.findViewById(R.id.fecha_orden);
            tiempoOrdenTextView = itemView.findViewById(R.id.tiempo_orden);
            buttonCrear = itemView.findViewById(R.id.buttonCrear);
        }
    }
}

