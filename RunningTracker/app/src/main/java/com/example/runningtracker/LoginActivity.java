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

import com.example.runningtracker.conexion.Conexion;
import com.google.android.material.navigation.NavigationView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private ImageView instagramImage,facebookImage;
    private Button btn_inicioSesion, btnRegistro;
    private EditText usernameInput, passwordInput, inputRol;
    private String nombreInstagram = "";
    boolean isValid=false;

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
        inputRol = findViewById(R.id.input_rol);

        //Botones inicio sesion y registro
        btn_inicioSesion = findViewById(R.id.btn_iniciosesion);
        btnRegistro = findViewById(R.id.btn_registro);

        //Boton registro de usuario
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });


        //Boton de inicio sesión
        btn_inicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameInput.getText().toString();
                String password = passwordInput.getText().toString();
                String permiso = inputRol.getText().toString();
                if (username.equals("") || password.equals("") || permiso.equals("")){
                    Toast.makeText(LoginActivity.this, "Campos vacios", Toast.LENGTH_SHORT).show();
                }else
                    new LoginTask().execute(username, password, permiso);
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



    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    private class LoginTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            String username = strings[0];
            String password = strings[1];
            String permiso = strings[2];
            return checkValue(username, password, permiso);

        }


        public boolean checkValue(String usu, String cont, String permiso) {
            String consulta = "SELECT * FROM usuarios";

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = Conexion.getConnection();
                PreparedStatement statement = connection.prepareStatement(consulta);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String id = resultSet.getString("id_usuario");
                    String usuario = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    String rol = resultSet.getString("tipo");
                    if (usuario.equals(usu) && password.equals(cont) && rol.equals(permiso)) {
                        //Toast.makeText(LoginActivity.this, "Usuario correcto", Toast.LENGTH_SHORT).show();

                        //Verifica si es usuario o administrador
                        if (permiso.equalsIgnoreCase("Usuario")){
                            isValid = true;
                            Intent i = new Intent(LoginActivity.this, MapsActivity.class);
                            startActivity(i);
                        }else if(permiso.equalsIgnoreCase("Admin")){
                            isValid = true;
                            Intent i = new Intent(LoginActivity.this, AdminActivity.class);
                            startActivity(i);
                        }


                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return isValid;
        }


        @Override
        protected void onPostExecute(Boolean res) {
            if(res){
                Toast.makeText(LoginActivity.this,"Iniciando la interfaz de mapas", Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(LoginActivity.this,"Error de acceso: Usuario o Contraseña incorrectos", Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(res);
        }
    }



}
