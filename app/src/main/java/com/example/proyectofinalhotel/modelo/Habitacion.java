package com.example.proyectofinalhotel.modelo;

public class Habitacion {

    private int numHabitacion;
    private int numMaxPersonas;
    private String disponibilidad;
    private int planta;

    public Habitacion(int numHabitacion,int numMaxPersonas, String disponibilidad, int planta) {
        this.numHabitacion=numHabitacion;
        this.numMaxPersonas = numMaxPersonas;
        this.disponibilidad = disponibilidad;
        this.planta = planta;
    }

    public int getNumHabitacion() {
        return numHabitacion;
    }

    public void setNumHabitacion(int numHabitacion) {
        this.numHabitacion = numHabitacion;
    }

    public int getNumMaxPersonas() {
        return numMaxPersonas;
    }

    public void setNumMaxPersonas(int numMaxPersonas) {
        this.numMaxPersonas = numMaxPersonas;
    }

    public String getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(String disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public int getPlanta() {
        return planta;
    }

    public void setPlanta(int planta) {
        this.planta = planta;
    }
}
