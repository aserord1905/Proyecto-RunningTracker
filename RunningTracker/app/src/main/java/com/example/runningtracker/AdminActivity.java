package com.example.runningtracker;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.runningtracker.conexion.Conexion;
import com.example.runningtracker.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdminActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayList<Usuario> usuarios = new ArrayList<>();
    private List<Usuario> listaUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        listView = findViewById(R.id.listview);
        listaUsuarios = new ArrayList<>();

        // Lanzar la tarea asincrona
        LoadDataTask task = new LoadDataTask();
        task.execute();
    }

    public void addItemsInListView(List<Usuario> usuarios) {
        List<String> nombres = usuarios.stream()
                .map(usuario -> String.format("%s - %s", usuario.getId_usuario(), usuario.getUsername()))
                .collect(Collectors.toList());
        ArrayAdapter<String> adaptador = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, nombres);
        listView.setAdapter(adaptador);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Mostrar el cuadro de dialogo cnd pulsamos un item.
                int posicion = i;
                AlertDialog.Builder dialogo = new AlertDialog.Builder(AdminActivity.this);
                dialogo.setTitle("Usuario: "+usuarios.get(posicion).getUsername()+"\nInformacion del usuario:");
                dialogo.setMessage("ID Usuario: "+usuarios.get(posicion).getId_usuario()+"\n"+"Sexo: "+usuarios.get(posicion).getSexo()+"\n"+"Peso: "+usuarios.get(posicion).getPeso());

                dialogo.setCancelable(false);
                dialogo.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogo, int i) {


                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogo, int i) {
                        dialogo.cancel();

                    }

                });
                dialogo.show();
            }

        });
    }

    private class LoadDataTask extends AsyncTask<Void, Void, List<Usuario>> {

        @Override
        protected List<Usuario> doInBackground(Void... voids) {
            String consulta = "SELECT * FROM usuarios";
            try (Connection connection = Conexion.getConnection();
                 PreparedStatement statement = connection.prepareStatement(consulta);
                 ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    String id_usuario = resultSet.getString("id_usuario");
                    String nombreU = resultSet.getString("username");
                    //Añadir todos los campos de la base de datos para posteriormente utilizarlos
                    Usuario usuario = new Usuario(id_usuario, nombreU);
                    listaUsuarios.add(usuario);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return listaUsuarios;
        }

        @Override
        protected void onPostExecute(List<Usuario> usuarios) {
            addItemsInListView(usuarios);
        }
    }
}
