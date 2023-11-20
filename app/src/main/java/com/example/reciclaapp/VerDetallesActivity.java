package com.example.reciclaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.reciclaapp.models.McqMaterial;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Actividad que muestra los detalles de una orden de reciclaje, incluyendo la fecha, horario,
 * tipo de entrega y los materiales seleccionados.
 */
public class VerDetallesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_detalles);
        // Find the Toolbar by its ID and set the Toolbar as the app bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Remove default title for the app bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        // Enable the back button (up navigation)
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // ajustar la fecha y horario
        TextView fecha = findViewById(R.id.fecha_orden);
        fecha.setText(getIntent().getStringExtra("fecha"));
        TextView tiempo = findViewById(R.id.tiempo_orden);
        tiempo.setText(getIntent().getStringExtra("horario"));

        // ajustar el mode de entrega y su imagen
        TextView tipoEntregaTxt = findViewById(R.id.tipoEntregaTxt);
        ImageView tipoEntregaImg = findViewById(R.id.tipoEntregaImg);
        if (getIntent().getBooleanExtra("enPersona", true)) {
            String mensaje = "Entrega en Persona";
            tipoEntregaTxt.setText(mensaje);
            tipoEntregaImg.setImageResource(R.drawable.icon_user_blue);
        } else {
            String mensaje = "Entrega en Puerta";
            tipoEntregaTxt.setText(mensaje);
            tipoEntregaImg.setImageResource(R.drawable.icon_home_blue);
        }


        // Inside your activity
        RecyclerView recyclerView = findViewById(R.id.recyclerMaterialesEscogidos);
        List<VerDetallesItem> itemList = new ArrayList<>();

        // Retrieve the JSON string from the intent
        String materialesListJson = getIntent().getStringExtra("data");

        // Check if the JSON string is not null
        if (materialesListJson != null) {
            // Use Gson to parse the JSON string into a list of McqMaterial objects
            Type listType = new TypeToken<List<McqMaterial>>() {
            }.getType();
            List<McqMaterial> materialesList = new Gson().fromJson(materialesListJson, listType);

            // Now you have the materialesList as a list of McqMaterial objects
            // You can iterate over the list and create VerDetallesItem objects
            for (McqMaterial material : materialesList) {
                // Create VerDetallesItem objects based on McqMaterial data and add them to itemList
                itemList.add(new VerDetallesItem(
                        getMaterialIcon(material.getNombre()),
                        material.getNombre(),
                        "Unidad: "+ material.getUnidad(),
                        "Cantidad: " + material.getCantidad(),
                        material.getFotoUrl()));
            }

            FrameLayout rootView = findViewById(android.R.id.content);
            VerDetallesAdapter adapter = new VerDetallesAdapter(itemList, this, rootView);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }
    // Obtener el icono del material basado en su nombre
    private int getMaterialIcon(String materialName) {
        // Define arrays for image resource IDs and corresponding text
        int[] imageResources = {
                R.drawable.material_aceite_auto, R.drawable.material_aceite_usado, R.drawable.material_arbol,R.drawable.material_baterias, R.drawable.material_bici, R.drawable.material_botellas, R.drawable.material_carton, R.drawable.material_electronicos, R.drawable.material_escombro, R.drawable.material_industriales, R.drawable.material_juguetes, R.drawable.material_libros, R.drawable.material_llantas, R.drawable.material_madera, R.drawable.material_medicina, R.drawable.material_metal, R.drawable.material_organico, R.drawable.material_pallets, R.drawable.material_papel, R.drawable.material_pilas, R.drawable.material_plasticos, R.drawable.material_ropa, R.drawable.material_tapitas, R.drawable.material_tetrapack, R.drawable.material_toner, R.drawable.material_voluminoso
        };

        String[] textArray = {
                "Aceite de Auto","Aceite Usado","Árbol","Baterías","Bicicletas","Botellas","Cartón","Electrónicos","Escombros","Industriales","Juguetes","Libros","Llantas","Madera","Medicinas","Metal","Orgánico","Pallets","Papel","Pilas","Plásticos","Ropa","Tapitas","Tetra Pack","Toner","Voluminoso"
        };

        for (int i = 0; i < textArray.length; i++) {
            if (textArray[i].equals(materialName)) {
                return imageResources[i];
            }
        }
        return -1; // Return -1 if the name is not found
    }
    // Manejar la acción de navegación hacia arriba
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}