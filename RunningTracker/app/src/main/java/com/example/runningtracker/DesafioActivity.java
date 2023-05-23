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
    private String idDesafio;

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

        //Asociar ids de las fechas de los desafios
        kmsPrimerDesafio = findViewById(R.id.fecha1);
        kmsSegundoDesafio = findViewById(R.id.fecha2);
        kmsTercerDesafio = findViewById(R.id.fecha3);

        //Asociar ids de descripcion de los desafios
        descripcionPrimerDesafio = findViewById(R.id.descripcion1);
        descripcionSegundoDesafio = findViewById(R.id.descripcion2);
        descripcionTercerDesafio = findViewById(R.id.descripcion3);

        //Asociar ids botones interfaz
        primerDesafio_btn = findViewById(R.id.btn_inscribirPrimerDesafio);
        segundoDesafio_btn = findViewById(R.id.btn_inscribirSegundoDesafio);
        tercerDesafio_btn = findViewById(R.id.btn_inscribirTercerDesafio);

        //PRIMER BOTON
        primerDesafio_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acciones a realizar cuando se hace clic en el primer botón
                d.setId_desafio("1");
                //CONTROLO SI EL USUARIO ESTA O NO INSCRITO POR EL TEXTO DEL BOTON
                if (primerDesafio_btn.getText().toString().equals("Inscribir")) {
                    new InscribirDesafio().execute(u.getId_usuario(), d.getId_desafio(), null);
                } else {
                    new EliminarDesafio().execute(u.getId_usuario(), d.getId_desafio());
                    Toast.makeText(DesafioActivity.this, "Desafio completado", Toast.LENGTH_SHORT).show();
                }
            }
        });

        segundoDesafio_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acciones a realizar cuando se hace clic en el segundo botón
                int id_Desafio = 2;
                d.setId_desafio(String.valueOf(id_Desafio));
                if (segundoDesafio_btn.getText().toString().equals("Inscribir")) {
                    new InscribirDesafio().execute(u.getId_usuario(), d.getId_desafio(), null);
                } else {
                    new EliminarDesafio().execute(u.getId_usuario(), d.getId_desafio());
                    Toast.makeText(DesafioActivity.this, "Desafio completado", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tercerDesafio_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acciones a realizar cuando se hace clic en el tercer botón
                int id_Desafio = 3;
                d.setId_desafio(String.valueOf(id_Desafio));
                if (tercerDesafio_btn.getText().toString().equals("Inscribir")) {
                    new InscribirDesafio().execute(u.getId_usuario(), d.getId_desafio(), null);
                } else {
                    new EliminarDesafio().execute(u.getId_usuario(), d.getId_desafio());
                    Toast.makeText(DesafioActivity.this, "Desafio completado", Toast.LENGTH_SHORT).show();
                }
            }
        });

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

            case R.id.nav_historial:
                //Bundle que te lleve a la pagina de las listas
                Intent i_historial = new Intent(this, HistorialActivity.class);
                startActivity(i_historial);
                Toast.makeText(this, "Visualizando el historial...", Toast.LENGTH_LONG).show();
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
            kmsPrimerDesafio.setText("Número de kilometos a realizar: " + primerDesafio.getKilometros_desafio() + "kms");
            descripcionPrimerDesafio.setText("Descripción:\n" + primerDesafio.getDescripcion());

            Desafios segundoDesafio = desafios.get(1);
            tituloSegundoDesafio.setText(segundoDesafio.getNombre());
            kmsSegundoDesafio.setText("Número de kilometos a realizar: " + segundoDesafio.getKilometros_desafio() + "kms");
            descripcionSegundoDesafio.setText("Descripción:\n" + segundoDesafio.getDescripcion());

            Desafios tercerDesafio = desafios.get(2);
            tituloTercerDesafio.setText(tercerDesafio.getNombre());
            kmsTercerDesafio.setText("Número de kilometos a realizar: " + tercerDesafio.getKilometros_desafio() + "kms");
            descripcionTercerDesafio.setText("Descripción:\n" + tercerDesafio.getDescripcion());

        }
    }

    private class InscribirDesafio extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            //Pasar id del usuario, id del desafio, kilometros realizados
            String idUsuario = strings[0];
            String idDesafio = strings[1];
            String kilometros_realizados = strings[2];
            return insertValue(idUsuario, idDesafio, kilometros_realizados);
        }

        public boolean insertValue(String idUsuario, String idDesafio, String kilometros_realizados) {

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = Conexion.getConnection();

                // Verificar si el id_usuario ya existe en la tabla
                String query = "SELECT id_usuario FROM historial_desafios WHERE id_usuario = ?";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setString(1, idUsuario);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    // El id_usuario ya existe en la tabla
                    return false;
                }

                // Obtener el nuevo id_historial
                query = "SELECT MAX(id_historial) FROM historial_desafios";
                stmt = connection.prepareStatement(query);
                rs = stmt.executeQuery();
                String lastIdStr = rs.next() ? rs.getString(1) : "0";
                int newId = 0;
                if (lastIdStr != null) {
                    newId = Integer.parseInt(lastIdStr) + 1;
                }


                if (lastIdStr != null) {
                    newId = Integer.parseInt(lastIdStr) + 1;
                }
                String consulta = "INSERT INTO historial_desafios (id_historial, id_usuario, id_desafio, kilometros_realizados)  VALUES ('" + newId + "','" + idUsuario + "', '" + idDesafio + "', '" + kilometros_realizados + "')";
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
            super.onPostExecute(res);
            if (res) {
                if (d.getId_desafio() != null && d.getId_desafio().equals("1")) {
                    // El id_desafio es igual a 1
                    primerDesafio_btn.setText("En curso");
                    estilosDesafios(tituloPrimerDesafio, tituloSegundoDesafio, tituloTercerDesafio);

                } else if (d.getId_desafio() != null && d.getId_desafio().equals("2")) {
                    segundoDesafio_btn.setText("En curso");
                    estilosDesafios(tituloSegundoDesafio, tituloPrimerDesafio, tituloTercerDesafio);

                } else if (d.getId_desafio() != null && d.getId_desafio().equals("3")) {
                    tercerDesafio_btn.setText("En curso");
                    estilosDesafios(tituloTercerDesafio, tituloPrimerDesafio, tituloSegundoDesafio);

                }
                // La inserción fue exitosa, mostrar un Toast indicando que el usuario se inscribió correctamente
                Toast.makeText(DesafioActivity.this, "Usuario inscrito correctamente", Toast.LENGTH_SHORT).show();
            } else {

                // La inserción falló, mostrar un Toast indicando que el usuario ya está inscrito
                Toast.makeText(DesafioActivity.this, "El usuario ya está inscrito", Toast.LENGTH_SHORT).show();
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
                    primerDesafio_btn.setText("En curso");
                    //Proporciona los colores
                    estilosDesafios(tituloPrimerDesafio, tituloSegundoDesafio, tituloTercerDesafio);

                } else if (idDesafio.equals("2")) {
                    segundoDesafio_btn.setText("En curso");
                    //Inscrito color verde
                    estilosDesafios(tituloSegundoDesafio, tituloPrimerDesafio, tituloTercerDesafio);

                } else if (idDesafio.equals("3")) {
                    tercerDesafio_btn.setText("En curso");

                    estilosDesafios(tituloTercerDesafio, tituloPrimerDesafio, tituloSegundoDesafio);
                }

            }
        }
    }

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

    private void estilosDesafios(TextView textoVerde, TextView textoRojo1, TextView textoRojo2) {
        textoVerde.setTextColor(Color.parseColor("#4cff33"));
        textoRojo1.setTextColor(Color.parseColor("#ff0000"));
        textoRojo2.setTextColor(Color.parseColor("#ff0000"));
    }

    private void restablecerDesafios(TextView textoVerde, TextView textoRojo1, TextView textoRojo2) {
        textoVerde.setTextColor(Color.parseColor("#ffffff"));
        textoRojo1.setTextColor(Color.parseColor("#ffffff"));
        textoRojo2.setTextColor(Color.parseColor("#ffffff"));
    }

    private class EliminarDesafio extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            // Obtener los parámetros necesarios para eliminar la fila
            String idUsuario = strings[0];
            String idDesafio = strings[1];

            // Realizar la eliminación de la fila en la base de datos
            try {
                Connection connection = Conexion.getConnection();
                String consulta = "DELETE FROM historial_desafios WHERE id_usuario = ? AND id_desafio = ?";
                PreparedStatement statement = connection.prepareStatement(consulta);
                statement.setString(1, idUsuario);
                statement.setString(2, idDesafio);
                int rowsDeleted = statement.executeUpdate();

                return rowsDeleted > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean res) {
            super.onPostExecute(res);
            if (res) {
                // Restaurar el estado original del botón de inscripción
                if (d.getId_desafio() != null && d.getId_desafio().equals("1")) {
                    primerDesafio_btn.setText("Inscribir");
                    //Restablecer colores
                    restablecerDesafios(tituloPrimerDesafio, tituloSegundoDesafio, tituloTercerDesafio);

                } else if (d.getId_desafio() != null && d.getId_desafio().equals("2")) {
                    segundoDesafio_btn.setText("Inscribir");
                    restablecerDesafios(tituloPrimerDesafio, tituloSegundoDesafio, tituloTercerDesafio);

                } else if (d.getId_desafio() != null && d.getId_desafio().equals("3")) {
                    tercerDesafio_btn.setText("Inscribir");
                    restablecerDesafios(tituloPrimerDesafio, tituloSegundoDesafio, tituloTercerDesafio);

                }
            }
        }
    }
}
