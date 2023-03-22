package com.example.proyectofinalhotel;

import java.util.regex.Pattern;

public class Validaciones {

    public static boolean comprobarDNI(String cadena){
        String regex="([0-9]{8}[A-Z]{1})|([X|Y|Z]{1}[0-9]{7}[A-Z]{1})";

        return Pattern.matches(regex,cadena);
    }

    public static boolean comprobarContrasenia(String texto) {


        int contMayusculas=0;
        int contNumero=0;

        for(int i=0;i<texto.length();i++) {
            if(Character.isDigit(texto.charAt(i))){
                contNumero++;
            }
            if(Character.isUpperCase(texto.charAt(i))) {
                contMayusculas++;
            }
        }

        if(contNumero>0 && contMayusculas>0 && (texto.length()>=6) && texto.length()<16) {
            return true;
        }

        return false;

    }

    public static boolean comprobarNumeroTelefono(String numero){

        String regex="[0-9]{9}";
        return Pattern.matches(regex,numero);

    }


}
