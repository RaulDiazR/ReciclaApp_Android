package com.greencarson.reciclaapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class CentroFavAdapter extends RecyclerView.Adapter<CentroFavAdapter.Viewholder> {

    private final Context context;
    private final ArrayList<CentroFavModel> centroFavModelArrayList;

    // Constructor
    public CentroFavAdapter(Context context, ArrayList<CentroFavModel> centroFavModelArrayList) {
        this.context = context;
        this.centroFavModelArrayList = centroFavModelArrayList;
    }

    @NonNull
    @Override
    public CentroFavAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.centrocard_layout, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CentroFavAdapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        CentroFavModel model = centroFavModelArrayList.get(position);
        holder.centroNameTV.setText(model.getCentro_name());
        holder.centroAddressTV.setText(model.getCentro_address());
        holder.centroIV.setImageResource(model.getCentro_image());
    }

    @Override
    public int getItemCount() {
        // this method is used for showing number of card items in recycler view
        return centroFavModelArrayList.size();
    }

    // View holder class for initializing of your views such as TextView and Imageview
    public static class Viewholder extends RecyclerView.ViewHolder {
        private final ImageView centroIV;
        private final TextView centroNameTV;
        private final TextView centroAddressTV;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            centroIV = itemView.findViewById(R.id.centroImage);
            centroNameTV = itemView.findViewById(R.id.centroName);
            centroAddressTV = itemView.findViewById(R.id.centroAddress);
        }
    }
}