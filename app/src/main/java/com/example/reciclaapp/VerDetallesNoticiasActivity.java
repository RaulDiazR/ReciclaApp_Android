package com.example.reciclaapp;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ImageSpan;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.squareup.picasso.Picasso;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class VerDetallesNoticiasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_detalles_noticias);

        // Busque la barra de herramientas por su ID y configúrela como barra de aplicaciones
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Eliminar el título predeterminado de la barra de aplicaciones
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        // Habilitar el botón Atrás (navegación hacia arriba)
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        // Retrieve data from intent extras
        Intent intent = getIntent();
        String tituloTxt = intent.getStringExtra("titulo");
        String cuerpoTxt = intent.getStringExtra("cuerpo");
        String autorTxt = intent.getStringExtra("autor");
        String fotoUrl = intent.getStringExtra("fotoUrl");

        ImageView imgNoticia = findViewById(R.id.imagen);
        TextView titulo = findViewById(R.id.titulo);
        TextView cuerpo = findViewById(R.id.cuerpo);
        TextView autor = findViewById(R.id.autor);

        Picasso.get().load(fotoUrl).placeholder(R.drawable.icon_loading).error(R.drawable.socio1).into(imgNoticia);
        titulo.setText(tituloTxt);
        cuerpo.setText(cuerpoTxt);
        autor.setText(autorTxt);


        justify(cuerpo);
    }

    public static void justify(final TextView textView) {

        final AtomicBoolean isJustify = new AtomicBoolean(false);

        final String textString = textView.getText().toString();

        final TextPaint textPaint = textView.getPaint();

        final SpannableStringBuilder builder = new SpannableStringBuilder();

        textView.post(() -> {

            if (!isJustify.get()) {

                final int lineCount = textView.getLineCount();
                final int textViewWidth = textView.getWidth();

                for (int i = 0; i < lineCount; i++) {

                    int lineStart = textView.getLayout().getLineStart(i);
                    int lineEnd = textView.getLayout().getLineEnd(i);

                    String lineString = textString.substring(lineStart, lineEnd);

                    if (i == lineCount - 1) {
                        builder.append(new SpannableString(lineString));
                        break;
                    }

                    String trimSpaceText = lineString.trim();
                    String removeSpaceText = lineString.replaceAll(" ", "");

                    float removeSpaceWidth = textPaint.measureText(removeSpaceText);
                    float spaceCount = trimSpaceText.length() - removeSpaceText.length();

                    float eachSpaceWidth = (textViewWidth - removeSpaceWidth) / spaceCount;

                    SpannableString spannableString = new SpannableString(lineString);
                    for (int j = 0; j < trimSpaceText.length(); j++) {
                        char c = trimSpaceText.charAt(j);
                        if (c == ' ') {
                            Drawable drawable = new ColorDrawable(0x00ffffff);
                            drawable.setBounds(0, 0, (int) eachSpaceWidth, 0);
                            ImageSpan span = new ImageSpan(drawable);
                            spannableString.setSpan(span, j, j + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    }

                    builder.append(spannableString);
                }

                textView.setText(builder);
                isJustify.set(true);
            }
        });
    }

    // Método llamado cuando se presiona el botón de navegación hacia arriba
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}