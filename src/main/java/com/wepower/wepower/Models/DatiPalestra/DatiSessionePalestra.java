package com.wepower.wepower.Models.DatiPalestra;

import com.wepower.wepower.Models.ConnessioneDatabase;
import com.wepower.wepower.Views.AdminView.RigaTabellaRichiesteScheda;
import com.wepower.wepower.Views.AlertHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DatiSessionePalestra {
    private static int numeroMassimePrenotazioniPerFascieOrarie;
    private static Map<PrenotazioneSalaPesiCliente, Integer> prenotazioniSalePesi= new HashMap<>();

    // orari utilizzati dal bot per decidere se la richiesta di prenotazione ha un orario valido
    private static String[] orariPrenotazione = {"08:00", "10:00", "12:00", "14:00", "16:00", "18:00", "20:00"};

    // Vado a prelevare tutti i dati degli utenti che hanno richiesto una scheda di allenamento
    public static ObservableList<RigaTabellaRichiesteScheda> getRichiesteScheda() {
        Connection conn = ConnessioneDatabase.getConnection();


        //Inizializzo la ObservableList vuota
        ObservableList<RigaTabellaRichiesteScheda> utenti= FXCollections.observableArrayList();
        String prendoIClienti="SELECT c.IdCliente, c.Nome, c.Cognome, c.Sesso, p.Peso\n" +
                "FROM Cliente c\n" +
                "LEFT JOIN PesoCliente p ON c.IdCliente = p.IdCliente\n" +
                "    AND p.DataRegistrazionePeso = (\n" +
                "        SELECT MAX(p2.DataRegistrazionePeso)\n" +
                "        FROM PesoCliente p2\n" +
                "        WHERE p2.IdCliente = c.IdCliente\n" +
                "    )\n" +
                "JOIN SchedaAllenamento s ON c.IdCliente = s.IdCliente\n" +
                "WHERE s.IdAdmin = 1\n" +
                "  AND s.SchedaAncoraInUso = 1\n" +
                "  AND s.SchedaCompilata = 0;\n";

        PreparedStatement preparo = null;
        ResultSet rs = null;
        try {
            preparo=conn.prepareStatement(prendoIClienti);
            rs=preparo.executeQuery();
            //In questo ciclo while, per ogni riga della tabella, creo un oggetto RigaTabellaRichiesteScheda e lo aggiungo alla lista
            while (rs.next()){
                RigaTabellaRichiesteScheda utente=new RigaTabellaRichiesteScheda(rs.getInt("IdCliente"),rs.getString("Nome"),rs.getString("Cognome"),rs.getInt("Peso"),rs.getString("Sesso"));
                utenti.add(utente);
            }
        } catch (SQLException e) {
            AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato storto nel prelevamento delle schede richieste", null, Alert.AlertType.ERROR);
        } finally {
            if (preparo != null) {
                try { preparo.close(); } catch (SQLException ignored) {}
            }
            if (rs != null) {
                try { rs.close(); } catch (SQLException ignored) {}
            }
        }
        return utenti;
    }

    //Funzione che mi serve per capire quanti posti prenotabili rimangono per ogni fascia oraria/giorno
    public static int getNumeroPrenotazioniDataOraResidue(PrenotazioneSalaPesiCliente prenotazione) {
        if(prenotazioniSalePesi.containsKey(prenotazione)) {
            return numeroMassimePrenotazioniPerFascieOrarie - prenotazioniSalePesi.get(prenotazione);
        } else {
            return numeroMassimePrenotazioniPerFascieOrarie;
        }
    }

    public static String[] getOrariPrenotazione() { return orariPrenotazione; }

    public static void setNumeroMassimoPrenotazioni() {
        Connection conn = ConnessioneDatabase.getConnection();

        String query="SELECT NumeriPostiMassimo FROM SalaPesi WHERE IdSalaPesi=1";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps=conn.prepareStatement(query);
            rs=ps.executeQuery();
            if(rs.next()) {
                numeroMassimePrenotazioniPerFascieOrarie=rs.getInt("NumeriPostiMassimo");
            }
        } catch (SQLException e) {
            AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato storto nell'impostazione", null, Alert.AlertType.ERROR);
        } finally {
            if (ps != null) {
                try { ps.close(); } catch (SQLException ignored) {}
            }
            if (rs != null) {
                try { rs.close(); } catch (SQLException ignored) {}
            }
        }
    }

    //Aggiungo una prenotazione, se in quella data e ora sono già state fatte delle prenotazioni, incremento il contatore
    public static void aggiungiPrenotazioneSalaPesi(PrenotazioneSalaPesiCliente prenotazione) {
        if(prenotazioniSalePesi.containsKey(prenotazione)) {
            prenotazioniSalePesi.put(prenotazione, prenotazioniSalePesi.get(prenotazione) + 1);
        } else {
            prenotazioniSalePesi.put(prenotazione, 1);
        }
    }

    public static void svuotaPrenotazioniSalaPesi() {prenotazioniSalePesi.clear();}

    public static boolean rimuoviPrenotazioneSalaPesi(PrenotazioneSalaPesiCliente prenotazione) {
        if(prenotazioniSalePesi.containsKey(prenotazione)) {
            int contatore = prenotazioniSalePesi.get(prenotazione);
            if(contatore > 1) {
                prenotazioniSalePesi.put(prenotazione, contatore - 1);
            } else {
                prenotazioniSalePesi.remove(prenotazione);
            }
            return true;
        } else return false;
    }
}
