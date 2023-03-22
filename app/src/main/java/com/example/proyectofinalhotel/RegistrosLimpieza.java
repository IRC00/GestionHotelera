package com.example.proyectofinalhotel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.proyectofinalhotel.modelo.GestorBasesDatos;
import com.google.android.material.navigation.NavigationView;

public class RegistrosLimpieza extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private ListView registros;
    GestorBasesDatos bd;
    String dni;
    SharedPreferences pref;
    Intent intent;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registros_limpieza);
        super.setTitle(R.string.titulo_registrosLimpieza);

        /**Menu*/
        drawerLayout=(DrawerLayout)findViewById(R.id.viewRegistros);
        navigationView=(NavigationView)findViewById(R.id.navigationView);

        toggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.drawer_abrir,R.string.drawer_cerrar);
        toggle.syncState();
        drawerLayout.addDrawerListener(toggle);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(this);

        updateMenu();

        registros = (ListView)findViewById(R.id.listViewRegistros);
        bd = new GestorBasesDatos(this);
        bd.getReadableDatabase();
        pref = getSharedPreferences("Login.txt", Context.MODE_PRIVATE);
        dni = pref.getString("dni","Error");

        ArrayAdapter<String>adapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.listapersonalizada, bd.registros(dni));
        registros.setAdapter(adapter);

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
}