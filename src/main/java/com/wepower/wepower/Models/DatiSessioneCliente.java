package com.wepower.wepower.Models;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DatiSessioneCliente {
    private static int idUtente;
    private static String email;
    private static String nome;
    private static String cognome;
    private static boolean certificato;
    private static String telefono;
    private static ArrayList<PrenotazioneSalaPesi> dateOrariPrenotazioni = new ArrayList<>();
    private static Set<String> datePrenotazioni = new HashSet<>();

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
    public static ArrayList<PrenotazioneSalaPesi> getDateOrariPrenotazioni() {
        return dateOrariPrenotazioni;
    }

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
    public static void setDateOrariPrenotazioni(ArrayList<PrenotazioneSalaPesi> d) {
        dateOrariPrenotazioni = d;

        datePrenotazioni.clear();
        for (int i = 0; i < dateOrariPrenotazioni.size(); i++) {
            datePrenotazioni.add(dateOrariPrenotazioni.get(i).getDataPrenotazione());
        }
    }


    // LOGOUT
    public static void logout() {
        idUtente = 0;
        email = null;
        nome = null;
        certificato = false;
        telefono = null;
    }

    // CONTROLLO DATA PRENOTAZIONE
    public static boolean controlloDataPrenotazione(LocalDate data) {
        String dataControllo = data.toString();

        if (datePrenotazioni.contains(dataControllo)) {
            return true;
        }
        return false;
    }
}
