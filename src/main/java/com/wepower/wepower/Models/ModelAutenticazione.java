package com.wepower.wepower.Models;

import javafx.util.Pair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ModelAutenticazione {
    public static boolean verificaCredenziali(String email, String password) throws SQLException {
        String Query2 = "SELECT * FROM Admin WHERE Email = ? AND Password = ?";
        String Query="SELECT c.idCliente,c.CertificatoValido,c.nome, c.cognome, cc.Email,cc.Telefono FROM CredenzialiCliente cc JOIN Cliente c ON cc.idCliente=c.idCliente WHERE cc.Email = ? AND cc.Password = ?";

        try (Connection conn = ConnessioneDatabase.getConnection()) {

            try (PreparedStatement datiCliente = conn.prepareStatement(Query)) {
                datiCliente.setString(1, email);
                datiCliente.setString(2, password);
                ResultSet risultatoClienti = datiCliente.executeQuery();
                if (risultatoClienti.next()) {
                    System.out.println("Login cliente effettuato con successo");
                    //Carico i dati del cliente loggato nella classe DatiSessioneCliente

                    DatiSessioneCliente.setIdUtente(risultatoClienti.getInt("idCliente"));
                    DatiSessioneCliente.setNomeUtente(risultatoClienti.getString("nome"));
                    DatiSessioneCliente.setCognome(risultatoClienti.getString("cognome"));
                    DatiSessioneCliente.setEmail(risultatoClienti.getString("Email"));
                    DatiSessioneCliente.setCertificato(risultatoClienti.getBoolean("CertificatoValido"));
                    DatiSessioneCliente.setDateOrariPrenotazioni(caricaDatePrenotazioni(risultatoClienti.getInt("idCliente")));
                    return true;
                }
                try(PreparedStatement datiAdmin =conn.prepareStatement(Query2)) {
                    datiAdmin.setString(1, email);
                    datiAdmin.setString(2, password);
                    ResultSet risultatoAdmin = datiAdmin.executeQuery();
                    if(risultatoAdmin.next()) {
                        System.out.println("Login admin effettuato con successo");
                        DatiSessioneCliente.setNomeUtente("Admin");
                        return true;
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("Login fallito");
        return false;
    }

//Vado a prendermi dal Database tutte le date in cui il cliente ha prenotato la salapesi per mostrarle nel calendario

    public static ArrayList<PrenotazioneSalaPesi> caricaDatePrenotazioni(int idUtente) throws SQLException{
        ArrayList<PrenotazioneSalaPesi>  datePrenotazioni = new ArrayList<>();

        String query = "SELECT DataPrenotazione, OrarioPrenotazione FROM PrenotazioneSalaPesi WHERE idCliente = ?";
        try (Connection conn = ConnessioneDatabase.getConnection()) {
            try (PreparedStatement datiPrenotazione = conn.prepareStatement(query)) {

                datiPrenotazione.setInt(1, idUtente);
                ResultSet risultatoPrenotazioni = datiPrenotazione.executeQuery();

                while (risultatoPrenotazioni.next()) {
                    String data = risultatoPrenotazioni.getString("DataPrenotazione");
                    String orario = risultatoPrenotazioni.getString("OrarioPrenotazione");
                    PrenotazioneSalaPesi prenotazione=new PrenotazioneSalaPesi(data,orario);
                    datePrenotazioni.add(prenotazione);
                }
            }
        }catch (SQLException e) {

        }
        return datePrenotazioni;
    }


}
