package com.example.proyectofinalhotel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.proyectofinalhotel.modelo.GestorBasesDatos;
import com.google.android.material.navigation.NavigationView;

public class PersonalGeneral extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    /**Declaracion de los elementos y variables*/

    private ListView listaPersonal;
    private GestorBasesDatos bd;
    private ListView listaEspecifica;
    private Spinner spinner;
    private String filtro;
    static String dni;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_general);

        /**Inicializacion de los elementos y variables*/

        listaPersonal=(ListView)findViewById(R.id.listaPersonalGeneral);
        listaEspecifica=(ListView) findViewById(R.id.listaPersonalEspecifica);
        spinner=(Spinner)findViewById(R.id.spinner);
        drawerLayout=(DrawerLayout)findViewById(R.id.viewGeneralPersonal);
        navigationView=(NavigationView)findViewById(R.id.navigationView);
        super.setTitle(R.string.titulo_personalGeneral);

        /**Creacion del NavigationView, indicandole los metodos necesarios para que se muestre y se oculte, los datos han sido insertados previamente en el layout respectivo
         * a esta clase*/

        toggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.drawer_abrir,R.string.drawer_cerrar);
        toggle.syncState();
        drawerLayout.addDrawerListener(toggle);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(this);

        bd=new GestorBasesDatos(this);

        /**Creo un adapter para insertar los datos desde un array estatica creada en un layout "array_spinner"
         * dependiendo de la opcion que se seleccione mostrara un tipo de personal diferente*/
        ArrayAdapter<CharSequence>ad=ArrayAdapter.createFromResource(this,R.array.opciones, android.R.layout.simple_spinner_item);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(ad);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.listapersonalizada, bd.mostrarPersonal(parent.getItemAtPosition(position).toString()));
                listaPersonal.setAdapter(adapter);
                filtro = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });

        /**Mostrara el personal en funcion que se ha seleccionado en en el spinner en un listView con datos mas reducidos*/
        listaPersonal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<String>adapt=new ArrayAdapter<>(getApplicationContext(),R.layout.listapersonalizada,bd.empleadoEscogido((int)parent.getItemIdAtPosition(position),filtro));
                listaEspecifica.setAdapter(adapt);
                dni=bd.dniEscogido();
            }
        });


        /**Lista en la que se muestran los datos en mas detalles de la persona que se ha seleccionado en el listView anterior, cogiendo el dni de la posicion que se ha seleccionado
         * anteriormente
         * Este metodo si mantienes el listView, se abrirá una ventana emergente con una serie de opciones a realizar con respecto a este empleado
         * @see Dialogos*/
        listaEspecifica.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                FragmentManager fm=getSupportFragmentManager();
                Dialogos dialogos=new Dialogos();
                dialogos.show(fm,"tagAlert");
                return true;
            }
        });

        updateMenu();

    }

    /**Este metodo es para que detecte que se esta pinchando en el boton del menu, si lo quito es como si no estuviera**/
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(toggle.onOptionsItemSelected(item)) {

            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    /**Este metodo detecta si el menu esta abierto o no*/

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }

    }

    /**Metodo que sirve para dar funcionalidad a los diferentes items del menu*/

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.Perfil:
                Intent intent=new Intent(this,ModificarDatos.class);
                intent.putExtra("vista","perfil");
                intent.putExtra("tipo_perfil","administrador");
                startActivity(intent);
                finish();
                break;
            case R.id.aniadirPersonal:
                startActivity(new Intent(this,CrearEmpleado.class));
                finish();
                break;
            case R.id.cambiarLimpiador:
                startActivity(new Intent(this,CambioLimpiador.class));
                finish();
                break;
            case R.id.registroLimpieza:
                startActivity(new Intent(this,RegistroLimpiezaAdmin.class));
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

    @Override
    public void finish() {
        super.finish();
    }


    /**Metodo que sirve para capturar el evento del movil del boton de retroceder, pero en este caso lo capturamos para evitar que se realice una accion no deseada, como
     * volver a una pantalla que no existe*/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


    /**Metodo que oculta los diferentes items del menu, ya que depende del perfil a acceder se mostraran unas opciones u otras*/
    public void updateMenu(){

        Menu nav=navigationView.getMenu();
        nav.findItem(R.id.infoClientes).setVisible(false);
        nav.findItem(R.id.bajaClientes).setVisible(false);
        nav.findItem(R.id.nuevoCliente).setVisible(false);
        nav.findItem(R.id.pendienteLimpieza).setVisible(false);
        nav.findItem(R.id.cambiaPendiente).setVisible(false);
        nav.findItem(R.id.limpiezaActual).setVisible(false);

    }
}