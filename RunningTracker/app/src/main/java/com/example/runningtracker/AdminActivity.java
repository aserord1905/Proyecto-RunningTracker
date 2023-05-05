package com.example.runningtracker;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
                .map(usuario -> String.format("%s - %s",usuario.getId_usuario(), usuario.getUsername()))
                .collect(Collectors.toList());
        //ArrayAdapter<String> adaptador = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, nombres);
        //Inicializamos el adaptador personalizado
        UserListAdapter adapter = new UserListAdapter(this, (ArrayList<Usuario>) usuarios);
        listView.setAdapter(adapter);

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
                        // Obtener el id del usuario seleccionado
                        String idUsuario = usuarios.get(posicion).getId_usuario();

                        // Crear AsyncTask para eliminar el usuario
                        DeleteUserTask task = new DeleteUserTask();
                        task.execute(idUsuario);
                        Toast.makeText(AdminActivity.this, "Usuario eliminado correctamente", Toast.LENGTH_SHORT).show();
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
                    String password = resultSet.getString("password");
                    String peso = resultSet.getString("peso");
                    String sexo = resultSet.getString("sexo");
                    String tipo = resultSet.getString("tipo");
                    //Añadir todos los campos de la base de datos para posteriormente utilizarlos
                    Usuario usuario = new Usuario(id_usuario, nombreU,password, peso, sexo, tipo);
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

    //Asyntask encargada de eliminar los usuarios por id
    private class DeleteUserTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... ids) {
            String idUsuario = ids[0];
            String consulta = "DELETE FROM usuarios WHERE id_usuario=?";
            try (Connection connection = Conexion.getConnection();
                 PreparedStatement statement = connection.prepareStatement(consulta)) {
                //Cogemos el id de usuario
                statement.setString(1, idUsuario);
                statement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // Actualizar la lista de usuarios después de eliminar el usuario
            listaUsuarios.clear();
            LoadDataTask task = new LoadDataTask();
            task.execute();
        }
    }

    //Adaptador personalizado con una foto
    public class UserListAdapter extends ArrayAdapter<Usuario> {
        // Contexto de la aplicación
        private final Context mContext;
        // Lista de usuarios a mostrar en la ListView
        private final ArrayList<Usuario> mUsers;

        public UserListAdapter(Context context, ArrayList<Usuario> users) {
            // Constructor que llama al constructor de la clase padre
            super(context, R.layout.custom_row_layout, users);
            // Asignar los parámetros a las variables de instancia correspondientes
            this.mContext = context;
            this.mUsers = users;
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
            Usuario currentUser = mUsers.get(position);
            userIcon.setImageResource(R.drawable.user_icon);
            userName.setText(currentUser.getUsername());

            // Establecer otros atributos de la vista personalizada, si es necesario
            customView.setBackgroundColor(Color.WHITE);

            // Devolver la vista personalizada
            return customView;
        }
    }

}
