package com.example.runningtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class LoginActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private ImageView instagramImage,facebookImage;
    private Button btn_inicioSesion;
    private EditText usernameInput, passwordInput;
    private String nombreInstagram = "jspeluquero";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Asignamos el imageview
        instagramImage = findViewById(R.id.image_instagram);
        facebookImage = findViewById(R.id.image_facebook);


        //Campos de la pantalla de inicio sesion
        usernameInput = findViewById(R.id.input_usuario);
        passwordInput = findViewById(R.id.input_contrasena);

        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();


        //Botones inicio sesion y registro
        btn_inicioSesion = findViewById(R.id.btn_iniciosesion);

        //Boton de inicio sesión
        btn_inicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.equals("") || password.equals("")){
                    Toast.makeText(LoginActivity.this, "Campos vacios", Toast.LENGTH_SHORT).show();
                }
            }
        });


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

        /* Intent ir a inicio seseion
        btn_inicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HistorialActivity.class);
                startActivity(intent);
            }
        });*/


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    private class LoginTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            return null;
        }

        @Override
        protected void onPostExecute(Boolean res) {
            if(res){
                Toast.makeText(LoginActivity.this,"Iniciando la interfaz de usuario", Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(LoginActivity.this,"Error de acceso: Usuario o Contraseña incorrectos", Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(res);
        }

    }
}