package com.example.runningtracker;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.runningtracker.dao.DAOImpl;
import com.example.runningtracker.model.Usuario;

import java.sql.SQLException;

public class RegisterActivity extends AppCompatActivity {

    private EditText sexoInput, pesoInput, usernameInput, passwordInput, rolInput;
    private Button btn_registro;


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
                //Llamar al metodo de insercion del usuario.
                insertarUsuario();
            }
        });

    }

    private void insertarUsuario(){
        String sexo = sexoInput.getText().toString();
        String username = usernameInput.getText().toString();
        float peso = Float.parseFloat(pesoInput.getText().toString());
        String password = passwordInput.getText().toString();
        String rol = rolInput.getText().toString();

        Usuario usuario = new Usuario(username, password, peso, sexo, rol);
        DAOImpl usuarioDAO = new DAOImpl();

        try {
            boolean resultado = usuarioDAO.insertarUsuario(usuario);
            if (resultado){
                Toast.makeText(this, "Usuario insertado exitosamente", Toast.LENGTH_SHORT).show();
            }else {
                // Error en la inserción, mostrar mensaje de error
                Toast.makeText(this, "Error al insertar usuario", Toast.LENGTH_SHORT).show();
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
