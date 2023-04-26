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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegisterActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private EditText sexoInput, pesoInput, usernameInput, passwordInput, rolInput;
    private Button btn_registro;
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


        btn_registro = findViewById(R.id.btn_registro);

        //Accion para el botón de registrar un nuevo usuario
        btn_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sexo = sexoInput.getText().toString();
                String username = usernameInput.getText().toString();
                String peso = pesoInput.getText().toString();
                String password = passwordInput.getText().toString();
                String rol = rolInput.getText().toString();
                //Llamar al metodo de insercion del usuario.

                if (username.equals("") || password.equals("") || rol.equals("") || sexo.equals("") || peso.equals("")){
                    Toast.makeText(RegisterActivity.this, "Campos vacios", Toast.LENGTH_SHORT).show();
                }else
                    new LoginTask().execute(sexo,peso,username, password,rol);
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

    private void insertarUsuario(){
        String sexo = sexoInput.getText().toString();
        String username = usernameInput.getText().toString();
        String peso = pesoInput.getText().toString();
        String password = passwordInput.getText().toString();
        String rol = rolInput.getText().toString();

        try {
            valueReturn = dao.insertarUsuario(new Usuario(username, password, peso, sexo, rol));
            if (valueReturn){
                Toast.makeText(this, "Usuario insertado exitosamente", Toast.LENGTH_SHORT).show();
            }else {
                // Error en la inserción, mostrar mensaje de error

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
