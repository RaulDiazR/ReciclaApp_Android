package com.example.reciclaapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class VerDetallesAdapter extends RecyclerView.Adapter<VerDetallesAdapter.ViewHolder> {
    private final List<VerDetallesItem> items;
    private final Context context;
    private final FrameLayout rootView;

    public VerDetallesAdapter(List<VerDetallesItem> items, Context context, FrameLayout rootView) {
        this.items = items;
        this.context = context;
        this.rootView = rootView;
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

        String titleBtn;
        Resources res = context.getResources();
        Drawable imgResource;
        if (item.getFotoUrl().equals("")) {
            titleBtn = "Sin Foto";
            imgResource = ResourcesCompat.getDrawable(res, R.drawable.button_variant_green_light, null);
            holder.verFotoBtn.setEnabled(false);
        }
        else {
            titleBtn = "Ver Foto";
            imgResource = ResourcesCompat.getDrawable(res, R.drawable.button_variant_green, null);
            holder.verFotoBtn.setEnabled(true);
        }
        holder.verFotoBtn.setBackground(imgResource);
        holder.verFotoBtn.setText(titleBtn);

        holder.verFotoBtn.setOnClickListener(view -> {
            if (!item.getFotoUrl().equals("")) {
                // Create a view for the semitransparent background
                final View backgroundView = new View(context);
                backgroundView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
                backgroundView.setBackgroundColor(Color.argb(150, 0, 0, 0)); // Semitransparent color

                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context, R.style.CustomAlertDialogTheme); // Apply the custom theme

                // Inflate the custom layout
                LayoutInflater inflater = LayoutInflater.from(context);
                View dialogView = inflater.inflate(R.layout.ver_detalles_item_ver_foto, null);

                // Configure the dialog
                builder.setView(dialogView);

                // Customize the dialog
                final AlertDialog alertDialog = builder.create();

                // Configure a semitransparent background
                Window window = alertDialog.getWindow();
                if (window != null) {
                    WindowManager.LayoutParams params = window.getAttributes();
                    params.gravity = Gravity.CENTER;
                    window.setAttributes(params);
                }

                alertDialog.show();

                // set image material
                ImageView imgFotoMaterial = dialogView.findViewById(R.id.fotoMaterial);
                String url = item.getFotoUrl();
                Picasso.get().load(url).placeholder(R.drawable.icon_loading).error(R.drawable.icon_loading).into(imgFotoMaterial);

                // Configure button actions
                Button btnContinuar = dialogView.findViewById(R.id.continuarBtn);

                btnContinuar.setOnClickListener(view1 -> {
                    alertDialog.dismiss();
                    this.rootView.removeView(backgroundView); // Remove the background
                });

                alertDialog.setOnDismissListener(v -> {
                    alertDialog.dismiss();
                    this.rootView.removeView(backgroundView); // Remove the background
                });

                // Add the background view and show the dialog
                this.rootView.addView(backgroundView);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
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

