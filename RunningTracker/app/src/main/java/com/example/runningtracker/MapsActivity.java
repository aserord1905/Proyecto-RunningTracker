package com.example.runningtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.runningtracker.conexion.Conexion;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.runningtracker.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.navigation.NavigationView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener, GoogleMap.OnMapClickListener, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private DrawerLayout drawerLayout;
    private Button btnGo;
    //Trazado de lineas.
    private boolean trazandoLinea = false;
    private Polyline polyline;
    //Variable booleana para controlar el botón
    private boolean carreraenCurso=false;
    private String idUsuario;
    private Location lastLocation;
    private double distanciaTotal;
    private TextView cronometroTextView;
    private long tiempoInicial = 0;
    private CountDownTimer countDownTimer;
    private int horas, minutos,segundos;
    //Declaración de variables
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        idUsuario = sharedPreferences.getString("id_usuario", null);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Metodo para capturar tu posición en el mapa
        getLocalizacion();

        //NAVIGATION DRAWLER
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        cronometroTextView = findViewById(R.id.tv_cronometro);
        //Asignación de las variables
        btnGo = findViewById(R.id.btn_go);

        //Accion del boton.
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new CountDownTimer(5000, 1000) {
                    public void onTick(long segundos) {
                        //Se visualiza los segundos en el botón, tambien se puede colocar en la pantalla
                        long segundosRestantes = segundos / 1000;
                        btnGo.setText("" + segundosRestantes);
                    }

                    //Cuando termina la cuenta atrás:
                    public void onFinish() {
                        if (!carreraenCurso){
                            btnGo.setText("En curso");
                            Toast.makeText(getApplicationContext(),"COMENZANDO LA CARRERA",Toast.LENGTH_LONG).show();
                            carreraenCurso = true;
                            //Lo vuelvo a inicializar para que vuelva a 0
                            tiempoInicial = 0;
                            //Llamamos al metodo para iniciar el trazado de linea.
                            empezarATrazarLinea();
                            startCronometro();
                        }else{
                            btnGo.setText("GO");
                            Toast.makeText(getApplicationContext(),"Has finalizado la carrera con exito",Toast.LENGTH_LONG).show();
                            carreraenCurso=false;
                            //Llamamos al metodo para detener el trazado de linea
                            detenerTrazadoLinea();
                            detenerCronometro();
                            //Insertar el entrenamiento cronometrado
                            InsertarEntrenamiento tareaInsertar = new InsertarEntrenamiento();
                            tareaInsertar.execute(idUsuario, String.valueOf(distanciaTotal), String.valueOf(tiempoInicial));
                        }
                    }
                }.start();
            }
        });
    }

    private void startCronometro() {
        countDownTimer = new CountDownTimer(Long.MAX_VALUE, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tiempoInicial += 1000;
                actualizarCronometro(tiempoInicial);
            }

            @Override
            public void onFinish() {
                //Acciones pare terminar el cronometro
            }
        };

        countDownTimer.start();
    }

    private void actualizarCronometro(long tiempo) {
        horas = (int) (tiempo / 3600000);
        minutos = (int) (tiempo - horas * 3600000) / 60000;
        segundos = (int) (tiempo - horas * 3600000 - minutos * 60000) / 1000;
        //hh:mm:ss
        String tiempoFormateado = String.format("%02d:%02d:%02d", horas, minutos, segundos);
        //x.xx
        String distanciaFormateada = String.format("%.2f", distanciaTotal);
        cronometroTextView.setText("Tiempo en carrera:\n"+tiempoFormateado+"\nDistancia: "+distanciaFormateada+" km");
    }

    private void detenerCronometro() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        BitmapDescriptor puntero = BitmapDescriptorFactory.fromResource(R.drawable.puntero);
        //Asociacion de los escuchadores necesarios.
        mMap.setOnMapClickListener(this);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);


        LocationManager locationManager = (LocationManager) MapsActivity.this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                //Obtener mi ubicacion
                LatLng miUbicacion = new LatLng(location.getLatitude(),location.getLongitude());
                //Mover la camara a la ubicacion actual
                mMap.moveCamera(CameraUpdateFactory.newLatLng(miUbicacion));
                //Añadimos efectos, añadimos mas zoom
                CameraPosition cameraPosition = new CameraPosition.Builder().target(miUbicacion).zoom(18).tilt(0).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                //Obtener los kilometros.
                if (lastLocation!=null){
                    //Obtencion en kilometros
                    distanciaTotal += lastLocation.distanceTo(location)/1000;
                }

                lastLocation = location;
            }
        };
        //Actualizacion de datos y le pasamos el escuchador
        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);

        //Mas preciso que con network
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {

    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        return false;
    }

    //Meotodo para dirigir a las pantallas
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_carrera:

                Intent i_mapa = new Intent(this,MapsActivity.class);
                startActivity(i_mapa);
                //Bundle que te lleve a la pagina de los mapas
                Toast.makeText(this,"Iniciando Mapas...",Toast.LENGTH_LONG).show();

                break;

            case R.id.nav_historialCarreras:

                Intent i_historial_carreras = new Intent(this,HistorialCarrerasActivity.class);
                i_historial_carreras.putExtra("id_usuario",idUsuario);
                startActivity(i_historial_carreras);
                Toast.makeText(this,"Visualizando el historial de las carreras...",Toast.LENGTH_LONG).show();
                break;

            case R.id.nav_desafio:
                Intent i_desafio = new Intent(this,DesafioActivity.class);
                startActivity(i_desafio);
                Toast.makeText(this,"Visualizando los desafios de este mes",Toast.LENGTH_LONG).show();
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

    //Obtención de los permisos de localización en tiempo real del usuario
    private void getLocalizacion(){
        int permiso = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permiso == PackageManager.PERMISSION_DENIED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    /*
    En este código, creamos un objeto PolylineOptions y lo pasamos al método addPolyline() para crear la línea en el mapa.
    Luego, en el método onMyLocationChange(), obtenemos la ubicación actual del usuario y la convertimos a un objeto LatLng.
    A continuación, obtenemos la lista actual de puntos de la línea mediante el método getPoints() de Polyline y añadimos el
    nuevo punto a esta lista con el método add(). Finalmente, actualizamos la lista de puntos de la línea con el método setPoints()
    de Polyline.
    Espero que esto solucione el problema que estás experimentando. Si necesitas ayuda adicional, por favor házmelo saber.
     */

    //METODOS DE TRAZADO DE LÍNEAS
    private void empezarATrazarLinea() {
        //Booleano controlar carrera.
        trazandoLinea = true;
        PolylineOptions polylineOptions = new PolylineOptions().clickable(true);
        polyline = mMap.addPolyline(polylineOptions);
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                LatLng punto = new LatLng(location.getLatitude(), location.getLongitude());
                List<LatLng> puntosActuales = polyline.getPoints();
                puntosActuales.add(punto);
                polyline.setPoints(puntosActuales);
            }
        });
    }


    private void detenerTrazadoLinea() {
        trazandoLinea = false;
        mMap.setOnMyLocationChangeListener(null);
        polyline.remove();
    }

    private class InsertarEntrenamiento extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            // sexo peso username password rol
            idUsuario = strings[0];
            distanciaTotal = Double.parseDouble(strings[1]);
            tiempoInicial = Long.parseLong(strings[2]);

            return insertValue(idUsuario, distanciaTotal, tiempoInicial);
        }

        public boolean insertValue(String idUsuario, double distanciaTotal, Long tiempoInicial) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = Conexion.getConnection();

                String query = "SELECT MAX(id_entrenamientos) FROM entrenamientos";
                PreparedStatement stmt = connection.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();
                String lastIdStr = rs.next() ? rs.getString(1) : "0"; // Si la tabla está vacía, se asigna el valor 0 por defecto

                // Convertir el último valor de id_usuario a int y sumarle uno
                int newIdEntrenamiento = Integer.parseInt(lastIdStr) + 1;
                String consulta = "INSERT INTO entrenamientos (id_entrenamientos, id_usuario, distancia, tiempo) VALUES (?, ?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(consulta);
                statement.setInt(1, newIdEntrenamiento);
                statement.setString(2, idUsuario);
                statement.setDouble(3, distanciaTotal);
                statement.setString(4, formatTiempo(tiempoInicial));

                int rowsInserted = statement.executeUpdate();
                return rowsInserted > 0;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        private String formatTiempo(Long tiempoInicial) {
            int horas = (int) (tiempoInicial / 3600000);
            int minutos = (int) (tiempoInicial - horas * 3600000) / 60000;
            int segundos = (int) (tiempoInicial - horas * 3600000 - minutos * 60000) / 1000;

            return String.format("%02d:%02d:%02d", horas, minutos, segundos);
        }


        @Override
        protected void onPostExecute(Boolean res) {
            if (res) {
                Toast.makeText(MapsActivity.this, "Carrera registrada exitosamente", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MapsActivity.this, "Error al registrar la carrera", Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(res);
        }
    }






}