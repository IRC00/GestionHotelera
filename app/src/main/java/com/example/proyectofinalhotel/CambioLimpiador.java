package com.example.proyectofinalhotel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectofinalhotel.modelo.GestorBasesDatos;

import java.util.ArrayList;

public class CambioLimpiador extends AppCompatActivity {

    /**Declaracion de elemetos y variables
     * posListaPrincipal es una variable que la usare para capturar la posicion seleccionada en el primer spinner, para luego bloquear esa misma posicion en el segundo
     * spinner y no se seleccione 2 limpiadores de la misma planta
     * las variables plantaSeleccionadaPrincipal,nombrePrincipalEscogido y dniPrincipalEscogido pertenecen a los datos obtenidos del primer spinner, el resto al segundo spinner*/

    private ListView listaPrincipal;
    private ListView listaSecundaria;
    private Spinner spinnerPrincipal;
    private Spinner spinnerSecundario;
    private TextView nombrePrincipal;
    private TextView nombreSecundario;
    private ArrayList<String> plantasPrincipales;
    private ArrayList<String> plantasSecundarias;
    GestorBasesDatos db;
    private int posListaPrincipal;
    int plantaSeleccionadaPrincipal;
    int plantaSeleccionadaSecundaria;
    String nombrePrincipalEscogido;
    String nombreSecundarioEscogido;
    String dniPrincipalEscogido;
    String dniSecundarioEscogido;


    /**Inicializo una array con las plantas para seleccionar que no va a cambiar nunca, por lo tanto es declarada con un final*/
    private final String numeroPlantas[]={"Selecciona una planta...","0","1","2","3","4","5"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambio_limpiador);

        /**Inicializacion de los elementos y variables*/

        listaPrincipal = (ListView) findViewById(R.id.listViewPlantaPrincipal);
        listaSecundaria = (ListView) findViewById(R.id.listViewPlantaSecundaria);
        spinnerPrincipal = (Spinner) findViewById(R.id.spinnerPlantasPrincipal);
        spinnerSecundario = (Spinner) findViewById(R.id.spinnerPlantasSecundarias);
        nombrePrincipal = (TextView) findViewById(R.id.textViewNombrePrincipal);
        nombreSecundario = (TextView) findViewById(R.id.textViewNombreSecundario);
        db = new GestorBasesDatos(this);
        super.setTitle(R.string.titulo_cambioLimpiador);

        /**Spinner principal
         * Aqui se inicializan las variables listaPrincipal,posListaPrincipal,plantaSeleccionadaPrincipal, y se hace un set de la visibilidad del spinner y la lista respectiva
         * para que no se vean hasta que no se seleccione una planta del spinner principal*/

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, numeroPlantas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPrincipal.setAdapter(adapter);

        spinnerPrincipal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemIdAtPosition(position) != 0) {
                    plantaSeleccionadaPrincipal=Integer.valueOf(numeroPlantas[position]);
                    ArrayAdapter<String> adaptList = new ArrayAdapter<>(getApplicationContext(), R.layout.listapersonalizada, db.mostrarLimpiadorPlanta(plantaSeleccionadaPrincipal));
                    posListaPrincipal = position;
                    listaPrincipal.setAdapter(adaptList);
                    listaSecundaria.setVisibility(View.INVISIBLE);
                    spinnerSecundario.setVisibility(View.VISIBLE);

                    listaPrincipal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            db.dniNombreLimpiador(plantaSeleccionadaPrincipal,(int)parent.getItemIdAtPosition(position));
                            nombrePrincipalEscogido=db.nombreEscogido();
                            dniPrincipalEscogido=db.dniEscogido();
                            nombrePrincipal.setText(nombrePrincipalEscogido);
                        }
                    });

                    /**Spinner secundario
                     * Aqui se inicializan el resto de las variables secundarias, y se bloquea la opcion escogida en el spinner principal y se consigue con los metodos
                     * isEnabled para que no se pueda pulsar y, con el metodo getDropDownView, para cambiarle el color y poder mostrar con ese color cual esta desactivada*/
                    ArrayAdapter<String>adapterSecundario=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,numeroPlantas){
                        @Override
                        public boolean isEnabled(int position) {
                            if(position==posListaPrincipal){
                                return false;
                            }else{
                                return true;
                            }
                        }

                        @Override
                        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                            View view=super.getDropDownView(position, convertView, parent);
                            TextView textView=(TextView)view;
                            if(position==posListaPrincipal){
                                textView.setTextColor(Color.GRAY);
                            }else{
                                textView.setTextColor(Color.BLACK);
                            }

                            return view;
                        }
                    };
                    adapterSecundario.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerSecundario.setAdapter(adapterSecundario);

                    spinnerSecundario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(parent.getItemIdAtPosition(position)!=0) {
                                plantaSeleccionadaSecundaria=Integer.valueOf(numeroPlantas[position]);
                                ArrayAdapter<String> adaptListSecundario = new ArrayAdapter<>(getApplicationContext(), R.layout.listapersonalizada, db.mostrarLimpiadorPlanta(plantaSeleccionadaSecundaria));
                                listaSecundaria.setAdapter(adaptListSecundario);
                                listaSecundaria.setVisibility(View.VISIBLE);

                                listaSecundaria.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        db.dniNombreLimpiador(plantaSeleccionadaSecundaria,(int)parent.getItemIdAtPosition(position));
                                        nombreSecundarioEscogido=db.nombreEscogido();
                                        dniSecundarioEscogido=db.dniEscogido();
                                        nombreSecundario.setText(nombreSecundarioEscogido);
                                    }
                                });
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    /**Metodo que esta hacidao al elemento "boton" del layout respectivo, que confirma el cambio de limpiadores de plantas si cumple con los requisitos*/

    public void cambiarLimpiador(View view) {

        if(!nombrePrincipal.getText().toString().equalsIgnoreCase("") && !nombreSecundario.getText().toString().equalsIgnoreCase("")) {

            Toast.makeText(this, db.updateLimpiador(dniPrincipalEscogido, dniSecundarioEscogido, plantaSeleccionadaPrincipal, plantaSeleccionadaSecundaria), Toast.LENGTH_SHORT).show();
            reiniciarActivity(this);
        }else{
            Toast.makeText(this, "Seleccione dos empleados para continuar", Toast.LENGTH_SHORT).show();
        }
    }

    /**Metodo que esta asociado al elemento "boton" del layout respectivo, que sirve para volver a la actividad respectiva*/

    public void volverGeneral(View view) {

        startActivity(new Intent(this,PersonalGeneral.class));
        super.onBackPressed();

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

    /**Metodo para reiniciar la actividad*/

    public void reiniciarActivity(Activity actividad){
        Intent reinicio=new Intent();
        reinicio.setClass(actividad, actividad.getClass());
        //Le mando un string
        //llamamos a la actividad
        actividad.startActivity(reinicio);
        //finalizamos la actividad actual
        actividad.finish();
    }
}