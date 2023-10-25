package com.example.reciclaapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MaterialesAdapter extends RecyclerView.Adapter<MaterialesAdapter.MaterialesViewHolder> {
    private final Context context;
    private final List<MaterialesItem> itemList; // Replace 'YourItemData' with the actual data type for your items.
    private OnItemRemovedListener itemRemovedListener;
    // Interface for communicating item removal

    public interface OnItemRemovedListener {
        void onItemRemoved(int position);
    }

    public MaterialesAdapter(Context context, List<MaterialesItem> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    public static class MaterialesViewHolder extends RecyclerView.ViewHolder {
        // Define your views here
        public Spinner spinner;
        public ImageButton minusButton;
        public TextView quantity;
        public ImageButton plusButton;
        public LinearLayout materialLayout;
        public ImageView imageView;
        public TextView textView;
        public Button tomarFoto;
        public Button eliminarMaterial;

        public MaterialesViewHolder(View view) {
            super(view);
            // Initialize your views here
            spinner = view.findViewById(R.id.spinner);
            minusButton = view.findViewById(R.id.minusButton);
            quantity = view.findViewById(R.id.materialQuantity);
            plusButton = view.findViewById(R.id.plusButton);
            materialLayout = view.findViewById(R.id.materialLayout);
            imageView = view.findViewById(R.id.image);
            textView = view.findViewById(R.id.text);
            tomarFoto = view.findViewById(R.id.tomarFoto);
            eliminarMaterial = view.findViewById(R.id.eliminarMaterial);
        }
    }

    @NonNull
    @Override
    public MaterialesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.materiales_item, parent, false);
        return new MaterialesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MaterialesViewHolder holder, int position) {
        MaterialesItem item = itemList.get(position);
        holder.imageView.setImageResource(item.getImageResource());
        holder.textView.setText(item.getText());

        holder.minusButton.setOnClickListener(v -> {
            int number = Integer.parseInt(holder.quantity.getText().toString());
            if (number > 1) {
                number --;
                holder.quantity.setText(String.valueOf(number));
            }
            else {
                holder.quantity.setText("1");
            }
        });

        holder.plusButton.setOnClickListener(v -> {
            int number = Integer.parseInt(holder.quantity.getText().toString());
            number ++;
            holder.quantity.setText(String.valueOf(number));

        });

        holder.eliminarMaterial.setOnClickListener(v -> {
            int itemPosition = holder.getBindingAdapterPosition(); // Get the item's position
            if (itemPosition != RecyclerView.NO_POSITION) {
                // Remove the item from the list
                itemList.remove(itemPosition);
                // Notify the adapter that an item has been removed
                notifyItemRemoved(itemPosition);

                if (itemRemovedListener != null) {
                    itemRemovedListener.onItemRemoved(itemPosition);
                }
            }
        });


        holder.tomarFoto.setOnClickListener(v -> {

        });



        // Create an array of items to populate the Spinner
        String[] items = {"Bolsas", "Cajas", "Kilos"};
        // Create a custom adapter to bind the array to the Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.spinner_materiales_item, items) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if (position == holder.spinner.getSelectedItemPosition()) {
                    view.setBackgroundResource(R.drawable.spinner_background_materiales_selected);
                } else {
                    view.setBackgroundResource(R.drawable.spinner_background_materiales);
                }
                return view;
            }
        };
        // Set the adapter for the Spinner
        holder.spinner.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}

