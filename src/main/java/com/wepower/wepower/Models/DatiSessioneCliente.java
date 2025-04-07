package com.wepower.wepower.Models;

public class DatiSessioneCliente {
    private static int idUtente;
    private static String email;
    private static String nome;
    private static String cognome;
    private static boolean certificato;
    private static String telefono;

    // GETTER
    public static int getIdUtente() {
        return idUtente;
    }
    public static String getEmail() {
        return email;
    }
    public static String getNomeUtente() {
        return nome;
    }
    public  static String getTelefono() {
        return telefono;
    }
    public static boolean Certificato() {
        return certificato;
    }
    public static String getCognome() { return cognome; }

    // SETTER
    public static void setNomeUtente(String n) {
        nome=n;
    }
    public static void setIdUtente(int id) {
        idUtente = id;
    }
    public static void setEmail(String e_mail){
        email = e_mail;
    }
    public static void setCertificato(boolean valore) {
        certificato = valore;
    }
    public static void setCognome(String c) { cognome = c; }

    // LOGOUT
    public static void logout() {
        idUtente = 0;
        email = null;
        nome = null;
        certificato = false;
        telefono = null;
    }
}
