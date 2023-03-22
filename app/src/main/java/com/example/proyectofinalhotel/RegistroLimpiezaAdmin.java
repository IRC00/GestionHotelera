package com.example.proyectofinalhotel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.proyectofinalhotel.modelo.GestorBasesDatos;

import java.util.ArrayList;

public class RegistroLimpiezaAdmin extends AppCompatActivity {

    /**Declaracion de los elementos y variables*/

    private ListView listaRegistros;
    GestorBasesDatos db;
    ArrayList<String>lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /**Inicializacion de los elementos y variables*/

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_limpieza_admin);
        listaRegistros=(ListView)findViewById(R.id.listViewRegistroLimpiezaAdmin);
        db=new GestorBasesDatos(this);
        db.getWritableDatabase();
        lista=new ArrayList<>();

        super.setTitle(R.string.titulo_registrosLimpieza);

        /**Creo un adaptar para insertar los datos resultantes de la base de datos de los registros generales de limpieza que se han ido haciendo*/

        ArrayAdapter<String>adapter=new ArrayAdapter<>(this,R.layout.listapersonalizada,db.listaRegistrosGenerales());
        listaRegistros.setAdapter(adapter);

    }


    /**Metodo que sirve para retroceder a la pantalla indicada*/

    public void retroceder(View view) {
        startActivity(new Intent(this,PersonalGeneral.class));
        finish();
    }
}