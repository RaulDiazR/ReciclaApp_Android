package com.greencarson.reciclaapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/**
 * Actividad para la selección de materiales.
 * Implementa la interfaz MaterialesSelectionClickListener para manejar los clics en los elementos.
 */
public class MaterialesSelectionActivity extends AppCompatActivity implements MaterialesSelectionClickListener {
    // 1- AdapterView
    RecyclerView recyclerView;

    // 2- DataSource
    List<MaterialesSelectionItem> itemList;

    // 3- Adapter
    MaterialesSelectionAdapter selectionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materiales_selection);
        // Find the Toolbar by its ID and set the Toolbar as the app bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Remove default title for the app bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        // Enable the back button (up navigation)
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerViewInPopup);

        // Define arrays for image resource IDs and corresponding text
        int[] imageResources = {
                R.drawable.material_aceite_auto, R.drawable.material_aceite_usado, R.drawable.material_arbol,R.drawable.material_baterias, R.drawable.material_bici, R.drawable.material_botellas, R.drawable.material_carton, R.drawable.material_electronicos, R.drawable.material_escombro, R.drawable.material_industriales, R.drawable.material_juguetes, R.drawable.material_metal, R.drawable.material_metal, R.drawable.material_libros, R.drawable.material_llantas, R.drawable.material_madera, R.drawable.material_medicina, R.drawable.material_metal, R.drawable.material_organico, R.drawable.material_pallets, R.drawable.material_papel, R.drawable.material_pilas, R.drawable.material_plasticos, R.drawable.material_ropa, R.drawable.material_tapitas, R.drawable.material_tetrapack, R.drawable.material_toner, R.drawable.material_voluminoso
        };

        String[] textArray = {
                "Aceite Auto","Aceite Usado", "Árbol","Baterias","Bicicletas","Botellas", "Cartón","Electrónicos","Escombros","Industriales","Juguetes","Lata Chilera","Lata","Libros","Llantas","Madera","Medicina", "Metal","Orgánico", "Pallets","Papel","Pilas","Plásticos","Ropa","Tapitas","Tetrapack","Toner","Voluminoso"
        };

        // 2- DataSource
        itemList = new ArrayList<>();

        // Use a for loop to populate the dataList
        for (int i = 0; i < imageResources.length; i++) {
            MaterialesSelectionItem item = new MaterialesSelectionItem(imageResources[i], textArray[i]);
            itemList.add(item);
        }


        // create and set the linear layout manager so the recycler view works properly like a scrolling view
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        selectionAdapter = new MaterialesSelectionAdapter(this, itemList);
        recyclerView.setAdapter(selectionAdapter);

        selectionAdapter.setClickListener(this);
    }

    // Maneja el evento de click en el botón de navegación hacia atrás.
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    // Maneja el evento de click en un elemento de la lista de materiales seleccionables.
    @Override
    public void onClick(View v, int position) {
        Intent intent = new Intent(this, MaterialesActivity.class);
        intent.putExtra("imageResource", itemList.get(position).getImageResource());
        intent.putExtra("text", itemList.get(position).getText());
        setResult(RESULT_OK, intent);
        finish();
    }
}