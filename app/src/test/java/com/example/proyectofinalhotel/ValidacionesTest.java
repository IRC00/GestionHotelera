package com.example.proyectofinalhotel;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ValidacionesTest {

  /*  private Validaciones validaciones;

    @Before
    public void setup(){
        validaciones=new Validaciones();
    }*/

    @Test
    public void comprobarDNI() {
        //Pruebas del NIE
        assertTrue(Validaciones.comprobarDNI("X1234567Y"));
        assertTrue(Validaciones.comprobarDNI("Y1234567E"));
        assertTrue(Validaciones.comprobarDNI("Z1234567P"));
        //Prueba del DNI
        assertTrue(Validaciones.comprobarDNI("12345678R"));

        assertFalse(Validaciones.comprobarDNI(""));
        assertFalse(Validaciones.comprobarDNI("1"));
        assertFalse(Validaciones.comprobarDNI("E"));
        //Pruebas del DNI
        assertFalse(Validaciones.comprobarDNI("123456Y"));
        assertFalse(Validaciones.comprobarDNI("123456789I"));
        assertFalse(Validaciones.comprobarDNI("12345678UY"));
        assertFalse(Validaciones.comprobarDNI("12345678j"));
        //Pruebas del NIE
        assertFalse(Validaciones.comprobarDNI("C1234567Y"));
        assertFalse(Validaciones.comprobarDNI("X1234F"));
        assertFalse(Validaciones.comprobarDNI("X1234567y"));
        assertFalse(Validaciones.comprobarDNI("x1234567i"));
        assertFalse(Validaciones.comprobarDNI("X123456789Y"));


    }

    @Test
    public void comprobarContrasenia() {
        assertTrue(Validaciones.comprobarContrasenia("asFca3ru"));

        assertFalse(Validaciones.comprobarContrasenia(""));
        assertFalse(Validaciones.comprobarContrasenia("12345672"));
        assertFalse(Validaciones.comprobarContrasenia("ABSDFREGR"));
        assertFalse(Validaciones.comprobarContrasenia("ABDBkilow"));
        assertFalse(Validaciones.comprobarContrasenia("contrasenia"));
        assertFalse(Validaciones.comprobarContrasenia("contrasenia2"));
        assertFalse(Validaciones.comprobarContrasenia("contraseniaF23ghjiiskaksaaalldjaas"));
        assertFalse(Validaciones.comprobarContrasenia("conT3"));

    }

    @Test
    public void comprobarNumeroTelefono() {
        assertTrue(Validaciones.comprobarNumeroTelefono("917537438"));

        assertFalse(Validaciones.comprobarNumeroTelefono(""));
        assertFalse(Validaciones.comprobarNumeroTelefono("r"));
        assertFalse(Validaciones.comprobarNumeroTelefono("8"));
        assertFalse(Validaciones.comprobarNumeroTelefono("telefonos"));
        assertFalse(Validaciones.comprobarNumeroTelefono("84758483921"));

    }
}