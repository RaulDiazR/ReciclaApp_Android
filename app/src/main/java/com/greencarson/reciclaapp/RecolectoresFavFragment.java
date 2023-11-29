package com.greencarson.reciclaapp;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class RecolectoresFavFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recolectoresfav, container, false);

        RecyclerView recolectorRV = rootView.findViewById(R.id.RVRecolector);

        ArrayList<RecolectorFavModel> recolectorFavModelArrayList = new ArrayList<RecolectorFavModel>();
        recolectorFavModelArrayList.add(new RecolectorFavModel("Recolector1", 5, R.drawable.default_recolector_icon));
        recolectorFavModelArrayList.add(new RecolectorFavModel("Recolector2", 4, R.drawable.default_recolector_icon));
        recolectorFavModelArrayList.add(new RecolectorFavModel("Recolector3", 3, R.drawable.default_recolector_icon));
        recolectorFavModelArrayList.add(new RecolectorFavModel("Recolector4", 4, R.drawable.default_recolector_icon));
        recolectorFavModelArrayList.add(new RecolectorFavModel("Recolector5", 5, R.drawable.default_recolector_icon));
        recolectorFavModelArrayList.add(new RecolectorFavModel("Recolector6", 4, R.drawable.default_recolector_icon));
        recolectorFavModelArrayList.add(new RecolectorFavModel("Recolector7", 3, R.drawable.default_recolector_icon));

        // Inicializa el adaptador y pasa la lista al adaptador.
        RecolectorFavAdapter courseAdapter = new RecolectorFavAdapter(getContext(), recolectorFavModelArrayList);

        // Configura el administrador de diseño para la vista de reciclaje.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        // Configura el administrador de diseño y el adaptador para la vista de reciclaje.
        recolectorRV.setLayoutManager(linearLayoutManager);
        recolectorRV.setAdapter(courseAdapter);

        return rootView;
    }
}
