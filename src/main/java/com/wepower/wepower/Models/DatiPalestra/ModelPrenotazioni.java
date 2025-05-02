package com.wepower.wepower.Models.DatiPalestra;

import com.wepower.wepower.Models.ConnessioneDatabase;
import com.wepower.wepower.Models.DatiSessioneCliente;
import com.wepower.wepower.Models.Model;
import com.wepower.wepower.Views.AlertHelper;
import javafx.scene.control.Alert;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ModelPrenotazioni {

    public static boolean aggiuntiPrenotazioneSalaPesi(String data, String orario, int idUtente) throws SQLException {
        if (!DatiSessioneCliente.getStatoAbbonamento()) {
            AlertHelper.showAlert("Errore", "Non hai un abbonamento attivo, impossibile prenotare", null, Alert.AlertType.ERROR);
            return false;
        }
        if (orario.matches("\\d{1,2}")) {
            int ora = Integer.parseInt(orario);
            orario = String.format("%02d:00", ora);
        }
        String Query = "INSERT into PrenotazioneSalaPesi (IdCliente,IdSalaPesi,DataPrenotazione,OrarioPrenotazione) VALUES (?,?,?,?)";
        try (Connection conn = ConnessioneDatabase.getConnection()) {
            try (PreparedStatement dati = conn.prepareStatement(Query)) {
                dati.setInt(1, idUtente);
                dati.setInt(2, 1);
                dati.setString(3, data);
                dati.setString(4, orario);


                int righeAffette = dati.executeUpdate();

                if (righeAffette > 0) {
                    System.out.println("Inserimento riuscito!");
                    Model.getInstance().getViewFactoryClient().invalidateDashboard();
                    return true;
                } else {
                    System.out.println("Errore nell'inserimento.");
                    return false;
                }
            } catch (SQLException e) {
                AlertHelper.showAlert("Errore", "Prenotazione già effettuata in questa giornata", null, Alert.AlertType.ERROR);
            }
        }
        return false;
    }

    public static boolean rimuoviPrenotazioneSalaPesi(String data,String orario,int idUtente)  {
        if (orario.matches("\\d{1,2}")) {
            int ora = Integer.parseInt(orario);
            orario = String.format("%02d:00", ora);
        }
        String rimuoviPrenotazione="DELETE FROM PrenotazioneSalaPesi WHERE IdCliente=? AND DataPrenotazione=? AND OrarioPrenotazione=?";
        try (Connection conn = ConnessioneDatabase.getConnection()) {
            try (PreparedStatement dati = conn.prepareStatement(rimuoviPrenotazione)) {
                dati.setInt(1, idUtente);
                dati.setString(2, data);
                dati.setString(3, orario);

                int righeAffette = dati.executeUpdate();

                if (righeAffette > 0) {
                    PrenotazioneSalaPesi temp=new PrenotazioneSalaPesi(data,orario);
                    DatiSessioneCliente.rimuoviPrenotazione(temp);
                    return true;
                } else {
                    return false;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}