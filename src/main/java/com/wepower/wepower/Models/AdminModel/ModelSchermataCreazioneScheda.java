package com.wepower.wepower.Models.AdminModel;

import com.wepower.wepower.Models.ConnessioneDatabase;
import com.wepower.wepower.Views.SchedaAllenamento.RigaEsercizioSchedaAdmin;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ModelSchermataCreazioneScheda {

    private static int idSchedaAllenamento = 0;

    public static String ottieniPercorsoImmagine(String nomeEsercizio) {
        String fetch = "SELECT PercorsoImmagine FROM Esercizio WHERE NomeEsercizio = ?";

        try (Connection conn = ConnessioneDatabase.getConnection()) {
            PreparedStatement prelievo = conn.prepareStatement(fetch);
            prelievo.setString(1, nomeEsercizio);
            ResultSet rs = prelievo.executeQuery();

            if (rs.next()) {
                return rs.getString("PercorsoImmagine");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void onConfermaScheda(int idUtente) {
        ArrayList<RigaEsercizioSchedaAdmin> schedaFinita = DatiSessioneAdmin.getEserciziSchedaTemp();

        String prelievoIdSchedaAllenamento = "SELECT IdScheda FROM SchedaAllenamento WHERE IdCliente = ? AND IdAdmin = 1 AND SchedaCompilata = 0";
        String inserimento = "INSERT INTO ComposizioneSchedaAllenamento (NomeEsercizio, IdSchedaAllenamento, NumeroRipetizioni, NumeroSerie) VALUES (?, ?, ?, ?)";
        String confermaSchedaCompilata = "UPDATE SchedaAllenamento SET SchedaCompilata = 1 WHERE IdScheda = ?";
        try (Connection conn = ConnessioneDatabase.getConnection()) {
            PreparedStatement prelievo = conn.prepareStatement(prelievoIdSchedaAllenamento);
            prelievo.setInt(1, idUtente);
            ResultSet rs = prelievo.executeQuery();

            if(rs.next()) {
                idSchedaAllenamento = rs.getInt("IdScheda");
            }

            if (idSchedaAllenamento != 0) {
                for (RigaEsercizioSchedaAdmin es : schedaFinita) {
                    String nomeEsercizio = es.getNomeEsercizio().getText();
                    String numeroRipetizioni = es.getNumeroRipetizioni().getText();
                    String numeroSerie = es.getNumeroSerie().getText();

                    PreparedStatement inserimentoEsercizio = conn.prepareStatement(inserimento);
                    inserimentoEsercizio.setString(1, nomeEsercizio);
                    inserimentoEsercizio.setInt(2, idSchedaAllenamento);
                    inserimentoEsercizio.setString(4, numeroRipetizioni);
                    inserimentoEsercizio.setString(3, numeroSerie);
                    inserimentoEsercizio.executeUpdate();
                }

                PreparedStatement confermaCompilazioneScheda = conn.prepareStatement(confermaSchedaCompilata);
                confermaCompilazioneScheda.setInt(1, idSchedaAllenamento);
                confermaCompilazioneScheda.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        DatiSessioneAdmin.pulisciScheda();
    }
}