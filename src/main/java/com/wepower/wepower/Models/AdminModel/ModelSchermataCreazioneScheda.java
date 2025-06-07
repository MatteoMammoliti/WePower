package com.wepower.wepower.Models.AdminModel;

import com.wepower.wepower.Models.ConnessioneDatabase;
import com.wepower.wepower.Views.AlertHelper;
import com.wepower.wepower.Views.SchedaAllenamento.RigaEsercizioSchedaAdmin;
import javafx.application.Preloader;
import javafx.scene.control.Alert;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ModelSchermataCreazioneScheda {

    private static int idSchedaAllenamento = 0;

    // prelievo dell'immagine per ciascun esercizio per poterla visualizzare
    public static String ottieniPercorsoImmagine(String nomeEsercizio) {
        Connection conn = ConnessioneDatabase.getConnection();

        String fetch = "SELECT PercorsoImmagine FROM Esercizio WHERE NomeEsercizio = ?";

        PreparedStatement prelievo = null;
        try {
            prelievo = conn.prepareStatement(fetch);
            prelievo.setString(1, nomeEsercizio);
            ResultSet rs = prelievo.executeQuery();

            if (rs.next()) {
                return rs.getString("PercorsoImmagine");
            }

        } catch (SQLException ex) {
            AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato storto nel prelievo del percorso immagine", null, Alert.AlertType.ERROR);
        } finally {
            if(prelievo != null) {
                try { prelievo.close(); } catch (SQLException ignored) {}
            }
        }
        return null;
    }

    public static void onConfermaScheda(int idUtente) throws SQLException {
        Connection conn = ConnessioneDatabase.getConnection();

        ArrayList<RigaEsercizioSchedaAdmin> schedaFinita = DatiSessioneAdmin.getEserciziSchedaTemp();

        // prelevo l'id della scheda associata all'utente
        String prelievoIdSchedaAllenamento = "SELECT IdScheda FROM SchedaAllenamento WHERE IdCliente = ? AND IdAdmin = 1 AND SchedaCompilata = 0 AND SchedaAncoraInUso = 1";

        // per ciascun esercizio della scheda compilata faccio la query di inserimento
        String inserimento = "INSERT INTO ComposizioneSchedaAllenamento (NomeEsercizio, IdSchedaAllenamento, NumeroRipetizioni, NumeroSerie) VALUES (?, ?, ?, ?)";

        // confermo il fatto che l'utente ha ricevuto la scheda compilata in modo che la sua richiesta non risulti più in attesa
        String confermaSchedaCompilata = "UPDATE SchedaAllenamento SET SchedaCompilata = 1 WHERE IdScheda = ?";

        PreparedStatement prelievo = null;
        ResultSet rs = null;
        try {
            conn.setAutoCommit(false);
            prelievo = conn.prepareStatement(prelievoIdSchedaAllenamento);
            prelievo.setInt(1, idUtente);
            rs = prelievo.executeQuery();

            if(rs.next()) idSchedaAllenamento = rs.getInt("IdScheda");

            int statusUpdate = 0;
            if (idSchedaAllenamento != 0) {
                for (RigaEsercizioSchedaAdmin es : schedaFinita) {
                    String nomeEsercizio = es.getNomeEsercizio().getText();
                    String numeroSerie = es.getNumeroSerie().getText().replace("Numero serie:", "");
                    String numeroRipetizioni = es.getNumeroRipetizioni().getText().replace("Numero ripetizioni:", "");

                    PreparedStatement inserimentoEsercizio = conn.prepareStatement(inserimento);
                    inserimentoEsercizio.setString(1, nomeEsercizio);
                    inserimentoEsercizio.setInt(2, idSchedaAllenamento);
                    inserimentoEsercizio.setString(3, numeroRipetizioni);
                    inserimentoEsercizio.setString(4, numeroSerie);
                    statusUpdate = inserimentoEsercizio.executeUpdate();

                    if(statusUpdate == 0) {
                        conn.rollback();
                        return;
                    }
                }

                PreparedStatement confermaCompilazioneScheda = null;
                try {
                    confermaCompilazioneScheda = conn.prepareStatement(confermaSchedaCompilata);
                    confermaCompilazioneScheda.setInt(1, idSchedaAllenamento);
                    confermaCompilazioneScheda.executeUpdate();
                    conn.commit();
                } catch (SQLException e)  {
                    AlertHelper.showAlert("Questo non doveva succedere", "Errroe durante la conferma della scheda", null, Alert.AlertType.ERROR);
                } finally {
                    if(confermaCompilazioneScheda != null) {
                        try { confermaCompilazioneScheda.close(); } catch (SQLException ignored) {}
                    }
                }
            }
        } catch (SQLException e) {
            AlertHelper.showAlert("Questo non doveva succedere", "Errroe durante la conferma della scheda", null, Alert.AlertType.ERROR);
        } finally {
            DatiSessioneAdmin.pulisciScheda();
            if(prelievo != null) {
                try { prelievo.close(); } catch (SQLException ignored) {}
            }

            if(rs != null) {
                try { rs.close(); } catch (SQLException ignored) {}
            }
        }
    }
}