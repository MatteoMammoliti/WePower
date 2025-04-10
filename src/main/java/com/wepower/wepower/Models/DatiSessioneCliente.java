package com.wepower.wepower.Models;

import com.wepower.wepower.Models.DatiPalestra.PrenotazioneSalaPesi;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DatiSessioneCliente {
    private static int idUtente;
    private static boolean statoAbbonamento;
    private static String email;
    private static String nome;
    private static String cognome;
    private static boolean certificato;
    private static String telefono;
    private static int idSchedaAllenamento;

    //Tutto lo storico delle prenotazioni sala pesi dell'utente
    private static ArrayList<PrenotazioneSalaPesi> dateOrariPrenotazioni = new ArrayList<>();

    //Set per accedere velocemente ad una dataPrenotazione per velocizzare e alleggerire i calcoli
    private static Set<String> datePrenotazioniSalaPesi = new HashSet<>();

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
    public static boolean getStatoAbbonamento(){return statoAbbonamento;}
    public static int getIdSchedaAllenamento() { return idSchedaAllenamento; }
    public static String getOrarioPrenotazione(String data){
        for (int i=0;i<dateOrariPrenotazioni.size();i++){
            if (dateOrariPrenotazioni.get(i).getDataPrenotazione().equals(data)){
                return dateOrariPrenotazioni.get(i).getOrarioPrenotazione();
            }
        }
        return null;
    }

    // SETTER
    public static void setStatoAbbonamento(boolean abbonamento){statoAbbonamento = abbonamento;}
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
    public static void setTelefono(String t) {telefono = t; }
    public static void setIdSchedaAbbonamento(int id) { idSchedaAllenamento = id; }
    public static void setDateOrariPrenotazioni(ArrayList<PrenotazioneSalaPesi> d) {
        dateOrariPrenotazioni = d;

        datePrenotazioniSalaPesi.clear();
        for (int i = 0; i < dateOrariPrenotazioni.size(); i++) {
            datePrenotazioniSalaPesi.add(dateOrariPrenotazioni.get(i).getDataPrenotazione());
        }
    }


    // LOGOUT
    public static void logout() {
        idUtente = 0;
        email = null;
        nome = null;
        certificato = false;
        telefono = null;
        dateOrariPrenotazioni.clear();
        datePrenotazioniSalaPesi.clear();
        statoAbbonamento = false;
        Model.invalidate();
    }
    // CONTROLLO DATA PRENOTAZIONE SALA PESI
    public static boolean controlloDataPrenotazioneSalaPesi(LocalDate data) {
        String dataControllo = data.toString();

        if (datePrenotazioniSalaPesi.contains(dataControllo)) {
            return true;
        }
        return false;
    }
    // CONTROLLO DATA E ORARIO PRENOTAZIONE SALA PESI PER STORICO
    public static boolean controlloDataPrenotazioneSalaPesi(LocalDate data,String ora) {
        String dataControllo = data.toString();
        PrenotazioneSalaPesi temp=new PrenotazioneSalaPesi(dataControllo,ora);

        if (dateOrariPrenotazioni.contains(temp)) {
            return true;
        }
        return false;
    }




    //AGGIUNGI UNA PRENOTAZIONE
    public static void aggiungiPrenotazione(PrenotazioneSalaPesi p) {
        dateOrariPrenotazioni.add(p);
        datePrenotazioniSalaPesi.add(p.getDataPrenotazione());
    }
    //RIMUOVI UNA PRENOTAZIONE
    public static void rimuoviPrenotazione(PrenotazioneSalaPesi p) {
        dateOrariPrenotazioni.remove(p);
        datePrenotazioniSalaPesi.remove(p.getDataPrenotazione());
        for (int i=0;dateOrariPrenotazioni.size()>i;i++){
            System.out.println(dateOrariPrenotazioni.get(i).getDataPrenotazione());
        }
    }

}
