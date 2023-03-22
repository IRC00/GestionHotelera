package com.example.proyectofinalhotel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.proyectofinalhotel.modelo.GestorBasesDatos;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /**Declaracion de elementos y variables*/

    private TextInputLayout tDni;
    private TextInputLayout tContrasenia;
    GestorBasesDatos db;
    Intent intent;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        super.setTitle("Inicio de sesión");

        /**Inicializacion de las variables
         * Usamos un sharedPreferences para indicar el dni del usuario que inicia sesion, para usarlo posteriormente en opciones como modificar datos entrando en el perfil*/

        tDni=(TextInputLayout)findViewById(R.id.textInputLayoutDniIncio);
        tContrasenia=(TextInputLayout)findViewById(R.id.textInputLayoutContraseniaInicio);
        db=new GestorBasesDatos(this);
        db.getWritableDatabase();
        pref = getSharedPreferences("Login.txt",Context.MODE_PRIVATE);

    }

    /**Metodo que captura el evento del movil que muestra/oculta el teclado, en este caso lo usamos para ocultar el teclado*/

    public void cerrarTeclado(View vista){
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        inputMethodManager.hideSoftInputFromWindow(vista.getWindowToken(), 0);
    }



    /**Metodo que esta asocidado a un elemento "boton" del layour respectivo, el cual confirma el inicio de sesion y
     * te lleva a las pantallas dependiendo del perfil que pertenezca
     * en el caso de que se cumplan las comprobaciones y sea correctas, se procedera a escribir en el sharedPreferences el dni introducido*/
    public void accederUsuario(View view) {

        String dni=tDni.getEditText().getText().toString().toUpperCase();
        String contrasenia=tContrasenia.getEditText().getText().toString();
        boolean error = true;
        editor=pref.edit();

        if(!dni.equalsIgnoreCase("") && !contrasenia.equalsIgnoreCase("")) {

            switch (db.tipoUsuario(dni, contrasenia)) {
                case 0:
                    intent = new Intent(this, PersonalGeneral.class);
                    startActivity(intent);
                    finish();
                    break;
                case 1:
                    intent = new Intent(this, RegistrosLimpieza.class);
                    startActivity(intent);
                    finish();
                    break;
                case 2:
                    intent = new Intent(this, InformacionClientes.class);
                    startActivity(intent);
                    finish();
                    break;
                case 3:
                    error = false;
                    Toast.makeText(this, "Este usuario ha sido dado de baja, no tiene permiso para acceder", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    error = false;
                    Toast.makeText(this, "No tienes permisos para acceder", Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    error = false;
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                    break;
                case 6:
                    error = false;
                    Toast.makeText(this, "DNI/Contraseña incorrectas", Toast.LENGTH_SHORT).show();
                    break;
            }

        }


        if(error){
            editor.putString("dni",dni);
            editor.commit();
        }


    }
}