package com.wepower.wepower.Models.SchedaAllenamento;

import com.wepower.wepower.Models.ConnessioneDatabase;
import com.wepower.wepower.Models.DatiSessioneCliente;
import com.wepower.wepower.Views.AlertHelper;
import com.wepower.wepower.Views.SchedaAllenamento.RigaEsercizioListaClient;
import com.wepower.wepower.Views.SchedaAllenamento.RigaEsercizioListaAdmin;
import com.wepower.wepower.Views.SchedaAllenamento.RigaEsercizioSchedaClient;
import javafx.scene.control.Alert;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TabellaElencoEsercizi {

    public static ArrayList<RigaEsercizioListaClient> riempiRigaEsercizio() throws SQLException {

        ArrayList<RigaEsercizioListaClient> ris = new ArrayList<RigaEsercizioListaClient>();

        String query = "SELECT NomeEsercizio, DescrizioneEsercizio, PercorsoImmagine FROM Esercizio";

        try (Connection conn = ConnessioneDatabase.getConnection()) {
            PreparedStatement datiEsercizi = conn.prepareStatement(query);
            ResultSet risultatoTuttiEsercizi = datiEsercizi.executeQuery();

            while (risultatoTuttiEsercizi.next()) {
                String NomeEsercizio = risultatoTuttiEsercizi.getString("NomeEsercizio");
                String DescrizioneEsercizio = risultatoTuttiEsercizi.getString("DescrizioneEsercizio");
                String PercorsoImmagine = risultatoTuttiEsercizi.getString("PercorsoImmagine");

                if (PercorsoImmagine == null || PercorsoImmagine.trim().isEmpty()) {
                    PercorsoImmagine = "images/LOGO.png";
                }
                RigaEsercizioListaClient esercizio = new RigaEsercizioListaClient(NomeEsercizio, DescrizioneEsercizio, PercorsoImmagine);
                ris.add(esercizio);
            }
        } catch (Exception e) {
            AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato storto :(", null, Alert.AlertType.ERROR);
        }
        return ris;
    }

    public static ArrayList<RigaEsercizioListaAdmin> riempiRigaEsercizioAdmin() throws SQLException {
        ArrayList<RigaEsercizioListaAdmin> ris = new ArrayList<>();

        String query = "SELECT NomeEsercizio, DescrizioneEsercizio, PercorsoImmagine FROM Esercizio";

        try (Connection conn = ConnessioneDatabase.getConnection()) {
            PreparedStatement datiEsercizi = conn.prepareStatement(query);
            ResultSet risultatoTuttiEsercizi = datiEsercizi.executeQuery();

            while (risultatoTuttiEsercizi.next()) {
                String NomeEsercizio = risultatoTuttiEsercizi.getString("NomeEsercizio");
                String DescrizioneEsercizio = risultatoTuttiEsercizi.getString("DescrizioneEsercizio");
                String PercorsoImmagine = risultatoTuttiEsercizi.getString("PercorsoImmagine");

                if (PercorsoImmagine == null || PercorsoImmagine.trim().isEmpty()) {
                    PercorsoImmagine = "images/LOGO.png";
                }
                RigaEsercizioListaAdmin esercizio = new RigaEsercizioListaAdmin(NomeEsercizio, DescrizioneEsercizio, PercorsoImmagine);
                ris.add(esercizio);
            }
        } catch (Exception e) {
            AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato storto :(", null, Alert.AlertType.ERROR);
        }
        return ris;
    }

    public static ArrayList<RigaEsercizioSchedaClient> riempiRigaEsercizioScheda() throws SQLException {

        ArrayList<RigaEsercizioSchedaClient> ris = new ArrayList<RigaEsercizioSchedaClient>();

        String query = "SELECT e.NomeEsercizio, e.PercorsoImmagine,\n" +
                "       csa.NumeroRipetizioni, csa.NumeroSerie, m.Peso, m.DataInserimento\n" +
                "FROM SchedaAllenamento s\n" +
                "JOIN ComposizioneSchedaAllenamento csa ON s.IdScheda = csa.IdSchedaAllenamento\n" +
                "JOIN Esercizio e ON csa.NomeEsercizio = e.NomeEsercizio\n" +
                "LEFT JOIN (\n" +
                "    SELECT mic.NomeEsercizio, mic.IdCliente, mic.Peso, mic.DataInserimento\n" +
                "    FROM MassimaleImpostatoCliente mic\n" +
                "    INNER JOIN (\n" +
                "        SELECT NomeEsercizio, IdCliente, MAX(DataInserimento) AS MaxData\n" +
                "        FROM MassimaleImpostatoCliente\n" +
                "        GROUP BY NomeEsercizio, IdCliente\n" +
                "    ) latest\n" +
                "    ON mic.NomeEsercizio = latest.NomeEsercizio\n" +
                "       AND mic.IdCliente = latest.IdCliente\n" +
                "       AND mic.DataInserimento = latest.MaxData\n" +
                ") m ON csa.NomeEsercizio = m.NomeEsercizio AND m.IdCliente = s.IdCliente\n" +
                "WHERE s.IdCliente = ? AND s.SchedaAncoraInUso = 1\n";

        try (Connection conn = ConnessioneDatabase.getConnection()) {
            PreparedStatement datiEsercizi = conn.prepareStatement(query);
            datiEsercizi.setInt(1, DatiSessioneCliente.getIdUtente());
            ResultSet risultatoTuttiEsercizi = datiEsercizi.executeQuery();

            while (risultatoTuttiEsercizi.next()) {
                String NomeEsercizio = risultatoTuttiEsercizi.getString("NomeEsercizio");
                String NumeroSerie = risultatoTuttiEsercizi.getString("NumeroSerie");
                String NumeroRipetizioni = risultatoTuttiEsercizi.getString("NumeroRipetizioni");

                String massimaleAttuale = risultatoTuttiEsercizi.getString("Peso");
                String dataImpostazioneMassimale = risultatoTuttiEsercizi.getString("DataInserimento");

                if (dataImpostazioneMassimale == null) {
                    dataImpostazioneMassimale = "Nessun massimale impostato";
                }

                if (massimaleAttuale == null) {
                    massimaleAttuale = "0";
                }

                String PercorsoImmagine = risultatoTuttiEsercizi.getString("PercorsoImmagine");

                if (PercorsoImmagine == null || PercorsoImmagine.trim().isEmpty()) {
                    PercorsoImmagine = "images/LOGO.png";
                }
                RigaEsercizioSchedaClient esercizio = new RigaEsercizioSchedaClient(NomeEsercizio, NumeroSerie, NumeroRipetizioni, PercorsoImmagine, massimaleAttuale, dataImpostazioneMassimale);
                ris.add(esercizio);
            }
        } catch (Exception e) {
            AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato storto :(", null, Alert.AlertType.ERROR);
        }
        return ris;
    }
}