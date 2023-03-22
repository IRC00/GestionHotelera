package com.example.proyectofinalhotel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectofinalhotel.modelo.Cliente;
import com.example.proyectofinalhotel.modelo.GestorBasesDatos;

import java.util.ArrayList;

public class InformacionClientesAnadidos extends AppCompatActivity {

    private TextView info;
    GestorBasesDatos db;
    public Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_clientes_anadidos);
        super.setTitle(R.string.titulo_infoClientesAnadidos);

        intent = getIntent();

        info = (TextView)findViewById(R.id.textViewInfoClientesAnadidos);

        info.setText(devolverInfo());

        db = new GestorBasesDatos(this);
        db.getWritableDatabase();
    }

    /**Metodo que muestra la informacion de los clientes que vamos a registrar*/
    public String devolverInfo(){
        ArrayList<Cliente> clientes = (ArrayList<Cliente>) intent.getSerializableExtra("clientes");
        String cadena = "Numero de habitacion: " + clientes.get(0).getNumHabitacion();
        for(int i = 0;i<clientes.size();i++){
            cadena += "\n\nDNI: " + clientes.get(i).getDni() + "\nNombre: " + clientes.get(i).getNombre() + "\nApellidos: " + clientes.get(i).getApellidos() +
                    "\nTeléfono: " + clientes.get(i).getTelefono() + "\nFecha de entrada: " + clientes.get(i).getFecha_entrada() + "\nFecha de salida: " + clientes.get(i).getFecha_salida();
        }
        return cadena;
    }

    /**Metodo que añade los clientes a la base de datos*/
    public void añadirClientesBBDD(View view){
        ArrayList<Cliente> clientes = (ArrayList<Cliente>) getIntent().getSerializableExtra("clientes");
        for(int i=0;i<clientes.size();i++){
            if(!db.crearClientes(clientes.get(i))){
                Toast.makeText(this, "Ha ocurrido un error pruebe mas tarde", Toast.LENGTH_SHORT).show();
            }
        }
        db.cambiarEstadoHabitacionOcupada(getIntent().getStringExtra("habitacion"));
        Toast.makeText(this, "Los clientes se han registrado correctamente", Toast.LENGTH_SHORT).show();
        this.finish();
        Intent intent = new Intent(this,InformacionClientes.class);
        startActivity(intent);
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
}