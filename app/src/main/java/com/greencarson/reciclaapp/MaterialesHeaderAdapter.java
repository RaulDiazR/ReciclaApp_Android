package com.greencarson.reciclaapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
/**
 * Adaptador para el encabezado de la RecyclerView en la actividad MaterialesActivity.
 * Muestra información general y proporciona un botón para crear nuevos materiales.
 */
public class MaterialesHeaderAdapter extends RecyclerView.Adapter<MaterialesHeaderAdapter.HeaderViewHolder> {
    private final Context context;
    private final MaterialesHeaderItem headerData;
    private final ActivityResultLauncher<Intent> MaterialesActivityResultLauncher;

    public MaterialesHeaderAdapter(Context context, MaterialesHeaderItem headerData, ActivityResultLauncher<Intent> materialesActivityResultLauncher) {
        this.context = context;
        this.headerData = headerData;
        MaterialesActivityResultLauncher = materialesActivityResultLauncher;
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
            MaterialesActivityResultLauncher.launch(intent);
            //((Activity)(context)).finish();
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

