package com.example.proyectofinalhotel.modelo;

public class Personal extends Personas{

    private String contrasenia;
    private String puesto;
    private int numPanta;
    private String fecha_incor;
    private String estado;


    public Personal(String dni, String nombre, String apellidos,int telefono, String contrasenia, String puesto, int numPanta, String fecha_incor, String estado) {
        super(dni,nombre,apellidos,telefono);
        this.contrasenia = contrasenia;
        this.puesto = puesto;
        this.numPanta = numPanta;
        this.fecha_incor = fecha_incor;
        this.estado = estado;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public int getNumPanta() {
        return numPanta;
    }

    public void setNumPanta(int numPanta) {
        this.numPanta = numPanta;
    }

    public String getFecha_incor() {
        return fecha_incor;
    }

    public void setFecha_incor(String fecha_incor) {
        this.fecha_incor = fecha_incor;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
