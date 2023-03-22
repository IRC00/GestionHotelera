package com.example.proyectofinalhotel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectofinalhotel.modelo.GestorBasesDatos;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;

public class DarDeBajaClientes extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private ListView clientes;
    private TextInputLayout habitacion;
    private ImageButton buscar;
    private Button baja;
    GestorBasesDatos bd;
    public String habitacionS;

    private TextView centro;
    private TextView derecha;

    Intent intent;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dar_de_baja_clientes);

        super.setTitle(R.string.titulo_darBajaCliente);

        /**Menu*/
        drawerLayout=(DrawerLayout)findViewById(R.id.viewDarDeBajaClientes);
        navigationView=(NavigationView)findViewById(R.id.navigationView);

        toggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.drawer_abrir,R.string.drawer_cerrar);
        toggle.syncState();
        drawerLayout.addDrawerListener(toggle);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(this);

        updateMenu();

        clientes = (ListView)findViewById(R.id.listViewPersonas);
        habitacion = (TextInputLayout)findViewById(R.id.textInputLayoutNHabitacion);
        buscar = (ImageButton)findViewById(R.id.imageButtonBuscarHabitacion2);
        baja = (Button)findViewById(R.id.buttonDarDeBajaClientes);

        centro = (TextView)findViewById(R.id.nombre);
        derecha = (TextView)findViewById(R.id.apellidos);

        centro.setText("Nombre");
        derecha.setText("Apellido");

        bd = new GestorBasesDatos(this);
        bd.getWritableDatabase();
    }

    /**Metodo que da de baja a los clientes de una habitacion*/
    public void darDeBaja(View view){
        habitacionS = habitacion.getEditText().getText().toString();
        if(!habitacionS.equals("")){
            bd.cambiarEstadoHabitacionLibre(habitacionS);
            Toast.makeText(this, "Los clientes se han dado de baja correctamente", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,InformacionClientes.class);
            startActivity(intent);
            this.finish();
        }else{
            Toast.makeText(this, "Rellene el campo habitacion", Toast.LENGTH_SHORT).show();
        }
    }

    /**Metodo que busca la habitacion que hemos escrito o seleccionado*/
    public void buscarHabitacion(View view){
            habitacionS = habitacion.getEditText().getText().toString();
            if(!habitacionS.equals("")) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, bd.infoClientes(habitacionS));
                clientes.setAdapter(adapter);
            }else{
                centro.setText("Nº Personas");
                derecha.setText("Nº Planta");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, bd.habitacionesOcupadas());
                clientes.setAdapter(adapter);
                clientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        habitacion.getEditText().setText(String.valueOf(bd.habitacionEscogidaParaBaja((int)parent.getItemIdAtPosition(position))));
                        habitacionS = habitacion.getEditText().getText().toString();
                        centro.setText("Nombre");
                        derecha.setText("Apellido");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1, bd.infoClientes(habitacionS));
                        clientes.setAdapter(adapter);
                    }
                });
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