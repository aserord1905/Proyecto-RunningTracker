package com.example.runningtracker;

import android.content.Intent;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.runningtracker.conexion.Conexion;
import com.example.runningtracker.dao.DAO;
import com.example.runningtracker.dao.DAOImpl;
import com.example.runningtracker.model.Usuario;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;

public class RegisterActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private EditText sexoInput, pesoInput, usernameInput, passwordInput, rolInput;
    private Button btn_registro, btn_iniciosesion;
    DAO dao = new DAOImpl();
    private String nombreInstagram = "runningtracker";
    private RadioButton radioUsuario, radioAdmin,radioHombre,radioMujer;
    private String rol = "";
    private String sexo;
    private ImageView instagramImage,facebookImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Asignamos el imageview
        instagramImage = findViewById(R.id.image_instagram);
        facebookImage = findViewById(R.id.image_facebook);
        //Campos de texto.

        pesoInput = findViewById(R.id.input_peso);
        usernameInput = findViewById(R.id.input_username);
        passwordInput = findViewById(R.id.input_password);
        // Referenciar los elementos
        radioUsuario = findViewById(R.id.radio_usuario);
        radioAdmin = findViewById(R.id.radio_admin);

        // Referenciar los elementos sexo
        radioHombre = findViewById(R.id.radio_hombre);
        radioMujer = findViewById(R.id.radio_mujer);

        //Botones
        btn_registro = findViewById(R.id.btn_registro);
        btn_iniciosesion = findViewById(R.id.btn_iniciosesion);

        radioMujer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Establecer el valor de rol como "usuario"
                sexo = "mujer";
            }
        });

        radioHombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Establecer el valor de rol como "usuario"
                sexo = "hombre";
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


        //Boton inicio sesion de la pantalla register te lleva a la pantalla inicio sesion
        btn_iniciosesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });

        //Accion para el botón de registrar un nuevo usuario
        btn_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameInput.getText().toString();
                String peso = pesoInput.getText().toString();
                String password = passwordInput.getText().toString();

                // Validar que se haya seleccionado un rol
                if (rol.isEmpty()) {
                    Snackbar.make(findViewById(R.id.layoutRegister), "Por favor, selecciona un rol", Snackbar.LENGTH_SHORT).show();
                    return;
                }else if(sexo.equals("")){
                    Snackbar.make(findViewById(R.id.layoutRegister), "Por favor, seleccione el sexo", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                //Llamar al metodo para validar el usuario
                if(validarUsuario(username, password, rol, sexo, peso)) {
                    new LoginTask().execute(sexo, peso, username, password, rol);
                }
            }
        });

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
            try {
                //sexo peso username password rol
                sexo = strings[0];
                String peso = strings[1];
                String username = strings[2];
                String password = strings[3];
                String permiso =  strings[4];
                return dao.insertarUsuario(sexo, peso, username, password, permiso);
            } catch (Exception e) {
                // Manejo de la excepción
                e.printStackTrace(); // Imprime el rastro de la excepción en la consola
                return false; // O realiza alguna otra acción según tus necesidades
            }
        }



        @Override
        protected void onPostExecute(Boolean res) {
            if (res) {
                Toast.makeText(RegisterActivity.this, "Usuario registrado exitosamente", Toast.LENGTH_LONG).show();
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
            } else {
                Toast.makeText(RegisterActivity.this, "Error al registrar usuario", Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(res);
        }
    }

    public boolean validarPassword(String password){
        if (password.length() >= 6) {
            boolean tieneMayuscula = false;
            boolean tieneMinuscula = false;
            boolean tieneNumero = false;

            for (int i = 0; i < password.length(); i++) {
                if (Character.isUpperCase(password.charAt(i))) {
                    tieneMayuscula = true;
                } else if (Character.isLowerCase(password.charAt(i))) {
                    tieneMinuscula = true;
                } else if (Character.isDigit(password.charAt(i))) {
                    tieneNumero = true;
                } else {
                    Toast.makeText(this, "La contraseña no contiene una mayúscula, una minúscula y un número", Toast.LENGTH_SHORT).show();
                    return false;
                }

                if (tieneMayuscula && tieneMinuscula && tieneNumero) {
                    // La contraseña cumple con los requisitos mínimos
                    Toast.makeText(this, "Contraseña válida", Toast.LENGTH_SHORT).show();
                    return true;
                }
            }
        }else
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres y contener una mayúscula, una minúscula y un número", Toast.LENGTH_SHORT).show();
        // La contraseña no cumple con los requisitos mínimos

        return false;
    }


    private boolean validarUsuario(String username, String password, String rol, String sexo, String peso) {
        if (username.equals("") || password.equals("") || rol.equals("") || sexo.equals("") || peso.equals("")) {
            Toast.makeText(RegisterActivity.this, "Campos vacios", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!rol.equalsIgnoreCase("Usuario") && !rol.equalsIgnoreCase("Admin")) {
            Snackbar.make(findViewById(R.id.layoutRegister), "El rol debe ser Usuario o Admin", Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (!validarPassword(password)) {
            Snackbar.make(findViewById(R.id.layoutRegister), "La contraseña debe tener al menos 6 caracteres y contener una mayúscula y una minúscula", Snackbar.LENGTH_SHORT).show();
            return false;
        }else if(!sexo.equalsIgnoreCase("mujer") && !sexo.equalsIgnoreCase("hombre")) {
            Snackbar.make(findViewById(R.id.layoutRegister), "El sexo debe ser hombre o mujer", Snackbar.LENGTH_SHORT).show();
        }
        return true;
    }

    }
