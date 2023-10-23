package com.example.reciclaapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistorialAdapter extends RecyclerView.Adapter<HistorialAdapter.MyViewHolder> {

    private final List<HistorialItem> itemList;
    public HistorialItemClickListener clickListener;

    public void setClickListener(HistorialItemClickListener myListener){
        this.clickListener = myListener;
    }

    public HistorialAdapter(List<HistorialItem> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // creates new view holders for you items

        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.historial_item, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // binds the data from your dataset to the views within the view holder

        HistorialItem item = itemList.get(position);

        holder.fecha.setText(item.getFecha());
        holder.horario.setText(item.getHorario());
        holder.materiales.setText(item.getMateriales());
        holder.estadoTexto.setText(item.getEstado());
        holder.estadoTexto.setTextColor(item.getEstadoColor());
        holder.estadoColor.setBackground(item.getBackgroundDrawable());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Holds references to the views within the item layout

        View estadoColor;

        TextView fecha;
        TextView horario;
        TextView materiales;
        TextView estadoTexto;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            estadoColor = itemView.findViewById(R.id.estado_orden_color);
            fecha = itemView.findViewById(R.id.fecha_orden);
            horario = itemView.findViewById(R.id.tiempo_orden);
            materiales = itemView.findViewById(R.id.materiales_orden);
            estadoTexto = itemView.findViewById(R.id.estado_orden_texto);

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
