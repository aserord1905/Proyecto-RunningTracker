package com.example.runningtracker;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
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
import com.google.android.material.navigation.NavigationView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DesafioActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawerLayout;
    private TextView tituloPrimerDesafio,tituloSegundoDesafio,tituloTercerDesafio,
            kmsPrimerDesafio, kmsSegundoDesafio, kmsTercerDesafio,descripcionPrimerDesafio,
            descripcionSegundoDesafio,descripcionTercerDesafio;

    private List<Desafios> listaDesafios;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desafio);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_nav,R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //Asociar ids de titulos de los desafios
        tituloPrimerDesafio = findViewById(R.id.titulo1);
        tituloSegundoDesafio = findViewById(R.id.titulo2);
        tituloTercerDesafio = findViewById(R.id.titulo3);

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
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_carrera:
                Intent i_mapa = new Intent(this,MapsActivity.class);
                startActivity(i_mapa);
                //Bundle que te lleve a la pagina de los mapas
                Toast.makeText(this,"Iniciando Mapas...",Toast.LENGTH_LONG).show();
                break;

            case R.id.nav_historial:
                //Bundle que te lleve a la pagina de las listas
                Intent i_historial = new Intent(this,HistorialActivity.class);
                startActivity(i_historial);
                Toast.makeText(this,"Visualizando el historial...",Toast.LENGTH_LONG).show();
                break;

            case R.id.nav_historialCarreras:
                Intent i_historial_carreras = new Intent(this,HistorialCarrerasActivity.class);
                startActivity(i_historial_carreras );
                Toast.makeText(this,"Visualizando el historial de las carreras...",Toast.LENGTH_LONG).show();
                break;

            case R.id.nav_desafio:
                Intent i_desafio = new Intent(this,DesafioActivity.class);
                startActivity(i_desafio);
                Toast.makeText(this,"Visualizando los desafios de este mes",Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_salir:
                finish();
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
                    Desafios desafios = new Desafios(id_desafio,nombre,kilometros_desafio,descripcion);
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
            kmsPrimerDesafio.setText("Número de kilometos a realizar: "+primerDesafio.getKilometros_desafio()+"kms");
            descripcionPrimerDesafio.setText("Descripción:\n"+primerDesafio.getDescripcion());

            Desafios segundoDesafio = desafios.get(1);
            tituloSegundoDesafio.setText(segundoDesafio.getNombre());
            kmsSegundoDesafio.setText("Número de kilometos a realizar: "+segundoDesafio.getKilometros_desafio()+"kms");
            descripcionSegundoDesafio.setText("Descripción:\n"+segundoDesafio.getDescripcion());

            Desafios tercerDesafio = desafios.get(2);
            tituloTercerDesafio.setText(tercerDesafio.getNombre());
            kmsTercerDesafio.setText("Número de kilometos a realizar: "+tercerDesafio.getKilometros_desafio()+"kms");
            descripcionTercerDesafio.setText("Descripción:\n"+tercerDesafio.getDescripcion());

        }
    }


}
