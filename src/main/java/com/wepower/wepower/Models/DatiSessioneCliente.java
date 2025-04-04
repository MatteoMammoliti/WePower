package com.wepower.wepower.Models;

public class DatiSessioneCliente {
    private static int idUtente;
    private static String email;
    private static String nome;
    private static boolean certificato;
    private static String telefono;


    public static int getIdUtente() {
        return idUtente;
    }

    public static void setNomeUtente(String n) {
        nome=n;
    }
    public static String getNomeUtente() {
        return nome;
    }
    public static String getEmail() {
        return email;
    }

    public static boolean Certificato() {
        return certificato;
    }

    public  static String getTelefono() {
        return telefono;
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
}
