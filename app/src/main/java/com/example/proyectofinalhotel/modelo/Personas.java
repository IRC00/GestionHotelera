package com.example.proyectofinalhotel.modelo;

import java.io.Serializable;
import java.util.regex.Pattern;

public abstract class Personas implements Serializable {

    private String dni;
    private String nombre;
    private String apellidos;
    private int telefono;


    public Personas(String dni,String nombre,String apellidos,int telefono){
        this.dni=dni;
        this.nombre=nombre;
        this.apellidos=apellidos;
        this.telefono=telefono;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }
}
