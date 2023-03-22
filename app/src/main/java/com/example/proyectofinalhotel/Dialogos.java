package com.example.proyectofinalhotel;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.proyectofinalhotel.modelo.GestorBasesDatos;
import com.example.proyectofinalhotel.modelo.Personal;

import java.util.ArrayList;

public class Dialogos extends DialogFragment {

    private Intent intent;

    /**Clase que se crea para posteriormente abrir una ventana emergente con las opciones indicadas en el arrayList lista
     * Depende de las opciones que pulses te lleva a una pantalla u otra, enviandoles un dni que es estatico de la pantalla
     * PersonalGeneral que pertenece a la persona que ha sido seleccionada en el listViewPrincipal*/

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        GestorBasesDatos db=new GestorBasesDatos(getContext());

        ArrayList<String>lista=new ArrayList<String>();

        boolean compruebaBaja=db.comprobarBaja(PersonalGeneral.dni);

        lista.add("Modificar datos");
        lista.add("Dar de baja");

        if(compruebaBaja){
            lista.add("Contratar");
        }

        String[] items = (String[])lista.toArray(new String[lista.size()]);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Selecciona una opcion:")
                .setIcon(R.drawable.ic_baseline_info_24)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which){
                            case 0:
                                if (!compruebaBaja) {
                                    intent = new Intent(getContext(), ModificarDatos.class);
                                    intent.putExtra("dni", PersonalGeneral.dni);
                                    startActivity(intent);
                                    getActivity().finish();
                                }else{
                                    Toast.makeText(getContext(), "Debe darle de alta antes de modificar", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case 1:
                                if(!compruebaBaja) {
                                    intent = new Intent(getContext(), BajaEmpleado.class);
                                    intent.putExtra("dni", PersonalGeneral.dni);
                                    startActivity(intent);
                                    getActivity().finish();
                                }else{
                                    Toast.makeText(getContext(), "Este empleado ya ha sido dado de baja", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case 2:
                                Toast.makeText(getContext(), db.setEstadoPersonal(PersonalGeneral.dni), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getContext(),PersonalGeneral.class));
                                getActivity().finish();
                                break;
                        }
                    }
                });
        return builder.create();
    }
}
