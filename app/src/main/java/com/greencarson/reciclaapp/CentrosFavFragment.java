package com.greencarson.reciclaapp;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class CentrosFavFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_centrosfav, container, false);

        RecyclerView centroRV = rootView.findViewById(R.id.RVCentro);

        ArrayList<CentroFavModel> centroFavModelArrayList = new ArrayList<CentroFavModel>();
        centroFavModelArrayList.add(new CentroFavModel("CentroDeRecolección1", "Calle Ficticia 123, Ciudad Imaginaria, Estado de Ejemplo, Código Postal 12345", R.drawable.default_centro_icon));
        centroFavModelArrayList.add(new CentroFavModel("CentroDeRecolección2", "Avenida de la Fantasía 456, Pueblo de la Ilusión, Provincia de Ejemplolandia, Código Postal 98765", R.drawable.default_centro_icon));
        centroFavModelArrayList.add(new CentroFavModel("CentroDeRecolección3", "Carretera Imaginaria 789, Villa de la Imaginación, Región de Sueñolandia, Código Postal 54321", R.drawable.default_centro_icon));
        centroFavModelArrayList.add(new CentroFavModel("CentroDeRecolección4", "Camino de los Sueños 101, Ciudad de la Creatividad, País de Ejemplo, Código Postal 67890.", R.drawable.default_centro_icon));

        // Inicializa el adaptador y pasa la lista al adaptador.
        CentroFavAdapter courseAdapter = new CentroFavAdapter(getContext(), centroFavModelArrayList);

        // Configura el administrador de diseño para la vista de reciclaje.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        // Configura el administrador de diseño y el adaptador para la vista de reciclaje.
        centroRV.setLayoutManager(linearLayoutManager);
        centroRV.setAdapter(courseAdapter);

        return rootView;
    }
}
