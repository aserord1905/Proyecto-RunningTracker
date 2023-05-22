package com.example.runningtracker;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    boolean valueReturn=false;
    boolean isValid=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Campos de texto.
        sexoInput = findViewById(R.id.input_sexo);
        pesoInput = findViewById(R.id.input_peso);
        usernameInput = findViewById(R.id.input_username);
        passwordInput = findViewById(R.id.input_password);
        rolInput = findViewById(R.id.input_rol);

        //Botones
        btn_registro = findViewById(R.id.btn_registro);
        btn_iniciosesion = findViewById(R.id.btn_iniciosesion);

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
                String sexo = sexoInput.getText().toString();
                String username = usernameInput.getText().toString();
                String peso = pesoInput.getText().toString();
                String password = passwordInput.getText().toString();
                String rol = rolInput.getText().toString();

                //Llamar al metodo para validar el usuario
                if(validarUsuario(username, password, rol, sexo, peso)) {
                    new LoginTask().execute(sexo, peso, username, password, rol);
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
            //sexo peso username password rol
            String sexo = strings[0];
            String peso = strings[1];
            String username = strings[2];
            String password = strings[3];
            String permiso =  strings[4];
            return insertValue(sexo, peso, username, password, permiso);
        }

        public boolean insertValue(String sexo, String peso, String username, String password, String permiso) {

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = Conexion.getConnection();

                String query = "SELECT MAX(id_usuario) FROM usuarios";
                PreparedStatement stmt = connection.prepareStatement(query);
                ResultSet rs = stmt.executeQuery(query);
                String lastIdStr = rs.next() ? rs.getString(1) : "0"; // Si la tabla está vacía, se asigna el valor 0 por defecto

                // Convertir el último valor de id_usuario a int y sumarle uno
                int newId = Integer.parseInt(lastIdStr) + 1;
                String consulta = "INSERT INTO usuarios (id_usuario, sexo, peso, username, password, tipo) VALUES ('"+newId+"','" + sexo + "', '" + peso + "', '" + username + "', '" + password + "', '" + permiso + "')";
                PreparedStatement statement = connection.prepareStatement(consulta);
                int rowsInserted = statement.executeUpdate();
                return rowsInserted > 0;

            } catch (Exception e) {
                e.printStackTrace();
                return false;
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
        }

        // La contraseña no cumple con los requisitos mínimos
        Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres y contener una mayúscula, una minúscula y un número", Toast.LENGTH_SHORT).show();
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
