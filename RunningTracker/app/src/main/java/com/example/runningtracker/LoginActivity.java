package com.example.runningtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class LoginActivity extends AppCompatActivity {

    private ImageView instagramImage;
    private ImageView facebookImage;
    private Button btn_inicioSesion;
    private String nombreInstagram = "jspeluquero";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Asignamos el imageview
        instagramImage = findViewById(R.id.image_instagram);
        facebookImage = findViewById(R.id.image_facebook);
        btn_inicioSesion = findViewById(R.id.btn_iniciosesion);

        //Cuando se pulse la imagen de instagram debe dirigirte a la pagina oficial
        instagramImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.instagram.com/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setPackage("com.instagram.android");

                // Verificar si la aplicación de Instagram está instalada en el dispositivo
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    // Si la aplicación de Instagram no está instalada, abrir la página de Instagram en el navegador
                    Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/"+nombreInstagram));
                    startActivity(webIntent);
                }
            }
        });


        facebookImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.facebook.com/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setPackage("com.facebook.katana");

                // Verificar si la aplicación de Instagram está instalada en el dispositivo
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    // Si la aplicación de Instagram no está instalada, abrir la página de Instagram en el navegador
                    Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/"));
                    startActivity(webIntent);
                }
            }
        });

        btn_inicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HistorialActivity.class);
                startActivity(intent);
            }
        });


    }
}