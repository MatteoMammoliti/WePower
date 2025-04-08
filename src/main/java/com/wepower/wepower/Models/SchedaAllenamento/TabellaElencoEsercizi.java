package com.wepower.wepower.Models.SchedaAllenamento;

import com.wepower.wepower.Models.ConnessioneDatabase;
import com.wepower.wepower.Models.DatiSessioneCliente;
import com.wepower.wepower.Views.SchedaAllenamento.EsercizioPerLista;
import com.wepower.wepower.Views.SchedaAllenamento.EsercizioPerScheda;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TabellaElencoEsercizi {

    public static ArrayList<EsercizioPerLista> riempiRigaEsercizio() throws SQLException {

        ArrayList<EsercizioPerLista> ris = new ArrayList<EsercizioPerLista>();

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
                EsercizioPerLista esercizio = new EsercizioPerLista(NomeEsercizio, DescrizioneEsercizio, MuscoloAllenato, PercorsoImmagine);
                ris.add(esercizio);
            }
        } catch (Exception e) {
            throw new SQLException(e.getMessage());
        }
        return ris;
    }

    public static ArrayList<EsercizioPerScheda> riempiRigaEsercizioScheda() throws SQLException {

        ArrayList<EsercizioPerScheda> ris = new ArrayList<EsercizioPerScheda>();

        String query = "SELECT e.NomeEsercizio, e.DescrizioneEsercizio, e.MuscoloAllenato, e.PercorsoImmagine, " +
                "csa.NumeroRipetizioni, csa.NumeroSerie " +
                "FROM SchedaAllenamento s " +
                "JOIN ComposizioneSchedaAllenamento csa ON s.IdScheda = csa.IdSchedaAllenamento " +
                "JOIN Esercizio e ON csa.NomeEsercizio = e.NomeEsercizio " +
                "WHERE s.idCliente = ?";

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
                String PercorsoImmagine = risultatoTuttiEsercizi.getString("PercorsoImmagine");

                if (PercorsoImmagine == null || PercorsoImmagine.trim().isEmpty()) {
                    PercorsoImmagine = "images/LOGO.png";
                }
                EsercizioPerScheda esercizio = new EsercizioPerScheda(NomeEsercizio, DescrizioneEsercizio, MuscoloAllenato, NumeroSerie, NumeroRipetizioni, PercorsoImmagine);
                ris.add(esercizio);
            }
        } catch (Exception e) {
            throw new SQLException(e.getMessage());
        }
        return ris;
    }
}
