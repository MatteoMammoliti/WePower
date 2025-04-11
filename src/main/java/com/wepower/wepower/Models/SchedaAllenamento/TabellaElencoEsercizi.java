package com.wepower.wepower.Models.SchedaAllenamento;

import com.wepower.wepower.Models.ConnessioneDatabase;
import com.wepower.wepower.Models.DatiSessioneCliente;
import com.wepower.wepower.Views.SchedaAllenamento.RigaEsercizioLista;
import com.wepower.wepower.Views.SchedaAllenamento.RigaEsercizioScheda;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TabellaElencoEsercizi {

    public static ArrayList<RigaEsercizioLista> riempiRigaEsercizio(Runnable aggiornaUI) throws SQLException {

        ArrayList<RigaEsercizioLista> ris = new ArrayList<RigaEsercizioLista>();

        String query = "SELECT NomeEsercizio, DescrizioneEsercizio, MuscoloAllenato, PercorsoImmagine FROM Esercizio";

        try (Connection conn = ConnessioneDatabase.getConnection()) {
            PreparedStatement datiEsercizi = conn.prepareStatement(query);
            ResultSet risultatoTuttiEsercizi = datiEsercizi.executeQuery();

            while (risultatoTuttiEsercizi.next()) {
                String NomeEsercizio = risultatoTuttiEsercizi.getString("NomeEsercizio");
                String DescrizioneEsercizio = risultatoTuttiEsercizi.getString("DescrizioneEsercizio");
                String MuscoloAllenato = risultatoTuttiEsercizi.getString("MuscoloAllenato");
                String PercorsoImmagine = risultatoTuttiEsercizi.getString("PercorsoImmagine");

                if (PercorsoImmagine == null || PercorsoImmagine.trim().isEmpty()) {
                    PercorsoImmagine = "images/LOGO.png";
                }
                RigaEsercizioLista esercizio = new RigaEsercizioLista(NomeEsercizio, DescrizioneEsercizio, MuscoloAllenato, PercorsoImmagine, aggiornaUI);
                ris.add(esercizio);
            }
        } catch (Exception e) {
            throw new SQLException(e.getMessage());
        }
        return ris;
    }

    public static ArrayList<RigaEsercizioScheda> riempiRigaEsercizioScheda(Runnable aggiornaUI) throws SQLException {

        ArrayList<RigaEsercizioScheda> ris = new ArrayList<RigaEsercizioScheda>();

        String query = "SELECT e.NomeEsercizio, e.DescrizioneEsercizio, e.MuscoloAllenato, e.PercorsoImmagine, " +
                "csa.NumeroRipetizioni, csa.NumeroSerie, m.Peso " +
                "FROM SchedaAllenamento s " +
                "JOIN ComposizioneSchedaAllenamento csa ON s.IdScheda = csa.IdSchedaAllenamento " +
                "JOIN Esercizio e ON csa.NomeEsercizio = e.NomeEsercizio " +
                "LEFT JOIN ( " +
                "   SELECT mic.NomeEsercizio, mic.IdCliente, mic.Peso " +
                "   FROM MassimaleImpostatoCliente mic " +
                "   INNER JOIN ( " +
                "       SELECT NomeEsercizio, IdCliente, MAX(DataInserimento) AS MaxData " +
                "       FROM MassimaleImpostatoCliente " +
                "       GROUP BY NomeEsercizio, IdCliente " +
                "   ) latest ON mic.NomeEsercizio = latest.NomeEsercizio AND mic.IdCliente = latest.IdCliente AND mic.DataInserimento = latest.MaxData " +
                ") m ON csa.NomeEsercizio = m.NomeEsercizio AND m.IdCliente = s.IdCliente " +
                "WHERE s.IdCliente = ?";

        try (Connection conn = ConnessioneDatabase.getConnection()) {
            PreparedStatement datiEsercizi = conn.prepareStatement(query);
            datiEsercizi.setInt(1, DatiSessioneCliente.getIdUtente());
            ResultSet risultatoTuttiEsercizi = datiEsercizi.executeQuery();

            while (risultatoTuttiEsercizi.next()) {
                String NomeEsercizio = risultatoTuttiEsercizi.getString("NomeEsercizio");
                String DescrizioneEsercizio = risultatoTuttiEsercizi.getString("DescrizioneEsercizio");
                String MuscoloAllenato = risultatoTuttiEsercizi.getString("MuscoloAllenato");
                String NumeroSerie = risultatoTuttiEsercizi.getString("NumeroSerie");
                String NumeroRipetizioni = risultatoTuttiEsercizi.getString("NumeroRipetizioni");

                String massimaleAttuale = risultatoTuttiEsercizi.getString("Peso");

                if (massimaleAttuale == null) {
                    massimaleAttuale = "0";
                }

                String PercorsoImmagine = risultatoTuttiEsercizi.getString("PercorsoImmagine");

                if (PercorsoImmagine == null || PercorsoImmagine.trim().isEmpty()) {
                    PercorsoImmagine = "images/LOGO.png";
                }
                RigaEsercizioScheda esercizio = new RigaEsercizioScheda(NomeEsercizio, DescrizioneEsercizio, MuscoloAllenato, NumeroSerie, NumeroRipetizioni, PercorsoImmagine, massimaleAttuale, aggiornaUI);
                ris.add(esercizio);
            }
        } catch (Exception e) {
            throw new SQLException(e.getMessage());
        }
        return ris;
    }
}
