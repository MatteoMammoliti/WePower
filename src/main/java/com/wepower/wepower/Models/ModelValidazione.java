package com.wepower.wepower.Models;

import com.dlsc.formsfx.model.validators.RegexValidator;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class ModelValidazione {
    public static boolean controlloEmailvalida(String email){
        return Pattern.matches("^[a-zA-Z-0-9]+@gmail.com$",email);
    }
    public static boolean controlloNome(String nome){
        return Pattern.matches("^[a-zA-Z]+",nome);
    }
    public static boolean controlloCognome(String cognome){
        return Pattern.matches("^[a-zA-Z-]+",cognome);
    }
    public static boolean controllonomeCognome(String nomeCognome){
        String regexNomeCognome="^[a-zA-Z]+(\\s[a-zA-Z]+)+$";
        return Pattern.matches(regexNomeCognome,nomeCognome);
    }
    public static boolean controlloNumeroCarta(String nCarta){
        return Pattern.matches("^\\d{13,19}$",nCarta);
    }

    public static boolean controlloNumeroCVC(String cvc){
        return Pattern.matches("^\\d{3}$",cvc);
    }
    public static boolean controlloDataScadenzacarta(String data){
        return Pattern.matches("^(0[1-9]|1[0-2])/(\\d{2})$",data);
    }
    public static boolean controlloAltezza(String altezza){
        return Pattern.matches("^\\d{2,3}$",altezza) || altezza.isEmpty();
    }

    public static boolean controlloData(LocalDate data){
        LocalDate dataMassima = LocalDate.of(2015, 12, 31); // limite massimo: fine 2015
        LocalDate dataMinima = LocalDate.now().minusYears(80); // limite minimo: massimo 80 anni fa

        return (data.isEqual(dataMassima) || data.isBefore(dataMassima)) && (data.isEqual(dataMinima) || data.isAfter(dataMinima));

    }


    public static boolean controlloPeso(String peso) {
        if (peso == null || peso.isEmpty()) {return true;}
        if (!Pattern.matches("^[0-9]{1,3}$", peso)) {return false;}
        if (Integer.parseInt(peso) < 30 || Integer.parseInt(peso) > 200) {return false;}
        return true;
    }

    public static boolean controlloNomeOfferta(String nome){
        return Pattern.matches("^[A-Za-zÀ-ÿ\s]{2,50}$",nome);
    }

    public static boolean controlloNumeroTelefono(String telefono){
        return Pattern.matches("^(\\+39)?\\s?\\d{9,10}$",telefono) || telefono.isEmpty();
    }

    public static boolean controlloPrezzoOfferta(String prezzo){
        return Pattern.matches("^[1-9][0-9]{0,3}$",prezzo);
    }

    public static boolean controlloDurataOfferta(String durata){
        return Pattern.matches("^[1-9][0-9]*$",durata);
    }
}
