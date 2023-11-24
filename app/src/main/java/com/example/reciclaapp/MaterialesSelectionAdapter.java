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
/**
 * Adaptador para la vista de selección de materiales.
 */
public class MaterialesSelectionAdapter extends RecyclerView.Adapter<MaterialesSelectionAdapter.MaterialViewHolder> {
    private final Context context;
    private final List<MaterialesSelectionItem> data; // You'll need to create a class for MaterialItem

    public MaterialesSelectionClickListener clickListener;

    /**
     * Establece el listener para los clics en los elementos de la lista.
     */
    public void setClickListener(MaterialesSelectionClickListener myListener){
        this.clickListener = myListener;
    }

    /**
     * Constructor de la clase.
     */
    public MaterialesSelectionAdapter(Context context, List<MaterialesSelectionItem> data) {
        this.context = context;
        this.data = data;
    }

    /**
     * Crea y devuelve un nuevo MaterialViewHolder.
     */
    @NonNull
    @Override
    public MaterialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.materiales_selection_item, parent, false);
        return new MaterialViewHolder(itemView);
    }

    /**
     * Vincula los datos del elemento a los elementos de la vista.
     */
    @Override
    public void onBindViewHolder(@NonNull MaterialViewHolder holder, int position) {
        MaterialesSelectionItem item = data.get(position);

        // Bind data to your view elements here
        holder.imageView.setImageResource(item.getImageResource());
        holder.textView.setText(item.getText());
    }

    /**
     * Devuelve la cantidad de elementos en la lista de datos.
     */
    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * Clase interna que representa la vista de cada elemento en la lista.
     */
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

