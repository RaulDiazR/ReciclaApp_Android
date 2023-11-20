package com.example.reciclaapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MaterialRVAdapter extends RecyclerView.Adapter<MaterialRVAdapter.ViewHolder> {
    private ArrayList<MaterialModel> MaterialsList;
    private Context context;

    public MaterialRVAdapter(ArrayList<MaterialModel> materialsList, Context context) {
        MaterialsList = materialsList;
        this.context = context;
    }

    @NonNull
    @Override
    public MaterialRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.material_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MaterialRVAdapter.ViewHolder holder, int position) {
        // setting data to our text views from our modal class.
        MaterialModel materialModel = MaterialsList.get(position);
        Picasso.get().load(materialModel.getImageURL()).into(holder.imageItem);
        holder.nameItem.setText(materialModel.getName());
    }

    @Override
    public int getItemCount() {
        // returning the size of our array list.
        return MaterialsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views.
        private final ImageView imageItem;
        private final TextView nameItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
            nameItem = itemView.findViewById(R.id.nameItem);
            imageItem = itemView.findViewById(R.id.imageItem);
        }
    }
}
