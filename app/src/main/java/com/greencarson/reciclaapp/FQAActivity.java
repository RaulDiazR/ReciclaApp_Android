package com.greencarson.reciclaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FQAActivity extends AppCompatActivity {

    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fqaactivity);

        // Find the Toolbar by its ID
        Toolbar toolbar = findViewById(R.id.toolbar);

        // Set the Toolbar as the app bar
        setSupportActionBar(toolbar);

        // Remove default title for app bar
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Enable the back button (up navigation)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        expandableListView = findViewById(R.id.expandableListView);
        expandableListAdapter = new ExpandableListAdapter();

        expandableListView.setAdapter(expandableListAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private class ExpandableListAdapter extends BaseExpandableListAdapter {
        private List<String> questions;
        private HashMap<String, String> answers;

        public ExpandableListAdapter() {
            questions = new ArrayList<>();
            answers = new HashMap<>();

            // Agregar tus preguntas y respuestas aquí
            questions.add("¿Qué es Puebla Reecicla?");
            answers.put("¿Qué es Puebla Reecicla?", "Puebla Reecicla es una aplicación para la recolección de materiales a la puerta de la casa por personas capacitadas y con la mejor atención y en el tiempo más rápido.");

            questions.add("¿Qué puedo ver en el mapa?");
            answers.put("¿Qué puedo ver en el mapa?", "Puedes ver los centros de recolección asociados con Green Carson, a los cuales puedes acudir a entregar tus desechos. Puedes consultar información esencial de cada centro, como su ubicación y los tipos de materiales que reciben.");

            questions.add("¿Cómo utilizar los filtros del mapa?");
            answers.put("¿Cómo utilizar los filtros del mapa?", "Puedes personalizar tu búsqueda de centros de recolección eligiendo un tipo de centro en específico, haciendo que solo se muestren los centros de ese tipo. Puedes desactivar el filtro en el momento que desees.");

            questions.add("¿Cómo agendo una recolección?");
            answers.put("¿Cómo agendo una recolección?", "En el menú Reciclaje, da click en el botón de + para agendar una nueva recolección, necesitarás seleccionar la fecha, horario y formato de entrega, y tendrás la opción de agregar algún comentario para el recolector. Posteriormente necesitarás introducir tu dirección y ubicación. Finalmente, debes elegir los materiales de tu recolección, de los cuales puedes introducir las cantidades y anexar imágenes. Finalmente, debes confirmar tu orden.");

            questions.add("¿Puedo calificar a mi recolector?");
            answers.put("¿Puedo calificar a mi recolector?", "Si, para ello, la recolección debe estar completada y no se debe haber calificado al recolector en esa recolección.");

            questions.add("¿Puedo cancelar una recolección?");
            answers.put("¿Puedo cancelar una recolección?", "Si, solo se puede cancelar si la recolección está en Iniciada o en Proceso.");

            questions.add("¿Cómo poner una foto de perfil?");
            answers.put("¿Cómo poner una foto de perfil?", "En el menú Ajustes da clic en el botón + ubicado debajo del ícono circular. Puedes subir una foto desde tu cámara o desde la galería de fotos de tu dispositivo. Antes asegúrate de tener activado para esta aplicación los permisos de acceso a la cámara y fotos.");

            questions.add("¿Puedo modificar mi información?");
            answers.put("¿Puedo modificar mi información?", "Si, en la sección Ajustes -> Perfil, puedes consultar la información con la que te registraste y también modificarla si la deseas, únicamente no puedes modificar tu correo y tu contraseña.");
        }

        @Override
        public int getGroupCount() {
            return questions.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return 1; // Siempre hay una respuesta por pregunta
        }

        @Override
        public Object getGroup(int groupPosition) {
            return questions.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            String question = questions.get(groupPosition);
            return answers.get(question);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.listgroup, null);
            }

            TextView questionTextView = convertView.findViewById(R.id.listTitle);
            questionTextView.setText(questions.get(groupPosition));

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            TextView answerTextView;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.listitem, null);
            }

            answerTextView = convertView.findViewById(R.id.listItem);
            String question = questions.get(groupPosition);
            String answer = answers.get(question);

            answerTextView.setText(answer);

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }
}