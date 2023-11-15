package com.example.reciclaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
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
            questions.add("¿Qué es Android?");
            answers.put("¿Qué es Android?", "Android es un sistema operativo para dispositivos móviles desarrollado por Google.");

            questions.add("¿Cómo se crea una actividad en Android?");
            answers.put("¿Cómo se crea una actividad en Android?", "Puedes crear una actividad extendiendo la clase AppCompatActivity y configurando su diseño en XML.");

            questions.add("¿Cómo puedo cambiar mi contraseña?");
            answers.put("¿Cómo puedo cambiar mi contraseña?", "Para cambiar tu contraseña, inicia sesión en tu cuenta, ve a la sección de configuración de la cuenta y busca la opción de cambiar contraseña.");

            questions.add("¿Qué dispositivos son compatibles con nuestra aplicación?");
            answers.put("¿Qué dispositivos son compatibles con nuestra aplicación?", "Nuestra aplicación es compatible con dispositivos Android con una versión de sistema operativo 4.4 (KitKat) o superior.");

            questions.add("¿Puedo usar la aplicación sin conexión a internet?");
            answers.put("¿Puedo usar la aplicación sin conexión a internet?", "Sí, algunas funciones de la aplicación se pueden utilizar sin conexión, pero ciertas características requerirán una conexión a internet.");

            questions.add("¿Cómo contacto al soporte técnico?");
            answers.put("¿Cómo contacto al soporte técnico?", "Puedes ponerte en contacto con nuestro equipo de soporte técnico a través del correo electrónico de soporte@example.com o llamando al número de soporte: +123456789.");

            questions.add("¿Cómo puedo actualizar la aplicación?");
            answers.put("¿Cómo puedo actualizar la aplicación?", "Para actualizar la aplicación, ve a la tienda de aplicaciones de tu dispositivo (Google Play Store) y busca nuestra aplicación. Allí encontrarás la opción de actualizar.");

            questions.add("¿Cuál es la política de devoluciones?");
            answers.put("¿Cuál es la política de devoluciones?", "Nuestra política de devoluciones permite devoluciones dentro de los primeros 30 días desde la compra. Por favor, revisa nuestra página de políticas para obtener más detalles.");

            questions.add("¿Cómo puedo restablecer mi PIN de acceso?");
            answers.put("¿Cómo puedo restablecer mi PIN de acceso?", "Para restablecer tu PIN de acceso, ve a la sección de seguridad de tu cuenta y selecciona la opción para restablecer el PIN.");

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