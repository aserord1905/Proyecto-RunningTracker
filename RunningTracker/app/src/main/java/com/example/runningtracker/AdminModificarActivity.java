package com.example.runningtracker;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;

import com.example.runningtracker.conexion.Conexion;
import com.example.runningtracker.dao.DAO;
import com.example.runningtracker.dao.DAOImpl;
import com.example.runningtracker.model.Desafios;
import com.google.android.material.navigation.NavigationView;
import androidx.appcompat.app.ActionBarDrawerToggle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminModificarActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private TextView tituloPrimerDesafio, tituloSegundoDesafio, tituloTercerDesafio;
    private EditText  kmsPrimerDesafio, kmsSegundoDesafio, kmsTercerDesafio, descripcionPrimerDesafio,
            descripcionSegundoDesafio, descripcionTercerDesafio;
    private List<Desafios> listaDesafios;
    private Button btn_modificarDesafios;
    DAO dao = new DAOImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_modificar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //Asociar ids de titulos de los desafios
        tituloPrimerDesafio = findViewById(R.id.titulo1);
        tituloSegundoDesafio = findViewById(R.id.titulo2);
        tituloTercerDesafio = findViewById(R.id.titulo3);

        //Asociar kms
        kmsPrimerDesafio = findViewById(R.id.fecha1);
        kmsSegundoDesafio = findViewById(R.id.fecha2);
        kmsTercerDesafio = findViewById(R.id.fecha3);

        //Asociar ids de descripcion de los desafios
        descripcionPrimerDesafio = findViewById(R.id.descripcion1);
        descripcionSegundoDesafio = findViewById(R.id.descripcion2);
        descripcionTercerDesafio = findViewById(R.id.descripcion3);

        //Inicializar la lista.
        listaDesafios = new ArrayList<>();

        //Obtengo los datos en los textviews y editText de los desafios
        ObtenerDesafiosTask obtenerDesafiosTask = new ObtenerDesafiosTask();
        obtenerDesafiosTask.execute();

        //Boton para modificar los desafios
        btn_modificarDesafios = findViewById(R.id.btn_modificar_desafios);

        btn_modificarDesafios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener los nuevos valores de los EditText
                String nuevoKmsPrimerDesafio = kmsPrimerDesafio.getText().toString();
                String nuevoKmsSegundoDesafio = kmsSegundoDesafio.getText().toString();
                String nuevoKmsTercerDesafio = kmsTercerDesafio.getText().toString();
                String nuevaDescripcionPrimerDesafio = descripcionPrimerDesafio.getText().toString();
                String nuevaDescripcionSegundoDesafio = descripcionSegundoDesafio.getText().toString();
                String nuevaDescripcionTercerDesafio = descripcionTercerDesafio.getText().toString();

                // Crear y ejecutar la tarea asincrónica
                ModificarDesafiosTask modificarDesafiosTask = new ModificarDesafiosTask(listaDesafios, nuevoKmsPrimerDesafio, nuevoKmsSegundoDesafio, nuevoKmsTercerDesafio, nuevaDescripcionPrimerDesafio, nuevaDescripcionSegundoDesafio, nuevaDescripcionTercerDesafio);
                modificarDesafiosTask.execute();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_eliminar_usuarios:
                Intent i_mapa = new Intent(this, AdminActivity.class);
                startActivity(i_mapa);
                //Bundle que te lleve a la pagina de los mapas
                Toast.makeText(this, "Iniciando Mapas...", Toast.LENGTH_LONG).show();
                break;

            case R.id.nav_modificar_usuarios:
                //Bundle que te lleve a la pagina de las listas
                Intent i_historial = new Intent(this, AdminModificarActivity.class);
                startActivity(i_historial);
                Toast.makeText(this, "Visualizando el historial...", Toast.LENGTH_LONG).show();
                break;

            case R.id.nav_salir:
                //Bundle que te lleve a la pagina de las listas
                Intent salir = new Intent(this,LoginActivity.class);
                startActivity(salir);
                Toast.makeText(this,"Saliendo de la app...",Toast.LENGTH_LONG).show();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public class ObtenerDesafiosTask extends AsyncTask<Void, Void, List<Desafios>> {
        @Override
        public List<Desafios> doInBackground(Void... voids) {
            String consulta = "SELECT * FROM desafios";
            try (Connection connection = Conexion.getConnection();
                 PreparedStatement statement = connection.prepareStatement(consulta);
                 ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    String id_desafio = resultSet.getString("id_desafio");
                    String nombre = resultSet.getString("nombre");
                    String kilometros_desafio = resultSet.getString("kilometros_desafio");
                    String descripcion = resultSet.getString("descripcion");

                    //Añadir todos los campos de la base de datos para posteriormente utilizarlos
                    Desafios desafios = new Desafios(id_desafio, nombre, kilometros_desafio, descripcion);
                    listaDesafios.add(desafios);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return listaDesafios;
        }

        @Override
        protected void onPostExecute(List<Desafios> desafios) {
            Desafios primerDesafio = desafios.get(0);
            tituloPrimerDesafio.setText(primerDesafio.getNombre());
            kmsPrimerDesafio.setText(primerDesafio.getKilometros_desafio());
            descripcionPrimerDesafio.setText(primerDesafio.getDescripcion());

            Desafios segundoDesafio = desafios.get(1);
            tituloSegundoDesafio.setText(segundoDesafio.getNombre());
            kmsSegundoDesafio.setText(segundoDesafio.getKilometros_desafio());
            descripcionSegundoDesafio.setText(segundoDesafio.getDescripcion());

            Desafios tercerDesafio = desafios.get(2);
            tituloTercerDesafio.setText(tercerDesafio.getNombre());
            kmsTercerDesafio.setText(tercerDesafio.getKilometros_desafio());
            descripcionTercerDesafio.setText(tercerDesafio.getDescripcion());

        }
    }

    public class ModificarDesafiosTask extends AsyncTask<Void, Void, Boolean> {
        private List<Desafios> listaDesafios;
        private String nuevoKmsPrimerDesafio, nuevoKmsSegundoDesafio, nuevoKmsTercerDesafio;
        private String nuevaDescripcionPrimerDesafio, nuevaDescripcionSegundoDesafio, nuevaDescripcionTercerDesafio;

        public ModificarDesafiosTask(List<Desafios> listaDesafios, String nuevoKmsPrimerDesafio, String nuevoKmsSegundoDesafio, String nuevoKmsTercerDesafio, String nuevaDescripcionPrimerDesafio, String nuevaDescripcionSegundoDesafio, String nuevaDescripcionTercerDesafio) {
            this.listaDesafios = listaDesafios;
            this.nuevoKmsPrimerDesafio = nuevoKmsPrimerDesafio;
            this.nuevoKmsSegundoDesafio = nuevoKmsSegundoDesafio;
            this.nuevoKmsTercerDesafio = nuevoKmsTercerDesafio;
            this.nuevaDescripcionPrimerDesafio = nuevaDescripcionPrimerDesafio;
            this.nuevaDescripcionSegundoDesafio = nuevaDescripcionSegundoDesafio;
            this.nuevaDescripcionTercerDesafio = nuevaDescripcionTercerDesafio;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            // Realizar las actualizaciones en la base de datos
            try (Connection connection = Conexion.getConnection()) {
                // Actualizar el primer desafío
                dao.actualizarDesafio(connection, listaDesafios.get(0).getId_desafio(), nuevoKmsPrimerDesafio, nuevaDescripcionPrimerDesafio);

                // Actualizar el segundo desafío
                dao.actualizarDesafio(connection, listaDesafios.get(1).getId_desafio(), nuevoKmsSegundoDesafio, nuevaDescripcionSegundoDesafio);

                // Actualizar el tercer desafío
                dao.actualizarDesafio(connection, listaDesafios.get(2).getId_desafio(), nuevoKmsTercerDesafio, nuevaDescripcionTercerDesafio);

                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean resultado) {
            if (resultado) {
                // Mostrar mensaje de éxito
                Toast.makeText(AdminModificarActivity.this, "Desafíos modificados con éxito", Toast.LENGTH_SHORT).show();
            } else {
                // Mostrar mensaje de error en caso de fallo
                Toast.makeText(AdminModificarActivity.this, "Error al modificar desafíos", Toast.LENGTH_SHORT).show();
            }
        }


    }

}
