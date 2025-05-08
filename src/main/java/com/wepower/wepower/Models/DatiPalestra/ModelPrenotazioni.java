package com.wepower.wepower.Models.DatiPalestra;
import com.wepower.wepower.Models.ConnessioneDatabase;
import com.wepower.wepower.Models.DatiSessioneCliente;
import com.wepower.wepower.Models.Model;
import com.wepower.wepower.Views.AlertHelper;
import javafx.scene.control.Alert;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ModelPrenotazioni {

    public static boolean aggiuntiPrenotazioneSalaPesi(String data, String orario, int idUtente) throws SQLException {
        Connection conn = ConnessioneDatabase.getConnection();

        if (!DatiSessioneCliente.getStatoAbbonamento()) {
            AlertHelper.showAlert("Errore", "Non hai un abbonamento attivo, impossibile prenotare", null, Alert.AlertType.ERROR);
            return false;
        }

        if (orario.matches("\\d{1,2}")) {
            int ora = Integer.parseInt(orario);
            orario = String.format("%02d:00", ora);
        }

        String inserimentoPrenotazione = "INSERT into PrenotazioneSalaPesi (IdCliente,IdSalaPesi,DataPrenotazione,OrarioPrenotazione) VALUES (?,?,?,?)";

        PreparedStatement dati = null;
        try {
            dati = conn.prepareStatement(inserimentoPrenotazione);
            dati.setInt(1, idUtente);
            dati.setInt(2, 1);
            dati.setString(3, data);
            dati.setString(4, orario);


            int righeAffette = dati.executeUpdate();

            if (righeAffette > 0) {
                Model.getInstance().getViewFactoryClient().invalidateDashboard();
                return true;
            } else return false;

        } catch (SQLException e) {
            AlertHelper.showAlert("Errore", "Prenotazione già effettuata in questa giornata", null, Alert.AlertType.ERROR);
        } finally {
            if (dati != null) {
                try { dati.close(); } catch (SQLException ignored) {}
            }
        }
        return false;
    }

    public static boolean rimuoviPrenotazioneSalaPesi(String data,String orario,int idUtente)  {
        Connection conn = ConnessioneDatabase.getConnection();

        if (orario.matches("\\d{1,2}")) {
            int ora = Integer.parseInt(orario);
            orario = String.format("%02d:00", ora);
        }
        String rimuoviPrenotazione = "DELETE FROM PrenotazioneSalaPesi WHERE IdCliente=? AND DataPrenotazione=? AND OrarioPrenotazione=?";

        PreparedStatement dati = null;
        try {
            dati = conn.prepareStatement(rimuoviPrenotazione);
            dati.setInt(1, idUtente);
            dati.setString(2, data);
            dati.setString(3, orario);

            int righeAffette = dati.executeUpdate();

            if (righeAffette > 0) {
                PrenotazioneSalaPesi temp=new PrenotazioneSalaPesi(data,orario);
                DatiSessioneCliente.rimuoviPrenotazione(temp);
                return true;
            } else return false;

        } catch (SQLException e) {
            AlertHelper.showAlert("Questo non doveva succedere", "Errore durante l'eliminazione della prenotazione", null, Alert.AlertType.ERROR);
        } finally {
            if (dati != null) {
                try { dati.close(); } catch (SQLException ignored) {}
            }
        }
        return false;
    }
}