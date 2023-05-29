package com.example.runningtracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.runningtracker.conexion.Conexion;
import com.example.runningtracker.model.Desafios;
import com.example.runningtracker.model.Historial;
import com.example.runningtracker.model.Usuario;
import com.google.android.material.navigation.NavigationView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DesafioActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private TextView tituloPrimerDesafio, tituloSegundoDesafio, tituloTercerDesafio,
            kmsPrimerDesafio, kmsSegundoDesafio, kmsTercerDesafio, descripcionPrimerDesafio,
            descripcionSegundoDesafio, descripcionTercerDesafio;
    private Button primerDesafio_btn, segundoDesafio_btn, tercerDesafio_btn;
    private String idUsuario;
    private Usuario u;
    private Desafios d;
    private Historial h;
    private List<Desafios> listaDesafios;
    private String idDesafio = "";
    private Desafios primerDesafio, segundoDesafio, tercerDesafio;

    /**TABLA BD HISTORIAL_DESAFIOS**/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desafio);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        idUsuario = sharedPreferences.getString("id_usuario", null);
        d = new Desafios();
        u = new Usuario();
        u.setId_usuario(idUsuario);
        //Asociar ids de titulos de los desafios
        tituloPrimerDesafio = findViewById(R.id.titulo1);
        tituloSegundoDesafio = findViewById(R.id.titulo2);
        tituloTercerDesafio = findViewById(R.id.titulo3);

        //Por defecto en rojo
        tituloPrimerDesafio.setTextColor(Color.RED);
        tituloSegundoDesafio.setTextColor(Color.RED);
        tituloTercerDesafio.setTextColor(Color.RED);

        //Asociar ids de las fechas de los desafios
        kmsPrimerDesafio = findViewById(R.id.fecha1);
        kmsSegundoDesafio = findViewById(R.id.fecha2);
        kmsTercerDesafio = findViewById(R.id.fecha3);

        //Asociar ids de descripcion de los desafios
        descripcionPrimerDesafio = findViewById(R.id.descripcion1);
        descripcionSegundoDesafio = findViewById(R.id.descripcion2);
        descripcionTercerDesafio = findViewById(R.id.descripcion3);

        listaDesafios = new ArrayList<>();

        ObtenerDesafiosTask obtenerDesafiosTask = new ObtenerDesafiosTask();
        obtenerDesafiosTask.execute();
        VerificarInscripcionTask verificarInscripcionTask = new VerificarInscripcionTask();
        verificarInscripcionTask.execute();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_carrera:
                Intent i_mapa = new Intent(this, MapsActivity.class);
                startActivity(i_mapa);
                //Bundle que te lleve a la pagina de los mapas
                Toast.makeText(this, "Iniciando Mapas...", Toast.LENGTH_LONG).show();
                break;

            case R.id.nav_historialCarreras:
                Intent i_historial_carreras = new Intent(this, HistorialCarrerasActivity.class);
                startActivity(i_historial_carreras);
                Toast.makeText(this, "Visualizando el historial de las carreras...", Toast.LENGTH_LONG).show();
                break;

            case R.id.nav_desafio:
                Intent i_desafio = new Intent(this, DesafioActivity.class);
                startActivity(i_desafio);
                Toast.makeText(this, "Visualizando los desafios de este mes", Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_salir:
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
        protected List<Desafios> doInBackground(Void... voids) {
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
            if (desafios != null && desafios.size() >= 3) {
                primerDesafio = desafios.get(0);
                tituloPrimerDesafio.setText(primerDesafio.getNombre());
                kmsPrimerDesafio.setText("Número de kilómetros a realizar: " + primerDesafio.getKilometros_desafio() + "kms");
                descripcionPrimerDesafio.setText("Descripción:\n" + primerDesafio.getDescripcion());

                segundoDesafio = desafios.get(1);
                tituloSegundoDesafio.setText(segundoDesafio.getNombre());
                kmsSegundoDesafio.setText("Número de kilómetros a realizar: " + segundoDesafio.getKilometros_desafio() + "kms");
                descripcionSegundoDesafio.setText("Descripción:\n" + segundoDesafio.getDescripcion());

                tercerDesafio = desafios.get(2);
                tituloTercerDesafio.setText(tercerDesafio.getNombre());
                kmsTercerDesafio.setText("Número de kilómetros a realizar: " + tercerDesafio.getKilometros_desafio() + "kms");
                descripcionTercerDesafio.setText("Descripción:\n" + tercerDesafio.getDescripcion());


                if (idDesafio.equals(primerDesafio.getId_desafio())) {
                    ObtenerNumDesafiosTask obtenerNumDesafiosTask1 = new ObtenerNumDesafiosTask();
                    obtenerNumDesafiosTask1.execute(primerDesafio.getId_desafio());
                } else if (idDesafio.equals(segundoDesafio.getId_desafio())) {
                    ObtenerNumDesafiosTask obtenerNumDesafiosTask2 = new ObtenerNumDesafiosTask();
                    obtenerNumDesafiosTask2.execute(segundoDesafio.getId_desafio());
                } else if (idDesafio.equals(tercerDesafio.getId_desafio())) {
                    ObtenerNumDesafiosTask obtenerNumDesafiosTask3 = new ObtenerNumDesafiosTask();
                    obtenerNumDesafiosTask3.execute(tercerDesafio.getId_desafio());
                }
            }

        }
    }

    private class VerificarInscripcionTask extends AsyncTask<Void, Void, Boolean> {


        @Override
        protected Boolean doInBackground(Void... voids) {
            // Verificar si el usuario está inscrito en segundo plano
            return usuarioEstaInscrito();
        }

        @Override
        protected void onPostExecute(Boolean inscrito) {
            // Actualizar el texto del botón en consecuencia
            if (inscrito) {
                if (idDesafio.equals("1")) {
                    // El id_desafio es igual a 1
                    tituloPrimerDesafio.setTextColor(Color.GREEN);
                } else if (idDesafio.equals("2")) {
                    tituloSegundoDesafio.setTextColor(Color.GREEN);
                } else if (idDesafio.equals("3")) {
                    tituloTercerDesafio.setTextColor(Color.GREEN);
                }
            }
        }
    }

    //Comprueba que tiene algun desafio y si lo tiene lo pinta en verde
    private boolean usuarioEstaInscrito() {
        try {
            // Establecer la conexión a la base de datos
            Connection connection = Conexion.getConnection();

            // Consultar si el usuario tiene algún registro en historial_desafios
            String consulta = "SELECT id_desafio FROM historial_desafios WHERE id_usuario = ?";
            PreparedStatement statement = connection.prepareStatement(consulta);
            statement.setString(1, idUsuario); // idUsuario debe estar definido en tu clase

            ResultSet resultSet = statement.executeQuery();

            // Verificar si se encontró algún registro en historial_desafios para el usuario
            if (resultSet.next()) {
                idDesafio = resultSet.getString("id_desafio");
                return true; // El usuario está inscrito en algún desafío
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // El usuario no está inscrito en ningún desafío
    }


    private class ObtenerNumDesafiosTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... params) {
            String idDesafio = params[0];
            int numDesafios = 0;
            try (Connection connection = Conexion.getConnection()) {
                String consulta = "SELECT COUNT(*) AS num_desafios FROM historial_desafios WHERE id_usuario = ? AND id_desafio = ?";
                try (PreparedStatement statement = connection.prepareStatement(consulta)) {
                    statement.setString(1, idUsuario);
                    statement.setString(2, idDesafio);
                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (resultSet.next()) {
                            numDesafios = resultSet.getInt("num_desafios");
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return numDesafios;
        }

        @Override
        protected void onPostExecute(Integer numDesafios) {
            // Actualizar el número de desafíos en los TextView correspondientes
            if (idDesafio.equals(primerDesafio.getId_desafio())) {
                tituloPrimerDesafio.setText(tituloPrimerDesafio.getText() + " (" + numDesafios + ")");
            } else if (idDesafio.equals(segundoDesafio.getId_desafio())) {
                tituloSegundoDesafio.setText(tituloSegundoDesafio.getText() + " (" + numDesafios + ")");
            } else if (idDesafio.equals(tercerDesafio.getId_desafio())) {
                tituloTercerDesafio.setText(tituloTercerDesafio.getText() + " (" + numDesafios + ")");
            }
        }
    }




}
