package com.wepower.wepower.Models;

import com.wepower.wepower.Views.SchedaAllenamento.Esercizio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TabellaElencoEsercizi {

    public static ArrayList<Esercizio> riempiRigaEsercizio() throws SQLException {

        ArrayList<Esercizio> ris = new ArrayList<Esercizio>();

        String query = "SELECT NomeEsercizio, DescrizioneEsercizio, MuscoloAllenato, PercorsoImmagine FROM Esercizio";

        try (Connection conn = ConnessioneDatabase.getConnection()) {
            PreparedStatement datiEsercizi = conn.prepareStatement(query);
            ResultSet risultatoTuttiEsercizi = datiEsercizi.executeQuery();

            while (risultatoTuttiEsercizi.next()) {
                String NomeEsercizio = risultatoTuttiEsercizi.getString("NomeEsercizio");
                String DescrizioneEsercizio = risultatoTuttiEsercizi.getString("DescrizioneEsercizio");
                String MuscoloAllenato = risultatoTuttiEsercizi.getString("MuscoloAllenato");
                String PercorsoImmagine = risultatoTuttiEsercizi.getString("PercorsoImmagine");

                System.out.println(PercorsoImmagine);
                if (PercorsoImmagine == null || PercorsoImmagine.trim().isEmpty()) {
                    PercorsoImmagine = "images/LOGO.png";
                }
                Esercizio esercizio = new Esercizio(NomeEsercizio, DescrizioneEsercizio, MuscoloAllenato, PercorsoImmagine);
                ris.add(esercizio);
            }
        } catch (Exception e) {
            throw new SQLException(e.getMessage());
        }
        return ris;
    }
}
