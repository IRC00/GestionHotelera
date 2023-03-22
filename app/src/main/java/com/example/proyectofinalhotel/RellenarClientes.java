package com.example.proyectofinalhotel;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.proyectofinalhotel.modelo.Cliente;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.TestOnly;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Pattern;

public class RellenarClientes extends AppCompatActivity {

    private TextInputLayout nombre;
    private TextInputLayout apellidos;
    private TextInputLayout dni;
    private TextInputLayout telefono;
    private TextInputLayout fecha_entrada;
    private TextInputLayout fecha_salida;
    private TextInputLayout habitacion;
    public static int cont = 0;
    public static ArrayList<Cliente> clientes = new ArrayList<Cliente>();
    public Intent intent;
    public static int habitacionIntent;
    public static int numeroPersonas;
    private int anio;
    private int mes;
    private int dia;
    private static String fechaEntrada;
    private static String fechaSalida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rellenar_clientes);
        super.setTitle(R.string.titulo_rellenarClientes);

        nombre = (TextInputLayout)findViewById(R.id.textInputLayoutNombreCliente);
        apellidos = (TextInputLayout)findViewById(R.id.textInputLayoutApellidosCliente);
        dni = (TextInputLayout)findViewById(R.id.textInputLayoutDniCliente);
        telefono = (TextInputLayout)findViewById(R.id.textInputLayoutTelefonoCliente);
        fecha_entrada = (TextInputLayout)findViewById(R.id.textInputLayoutFechaEntrada);
        fecha_salida = (TextInputLayout)findViewById(R.id.textInputLayoutFechaSalida);
        habitacion = (TextInputLayout)findViewById(R.id.textInputLayoutNHabitacion);

        intent = getIntent();
        if (cont == 0) {
            numeroPersonas = intent.getIntExtra("personas", 0);
            fechaEntrada = "";
            fechaSalida = "";
        } else {
            fechaEntrada = intent.getStringExtra("fechaEntrada");
            fechaSalida = intent.getStringExtra("fechaSalida");
            fecha_entrada.getEditText().setText(fechaEntrada);
            fecha_salida.getEditText().setText(fechaSalida);
        }

        habitacionIntent = intent.getIntExtra("habitacion",0);

        habitacion.getEditText().setText(String.valueOf(habitacionIntent));
        fecha_entrada.setEnabled(false);
        fecha_salida.setEnabled(false);
        habitacion.setEnabled(false);
    }

    /**Metodo que añade el numero de clientes que pusimos en la ventana de ReservarHabitacion a un array antes de
     * meterlos en la base de datos
     */
    public void añadir(View view){

        String dniS = dni.getEditText().getText().toString();
        String nombreS = nombre.getEditText().getText().toString();
        String apellidosS = apellidos.getEditText().getText().toString();
        String telefonoS = telefono.getEditText().getText().toString();
        String fecha_entradaS = fecha_entrada.getEditText().getText().toString();
        String fecha_salidaS = fecha_salida.getEditText().getText().toString();
        String habitacionS = habitacion.getEditText().getText().toString();
        if(cont == 0) {
            fechaEntrada = fecha_entradaS;
            fechaSalida = fecha_salidaS;
        }

        if( dniS!="" && nombreS!="" && apellidosS!="" && telefonoS!="" && fecha_entradaS!="" && fecha_salidaS!="") {
            if(Validaciones.comprobarDNI(dniS) && Validaciones.comprobarNumeroTelefono(telefonoS)) {
                Cliente c = new Cliente(dniS, nombreS, apellidosS, Integer.parseInt(telefonoS), Integer.parseInt(habitacionS), fecha_entradaS, fecha_salidaS, "alta");
                clientes.add(c);
                cont++;
                /**Con esto comprobamos que se vuelva a abrir la ventana de rellenar tantas veces como numero de clientes hayamos puesto en la ventana anterior*/
                if (cont != numeroPersonas) {
                    reiniciarActivity(this);
                } else {
                    Intent info = new Intent(this, InformacionClientesAnadidos.class);
                    info.putExtra("clientes", clientes);
                    info.putExtra("habitacion", habitacionS);
                    startActivity(info);
                    this.finish();
                }
            }else{
                Toast.makeText(this, "El DNI no es valido", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**Metodo que vuelve a lanzar la activity actual*/
    public static void reiniciarActivity(Activity actividad){
        Intent reinicio=new Intent();
        reinicio.setClass(actividad, actividad.getClass());
        reinicio.putExtra("habitacion", habitacionIntent);
        reinicio.putExtra("fechaEntrada", fechaEntrada);
        reinicio.putExtra("fechaSalida", fechaSalida);
        actividad.startActivity(reinicio);
        actividad.finish();
    }

    /**Metodo que rellena la fecha de entrada escogida pulsando el boton del calendario*/
    public void escogerFechaEntrada(View view){

        Calendar calendar=Calendar.getInstance();
        anio=calendar.get(Calendar.YEAR);
        mes=calendar.get(Calendar.MONTH);
        dia=calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if(dayOfMonth>dia) {
                    Toast.makeText(RellenarClientes.this, "El dia tiene que ser menor o igual al dia actual", Toast.LENGTH_SHORT).show();
                }else {
                    String fechaCompleta = dayOfMonth + "/" + (month + 1) + "/" + year;
                    fecha_entrada.getEditText().setText(fechaCompleta);
                }

            }
        },anio,mes,dia);

        datePickerDialog.show();
    }

    /**Metodo que rellena la fecha de salida escogida pulsando el boton del calendario*/
    public void escogerFechaSalida(View view){

        Calendar calendar=Calendar.getInstance();
        anio=calendar.get(Calendar.YEAR);
        mes=calendar.get(Calendar.MONTH);
        dia=calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if(dayOfMonth<=dia) {
                    Toast.makeText(RellenarClientes.this, "El dia tiene que ser mayor que el dia actual", Toast.LENGTH_SHORT).show();
                }else {
                    String fechaCompleta = dayOfMonth + "/" + (month + 1) + "/" + year;
                    fecha_salida.getEditText().setText(fechaCompleta);
                }

            }
        },anio,mes,dia);

        datePickerDialog.show();
    }

    /**Metodo que bloquea el boton atras del telefono*/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**Metodo que vuelve a la ventana de reservarHabitacion si queremos cancelar el registro*/
    public void cancelar(View view){
        Intent intent = new Intent(this,ReservarHabitacion.class);
        startActivity(intent);
        this.finish();
    }

    /** Metodo que cierra el teclado cuando pulsas en la pantalla*/
    public void cerrarTeclado(View vista){
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        inputMethodManager.hideSoftInputFromWindow(vista.getWindowToken(), 0);
    }
}