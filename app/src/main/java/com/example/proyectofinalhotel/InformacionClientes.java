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

import java.util.regex.Pattern;

public class InformacionClientes extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private ListView clientes;
    private ImageButton buscar;
    private ListView info;
    private TextInputLayout filtroDni;
    GestorBasesDatos bd;

    Intent intent;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_clientes);

        super.setTitle(R.string.titulo_infoClientes);

        drawerLayout=(DrawerLayout)findViewById(R.id.viewInformacionClientes);
        navigationView=(NavigationView)findViewById(R.id.navigationView);

        toggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.drawer_abrir,R.string.drawer_cerrar);
        toggle.syncState();
        drawerLayout.addDrawerListener(toggle);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(this);

        updateMenu();

        clientes = (ListView)findViewById(R.id.listViewCliente);
        buscar = (ImageButton) findViewById(R.id.imageButtonBuscarCliente);
        info = (ListView) findViewById(R.id.listViewInfoClientes);
        filtroDni = (TextInputLayout)findViewById(R.id.textInputLayoutFiltroDni);
        bd = new GestorBasesDatos(this);
        bd.getWritableDatabase();

        ArrayAdapter<String>adapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.listapersonalizada, bd.infoTodosClientes());
        clientes.setAdapter(adapter);
        clientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<String>adapt=new ArrayAdapter<>(getApplicationContext(),R.layout.listapersonalizada,bd.clienteEscogido((int)parent.getItemIdAtPosition(position)));
                info.setAdapter(adapt);
            }
        });
    }

    public void mostrarInfo(View view){
        String dniS = filtroDni.getEditText().getText().toString();
        if(!dniS.equals("")) {
            if (Validaciones.comprobarDNI(dniS)) {
                ArrayAdapter<String>adapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.listapersonalizada, bd.infoClientePorDni(dniS));
                clientes.setAdapter(adapter);
                clientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ArrayAdapter<String>adapt=new ArrayAdapter<>(getApplicationContext(),R.layout.listapersonalizada,bd.clienteEscogidoPorDni((int)parent.getItemIdAtPosition(position),dniS));
                        info.setAdapter(adapt);
                    }
                });
            }else{
                Toast.makeText(this, "El DNI es incorrecto", Toast.LENGTH_SHORT).show();
            }
        }else{
            ArrayAdapter<String>adapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.listapersonalizada, bd.infoTodosClientes());
            clientes.setAdapter(adapter);
            clientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ArrayAdapter<String>adapt=new ArrayAdapter<>(getApplicationContext(),R.layout.listapersonalizada,bd.clienteEscogido((int)parent.getItemIdAtPosition(position)));
                    info.setAdapter(adapt);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void updateMenu(){

        Menu nav=navigationView.getMenu();
        nav.findItem(R.id.registroLimpieza).setVisible(false);
        nav.findItem(R.id.pendienteLimpieza).setVisible(false);
        nav.findItem(R.id.limpiezaActual).setVisible(false);
        nav.findItem(R.id.aniadirPersonal).setVisible(false);
        nav.findItem(R.id.cambiarLimpiador).setVisible(false);

    }

    public void cerrarTeclado(View vista){
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        inputMethodManager.hideSoftInputFromWindow(vista.getWindowToken(), 0);
    }
}