package com.example.reciclaapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class RecolectorFavAdapter extends RecyclerView.Adapter<RecolectorFavAdapter.Viewholder> {

    private final Context context;
    private final ArrayList<RecolectorFavModel> recolectorFavModelArrayList;

    // Constructor
    public RecolectorFavAdapter(Context context, ArrayList<RecolectorFavModel> recolectorFavModelArrayList) {
        this.context = context;
        this.recolectorFavModelArrayList = recolectorFavModelArrayList;
    }

    @NonNull
    @Override
    public RecolectorFavAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recolectorcard_layout, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecolectorFavAdapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        RecolectorFavModel model = recolectorFavModelArrayList.get(position);
        holder.recolectorNameTV.setText(model.getRecolector_name());
        holder.recolectorRatingTV.setText("" + model.getRecolector_rating());
        holder.recolectorIV.setImageResource(model.getRecolector_image());
    }

    @Override
    public int getItemCount() {
        // this method is used for showing number of card items in recycler view
        return recolectorFavModelArrayList.size();
    }

    // View holder class for initializing of your views such as TextView and Imageview
    public static class Viewholder extends RecyclerView.ViewHolder {
        private final ImageView recolectorIV;
        private final TextView recolectorNameTV;
        private final TextView recolectorRatingTV;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            recolectorIV = itemView.findViewById(R.id.recolectorImage);
            recolectorNameTV = itemView.findViewById(R.id.recolectorName);
            recolectorRatingTV = itemView.findViewById(R.id.recolectorRating);
        }
    }
}