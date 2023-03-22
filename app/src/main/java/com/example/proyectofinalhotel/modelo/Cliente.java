package com.example.proyectofinalhotel.modelo;

import java.io.Serializable;

public class Cliente extends Personas implements Serializable {

    private int numHabitacion;
    private String fecha_entrada;
    private String fecha_salida;
    private String estado;

    public Cliente(String dni, String nombre, String apellidos,int telefono, int numHabitacion, String fecha_entrada, String fecha_salida, String estado) {
        super(dni,nombre,apellidos,telefono);
        this.numHabitacion = numHabitacion;
        this.fecha_entrada = fecha_entrada;
        this.fecha_salida = fecha_salida;
        this.estado = estado;
    }

    public int getNumHabitacion() {
        return numHabitacion;
    }

    public void setNumHabitacion(int numHabitacion) {
        this.numHabitacion = numHabitacion;
    }

    public String getFecha_entrada() {
        return fecha_entrada;
    }

    public void setFecha_entrada(String fecha_entrada) {
        this.fecha_entrada = fecha_entrada;
    }

    public String getFecha_salida() {
        return fecha_salida;
    }

    public void setFecha_salida(String fecha_salida) {
        this.fecha_salida = fecha_salida;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
