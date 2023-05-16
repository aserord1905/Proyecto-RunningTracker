package com.example.runningtracker;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class HistorialActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private TextView tv_cronometro;
    private CountDownTimer timer;
    private String idUsuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_nav,R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //TextView del cronometro
        tv_cronometro = findViewById(R.id.tv_cronometro);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            idUsuario = bundle.getString("id_usuario");
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_carrera:
                Bundle bundle = new Bundle();
                bundle.putString("id_usuario", idUsuario);
                Intent i_mapa = new Intent(this,MapsActivity.class);
                startActivity(i_mapa);
                //Bundle que te lleve a la pagina de los mapas
                Toast.makeText(this,"Iniciando Mapas...",Toast.LENGTH_LONG).show();
                break;

            case R.id.nav_historial:
                Bundle bundle1 = new Bundle();
                bundle1.putString("id_usuario", idUsuario);
                //Bundle que te lleve a la pagina de las listas
                Intent i_historial = new Intent(this,HistorialActivity.class);
                startActivity(i_historial);
                Toast.makeText(this,"Visualizando el historial...",Toast.LENGTH_LONG).show();
                break;

            case R.id.nav_historialCarreras:
                Bundle bundle2 = new Bundle();
                bundle2.putString("id_usuario", idUsuario);
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

    public void inicioCronometro(){
        timer = new CountDownTimer(Long.MAX_VALUE, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //Actualización el textview con el tiempo transcurrido.
                long seconds = (millisUntilFinished / 1000) % 60;
                long minutes = (millisUntilFinished / (1000*60)) % 60;
                long hours = (millisUntilFinished/(1000 * 60 * 60)) % 24;

                String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                tv_cronometro.setText(timeString);
            }

            @Override
            public void onFinish() {
                //Manejar la finalizacion del cronometro
            }
        };
        timer.start();
    }
}
