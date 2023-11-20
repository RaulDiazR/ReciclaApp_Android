package com.example.reciclaapp;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
/**
 * Adaptador personalizado para la RecyclerView en la actividad MaterialesActivity.
 * Permite la visualización y manipulación de elementos de materiales.
 */
public class MaterialesAdapter extends RecyclerView.Adapter<MaterialesAdapter.MaterialesViewHolder> {
    private final Context context;
    private final List<MaterialesItem> itemList;
    private final OnItemRemovedListener itemRemovedListener;
    private final ActivityResultLauncher<Intent> takePhotoLauncher;
    private final ActivityResultLauncher<Intent> openGallery;
    private static final int CAMERA_PERMISSION_REQUEST = 1001;
    private static final int PICK_FROM_GALLERY = 1;
    private final FrameLayout rootView;

    public interface OnItemRemovedListener {
        void onItemRemoved(int position);
    }

    public MaterialesAdapter(Context context, List<MaterialesItem> itemList, ActivityResultLauncher<Intent> takePhotoLauncher, ActivityResultLauncher<Intent> openGallery, FrameLayout rootView) {
        this.context = context;
        this.itemList = itemList;
        this.itemRemovedListener = position -> {};
        this.takePhotoLauncher = takePhotoLauncher;
        this.openGallery = openGallery;
        this.rootView = rootView;
    }


    public static class MaterialesViewHolder extends RecyclerView.ViewHolder {
        public Spinner spinner;
        public ImageButton minusButton;
        public TextView quantity;
        public ImageButton plusButton;
        public LinearLayout materialLayout;
        public ImageView imageView;
        public TextView textView;
        public Button tomarFoto;
        public Button eliminarMaterial;
        public boolean isPhotoTaken;

        public MaterialesViewHolder(View view) {
            super(view);
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
        holder.textView.setText(item.getName());
        item.setMaterialQuantity(1);
        if (item.getFotoMaterial() == null) {
            holder.tomarFoto.setText(R.string.tomar_foto);
        } else {
            holder.tomarFoto.setText(R.string.ver_foto);
        }

        holder.minusButton.setOnClickListener(v -> {
            int number = Integer.parseInt(holder.quantity.getText().toString());
            if (number > 1) {
                number--;
                holder.quantity.setText(String.valueOf(number));
            } else {
                holder.quantity.setText("1");
            }
            item.setMaterialQuantity(number);
        });

        holder.plusButton.setOnClickListener(v -> {
            int number = Integer.parseInt(holder.quantity.getText().toString());
            number++;
            holder.quantity.setText(String.valueOf(number));
            item.setMaterialQuantity(number);
        });

        holder.eliminarMaterial.setOnClickListener(v -> {
            int itemPosition = holder.getBindingAdapterPosition();
            if (itemPosition != RecyclerView.NO_POSITION) {
                itemList.remove(itemPosition);
                notifyItemRemoved(itemPosition);
                itemRemovedListener.onItemRemoved(itemPosition);
            }
        });

        holder.tomarFoto.setOnClickListener(view -> {

            if (item.getFotoMaterial() != null) {
                holder.tomarFoto.setText(R.string.tomar_foto);
            } else {
                holder.tomarFoto.setText(R.string.ver_foto);
            }
            // Create a view for the semitransparent background
            final View backgroundView = new View(context);
            backgroundView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            backgroundView.setBackgroundColor(Color.argb(150, 0, 0, 0)); // Semitransparent color

            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context, R.style.CustomAlertDialogTheme); // Apply the custom theme

            // Inflate the custom layout
            LayoutInflater inflater = LayoutInflater.from(context);
            View dialogView = inflater.inflate( R.layout.materiales_popup_fotos, null );

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

            ImageView imgFotoMaterial = dialogView.findViewById(R.id.fotoMaterial);

            if (item.getFotoMaterial() == null) {
                holder.tomarFoto.setText(R.string.tomar_foto);
            } else {
                holder.tomarFoto.setText(R.string.ver_foto);
                imgFotoMaterial.setImageURI(item.getFotoMaterial());
                imgFotoMaterial.setVisibility(View.VISIBLE);
            }

            // Configure button actions
            Button btnVerGaleria = dialogView.findViewById(R.id.verGaleria);
            Button btnTomarFoto = dialogView.findViewById(R.id.tomarFoto);

            btnVerGaleria.setOnClickListener(v -> {
                // Crea un intent para abrir la galería
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                MaterialesActivity.lastItemPosPhotoTaken = holder.getBindingAdapterPosition();
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    openGallery.launch(openGalleryIntent);
                }
                else {
                    // If the permission is not granted, request it from the user
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
                }

                alertDialog.dismiss();
                this.rootView.removeView(backgroundView); // Remove the background
            });

            btnTomarFoto.setOnClickListener(v -> {
                // Crea un intent para capturar una foto
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                MaterialesActivity.lastItemPosPhotoTaken = holder.getBindingAdapterPosition();
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    // Ensure that there's a camera activity to handle the intent
                    if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
                        // Create the File where the photo should go
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        }
                        catch (IOException ex) {
                            // Error occurred while creating the File
                            ex.printStackTrace();
                        }

                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            Uri photoURI = FileProvider.getUriForFile(context,
                                    "com.example.reciclaapp.fileprovider",
                                    photoFile);
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            takePhotoLauncher.launch(takePictureIntent);
                        }
                    }
                }
                else {
                    // If the permission is not granted, request it from the user
                    ActivityCompat.requestPermissions((Activity) context, new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
                }

                alertDialog.dismiss();
                this.rootView.removeView(backgroundView); // Remove the background
            });

            alertDialog.setOnDismissListener(v -> {
                alertDialog.dismiss();
                this.rootView.removeView(backgroundView); // Remove the background
            });

            // Add the background view and show the dialog
            this.rootView.addView(backgroundView);

        });


        // Create an array of items to populate the Spinner
        String[] items = {"Bolsas", "Bote", "Cajas", "Kilos"};
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
                item.setMaterialUnit(String.valueOf(holder.spinner.getSelectedItem()));
                return view;
            }
        };
        // Set the adapter for the Spinner
        holder.spinner.setAdapter(adapter);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        MaterialesActivity.currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}


