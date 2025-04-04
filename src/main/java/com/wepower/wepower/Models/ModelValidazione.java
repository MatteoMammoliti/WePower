package com.wepower.wepower.Models;

import com.dlsc.formsfx.model.validators.RegexValidator;

import java.util.regex.Pattern;

public class ModelValidazione {
    public static boolean controlloEmailvalida(String email){
        return Pattern.matches("^[a-zA-Z-0-9]+@gmail.com$",email);
    }
    public static boolean controlloNome(String nome){
        return Pattern.matches("^[a-zA-Z]+",nome);
    }
    public static boolean controlloCognome(String cognome){
        return Pattern.matches("^[a-zA-Z]+",cognome);
    }
}
