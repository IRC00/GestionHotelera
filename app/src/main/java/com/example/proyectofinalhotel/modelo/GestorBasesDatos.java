package com.example.proyectofinalhotel.modelo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Random;


public class GestorBasesDatos extends SQLiteOpenHelper {

    private static String nombreBBDD="hotel.db";
    private static String tablaClientes="clientes";
    private static String tablaPlantas="plantas";
    private static String tablaPersonal="personal";
    private static String tablaHabitaciones="habitaciones";
    private static String tablaRegistrosLimpieza="registro_limpieza";
    private String dni;
    private String nombre;

    public GestorBasesDatos(@Nullable Context context) {
        super(context, nombreBBDD, null, 1);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s (num_planta INTEGER PRIMARY KEY,descripcion TEXT);",tablaPlantas));

        db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s (id INTEGER PRIMARY KEY AUTOINCREMENT,dni TEXT UNIQUE,nombre TEXT,apellidos TEXT," +
                "telefono INTEGER UNIQUE,contrasenia TEXT,puesto TEXT,numero_planta INTEGER,fecha_incorporacion TEXT,estado TEXT," +
                "CONSTRAINT fk_numPlanta_Personal FOREIGN KEY (numero_planta) REFERENCES plantas (num_planta));",tablaPersonal));

        db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s (num_habitacion INTEGER PRIMARY KEY,num_personas INTEGER,disponibilidad TEXT,numero_planta INTEGER, estado_limpieza TEXT, " +
                "CONSTRAINT fk_numPlanta_Habitacion FOREIGN KEY (numero_planta) REFERENCES plantas (num_planta));",tablaHabitaciones));

        db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s (id INTEGER PRIMARY KEY AUTOINCREMENT,dni TEXT UNIQUE,nombre TEXT,apellidos TEXT," +
                "telefono INTEGER UNIQUE,numero_habitacion INTEGER,fecha_entrada TEXT,fecha_salida TEXT,estado TEXT, " +
                "CONSTRAINT fk_numHab_Clientes FOREIGN KEY (numero_habitacion) REFERENCES habitaciones (num_habitacion));",tablaClientes));

        db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s (num_registro INTEGER PRIMARY KEY AUTOINCREMENT, numero_habitacion INTEGER, numero_planta INTEGER,dni_personal TEXT, estado_limpieza TEXT, " +
                "CONSTRAINT fk_numHabitacion_Registro FOREIGN KEY (numero_habitacion) REFERENCES habitaciones (num_habitacion)," +
                "CONSTRAINT fk_numeroPlanta_Registro FOREIGN KEY (numero_planta) REFERENCES plantas (num_planta));",tablaRegistrosLimpieza));

        crearPlantasIniciales(db);
        crearHabitacionesIniciales(db);
        crearPerfilesIniciales(db);
        registrosIniciales(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**Metodo que sirve para activar las restricciones de la clave foranea, sin este metodo ignora las claves foraneas*/

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    public void registrosIniciales(SQLiteDatabase db){


        db.execSQL(String.format("INSERT OR IGNORE INTO %s(dni,nombre,apellidos,telefono,contrasenia,puesto,numero_planta,fecha_incorporacion,estado)" +
                " VALUES ('X1234564T','Darius','Daniel',910000011,'limpiezA1','limpieza',1,'12/10/2021','activo');",tablaPersonal));

        db.execSQL(String.format("INSERT OR IGNORE INTO %s(dni,nombre,apellidos,telefono,contrasenia,puesto,numero_planta,fecha_incorporacion,estado)" +
                " VALUES ('X4324432R','Maria','Teresa',910000012,'limpiezA2','limpieza',1,'12/10/2021','activo');",tablaPersonal));

        db.execSQL("INSERT OR IGNORE INTO registro_limpieza (numero_habitacion,numero_planta,dni_personal,estado_limpieza) VALUES(110,1,'X1234564T','limpio')");
        db.execSQL("INSERT OR IGNORE INTO registro_limpieza (numero_habitacion,numero_planta,dni_personal,estado_limpieza) VALUES(110,1,'X4324432R','limpio')");
    }

    /**Inserto en la base de datos en la tabla personal los datos que tiene el objeto personal pasado por parametro*/

    public String crearPersonal(Personal personal){
        SQLiteDatabase db=this.getWritableDatabase();
        try{
            db.execSQL(String.format("INSERT INTO %s(dni,nombre,apellidos,telefono,contrasenia,puesto,numero_planta,fecha_incorporacion,estado) VALUES ('"+personal.getDni()+"','"+personal.getNombre()+"','"+personal.getApellidos() +"',"
                    + personal.getTelefono() + ",'"+personal.getContrasenia()+ "','"+personal.getPuesto()+"',"+personal.getNumPanta()+",'"+personal.getFecha_incor()+"','"+personal.getEstado()+"');",tablaPersonal));
            db.close();
            return "Datos correctamente introducidos";
        }catch (SQLiteException ex){
            return "Error en la inserccion de los datos";
        }


    }


    /**Inserto en la base de datos en la tabla clientes los datos que tiene el objeto cliente pasodo por parametro*/
    public boolean crearClientes(Cliente cliente){
        SQLiteDatabase db=this.getWritableDatabase();

        try{
            db.execSQL(String.format("INSERT into %s(dni,nombre,apellidos,telefono,numero_habitacion,fecha_entrada,fecha_salida,estado) VALUES ('"+cliente.getDni()+
                    "','"+cliente.getNombre()+"','"+cliente.getApellidos()+"'," + cliente.getTelefono() + "," +cliente.getNumHabitacion()+",'"+cliente.getFecha_entrada()+"','"+cliente.getFecha_salida()+"','"+cliente.getEstado()+"');",tablaClientes));
            return true;
        }catch (SQLiteException sqLiteException){
            return false;
        }
    }

    /**Inserto en la base de datos en la tabla registrosLimpieza los datos pasados por parametros, que se llevara en cuenta los datos de limpieza que ha realizado un personal de limpieza*/

    public String crearRegistros(int numHab,int numPlanta,String dni, String estadoLimpieza){
        SQLiteDatabase db=this.getWritableDatabase();

        try{
            db.execSQL(String.format("INSERT INTO %s(numero_habitacion,numero_planta,dni_personal, estado_limpieza) VALUES (" + numHab + "," + numPlanta + ",'"+dni+"','" + estadoLimpieza + "');",tablaRegistrosLimpieza));
            return "registro creado";
        }catch (SQLiteException sqlex){
            return "datos no insertados " + sqlex.getLocalizedMessage();
        }
    }
    


    /**Metodo que se ejecuta al crear la base de datos con unos valores iniciales, ya que son unos datos estaticos*/

    public void crearPlantasIniciales(SQLiteDatabase db){

        for(int i=0;i<=5;i++){
            if(i==0){
                db.execSQL(String.format("INSERT OR IGNORE INTO %s (num_planta,descripcion) VALUES ("+i+",'recepcion');",tablaPlantas));
            }else {
                db.execSQL(String.format("INSERT OR IGNORE INTO %s (num_planta,descripcion) VALUES (" + i + ",'planta" + i + "');", tablaPlantas));
            }
        }
    }

    /**Metodo que muestra los datos segun el puesto del personal mediante un filtro que se le pasa por parametro
     * return ArrayList con los datos de cada empleado*/

    public ArrayList<String>mostrarPersonal(String filtro){

        ArrayList<String>lista=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        int cont=0;
        String cadena="";
        Cursor cursor=null;

        if(filtro.equalsIgnoreCase("todos")) {
            cursor = db.rawQuery(String.format("SELECT nombre,apellidos,puesto FROM %s where puesto<>'administrador' AND estado='activo'", tablaPersonal), null);
        }else if(filtro.equalsIgnoreCase("limpiadores")){
            cursor = db.rawQuery(String.format("SELECT nombre,apellidos,puesto FROM %s where puesto='limpieza' AND estado='activo'", tablaPersonal), null);
        }else if(filtro.equalsIgnoreCase("personal")){
            cursor = db.rawQuery(String.format("SELECT nombre,apellidos,puesto FROM %s where puesto<>'limpieza' and puesto<>'administrador' AND estado='activo'", tablaPersonal), null);
        }else if(filtro.equalsIgnoreCase("inactivos")) {
            cursor = db.rawQuery(String.format("SELECT nombre,apellidos,puesto FROM %s where estado='baja'", tablaPersonal), null);
        }
        while(cursor.moveToNext()){
            cont++;
            cadena="";
            cadena+=cont+"     ";
            for(int i=0;i<cursor.getColumnCount();i++) {
                switch (cursor.getType(i)) {
                        case Cursor.FIELD_TYPE_STRING:
                            cadena +=cursor.getString(i) + "       ";
                            break;
                        case Cursor.FIELD_TYPE_INTEGER:
                            cadena += cursor.getInt(i) +"     ";
                            break;

                }
            }

            if(!cadena.equalsIgnoreCase("")){
                lista.add(cadena);
            }
        }

        if(cadena.equalsIgnoreCase("")){
            lista.add("No hay datos que mostrar");
        }

        db.close();
        return lista;

    }

    /**Metodo que se compagina con el metodo mostrarPersonal, en el cual muestra los datos de un solo empleado en el cual se basa en el numero que se le pasa por parametro y el filtro
     * parametro num es un contador que su valor corresponde a la posicion que ocupa en el ListView a la hora de pinchar sobre otro
     * parametro filtro que sus empleados cambia segun el tipo de puesto que tienen, por lo tanto el contador tambien difiere
     * return ArrayList que contiene un cadena con los datos que le corresponde a ese empleado*/

    public ArrayList empleadoEscogido(int num,String filtro){
        int cont=0;
        ArrayList<String>lista=new ArrayList<>();
        SQLiteDatabase read=this.getReadableDatabase();
        String cadena="";
        Cursor cursor=null;

        if(filtro.equalsIgnoreCase("todos")) {
            cursor = read.rawQuery(String.format("SELECT*FROM %s where puesto<>'administrador' AND estado='activo'", tablaPersonal), null);
        }else if(filtro.equalsIgnoreCase("limpiadores")){
            cursor = read.rawQuery(String.format("SELECT*FROM %s where puesto='limpieza' AND estado='activo'", tablaPersonal), null);
        }else if(filtro.equalsIgnoreCase("personal")){
            cursor = read.rawQuery(String.format("SELECT*FROM %s where puesto<>'limpieza' and puesto<>'administrador' AND estado='activo'", tablaPersonal), null);
        }else if(filtro.equalsIgnoreCase("inactivos")) {
            cursor = read.rawQuery(String.format("SELECT * FROM %s where estado='baja'", tablaPersonal), null);
        }
        while (cursor.moveToNext()){
            if(cont==num){
                for(int i=0;i<cursor.getColumnCount();i++){
                    cadena+=upperLetter(cursor.getColumnName(i)).replace("_"," ") + ": ";
                    switch (cursor.getType(i)){
                        case Cursor.FIELD_TYPE_STRING:
                            if(cursor.getColumnName(i).equalsIgnoreCase("dni")){
                                this.dni=cursor.getString(i);
                            }
                            cadena+=cursor.getString(i) + "\n";
                            break;
                        case Cursor.FIELD_TYPE_INTEGER:
                            cadena+=cursor.getInt(i) + "\n";
                            break;
                    }
                }
                lista.add(cadena);
                break;
            }else{
                cont++;
            }
        }

        read.close();
        return lista;
    }

    private String upperLetter(String cadena) {
        char array[]=cadena.toCharArray();
        array[0]=Character.toUpperCase(array[0]);
        return new String(array);
    }

    /**Metodo que devuelve el dni*/
    public String dniEscogido(){
        return dni;
    }

    /**Metodo que sirve para buscar un empleado mediante un dni que se le pasa por parametro
     * return ArrayList con una cadena que contiene los datos del empleado escogido*/

    public ArrayList<String>buscarEmpleado(String dni){
        ArrayList<String>lista=new ArrayList<>();
        SQLiteDatabase lectura=this.getReadableDatabase();
        String cadena="";

        Cursor cursor=lectura.rawQuery(String.format("SELECT * FROM %s WHERE dni='" + dni+"';",tablaPersonal),null);

        while(cursor.moveToNext()){
            for(int i=0;i<cursor.getColumnCount();i++){
                cadena="";
                switch (cursor.getType(i)){
                    case Cursor.FIELD_TYPE_STRING:
                        cadena+=cursor.getString(i);
                        break;
                    case Cursor.FIELD_TYPE_INTEGER:
                        cadena+=cursor.getInt(i);
                        break;
                }

                lista.add(cadena);
            }
        }

        if(cadena.equalsIgnoreCase("")){
            lista.add("No hay datos para este DNI/NIE");
        }

        lectura.close();
        return lista;


    }

    /**Metodo que devuelve una arrayList del personal de limpieza que hay en la planta que se le pasa por parametro*/

    public ArrayList<String>mostrarLimpiadorPlanta(int numPlanta){

        ArrayList<String>lista=new ArrayList<>();
        String cadena="";
        int cont=0;
        SQLiteDatabase read=this.getReadableDatabase();
        Cursor cursor=read.rawQuery(String.format("SELECT dni,nombre,apellidos FROM %s WHERE numero_planta="+numPlanta+" and puesto='limpieza' AND estado='activo';",tablaPersonal),null);

        while(cursor.moveToNext()){
            cadena="";
            cont++;
            cadena+="   "+String.valueOf(cont) + "     ";
            for(int i=0;i<cursor.getColumnCount();i++){
                switch (cursor.getType(i)){
                    case Cursor.FIELD_TYPE_STRING:
                        cadena+=cursor.getString(i) + "     ";
                        break;
                }
            }
            lista.add(cadena);
        }
        return lista;
    }


    /**Metodo que muestra el nombre y el dni del personal de limpieza indicando la planta y la posicion en la que se encuentra, inicializando las variables dni y nombre*/
    public void dniNombreLimpiador(int planta,int num){
        int cont=0;
        SQLiteDatabase read=this.getReadableDatabase();
        Cursor cursor=read.rawQuery(String.format("SELECT dni,nombre FROM %s WHERE numero_planta=" + planta +" AND puesto='limpieza' AND estado='activo';",tablaPersonal),null);
        while(cursor.moveToNext()){
            if(cont==num){
               this.dni=cursor.getString(0);
               this.nombre=cursor.getString(1);
                break;
            }else{
                cont++;
            }
        }
    }

    /**Metodo que devuelve el nombre*/
    public String nombreEscogido(){
        return nombre;
    }

    public String updateLimpiador(String dni1,String dni2,int planta1,int planta2){

        SQLiteDatabase update=this.getWritableDatabase();

        try{
            update.execSQL(String.format("UPDATE %s SET numero_planta="+planta2 + " WHERE dni='" + dni1+"';",tablaPersonal));
            update.execSQL(String.format("UPDATE %s SET numero_planta="+planta1 + " WHERE dni='" + dni2+"';",tablaPersonal));

            return "Limpiador cambiado de planta correctamente";
        }catch (SQLiteException sqLiteException){
            return "No se pudo cambiar el limpiador de planta";
        }

    }

    /**Comprueba que el personal con el dni indicado por paramentro esta o no de baja*/

    public boolean comprobarBaja(String dni){

        SQLiteDatabase db=this.getReadableDatabase();

        Cursor cursor=db.rawQuery(String.format("SELECT estado FROM %s WHERE dni='"+dni+"';",tablaPersonal),null);

        cursor.moveToFirst();

        if(cursor.getString(0).equalsIgnoreCase("baja")){
            return true;
        }


        return false;

    }

    /**Metodo que devuelve una cadena informando de la accion, el cual cambia el estado del limpiador de baja a activo, contratandole de nuevo*/

    public String setEstadoPersonal(String dni){

        SQLiteDatabase sql=this.getWritableDatabase();
        try {
            sql.execSQL(String.format("UPDATE %s SET estado='activo' WHERE dni='"+dni+"';",tablaPersonal));
            return "Usuario dado de alta de nuevo";
        }catch (SQLiteException sqlEx){
            return "Error";
        }
    }

    /**Metodo que sirve para modificar los datos de un personal con los datos pasados por parametro
     * return un cadena que informa si hubo un error a la hora de modificarlos o si la modificacion a sido exitosa*/
    public String modificarDatos(String dniPrincipal,String dni,String nombre,String apellidos,int telefono,String contrasenia,String puesto,int planta,String fecha){

        SQLiteDatabase database=this.getWritableDatabase();
        String tipoDato="";


        try {

            if (!dni.equalsIgnoreCase("nulo")) {
                database.execSQL(String.format("UPDATE %s SET dni='" + dni + "' WHERE dni='" + dniPrincipal + "';", tablaPersonal));
                database.execSQL(String.format("UPDATE %s SET dni_personal='" + dni + "' WHERE dni_personal='" + dniPrincipal + "';", tablaRegistrosLimpieza));
                dniPrincipal=dni;
                tipoDato+="DNI ";
            }
            if (!nombre.equalsIgnoreCase("nulo")) {
                database.execSQL(String.format("UPDATE %s SET nombre='" + nombre + "' WHERE dni='" + dniPrincipal + "';", tablaPersonal));
                tipoDato+="NOMBRE ";
            }
            if (!apellidos.equalsIgnoreCase("nulo")) {
                database.execSQL(String.format("UPDATE %s SET apellidos='" + apellidos + "' WHERE dni='" + dniPrincipal + "';", tablaPersonal));
                tipoDato+="APELLIDOS ";
            }
            if (telefono != 0) {
                database.execSQL(String.format("UPDATE %s SET telefono=" + telefono + " WHERE dni='" + dniPrincipal + "';", tablaPersonal));
                tipoDato+="TELEFONO ";
            }
            if (!contrasenia.equalsIgnoreCase("nulo")) {
                database.execSQL(String.format("UPDATE %s SET contrasenia='" + contrasenia + "' WHERE dni='" + dniPrincipal + "';", tablaPersonal));
                tipoDato+="CONTRASEÑA ";
            }
            if (!puesto.equalsIgnoreCase("nulo")) {
                database.execSQL(String.format("UPDATE %s SET puesto='" + puesto + "' WHERE dni='" + dniPrincipal + "';", tablaPersonal));
                tipoDato+="PUESTO ";
            }
            if (planta != -1) {
                database.execSQL(String.format("UPDATE %s SET numero_planta=" + planta + " WHERE dni='" + dniPrincipal + "';", tablaPersonal));
                tipoDato+="PLANTA ";
            }
            if (!fecha.equalsIgnoreCase("nulo")) {
                database.execSQL(String.format("UPDATE %s SET fecha_incorporacion='" + fecha + "' WHERE dni='" + dniPrincipal + "';", tablaPersonal));
                tipoDato+="FECHA ";
            }

            if(tipoDato.equalsIgnoreCase("")){
                tipoDato="Ningun dato ha sido modificado";
            }

            return "Datos modificados correctamente:\n" + tipoDato ;

        }catch (SQLiteException sqlEx){
            return "Alguno de los datos introducidos ya existen en nuestra base de datos";
        }

    }


    /**Metodo que sirve para dar de baja a un empleado que se busca mediante el dni pasado por parametro*/
    public boolean borrarEmpleado(String dni){

        SQLiteDatabase sql=this.getWritableDatabase();
        try {
            sql.execSQL(String.format("UPDATE %s SET estado='baja' WHERE dni='"+dni+"';",tablaPersonal));
            return true;
        }catch (SQLiteException sqlEx){
            return false;
        }
    }

    /**Devuelve un numero con el tipo de usuario que esta iniciando sesion con el dni y la contraseña pasados por parametros*/

    public int tipoUsuario(String dni,String contrasenia){

        SQLiteDatabase db=this.getReadableDatabase();




        try {
            Cursor cursor=db.rawQuery(String.format("SELECT puesto,estado FROM %s WHERE dni='"+dni+"' and contrasenia='"+ contrasenia + "';",tablaPersonal),null);

            if(cursor.getCount()<=0){
                return 6;
            }

            while(cursor.moveToNext()) {

                if (cursor.getString(0).equalsIgnoreCase("administrador")) {
                    if(cursor.getString(1).equalsIgnoreCase("baja")){
                        return 3;
                    }else {
                        return 0;
                    }
                } else if (cursor.getString(0).equalsIgnoreCase("limpieza")) {
                    if(cursor.getString(1).equalsIgnoreCase("baja")){
                        return 3;
                    }else {
                        return 1;
                    }
                } else if (cursor.getString(0).equalsIgnoreCase("recepcion")) {
                    if(cursor.getString(1).equalsIgnoreCase("baja")){
                        return 3;
                    }else {
                        return 2;
                    }
                }
            }
        }catch (Exception ex){
           return 5;
        }

       return 4;
    }

    /**Metodo que devuelve una arrayList con todos los registros que coincidan su dni en el personal*/

    public ArrayList<String>listaRegistrosGenerales(){

        ArrayList<String>lista=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        String cadena="";
        Cursor cursor=db.rawQuery("SELECT numero_habitacion,registro_limpieza.numero_planta, nombre,apellidos FROM registro_limpieza,personal WHERE dni=dni_personal",null);
        while(cursor.moveToNext()){
            cadena="";
            for(int i=0;i<cursor.getColumnCount();i++){
                switch (cursor.getType(i)){
                    case Cursor.FIELD_TYPE_STRING:
                        cadena+=cursor.getString(i) + "              ";
                        break;
                    case Cursor.FIELD_TYPE_INTEGER:
                        cadena+=cursor.getInt(i) + "           ";
                }
            }

            lista.add(cadena);
        }

        return lista;
    }

    /**Metodo que se ejecuta a la hora de crear la base datos ya que contiene valores iniciales estaticos*/

    private void crearHabitacionesIniciales(SQLiteDatabase db){

        Random r=new Random();
        ArrayList<Object>lista=new ArrayList<>();
        int numPersonas=0;

        for(int i=100;i<600;i+=10){
            numPersonas=r.nextInt(4)+1;
            if(i>=100 && i<=190){
                db.execSQL(String.format("INSERT OR IGNORE INTO %s (num_habitacion,num_personas,disponibilidad,numero_planta,estado_limpieza) VALUES ("+i+","+numPersonas+",'disponible',1,'pendiente');",tablaHabitaciones));
            }else if(i>=200 && i<=290){
                db.execSQL(String.format("INSERT OR IGNORE INTO %s (num_habitacion,num_personas,disponibilidad,numero_planta,estado_limpieza) VALUES ("+i+","+numPersonas+",'disponible',2,'limpia');",tablaHabitaciones));
            }else if(i>=300 && i<=390){
                db.execSQL(String.format("INSERT OR IGNORE INTO %s (num_habitacion,num_personas,disponibilidad,numero_planta,estado_limpieza) VALUES ("+i+","+numPersonas+",'disponible',3,'pendiente');",tablaHabitaciones));
            }else if(i>=400 && i<=490){
                db.execSQL(String.format("INSERT OR IGNORE INTO %s (num_habitacion,num_personas,disponibilidad,numero_planta,estado_limpieza) VALUES ("+i+","+numPersonas+",'disponible',4,'limpia');",tablaHabitaciones));
            }else if(i>=500 && i<=590){
                db.execSQL(String.format("INSERT OR IGNORE INTO %s (num_habitacion,num_personas,disponibilidad,numero_planta,estado_limpieza) VALUES ("+i+","+numPersonas+",'disponible',5,'limpia');",tablaHabitaciones));
            }
        }
    }

    /**Metodo que se ejecuta al ahora de crear la base de datos, ya que contiene valores inciales para la primera ejecucion del programa en la tabla personal*/
    private void crearPerfilesIniciales(SQLiteDatabase db){

        db.execSQL(String.format("INSERT OR IGNORE INTO %s(dni,nombre,apellidos,telefono,contrasenia,puesto,numero_planta,fecha_incorporacion,estado)" +
                " VALUES ('01234567P','Luis','Mendez',910000000,'admin1','administrador',0,'12/10/2021','activo');",tablaPersonal));

        db.execSQL(String.format("INSERT OR IGNORE INTO %s (dni,nombre,apellidos,telefono,contrasenia,puesto,numero_planta,fecha_incorporacion,estado) VALUES " +
                "('09876453A','Angel','Perez',982345867,'limpieza1','limpieza',1,'12/12/2020','activo')",tablaPersonal));

        db.execSQL(String.format("INSERT OR IGNORE INTO %s (dni,nombre,apellidos,telefono,contrasenia,puesto,numero_planta,fecha_incorporacion,estado) VALUES " +
                "('02345678D','Carlos','Sanchez',982346321,'recepcionista1','recepcion',0,'12/12/2020','activo')",tablaPersonal));
    }

    /**Metodo que cambia el estado de la habitacion pasada como parametro a ocupada*/
    public void cambiarEstadoHabitacionOcupada(String habitacion) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(String.format("UPDATE %s SET disponibilidad = 'ocupada' WHERE num_habitacion = " + habitacion + ";", tablaHabitaciones));
    }

    /**Metodo que cambia el estado de la habitacion pasada como parametro a disponible*/
    public void cambiarEstadoHabitacionLibre(String habitacion){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(String.format("UPDATE %s SET estado = 'Baja' WHERE numero_habitacion = " + habitacion + ";",tablaClientes));
        db.execSQL(String.format("UPDATE %s SET disponibilidad = 'disponible' WHERE num_habitacion = " + habitacion + ";",tablaHabitaciones));
    }

    /**Metodo que devuelve los clientes que hay en la habitacion pasada como parametro siempre y cuando esten dados de alta*/
    public ArrayList<String> infoClientes(String habitacion){
        ArrayList<String> clientes = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        int habitacionI = Integer.parseInt(habitacion);
        String cadena;
        Cursor cursor = db.rawQuery(String.format("SELECT numero_habitacion, nombre, apellidos FROM %s WHERE numero_habitacion = " + habitacionI + " and estado = 'alta';",tablaClientes),null);

        while(cursor.moveToNext()){
            cadena="";
            for(int i=0;i<cursor.getColumnCount();i++){
                switch (cursor.getType(i)){
                    case Cursor.FIELD_TYPE_STRING:
                        cadena+=cursor.getString(i) + "               ";
                        break;
                    case Cursor.FIELD_TYPE_INTEGER:
                        cadena+=cursor.getInt(i) + "               ";
                        break;
                }
            }
            clientes.add(cadena);
        }
        return clientes;
    }

    /**Metodo que nos devuelve una lista de las habitaciones que estan ocupadas*/
    public ArrayList<String> habitacionesOcupadas() {
        ArrayList<String> habitaciones = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String cadena;
        Cursor cursor = db.rawQuery(String.format("SELECT num_habitacion, num_personas, numero_planta FROM %s WHERE disponibilidad = 'ocupada';", tablaHabitaciones), null);

        while (cursor.moveToNext()) {
            cadena = "";
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                switch (cursor.getType(i)) {
                    case Cursor.FIELD_TYPE_STRING:
                        cadena += cursor.getString(i) + "                            ";
                        break;
                    case Cursor.FIELD_TYPE_INTEGER:
                        cadena += cursor.getInt(i) + "                            ";
                        break;
                }
            }
            habitaciones.add(cadena);
        }
        return habitaciones;
    }

    /**Metodo que devuelve el cliente con el dni que le pasamos como parametro*/
    public ArrayList<String> infoClientePorDni(String dni){
        ArrayList<String> clientes = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String cadena;
        Cursor cursor = db.rawQuery(String.format("SELECT numero_habitacion, nombre, apellidos FROM %s WHERE dni = '" + dni + "';",tablaClientes),null);

        while(cursor.moveToNext()){
            cadena="";
            for(int i=0;i<cursor.getColumnCount();i++){
                switch (cursor.getType(i)){
                    case Cursor.FIELD_TYPE_STRING:
                        cadena+=cursor.getString(i) + "              ";
                        break;
                    case Cursor.FIELD_TYPE_INTEGER:
                        cadena+=cursor.getInt(i) + "              ";
                        break;
                }
            }
            clientes.add(cadena);
        }
        return clientes;
    }

    /**Metodo que devuelve todos los clientes siempre y cuando esten dados de alta*/
    public ArrayList<String> infoTodosClientes(){
        ArrayList<String> clientes = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String cadena;
        Cursor cursor = db.rawQuery(String.format("SELECT numero_habitacion, nombre, apellidos FROM %s WHERE estado = 'alta';",tablaClientes),null);

        while(cursor.moveToNext()){
            cadena="";
            for(int i=0;i<cursor.getColumnCount();i++){
                switch (cursor.getType(i)){
                    case Cursor.FIELD_TYPE_STRING:
                        cadena+=cursor.getString(i) + "              ";
                        break;
                    case Cursor.FIELD_TYPE_INTEGER:
                        cadena+=cursor.getInt(i) + "              ";
                        break;
                }
            }
            clientes.add(cadena);
        }
        return clientes;
    }

    /**Metodo que devuelve las habitaciones disponibles en las que se pueden registrar el numero de personas pasadas como parametro*/
    public ArrayList<String> infoHabitaciones(int personas){
        ArrayList<String> clientes = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String cadena;
        Cursor cursor = db.rawQuery(String.format("SELECT num_habitacion,num_personas,numero_planta FROM %s WHERE num_personas = " + personas + " and disponibilidad = 'disponible';",tablaHabitaciones),null);

        while(cursor.moveToNext()){
            cadena="";
            for(int i=0;i<cursor.getColumnCount();i++){
                switch (cursor.getType(i)){
                    case Cursor.FIELD_TYPE_STRING:
                        cadena+=cursor.getString(i) + "                           ";
                        break;
                    case Cursor.FIELD_TYPE_INTEGER:
                        cadena+=cursor.getInt(i) + "                           ";
                        break;
                }
            }
            clientes.add(cadena);
        }
        return clientes;
    }

    /**Metodo que devuelve las habitaciones de una plasnta pasado como parametro siempre y que su estado de limpieza sea el mismo que el que le pasamos como parametrp*/
    public ArrayList<String> mostrarHabSegunEstado(String estado,int planta){
        ArrayList<String> habitaciones = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String cadena;
        Cursor cursor = db.rawQuery(String.format("SELECT num_habitacion, numero_planta, estado_limpieza FROM %s WHERE estado_limpieza = '" + estado + "' AND numero_planta = " + planta + ";",tablaHabitaciones),null);
        while(cursor.moveToNext()){
            cadena="";
            for(int i=0;i<cursor.getColumnCount();i++){
                switch (cursor.getType(i)){
                    case Cursor.FIELD_TYPE_STRING:
                        cadena+=cursor.getString(i) + "                        ";
                        break;
                    case Cursor.FIELD_TYPE_INTEGER:
                        cadena+=cursor.getInt(i) + "                        ";
                        break;
                }
            }
            habitaciones.add(cadena);
        }
        return habitaciones;
    }

    /**Metodo que nos devuelve una lista con todos los datos del cliente seleccionado
     * parametro num es un contador que su valor corresponde a la posicion que ocupa en el ListView a la hora de pinchar sobre el
     * return ArrayList que contiene un cadena con los datos que le corresponde a ese cliente*/
    public ArrayList clienteEscogido(int num){
        int cont=0;
        ArrayList<String>lista=new ArrayList<>();
        SQLiteDatabase read=this.getReadableDatabase();
        String cadena="";

        Cursor cursor=read.rawQuery(String.format("SELECT numero_habitacion, nombre, apellidos, dni, telefono, fecha_entrada, fecha_salida, estado FROM %s WHERE estado = 'alta';",tablaClientes),null);;

        while (cursor.moveToNext()){
            if(cont==num){
                for(int i=0;i<cursor.getColumnCount();i++){
                    cadena+=upperLetter(cursor.getColumnName(i)).replace("_"," ") + ": ";
                    switch (cursor.getType(i)){
                        case Cursor.FIELD_TYPE_STRING:
                            cadena+=cursor.getString(i) + "\n";
                            break;
                        case Cursor.FIELD_TYPE_INTEGER:
                            cadena+=cursor.getInt(i) + "\n";
                            break;
                    }
                }
                lista.add(cadena);
                break;
            }else{
                cont++;
            }
        }

        read.close();
        return lista;
    }

    /**Metodo que nos devuelve una lista con todos los datos del cliente que tenga el mismo dni que el que le pasamos como parametro
     * parametro num es un contador que su valor corresponde a la posicion que ocupa en el ListView a la hora de pinchar sobre el
     * return ArrayList que contiene un cadena con los datos que le corresponde a ese cliente*/
    public ArrayList clienteEscogidoPorDni(int num, String dni){
        int cont=0;
        ArrayList<String>lista=new ArrayList<>();
        SQLiteDatabase read=this.getReadableDatabase();
        String cadena="";

        Cursor cursor=read.rawQuery(String.format("SELECT numero_habitacion, nombre, apellidos, dni, telefono, fecha_entrada, fecha_salida, estado FROM %s WHERE dni = '" + dni + "';",tablaClientes),null);;

        while (cursor.moveToNext()){
            if(cont==num){
                for(int i=0;i<cursor.getColumnCount();i++){
                    cadena+=upperLetter(cursor.getColumnName(i)).replace("_"," ") + ": ";
                    switch (cursor.getType(i)){
                        case Cursor.FIELD_TYPE_STRING:
                            cadena+=cursor.getString(i) + "\n";
                            break;
                        case Cursor.FIELD_TYPE_INTEGER:
                            cadena+=cursor.getInt(i) + "\n";
                            break;
                    }
                }
                lista.add(cadena);
                break;
            }else{
                cont++;
            }
        }

        read.close();
        return lista;
    }

    /**Metodo que nos devuelve el numero de habitacion en la que pueda haber el mismo numero de personas que el que le pasamos como parametro
     * parametro num es un contador que su valor corresponde a la posicion que ocupa en el ListView a la hora de pinchar sobre el
     * return int que contiene el numero de habitacion que le corresponde*/
    public int habitacionEscogida(int num, int personas){
        int cont=0;
        ArrayList<String>lista=new ArrayList<>();
        SQLiteDatabase read=this.getReadableDatabase();
        int habitacion=0;
        Cursor cursor=read.rawQuery(String.format("SELECT num_habitacion,num_personas,numero_planta FROM %s WHERE num_personas = " + personas + " and disponibilidad = 'disponible';",tablaHabitaciones),null);;

        while (cursor.moveToNext()){
            if(cont==num){
                for(int i=0;i<cursor.getColumnCount();i++){
                    if(cursor.getColumnName(i).equalsIgnoreCase("num_habitacion")){
                        habitacion = Integer.parseInt(cursor.getString(i));
                    }
                }
                break;
            }else{
                cont++;
            }
        }
        read.close();
        return habitacion;
    }

    /**Metodo que nos devuelve una lista con el numero de habitacion y la planta que tenga el estado de limpieza que el que le pasamos como parametro
     * parametro num es un contador que su valor corresponde a la posicion que ocupa en el ListView a la hora de pinchar sobre el
     * return int[] que contiene un cadena con los datos de numero de habitacion y planta que le corresponde a esa habitacion*/
    public int[] habitacionEscogidaParaLimpieza(int num, String estado){
        int cont=0;
        SQLiteDatabase read=this.getReadableDatabase();
        int habitacion=0;
        int planta=0;
        Cursor cursor=read.rawQuery(String.format("SELECT num_habitacion, numero_planta FROM %s WHERE estado_limpieza = '" + estado + "';",tablaHabitaciones),null);;

        while (cursor.moveToNext()){
            if(cont==num){
                for(int i=0;i<cursor.getColumnCount();i++){
                    if(cursor.getColumnName(i).equalsIgnoreCase("num_habitacion")){
                        habitacion = Integer.parseInt(cursor.getString(i));
                    }
                    if(cursor.getColumnName(i).equalsIgnoreCase("numero_planta")){
                        planta = Integer.parseInt(cursor.getString(i));
                    }
                }
                break;
            }else{
                cont++;
            }
        }
        read.close();
        int[] datos = new int[2];
        datos[0] = habitacion;
        datos[1] = planta;
        return datos;
    }

    /**Metodo que nos devuelve el numero de habitacion que queremos dar de baja
     * parametro num es un contador que su valor corresponde a la posicion que ocupa en el ListView a la hora de pinchar sobre el
     * return int que contiene el numero de habitacion que le corresponde*/
    public int habitacionEscogidaParaBaja(int num){
        int cont=0;
        ArrayList<String>lista=new ArrayList<>();
        SQLiteDatabase read=this.getReadableDatabase();
        int habitacion=0;
        Cursor cursor=read.rawQuery(String.format("SELECT num_habitacion FROM %s WHERE disponibilidad = 'ocupada';",tablaHabitaciones),null);;

        while (cursor.moveToNext()){
            if(cont==num){
                for(int i=0;i<cursor.getColumnCount();i++){
                    if(cursor.getColumnName(i).equalsIgnoreCase("num_habitacion")){
                        habitacion = Integer.parseInt(cursor.getString(i));
                    }
                }
                break;
            }else{
                cont++;
            }
        }
        read.close();
        return habitacion;
    }

    /**Metodo para pasar a estado de limpieza pendiente en todas las habitaciones*/
    public void pasarAPendiente(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(String.format("UPDATE %s SET estado_limpieza = 'pendiente' ;",tablaHabitaciones));
    }

    /**Metodo que cambia el estado de limpieza de una habitacion al estado que le pasamos por completo cuando el numero de
     * habitacion sea igual al que le pasamos como parametro
     * Ademas creamos un registro para tener constancia de quien, a que estado, que habitacion y en que planta se ha cambiado de estado
     */
    public void cambiarEstado(int numHabitacion, int numPlanta, String dni, String estado){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(String.format("UPDATE %s SET estado_limpieza = '" + estado + "' WHERE num_habitacion = " + numHabitacion + ";",tablaHabitaciones));
        crearRegistros(numHabitacion,numPlanta,dni,estado);
    }

    /**Metodo que muestra todos los registros de los cambios de limpieza hechos por el limpiador que tenga el mismo dni que le pasamos por parametro*/
    public ArrayList<String> registros(String dni){
        ArrayList<String> registros = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String cadena;
        Cursor cursor = db.rawQuery(String.format("SELECT numero_habitacion, numero_planta, estado_limpieza FROM %s WHERE dni_personal = '" + dni + "';",tablaRegistrosLimpieza),null);
        while(cursor.moveToNext()){
            cadena="";
            for(int i=0;i<cursor.getColumnCount();i++){
                switch (cursor.getType(i)){
                    case Cursor.FIELD_TYPE_STRING:
                        cadena+=cursor.getString(i) + "                 ";
                        break;
                    case Cursor.FIELD_TYPE_INTEGER:
                        cadena+=cursor.getInt(i) + "                 ";
                        break;
                }
            }
            registros.add(cadena);
        }
        return registros;
    }

    /**Metodo para obtener la planta en la que esta un empleado que tenga el mismo dni que el que le pasamos como parametro*/
    public int obtenerPlanta(String dni){
        int planta=0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(String.format("SELECT numero_planta FROM %s WHERE dni = '" + dni + "';",tablaPersonal),null);
        while(cursor.moveToNext()){
            for(int i=0;i<cursor.getColumnCount();i++){
                switch (cursor.getType(i)){
                    case Cursor.FIELD_TYPE_INTEGER:
                        planta=cursor.getInt(i);
                        break;
                }
            }
        }
        return planta;
    }
}
