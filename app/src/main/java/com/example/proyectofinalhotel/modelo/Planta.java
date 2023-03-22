package com.example.proyectofinalhotel.modelo;

public class Planta {

    private int numPlanta;
    private String descripcion;

    public Planta(int numPlanta, String descripcion) {
        this.numPlanta = numPlanta;
        this.descripcion = descripcion;
    }

    public int getNumPlanta() {
        return numPlanta;
    }

    public void setNumPlanta(int numPlanta) {
        this.numPlanta = numPlanta;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
