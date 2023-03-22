package com.example.proyectofinalhotel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectofinalhotel.modelo.GestorBasesDatos;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;

public class Limpiar extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private TextView infoPagina;
    private ListView habitaciones;
    private TextInputLayout habitacion;
    private Button limpiando;
    GestorBasesDatos db;
    int planta;
    SharedPreferences pref;
    private static String estado;
    Intent intent;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_limpiar);

        super.setTitle(R.string.titulo_limpiar);

        /**Menu*/
        drawerLayout=(DrawerLayout)findViewById(R.id.viewLimpiar);
        navigationView=(NavigationView)findViewById(R.id.navigationView);

        toggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.drawer_abrir,R.string.drawer_cerrar);
        toggle.syncState();
        drawerLayout.addDrawerListener(toggle);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(this);

        updateMenu();

        pref = getSharedPreferences("Login.txt", Context.MODE_PRIVATE);

        db = new GestorBasesDatos(this);
        db.getWritableDatabase();
        infoPagina = (TextView)findViewById(R.id.textViewEstado);
        habitaciones = (ListView)findViewById(R.id.listViewHabitacionesPendientes);
        habitacion = (TextInputLayout)findViewById(R.id.textInputLayoutNHab);
        limpiando = (Button)findViewById(R.id.buttonLimpiando);

        intent = getIntent();
        estado = intent.getStringExtra("estado");
        if(estado==null){
            estado="limpiando";
        }
            if (estado.equals("pendiente")) {
                infoPagina.setText(estado.toUpperCase() + "S");
                limpiando.setText(R.string.estado_limpiando);
            } else if (estado.equals("limpiando")) {
                infoPagina.setText(estado.toUpperCase());
                limpiando.setText(R.string.estado_limpio);
            }

        mostrarHabitaciones();
    }

    /**Metodo que muestra las habitaciones segun el estado de limpieza en el que estemos*/
    public void mostrarHabitaciones(){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,db.mostrarHabSegunEstado(estado,db.obtenerPlanta(pref.getString("dni","Error"))));
        habitaciones.setAdapter(adapter);
        habitaciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int[] datos = db.habitacionEscogidaParaLimpieza((int)parent.getItemIdAtPosition(position),estado);
                habitacion.getEditText().setText(String.valueOf(datos[0]));
                planta = datos[1];
            }
        });
    }

    /**Metodo que cambia el estado de limpieza al siguiente estado segun en el estado que estemos actualmente*/
    public void limpiar(){
        int hab = Integer.parseInt(habitacion.getEditText().getText().toString());
        db.getWritableDatabase();
        if(estado.equals("pendiente")) {
            db.cambiarEstado(hab, planta, pref.getString("dni", "Error"), "limpiando");
            estado = "limpiando";
            reiniciarActivity(this);
        }else if(estado.equals("limpiando")){
            db.cambiarEstado(hab, planta, pref.getString("dni", "Error"), "limpia");
            Intent intent = new Intent(this,RegistrosLimpieza.class);
            estado = "pendiente";
            startActivity(intent);
            this.finish();
        }
    }

    /**Metodo que vuelve a lanzar la activity actual*/
    public static void reiniciarActivity(Activity actividad){
        Intent reinicio=new Intent();
        reinicio.setClass(actividad, actividad.getClass());
        actividad.startActivity(reinicio);
        actividad.finish();
    }

    /**Metodo que abre una ventana emergente de confirmacion de si estas seguro de que quieres cambiar el estado de la habitacion*/
    public void abrirVentana(View view){
        if(!habitacion.getEditText().getText().toString().equals("")){
            FragmentManager fm = getSupportFragmentManager();
            VentanaEmergente ve = new VentanaEmergente();
            ve.show(fm,"tagVentana");
        }else{
            Toast.makeText(this, "Selecciona una habitacion", Toast.LENGTH_SHORT).show();
        }
    }

    /**Este metodo es para que detecte que se esta pinchando en el boton del menu, si lo quito es como si no estuviera**/
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(toggle.onOptionsItemSelected(item)) {

            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }

    }

    /**Menu*/
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.Perfil:
                intent=new Intent(this,ModificarDatos.class);
                intent.putExtra("vista","perfil");
                intent.putExtra("tipo_perfil","limpieza");
                startActivity(intent);
                finish();
                break;
            case R.id.registroLimpieza:
                startActivity(new Intent(this,RegistrosLimpieza.class));
                finish();
                break;
            case R.id.pendienteLimpieza:
                intent = new Intent(this, Limpiar.class);
                intent.putExtra("estado","pendiente");
                startActivity(intent);
                finish();
                break;
            case R.id.limpiezaActual:
                intent = new Intent(this, Limpiar.class);
                intent.putExtra("estado","limpiando");
                startActivity(intent);
                finish();
                break;
            case R.id.cerrarSesion:
                startActivity(new Intent(this,MainActivity.class));
                finish();
                break;
            case R.id.salirApp:
                super.finish();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**Metodo que bloquea el boton atras del telefono*/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**Metodo que define que campos del menu no seran visibles para el usuario logueado*/
    public void updateMenu(){

        Menu nav=navigationView.getMenu();
        nav.findItem(R.id.infoClientes).setVisible(false);
        nav.findItem(R.id.bajaClientes).setVisible(false);
        nav.findItem(R.id.nuevoCliente).setVisible(false);
        nav.findItem(R.id.aniadirPersonal).setVisible(false);
        nav.findItem(R.id.cambiarLimpiador).setVisible(false);
        nav.findItem(R.id.cambiaPendiente).setVisible(false);

    }

    /** Metodo que cierra el teclado cuando pulsas en la pantalla*/
    public void cerrarTeclado(View vista){
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        inputMethodManager.hideSoftInputFromWindow(vista.getWindowToken(), 0);
    }
}