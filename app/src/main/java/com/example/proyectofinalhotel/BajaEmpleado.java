package com.example.proyectofinalhotel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.proyectofinalhotel.modelo.GestorBasesDatos;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class BajaEmpleado extends AppCompatActivity {

    /**Declaracion de los elementos y variables
     * dni, esta variable se usa para indicar la informacion del personal seleccionado*/

    private TextInputLayout tDni;
    private ListView listView;
    private ArrayList<String>arrayList;
    private GestorBasesDatos bd;
    private String dni="";
    Intent intent;
    String array[]={"ID:","DNI/NIE:","Nombre:","Apellidos:","Telefono:","Constraseña:","Puesto:","Planta:","Fecha Incorp.:","Estado:"};
    private ListView info;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baja_empleado);

        /**Inicicalizacion de los elementos y variables*/

        tDni=(TextInputLayout)findViewById(R.id.layoutDNI);
        listView=(ListView)findViewById(R.id.listViewDatosBaja);
        info=(ListView)findViewById(R.id.listViewDatosInfo);
        bd=new GestorBasesDatos(this);
        arrayList=new ArrayList<>();
        super.setTitle(R.string.titulo_bajaEmpleado);

        /**Inicializo el intent
         * Inicializo el dni mediante el intent, que se recoge de la clase Dialogos.java, este dni pertenece a la persona seleccionada en PersonalGeneral.java*/

        intent=getIntent();

        if(intent.getStringExtra("dni")!=null){
            tDni.getEditText().setText(intent.getStringExtra("dni"));
        }


        /**Creo un adapter para insertar informacion en el listView*/

        ArrayAdapter<String>ad=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,array);
        info.setAdapter(ad);


    }

    /**Metodo asociado al boton Buscar del layout correspondiente
     * Sirve para buscar el empleado con el dni indicado en el TextInputLayout
     * Si se encuentra se crea un ListView que se rellena con la informacion del empleado a buscar*/

    public void buscarPersonal(View view){

        dni=tDni.getEditText().getText().toString();

        if(!dni.equalsIgnoreCase("")){
            if(Validaciones.comprobarDNI(dni)) {
                ocultarTeclado(view);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, bd.buscarEmpleado(dni));
                listView.setAdapter(adapter);
            }else{
                Toast.makeText(this, "DNI/NIE incorrecto", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Introduce el DNI/NIE", Toast.LENGTH_SHORT).show();
        }

    }

    /**Metodo que confirma la accion de dar de baja a un empleado en el caso de que se encuentre y
     * se cumplan las comprobaciones
     * @see Validaciones
     * @see GestorBasesDatos el metodo borrarEmpleado(String dni)*/

    public void borrarEmpleado(View view){

        dni=tDni.getEditText().getText().toString();

        if(!dni.equalsIgnoreCase("")) {
            if(Validaciones.comprobarDNI(dni)) {
                if (bd.borrarEmpleado(dni)) {
                    Toast.makeText(this, "Emplado con el DNI/NIE: " + dni + " borrado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "No se ha podido borrar, compruebe el DNI/NIE", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "DNI/NIE incorrecto", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Asegurase de introducir un DNI/NIE antes de borrar", Toast.LENGTH_SHORT).show();
        }

    }

    /**Metodo que sirve para recoger el evento de mostrar/ocultar teclado, en este caso, le indicamos que el teclado se oculte*/

    public void ocultarTeclado(View view) {

        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

    /**Metodo que esta asociado al boton de volver del layout respectivo, sirve para retroceder a la activity indicada*/

    public void cancelarBaja(View view) {

        startActivity(new Intent(this,PersonalGeneral.class));
        finish();

    }

    /**Metodo que sirve para recoger el evento del movil del boton de volver hacia atrás, en este caso, lo capturo y lo bloqueo para evitar
     * que se pulse por error o se produzca una accion no deseada*/

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}