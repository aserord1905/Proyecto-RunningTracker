package com.example.runningtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.runningtracker.conexion.Conexion;
import com.google.android.material.navigation.NavigationView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private ImageView instagramImage,facebookImage;
    private Button btn_inicioSesion, btnRegistro;
    private EditText usernameInput, passwordInput, inputRol;
    private String nombreInstagram = "";
    boolean isValid=false;
    private RadioButton radioUsuario, radioAdmin;
    private String rol = "";
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

         // Referenciar los elementos
        radioUsuario = findViewById(R.id.radio_usuario);
        radioAdmin = findViewById(R.id.radio_admin);
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

        radioUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Establecer el valor de rol como "usuario"
                rol = "usuario";
            }
        });

        radioAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Establecer el valor de rol como "admin"
                rol = "admin";
            }
        });
        //Boton de inicio sesión
        btn_inicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameInput.getText().toString();
                String password = passwordInput.getText().toString();

                if (username.equals("") || password.equals("") || rol.equals("")){
                    Toast.makeText(LoginActivity.this, "Campos vacios", Toast.LENGTH_SHORT).show();
                }else
                    new LoginTask().execute(username, password, rol);
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

    public class LoginTask extends AsyncTask<String, Void, Boolean> {
        private String userID = null;

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

                    // Verificar si el usuario y la contraseña coinciden
                    if (usuario.equals(usu) && password.equals(cont)) {
                        // Obtener el id de la tabla usuario_rol según el id_usuario
                        int idUsuario = Integer.parseInt(id);
                        int idRol = getRolFromUsuarioRol(connection, idUsuario);

                        // Verificar si el idRol coincide con el rol buscado
                        if (idRol > 0) {
                            // Verifica si es usuario o administrador
                            if (permiso.equalsIgnoreCase("Usuario") && idRol == 1) {
                                isValid = true;
                                // Utilización del SharedPreferences para poder obtener el id del usuario y no perderlo al pasar entre páginas
                                // Esto crea o recupera un archivo de preferencias llamado "MyPrefs".
                                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                                // Este objeto se utiliza para realizar modificaciones en las preferencias almacenadas.
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                // Se almacena el id del usuario
                                editor.putString("id_usuario", id);
                                editor.apply();
                                Intent i = new Intent(LoginActivity.this, MapsActivity.class);
                                startActivity(i);
                            } else if (permiso.equalsIgnoreCase("Admin") && idRol == 2) {
                                isValid = true;
                                Intent i = new Intent(LoginActivity.this, AdminActivity.class);
                                startActivity(i);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return isValid;
        }

        private int getRolFromUsuarioRol(Connection connection, int idUsuario) throws SQLException {
            String consulta = "SELECT id_rol FROM usuariorol WHERE id_usuario = ?";
            PreparedStatement statement = connection.prepareStatement(consulta);
            statement.setInt(1, idUsuario);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id_rol");
            }
            return -1; // Valor negativo para indicar que no se encontró el rol
        }

        public String getUserID() {
            return userID;
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
