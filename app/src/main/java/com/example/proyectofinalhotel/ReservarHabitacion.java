package com.example.proyectofinalhotel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.proyectofinalhotel.modelo.GestorBasesDatos;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;

public class ReservarHabitacion extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private ImageButton buscar;
    private TextInputLayout nPersonas;
    private ListView datos;
    GestorBasesDatos bd;
    int numHabitacion;

    Intent intent;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservar_habitacion);

        super.setTitle(R.string.titulo_reservarHabitacion);

        /**Menu*/
        drawerLayout=(DrawerLayout)findViewById(R.id.viewReservarHabitacion);
        navigationView=(NavigationView)findViewById(R.id.navigationView);

        toggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.drawer_abrir,R.string.drawer_cerrar);
        toggle.syncState();
        drawerLayout.addDrawerListener(toggle);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(this);

        updateMenu();

        buscar=(ImageButton) findViewById(R.id.imageButtonBuscarHabitacion);
        nPersonas=(TextInputLayout)findViewById(R.id.textInputLayoutNHabitacion);
        datos=(ListView)findViewById(R.id.listViewPersonas);
        bd=new GestorBasesDatos(this);
        bd.getReadableDatabase();
    }

    /** Metodo que se ejecuta al pulsar el boton de buscar y rellena la lista de las habitaciones que estan disponibles en las que entran el numero de personas que hayamos puesto */
    public void rellenarLista(View view){
        int numPersonas = Integer.parseInt(nPersonas.getEditText().getText().toString());
        if(numPersonas<=4 && numPersonas>0) {
            if (bd.infoHabitaciones(numPersonas) == null) {
                Toast.makeText(this, "No hay resultados", Toast.LENGTH_SHORT).show();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, bd.infoHabitaciones(numPersonas));
            datos.setAdapter(adapter);
            datos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    numHabitacion = bd.habitacionEscogida((int) parent.getItemIdAtPosition(position), numPersonas);
                    añadirClientes();
                }
            });
        }else{
            Toast.makeText(this, "El numero de personas tiene que estar entre 1 y 4 ambos incluidos", Toast.LENGTH_SHORT).show();
        }
    }

    /** Metodo que abre la ventana de RellenarClientes cuando se selecciona una habitacion pasandole el numero de personas y la habitacion*/
    public void añadirClientes(){
        Intent intent = new Intent(this,RellenarClientes.class);
        intent.putExtra("personas",Integer.parseInt(nPersonas.getEditText().getText().toString()));
        intent.putExtra("habitacion",numHabitacion);
        startActivity(intent);
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
                intent.putExtra("tipo_perfil","recepcionista");
                startActivity(intent);
                finish();
                break;
            case R.id.nuevoCliente:
                startActivity(new Intent(this,ReservarHabitacion.class));
                finish();
                break;
            case R.id.bajaClientes:
                startActivity(new Intent(this,DarDeBajaClientes.class));
                finish();
                break;
            case R.id.infoClientes:
                startActivity(new Intent(this,InformacionClientes.class));
                finish();
                break;
            case R.id.cambiaPendiente:
                bd.pasarAPendiente();
                Toast.makeText(this, "El estado de las habitaciones se ha cambiado a pendiente", Toast.LENGTH_SHORT).show();
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
        nav.findItem(R.id.registroLimpieza).setVisible(false);
        nav.findItem(R.id.pendienteLimpieza).setVisible(false);
        nav.findItem(R.id.limpiezaActual).setVisible(false);
        nav.findItem(R.id.aniadirPersonal).setVisible(false);
        nav.findItem(R.id.cambiarLimpiador).setVisible(false);

    }

    /** Metodo que cierra el teclado cuando pulsas en la pantalla*/
    public void cerrarTeclado(View vista){
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        inputMethodManager.hideSoftInputFromWindow(vista.getWindowToken(), 0);
    }
}