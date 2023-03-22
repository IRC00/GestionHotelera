package com.example.proyectofinalhotel;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectofinalhotel.modelo.GestorBasesDatos;
import com.example.proyectofinalhotel.modelo.Personal;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Pattern;

public class ModificarDatos extends AppCompatActivity implements View.OnClickListener {


    /**Declaracion de elementos y de los datos a usar*/

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
    private Button bDni;
    private Button bNombre;
    private Button bApellidos;
    private Button bContrasenia;
    private Button bTelefono;
    private Button bImagenCalendario;
    private Button bPuesto;
    private Button bPlanta;
    private Button modificar;
    private Button terminar;
    Intent intent;
    View fragmentDatos;
    View fragmentDatosAdmin;
    private String dniPrincipal;
    private ArrayList<String>lista;
    private TextView nombrePersona;
    private Button bCancelar;
    private String dniIncial;
    private String nombreIncial;
    private String apellidosIncial;
    private String contraseniaIncial;
    private String telefonoIncial;
    private String calendarioIncial;
    private String puestoIncial;
    private String plantaIncial;
    SharedPreferences shared;
    SharedPreferences.Editor editor;
    private String tipoVista="";
    private String tipoPerfil="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_datos);


        /**Inicializacion de los datos y elementos declarados anteriormente*/

        tDni=(TextInputLayout)findViewById(R.id.textInputLayoutDni);
        tNombre=(TextInputLayout)findViewById(R.id.textInputLayoutNombre);
        tApellidos=(TextInputLayout)findViewById(R.id.textInputLayoutApellidos);
        tContrasenia=(TextInputLayout)findViewById(R.id.textInputLayoutContrasenia);
        tTelefono=(TextInputLayout)findViewById(R.id.textInputLayoutTelefono);
        tPuesto=(TextInputLayout)findViewById(R.id.textInputLayoutPuesto);
        tPlanta=(TextInputLayout)findViewById(R.id.textInputLayoutPlanta);
        imagenCalendario=(ImageView)findViewById(R.id.imageViewCalendario);
        vistaFecha=(TextView)findViewById(R.id.textViewVistaFecha);
        nombrePersona=(TextView)findViewById(R.id.textViewNombrePersona);
        db=new GestorBasesDatos(this);
        //db.getWritableDatabase();
        bDni=(Button)findViewById(R.id.buttonCambiarDni);
        bNombre=(Button)findViewById(R.id.buttonCambiarNombre);
        bApellidos=(Button)findViewById(R.id.buttonCambiarApellido);
        bContrasenia=(Button)findViewById(R.id.buttonCambiarContrasenia);
        bTelefono=(Button)findViewById(R.id.buttonCambiarTelefono);
        bImagenCalendario=(Button)findViewById(R.id.buttonCambiarFecha);
        bPuesto=(Button)findViewById(R.id.buttonCambiarPuesto);
        bPlanta=(Button)findViewById(R.id.buttonCambiarPlanta);
        modificar=(Button)findViewById(R.id.buttonModificar);
        terminar=(Button)findViewById(R.id.buttonTerminar);
        fragmentDatos=(View)findViewById(R.id.fragmentInserta);
        fragmentDatosAdmin=(View)findViewById(R.id.fragmentInsertaAdmin);
        bCancelar=(Button)findViewById(R.id.buttonVolver);

        /**Asocio los botones al evento onClickListener*/

        bDni.setOnClickListener(this);
        bNombre.setOnClickListener(this);
        bApellidos.setOnClickListener(this);
        bContrasenia.setOnClickListener(this);
        bTelefono.setOnClickListener(this);
        bImagenCalendario.setOnClickListener(this);
        bPuesto.setOnClickListener(this);
        bPlanta.setOnClickListener(this);
        modificar.setOnClickListener(this);
        bCancelar.setOnClickListener(this);
        lista=new ArrayList<>();

        /**El shared preferences para que en caso se modifique el dni de la persona respectiva, tambien se modifique en el shared*/

        shared=getSharedPreferences("Login.txt", Context.MODE_PRIVATE);
        super.setTitle(R.string.titulo_modificarDatos);

        /**Debido a que esta pantalla es compartida por varios perfiles, se debe comprobar desde que perfil se ha accedido a esta pantalla para
         * poder retroceder a las pantallas respectivas de ese perfil
         * Tambien se comprueba que si el dni es nulo, se accede con el dni del sharedPreferences, por lo tanto se muestran los datos preparados para
         * ser de perfil, en caso contrario se mostraran los datos a modificar como administrador*/

        try {

            intent = getIntent();

            if(intent.getStringExtra("tipo_perfil")!=null) {

                if (intent.getStringExtra("tipo_perfil").equalsIgnoreCase("limpieza")) {
                    tipoPerfil = "limpieza";
                } else if (intent.getStringExtra("tipo_perfil").equalsIgnoreCase("recepcionista")) {
                    tipoPerfil = "recepcionista";
                } else if (intent.getStringExtra("tipo_perfil").equalsIgnoreCase("administrador")) {
                    tipoPerfil = "administrador";
                }
            }else{
                tipoPerfil = "administrador";
            }


            if(intent.getStringExtra("dni")!=null){
                dniPrincipal=intent.getStringExtra("dni");
                updateUIInical();
            }else {

                if (intent.getStringExtra("vista").equalsIgnoreCase("perfil")) {
                    dniPrincipal = shared.getString("dni", "nulo");
                    updateUIPerfil();
                } else if (intent.getStringExtra("vista").equalsIgnoreCase("modificar")) {
                    dniPrincipal = intent.getStringExtra("dniReinicio");
                    tipoVista="perfil";
                    updateUIModificar();
                }
            }


        }catch(Exception e){
        }
    }

    /**Asocia los eventos a los botones para realizar diferentes accion segun el boton que se pulse
     * Los botones que estan asociados a los campos se usan para indicar que campo quieres desbloquear para proceder a modificarlo*/

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.buttonModificar:
                reiniciarActivity(this);
                break;
            case R.id.buttonVolver:
                switch (tipoPerfil){
                    case "limpieza":
                        startActivity(new Intent(this,RegistrosLimpieza.class));
                        finish();
                        break;
                    case "recepcionista":
                        startActivity(new Intent(this,InformacionClientes.class));
                        finish();
                        break;
                    case "administrador":
                        startActivity(new Intent(this,PersonalGeneral.class));
                        finish();
                        break;
                }
                break;
            case R.id.buttonCambiarDni:
                tDni.setEnabled(true);
                terminar.setEnabled(true);
                break;
            case R.id.buttonCambiarNombre:
                tNombre.setEnabled(true);
                terminar.setEnabled(true);
                break;
            case R.id.buttonCambiarApellido:
                tApellidos.setEnabled(true);
                terminar.setEnabled(true);
                break;
            case R.id.buttonCambiarContrasenia:
                tContrasenia.setEnabled(true);
                terminar.setEnabled(true);
                break;
            case R.id.buttonCambiarTelefono:
                tTelefono.setEnabled(true);
                terminar.setEnabled(true);
                break;
            case R.id.buttonCambiarFecha:
                imagenCalendario.setEnabled(true);
                terminar.setEnabled(true);
                break;
            case R.id.buttonCambiarPuesto:
                tPuesto.setEnabled(true);
                terminar.setEnabled(true);
                break;
            case R.id.buttonCambiarPlanta:
                tPlanta.setEnabled(true);
                terminar.setEnabled(true);
                break;
        }


    }

    /**Metodo que esta asociado a un elemento "boton" "terminar" del layout respectivo que, en caso de que se cumplen con las
     * con los requisitos, se procedera a cambiar los datos en la base de datos, y en caso de que se proceda desde perfil, el sharedPreferences
     * @see Validaciones
     * Las comprobaciones se procede tanto si se pulsa un boton para desbloquear y comprobar si se ha moficado o no el dato respectivo, o si no cumplen con las
     * validaciones de la clase mencionada en el apartado anteior
     **/

    public void confirmarModificacion(View view) {

        String dni="nulo";
        String nombre="nulo";
        String apellido="nulo";
        String contrasenia="nulo";
        String telefono="0";
        String puesto="nulo";
        String planta="-1";
        String fecha="nulo";
        String error="";

        if(tDni.getEditText().isEnabled()){
            if(!tDni.getEditText().getText().toString().equalsIgnoreCase(dniIncial)) {
                if (Validaciones.comprobarDNI(tDni.getEditText().getText().toString())) {
                    dni = tDni.getEditText().getText().toString().toUpperCase();
                } else {
                    error+="DNI/NIE ";
                }
            }else{
                error+="DNI/NIE ";
            }
        }
        if(tNombre.getEditText().isEnabled()){
            if(!tNombre.getEditText().getText().toString().equalsIgnoreCase(nombreIncial)) {
                nombre = tNombre.getEditText().getText().toString();
            }else{
                error+="NOMBRE ";
            }
        }
        if(tApellidos.getEditText().isEnabled()){
            if(!tApellidos.getEditText().getText().toString().equalsIgnoreCase(apellidosIncial)) {
                apellido = tApellidos.getEditText().getText().toString();
            }else{
                error+="APELLIDOS ";
            }
        }
        if(tContrasenia.getEditText().isEnabled()){
            if(!tContrasenia.getEditText().getText().toString().equalsIgnoreCase(contraseniaIncial)) {
                if(Validaciones.comprobarContrasenia(tContrasenia.getEditText().getText().toString())) {
                    contrasenia = tContrasenia.getEditText().getText().toString();
                }else{
                    Toast.makeText(this, "Contraseña incorrecta: \n1 Numero \n1 mayúscula \nLoguitud de 6 a 15 caracteres", Toast.LENGTH_LONG).show();
                    error+="CONTRASEÑA ";
                }
            }else{
                error+="CONTRASEÑA ";
            }
        }
        if(tTelefono.getEditText().isEnabled()){
            if(!tTelefono.getEditText().getText().toString().equalsIgnoreCase(telefonoIncial)) {
                if(Validaciones.comprobarNumeroTelefono(tTelefono.getEditText().getText().toString())) {
                    telefono = tTelefono.getEditText().getText().toString();
                }
                else{
                    error+="TELEFONO ";
                }
            }else{
                error+="TELEFONO ";
            }
        }
        if(tPuesto.getEditText().isEnabled()) {
            if (tPuesto.getEditText().getText().toString().equalsIgnoreCase("limpieza") || tPuesto.getEditText().getText().toString().equalsIgnoreCase("recepcion")
                    || tPuesto.getEditText().getText().toString().equalsIgnoreCase("camarero") ||
                    tPuesto.getEditText().getText().toString().equalsIgnoreCase("cocinero") || tPuesto.getEditText().getText().toString().equalsIgnoreCase("mantenimiento")) {
                if(!tPuesto.getEditText().getText().toString().equalsIgnoreCase(puestoIncial)) {
                    puesto = tPuesto.getEditText().getText().toString().toLowerCase();
                }else{
                    error+="PUESTO ";
                }
            } else {
                Toast.makeText(this, "Las opciones correctas son: limpieza, recepcion, camarero, cocinero, mantenimiento", Toast.LENGTH_LONG).show();
            }
        }
        if(tPlanta.getEditText().isEnabled()){
            if(!tPlanta.getEditText().getText().toString().equalsIgnoreCase(plantaIncial)) {
                planta = tPlanta.getEditText().getText().toString();
            }else{
                error+="PLANTA ";
            }
        }

        if(imagenCalendario.isEnabled()){
            if(!vistaFecha.getText().toString().equalsIgnoreCase(calendarioIncial)) {
                fecha = vistaFecha.getText().toString();
            }else{
                error+="FECHA ";
            }
        }


        if(dni!="" && nombre!="" && apellido!="" && contrasenia!="" && telefono!="" && puesto!="" && planta!="" && vistaFecha.getText().toString()!=""){
            if(error.equalsIgnoreCase("")) {
                Toast.makeText(this, db.modificarDatos(dniPrincipal, dni, nombre, apellido, Integer.parseInt(telefono), contrasenia, puesto, Integer.valueOf(planta), fecha), Toast.LENGTH_SHORT).show();
                if(comprobarDniDistinto(dni,shared.getString("dni","nulo"))){
                    editor=shared.edit();
                    editor.putString("dni",dni);
                    editor.commit();
                }
            }else{
                Toast.makeText(this, "No han sido modificados los siguientes campos:\n" + error, Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "Compruebe que todos los datos esten completos", Toast.LENGTH_SHORT).show();
        }

    }

    /**Metodo que esta asociado a un elemento ImageView de calendario en el cual si pulsas, te abre una ventana emergente con un calendario paa seleccionar la fecha*/

    public void escogerFecha(View view){

        Calendar calendar=Calendar.getInstance();
        anio=calendar.get(Calendar.YEAR);
        mes=calendar.get(Calendar.MONTH);
        dia=calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if(dayOfMonth>dia) {
                    Toast.makeText(ModificarDatos.this, "El dia tiene que ser menor o igual al dia actual", Toast.LENGTH_SHORT).show();
                }else {
                    String fechaCompleta = dayOfMonth + "/" + (month + 1) + "/" + year;
                    vistaFecha.setText(fechaCompleta);
                }

            }
        },anio,mes,dia);

        datePickerDialog.show();
    }


    /**Metodo que se inicia al principio de esta actividad, que rellena los campos de con los datos resultantes de la base de datos que a su vez se guaran en una
     * arrayList, poniendo todos los campos como bloqueados, los datos son procedidos del dni que ha accedido a esta pantalla
     * este dni esta guardado en TODO dniPrincipal*/
    private void updateUIInical(){


        tDni.setEnabled(false);
        tNombre.setEnabled(false);
        tApellidos.setEnabled(false);
        tContrasenia.setEnabled(false);
        tTelefono.setEnabled(false);
        tPuesto.setEnabled(false);
        tPlanta.setEnabled(false);
        imagenCalendario.setEnabled(false);
        lista=db.buscarEmpleado(dniPrincipal);
       nombrePersona.setText(lista.get(2) + " " + lista.get(3));
        tDni.getEditText().setText(lista.get(1));
        dniIncial=lista.get(1);
        tNombre.getEditText().setText(lista.get(2));
        nombreIncial=lista.get(2);
        tApellidos.getEditText().setText(lista.get(3));
        apellidosIncial=lista.get(3);
        tTelefono.getEditText().setText(lista.get(4));
        telefonoIncial=lista.get(4);
        tContrasenia.getEditText().setText(lista.get(5));
        contraseniaIncial=lista.get(5);
        tPuesto.getEditText().setText(lista.get(6));
        puestoIncial=lista.get(6);
        tPlanta.getEditText().setText(lista.get(7));
        plantaIncial=lista.get(7);
        vistaFecha.setText(lista.get(8));
        calendarioIncial=lista.get(8);
        terminar.setEnabled(false);

    }


    /**Pantalla que se cambia cuando el usuario pulsa la opcion "perfil" del menu, el cual permite ver solo sus datos, por lo tanto se usa el fragment insertDatos,
     * se llama al metodo UpdateUIInicila, con algunas mofificaciones como un boton de modificar, y los campos mas grandes, ocultando los botones antes*/
    private void updateUIPerfil(){

        ViewGroup.LayoutParams params=fragmentDatos.getLayoutParams();
        params.width= ViewGroup.LayoutParams.MATCH_PARENT;
        fragmentDatos.setLayoutParams(params);

        fragmentDatosAdmin.setVisibility(View.INVISIBLE);
        bImagenCalendario.setVisibility(View.INVISIBLE);
        bDni.setVisibility(View.INVISIBLE);
        bNombre.setVisibility(View.INVISIBLE);
        bApellidos.setVisibility(View.INVISIBLE);
        bContrasenia.setVisibility(View.INVISIBLE);
        bTelefono.setVisibility(View.INVISIBLE);
        bImagenCalendario.setVisibility(View.INVISIBLE);
        bPuesto.setVisibility(View.INVISIBLE);
        bPlanta.setVisibility(View.INVISIBLE);
        modificar.setVisibility(View.VISIBLE);
        terminar.setVisibility(View.INVISIBLE);
        bPlanta.setVisibility(View.INVISIBLE);
        bPuesto.setVisibility(View.INVISIBLE);
        super.setTitle("Perfil");
        updateUIInical();



    }

    /**Pantalla que se cambiar al pulsar el boton modificar en el apartado perfil, donde vienen los mismos datos que en UpdateUIPerfil pero añadiendo los botones
     * para seleccionar que campos modificar*/
    private void updateUIModificar(){
        fragmentDatosAdmin.setVisibility(View.INVISIBLE);
        bImagenCalendario.setVisibility(View.INVISIBLE);
        bPuesto.setVisibility(View.INVISIBLE);
        bPlanta.setVisibility(View.INVISIBLE);
        terminar.setVisibility(View.VISIBLE);
        super.setTitle("Perfil");
        updateUIInical();
    }


    /**Metodo que sirve para reiniciar la actividad enviandoles algunos datos como el dni de la persona que ha accedido a esta pantalla, y el tipo de perfil*/
    public void reiniciarActivity(Activity actividad){
        Intent reinicio=new Intent();
        reinicio.setClass(actividad, actividad.getClass());
        //Le mando un string
        reinicio.putExtra("vista","modificar");
        reinicio.putExtra("dniReinicio",dniPrincipal);
        reinicio.putExtra("tipo_perfil",tipoPerfil);
        //llamamos a la actividad
        actividad.startActivity(reinicio);
        //finalizamos la actividad actual
        actividad.finish();
    }


    /**Metodo que comprueba que el dni del campo dni sea diferente al dni del shared, al igual que el campo no tenga el valor nulo y provenga de la opcion perfil para
     * proceder el cambio en el shared, si no se hace esta comprobacion, siempre que se modifique el dni de una persona desde el administrador, el shared tambien se modifica */
    private boolean comprobarDniDistinto(String dniEdit,String dniShared){
        if(!dniShared.equalsIgnoreCase(dniEdit) && !dniEdit.equalsIgnoreCase("nulo") && tipoVista.equalsIgnoreCase("perfil")){
            return true;
        }
        return false;
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