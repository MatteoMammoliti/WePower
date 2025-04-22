package com.wepower.wepower.Models.SchedaAllenamento;

import com.wepower.wepower.Models.ConnessioneDatabase;
import com.wepower.wepower.Models.DatiSessioneCliente;
import com.wepower.wepower.Views.SchedaAllenamento.RigaEsercizioLista;
import com.wepower.wepower.Views.SchedaAllenamento.RigaEsercizioScheda;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;

public class TabellaElencoEsercizi {

    public static ArrayList<RigaEsercizioLista> riempiRigaEsercizio() throws SQLException {

        ArrayList<RigaEsercizioLista> ris = new ArrayList<RigaEsercizioLista>();

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
                RigaEsercizioLista esercizio = new RigaEsercizioLista(NomeEsercizio, DescrizioneEsercizio, PercorsoImmagine);
                ris.add(esercizio);
            }
        } catch (Exception e) {
            throw new SQLException(e.getMessage());
        }
        return ris;
    }

    public static ArrayList<RigaEsercizioScheda> riempiRigaEsercizioScheda() throws SQLException {

        ArrayList<RigaEsercizioScheda> ris = new ArrayList<RigaEsercizioScheda>();

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
                else {
                    LocalDate data = Instant.ofEpochMilli(Long.parseLong(dataImpostazioneMassimale)).atZone(ZoneId.systemDefault()).toLocalDate();
                    dataImpostazioneMassimale = data.toString();
                }

                if (massimaleAttuale == null) {
                    massimaleAttuale = "0";
                }

                String PercorsoImmagine = risultatoTuttiEsercizi.getString("PercorsoImmagine");

                if (PercorsoImmagine == null || PercorsoImmagine.trim().isEmpty()) {
                    PercorsoImmagine = "images/LOGO.png";
                }
                RigaEsercizioScheda esercizio = new RigaEsercizioScheda(NomeEsercizio, NumeroSerie, NumeroRipetizioni, PercorsoImmagine, massimaleAttuale, dataImpostazioneMassimale);
                ris.add(esercizio);
            }
        } catch (Exception e) {
            throw new SQLException(e.getMessage());
        }
        return ris;
    }
}
