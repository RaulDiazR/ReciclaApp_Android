package com.example.reciclaapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.FragmentTransaction;

public class FavoritosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);

        // Encuentra la Toolbar por su ID
        Toolbar toolbar = findViewById(R.id.toolbar);

        // Configura la Toolbar como la barra de la aplicación
        setSupportActionBar(toolbar);

        // Elimina el título predeterminado de la barra de la aplicación
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Habilita el botón de retroceso (navegación hacia arriba)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Encuentra el TabLayout por su ID
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        // Configura las pestañas
        TabLayout.Tab recolectoresTab = tabLayout.getTabAt(0); // Tab "Recolectores"
        TabLayout.Tab centrosTab = tabLayout.getTabAt(1); // Tab "Centros"

        // Selecciona la pestaña "Recolectores" al inicio
        if (recolectoresTab != null) {
            recolectoresTab.select();
            // Inicialmente, carga el fragmento de "Recolectores" en el FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, new RecolectoresFavFragment())
                    .commit();
        }

        // Manejo de selección de pestañas
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                if (tab == recolectoresTab) {
                    transaction.replace(R.id.frameLayout, new RecolectoresFavFragment());
                } else if (tab == centrosTab) {
                    transaction.replace(R.id.frameLayout, new CentrosFavFragment());
                }

                transaction.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
