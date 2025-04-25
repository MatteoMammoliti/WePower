package com.wepower.wepower.Models.DatiPalestra;

import com.wepower.wepower.Models.ConnessioneDatabase;
import com.wepower.wepower.Views.AdminView.RigaTabellaRichiesteScheda;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DatiSessionePalestra {
    private static int numeroMassimePrenotazioniPerFascieOrarie=50;
    private static Map<PrenotazioneSalaPesiCliente, Integer> prenotazioniSalePesi= new HashMap<>();
    private static String[] orariPrenotazione = {"08:00", "10:00", "12:00", "14:00", "16:00", "18:00", "20:00"};

    //Aggiungo una prenotazione, se in quella data e ora sono già state fatte delle prenotazioni, incremento il contatore
    public static void aggiungiPrenotazioneSalaPesi(PrenotazioneSalaPesiCliente prenotazione) {
        if(prenotazioniSalePesi.containsKey(prenotazione)) {
            prenotazioniSalePesi.put(prenotazione, prenotazioniSalePesi.get(prenotazione) + 1);
        } else {
            prenotazioniSalePesi.put(prenotazione, 1);
        }
    }

    //Funzione che mi serve per capire quanti posti prenotabili rimangono per ogni fascia oraria/giorno
    public static int getNumeroPrenotazioniDataOraResidue(PrenotazioneSalaPesiCliente prenotazione) {
        if(prenotazioniSalePesi.containsKey(prenotazione)) {
            return numeroMassimePrenotazioniPerFascieOrarie - prenotazioniSalePesi.get(prenotazione);
        } else {
            return numeroMassimePrenotazioniPerFascieOrarie;
        }
    }

    public static boolean rimuoviPrenotazioneSalaPesi(PrenotazioneSalaPesiCliente prenotazione) {
        if(prenotazioniSalePesi.containsKey(prenotazione)) {
            int contatore = prenotazioniSalePesi.get(prenotazione);
            if(contatore > 1) {
                prenotazioniSalePesi.put(prenotazione, contatore - 1);
            } else {
                prenotazioniSalePesi.remove(prenotazione);
            }
            return true;
        } else {
            return false;
        }
    }

    public static String[] getOrariPrenotazione() {
        return orariPrenotazione;
    }

    // Vado a prelevare tutti i dati degli utenti che hanno richiesto una scheda di allenamento
    public static ObservableList<RigaTabellaRichiesteScheda> getRichiesteScheda() {

        //Inizializzo la ObservableList vuota
        ObservableList<RigaTabellaRichiesteScheda> utenti= FXCollections.observableArrayList();
        String prendoIClienti="SELECT c.IdCliente,c.Nome,c.Cognome,c.Sesso,p.Peso FROM Cliente c JOIN PesoCliente p ON c.IdCliente=p.IdCliente JOIN  SchedaAllenamento s ON c.IdCliente=s.IdCliente WHERE s.IdAdmin=1 AND s.SchedaAncoraInUso=1 AND s.SchedaCompilata=0  AND NOT EXISTS " +
                "(\n" +
                "        SELECT 1\n" +
                "        FROM   PesoCliente p2\n" +
                "        WHERE  p2.IdCliente = p.IdCliente\n" +
                "          AND  p2.DataRegistrazionePeso > p.DataRegistrazionePeso\n" +
                "      );";
        try(Connection conn= ConnessioneDatabase.getConnection()) {
            PreparedStatement preparo=conn.prepareStatement(prendoIClienti);
            ResultSet rs=preparo.executeQuery();
            //In questo ciclo while, per ogni riga della tabella, creo un oggetto RigaTabellaRichiesteScheda e lo aggiungo alla lista
            while (rs.next()){
                RigaTabellaRichiesteScheda utente=new RigaTabellaRichiesteScheda(rs.getInt("IdCliente"),rs.getString("Nome"),rs.getString("Cognome"),rs.getInt("Peso"),rs.getString("Sesso"));
                utenti.add(utente);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return utenti;


    }


}
