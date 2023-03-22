package com.example.proyectofinalhotel;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectofinalhotel.modelo.GestorBasesDatos;
import com.example.proyectofinalhotel.modelo.Personal;
import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.regex.Pattern;

public class CrearEmpleado extends AppCompatActivity {

    /**Declaracion de los elementos y variables*/

    private TextInputLayout tDni;
    private TextInputLayout tNombre;
    private TextInputLayout tApellidos;
    private TextInputLayout tContrasenia;
    private TextInputLayout tTelefono;
    private ImageView imagenCalendario;
    private TextView vistaFecha;
    private TextInputLayout tPuesto;
    private TextInputLayout tPlanta;
    private int anio;
    private int mes;
    private int dia;
    private Personal personal;
    GestorBasesDatos db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_empleado);


        /**Inicializacion de los elementos y variables*/

        tDni=(TextInputLayout)findViewById(R.id.textInputLayoutDni);
        tNombre=(TextInputLayout)findViewById(R.id.textInputLayoutNombre);
        tApellidos=(TextInputLayout)findViewById(R.id.textInputLayoutApellidos);
        tContrasenia=(TextInputLayout)findViewById(R.id.textInputLayoutContrasenia);
        tTelefono=(TextInputLayout)findViewById(R.id.textInputLayoutTelefono);
        tPuesto=(TextInputLayout)findViewById(R.id.textInputLayoutPuesto);
        tPlanta=(TextInputLayout)findViewById(R.id.textInputLayoutPlanta);
        imagenCalendario=(ImageView)findViewById(R.id.imageViewCalendario);
        vistaFecha=(TextView)findViewById(R.id.textViewVistaFecha);
        db=new GestorBasesDatos(this);
       // db.getWritableDatabase();
        super.setTitle(R.string.titulo_crearEmpleado);
    }

    /**Metodo que esta asociado al elemento "boton" "aceptar" de la actividad respectiva, el cual confirma el insertar un nuevo personal en la base de datos si cumple
     * las comprobaciones realizadas antes */

    public void insertarEmpleado(View view) {

        String dni=tDni.getEditText().getText().toString().toUpperCase();
        String nombre=tNombre.getEditText().getText().toString();
        String apellido=tApellidos.getEditText().getText().toString();
        String contrasenia=tContrasenia.getEditText().getText().toString();
        String telefono=tTelefono.getEditText().getText().toString();
        String puesto=tPuesto.getEditText().getText().toString().toLowerCase();
        String planta=tPlanta.getEditText().getText().toString();


        if(dni!="" && nombre!="" && apellido!="" && contrasenia!="" && telefono!="" && puesto!="" && planta!="" && vistaFecha.getText().toString()!=""){
            if(Validaciones.comprobarDNI(dni)) {
                if(Validaciones.comprobarNumeroTelefono(telefono)) {
                    if(Validaciones.comprobarContrasenia(contrasenia)) {
                        if (puesto.equalsIgnoreCase("limpieza") || puesto.equalsIgnoreCase("recepcion") || puesto.equalsIgnoreCase("camarero") ||
                                puesto.equalsIgnoreCase("cocinero") || puesto.equalsIgnoreCase("mantenimiento")) {
                            personal = new Personal(dni, nombre, apellido, Integer.parseInt(telefono), contrasenia, puesto, Integer.valueOf(planta), vistaFecha.getText().toString(), "activo");
                            Toast.makeText(this, db.crearPersonal(personal), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this,PersonalGeneral.class));
                            finish();
                        } else {
                            Toast.makeText(this, "Las opciones correctas son: limpieza, recepcion, camarero, cocinero, mantenimiento", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(this, "Contraseña incorrecta: \n1 Numero \n1 mayúscula \nLoguitud de 6 a 15 caracteres", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(this, "El telefono debe tener 9 numeros", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "Compruebe que el DNI/NIE este correcto", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Compruebe que todos los datos esten completos", Toast.LENGTH_SHORT).show();
        }

    }

    /**Metodo que esta asociado a un "imagenView" del fragment "InsertaDatosAdmin", la cual habre una ventana emergente con un calendario para escoger la fecha deseada*/

    public void escogerFecha(View view){

        Calendar calendar=Calendar.getInstance();
        anio=calendar.get(Calendar.YEAR);
        mes=calendar.get(Calendar.MONTH);
        dia=calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if(dayOfMonth>dia) {
                    Toast.makeText(CrearEmpleado.this, "El dia tiene que ser menor o igual al dia actual", Toast.LENGTH_SHORT).show();
                }else {
                    String fechaCompleta = dayOfMonth + "/" + (month + 1) + "/" + year;
                    vistaFecha.setText(fechaCompleta);
                }

            }
        },anio,mes,dia);

        datePickerDialog.show();
    }

    /**Metodo que esta asociado a un elemento "boton" "volver" del layour respectivo, el cual sirve para ir a la pantalla indicada*/

    public void cancelar(View view) {
        startActivity(new Intent(this,PersonalGeneral.class));
        finish();
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
}