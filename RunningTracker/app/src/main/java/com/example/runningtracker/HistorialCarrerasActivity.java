package com.example.runningtracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.runningtracker.conexion.Conexion;
import com.example.runningtracker.model.Entrenamiento;
import com.example.runningtracker.model.Usuario;
import com.google.android.material.navigation.NavigationView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HistorialCarrerasActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawerLayout;
    private ListView listView;
    private List<Entrenamiento> listaEntrenamientos;
    private String idUsuario;
    private Usuario u;
    private Button limpiarHistorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_carreras);
        listView = findViewById(R.id.listview);

       Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_nav,R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        idUsuario = sharedPreferences.getString("id_usuario", null);

        u=new Usuario();
        u.setId_usuario(idUsuario);
        listaEntrenamientos = new ArrayList<>();

        LoadDataTask task = new LoadDataTask();
        task.execute();
    }

    public void addItemsInListView(List<Entrenamiento> entrenamientos) {
        //Inicializamos el adaptador personalizado
       UserListAdapter adapter = new UserListAdapter(this, (ArrayList<Entrenamiento>) entrenamientos,idUsuario);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Mostrar el cuadro de dialogo cnd pulsamos un item.
                int posicion = i;

            }

        });
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


            case R.id.nav_salir:
                finish();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private class LoadDataTask extends AsyncTask<Void, Void, List<Entrenamiento>> {

        @Override
        protected List<Entrenamiento> doInBackground(Void... voids) {
            // Realizar la consulta a la base de datos utilizando el ID del usuario
            String consulta = "SELECT * FROM entrenamientos where id_usuario=?";
            try {
                Connection connection = Conexion.getConnection();
                PreparedStatement statement = connection.prepareStatement(consulta);
                statement.setString(1, u.getId_usuario());
                ResultSet resultSet = statement.executeQuery();

                // Recorrer los resultados de la consulta y crear objetos de entrenamiento
                while (resultSet.next()) {
                    String id_entrenamientos = resultSet.getString("id_entrenamientos");
                    String id_usuario = resultSet.getString("id_usuario");
                    String distancia = resultSet.getString("distancia");
                    String velocidad_media = resultSet.getString("velocidad_media");
                    String calorias_quemadas = resultSet.getString("calorias_quemadas");

                    // Crear un objeto Entrenamiento y agregarlo a la lista
                    Entrenamiento entrenamiento = new Entrenamiento(id_entrenamientos,id_usuario,distancia,velocidad_media,calorias_quemadas);
                    listaEntrenamientos.add(entrenamiento);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return listaEntrenamientos;
        }

        @Override
        protected void onPostExecute(List<Entrenamiento> entrenamientos) {
            addItemsInListView(entrenamientos);
        }


    }
    //Adaptador personalizado con una foto
    public class UserListAdapter extends ArrayAdapter<Entrenamiento> {
        // Contexto de la aplicación
        private final Context mContext;
        // Lista de usuarios a mostrar en la ListView
        private final ArrayList<Entrenamiento> mEntrenamientos;
        // ID del usuario
        private final String mIdUsuario;

        public UserListAdapter(Context context, ArrayList<Entrenamiento> entrenamientos, String idUsuario) {
            super(context, R.layout.custom_row_layout, entrenamientos);
            this.mContext = context;
            this.mEntrenamientos = entrenamientos;
            this.mIdUsuario = idUsuario;
        }

        // Método que se llama para crear la vista de cada elemento de la ListView
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            // Inflar la vista personalizada para cada elemento de la ListView
            LayoutInflater inflater = LayoutInflater.from(mContext);
            //inflater.inflate sirve parta poder crear una vista a partir de un diseño xml, convierte un XML a un objeto View para cada fila.
            View customView = inflater.inflate(R.layout.custom_row_layout, parent, false);

            // Obtener los elementos de la vista personalizada
            ImageView userIcon = customView.findViewById(R.id.user_icon);
            TextView userName = customView.findViewById(R.id.user_name);

            // Establecer el icono y el nombre del usuario en la vista personalizada
            Entrenamiento currentEntrenamiento = mEntrenamientos.get(position);
            userIcon.setImageResource(R.drawable.historial_carreras);
            userName.setText("Entrenamiento: "+currentEntrenamiento.getId_entrenamientos()+"\nCalorias: "+currentEntrenamiento.getCalorias_quemadas()+"kcal     " +
                    "Distancia:"+currentEntrenamiento.getDistancia()+" km\nVelocidad Media: "+currentEntrenamiento.getVelocidad_media()+"m/s");

            // Establecer otros atributos de la vista personalizada, si es necesario


            // Devolver la vista personalizada
            return customView;
        }
    }
}
